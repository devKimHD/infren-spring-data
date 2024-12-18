package study.dataJPA.repository;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.dataJPA.entity.Member;
import study.dataJPA.entity.Team;

public class MemberSpec {

    public static Specification<Member> teamName(final String teamName)
    {
        return new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (StringUtils.isEmpty(teamName))
                {
                    return null;
                }
                Join<Member, Team> team = root.join("team", JoinType.INNER);//회원과 조인
                return criteriaBuilder.equal(team.get("name"),teamName);
            }
        };
    }
    public static Specification<Member> userName(final String username)
    {
        return (Specification<Member>) (root, query, builder)->builder.equal(root.get("username"),username);

    }
}
