package study.dataJPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.dataJPA.entity.Member;

// ctrl p 파라미터 보기
public interface MemberRepository extends JpaRepository<Member,Long> {
}
