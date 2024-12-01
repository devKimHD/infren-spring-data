package study.dataJPA.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.dataJPA.dto.MemberDto;
import study.dataJPA.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

// ctrl p 파라미터 보기
public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member>
{
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

    @Modifying(clearAutomatically = true)//쿼리 진행 후 영속성 컨텍스트 초기화
    @Query("update Member m set m.age= m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age")int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

//    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(@Param("username")String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly",value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

//    @Query("SELECT new study.dataJPA.repository.UsernameOnlyDto(m.username) FROM Member m WHERE m.username = :username")
    <T> List<T> findProjectionByUsername(@Param("username")String username,Class<T> type);

    @Query(value = "select * from member where username=? ",nativeQuery = true)
    Member findByNativeQuery(String username);

    @Query(value = "select m.member_id as id, m.username, t.name as teamName from member m left join team t on m.team_id = t.team_id",nativeQuery = true,
    countQuery = "select count(*) from member")
    Page <MemberProjection> findByNativeProjection(Pageable pageable);

}
