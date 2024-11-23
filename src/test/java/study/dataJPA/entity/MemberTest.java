package study.dataJPA.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.dataJPA.repository.MemberJpaRepository;
import study.dataJPA.repository.MemberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    MemberRepository memberRepository;
    @Test
    public void testEntity()
    {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member = new Member("member1",10, teamA);
        Member member2 = new Member("member1",20, teamA);
        Member member3 = new Member("member1",30, teamB);
        Member member4 = new Member("member1",40, teamB);
        em.persist(member);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        
        //초기화
        em.flush();
        em.clear();
        
        //확인
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member memberFor : members) {
            System.out.println("memberFor = " + memberFor);
            System.out.println("memberFor.getTeam() = " + memberFor.getTeam());
        }
        


    }
    @Test
    public void basicCRUD()
    {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findByid(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findByid(member2.getId()).get();
        //단건 조회 검증
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);
        long count = memberJpaRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long countDel = memberJpaRepository.count();
        Assertions.assertThat(countDel).isEqualTo(0);
    }

    @Test
    public void jpaEventBestEntity() throws Exception
    {
        //given
        Member member = new Member("member1");
        memberRepository.save(member);

        Thread.sleep(100);
        member.setUsername("member2");
        em.flush();
        em.clear();
        //when
        Member member1 = memberRepository.findById(member.getId()).get();
        //then
        System.out.println("member1.getCreatedDate() = " + member1.getCreatedDate());
        System.out.println("member1.getUpdatedDate() = " + member1.getLastModifiedDate());
        System.out.println("member1.getCreatedBy() = " + member1.getCreatedBy());
        System.out.println("member1.getLastModifiedBy() = " + member1.getLastModifiedBy());
    }
}