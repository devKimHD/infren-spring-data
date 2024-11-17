package study.dataJPA.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.dataJPA.entity.Member;

import java.util.List;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberJpaRepositoryTest {
    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember()
    {
        Member member = new Member("memberA");
        Member saveMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(saveMember.getId());

        org.assertj.core.api.Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        org.assertj.core.api.Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        org.assertj.core.api.Assertions.assertThat(findMember).isEqualTo(member);
        // 1차캐시에 의해 같음
    }
    @Test
    public void findByUsernameAndAgeGreaterThan()
    {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("AAA",20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        org.assertj.core.api.Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        org.assertj.core.api.Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        org.assertj.core.api.Assertions.assertThat(result.size()).isEqualTo(1);
    }
    @Test
    public void testNamedQuery()
    {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberJpaRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        Assertions.assertThat(findMember).isEqualTo(member1);

    }
    @Test
    public void paging()
    {
        //given
        Member member1 = new Member("AAA1",10);
        Member member2 = new Member("BBB2",10);
        Member member3 = new Member("BBB3",10);
        Member member4 = new Member("BBB4",10);
        Member member5 = new Member("BBB5",10);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);
        //when
        int age = 10;
        int offset = 0;
        int limit = 3;
        //then
        List<Member> members = memberJpaRepository.findByPages(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);
        Assertions.assertThat(members.size()).isEqualTo(3);
        Assertions.assertThat(totalCount).isEqualTo(5);


    }
}