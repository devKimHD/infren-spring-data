package study.dataJPA.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.dataJPA.dto.MemberDto;
import study.dataJPA.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

// ctrl p 파라미터 보기
public interface MemberRepository extends JpaRepository<Member,Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

//    @Query(name = "Member.findByUsername")
    // 쿼리이름으로 @Query 와 매칭이 안되면 쿼리이름으로 쿼리문 재생성
    List<Member> findByUsername(@Param("username")String username);
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findMember(@Param("username")String username, @Param("age") int age);
    @Query("select m.username from Member m")
    List<String> findUseranmeList();
    @Query("select new study.dataJPA.dto.MemberDto( m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);// collection
    Member findMemberByUsername(String username);//one
    Optional<Member> findOptionalByUsername(String username);// null or Member

    @Query(value = "select m from Member m left join m.team t",countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

}
