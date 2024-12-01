package study.dataJPA.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.dataJPA.dto.MemberDto;
import study.dataJPA.entity.Member;
import study.dataJPA.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;
    @Test
    public void testMember()
    {
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get();

        org.assertj.core.api.Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        org.assertj.core.api.Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        org.assertj.core.api.Assertions.assertThat(findMember).isEqualTo(member);
        // 1차캐시에 의해 같음
    }

    @Test
    public void basicCRUD()
    {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        //단건 조회 검증
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long countDel = memberRepository.count();
        Assertions.assertThat(countDel).isEqualTo(0);
    }
    @Test
    public void findByUsernameAndAgeGreaterThen()
    {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("AAA",20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        org.assertj.core.api.Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        org.assertj.core.api.Assertions.assertThat(result.get(0).getAge()).isEqualTo(201);
        org.assertj.core.api.Assertions.assertThat(result.size()).isEqualTo(1);
    }
    @Test
    public void testNamedQuery()
    {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        Assertions.assertThat(findMember).isEqualTo(member1);
    }
    @Test
    public void testQuery()
    {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findMember("AAA",10);
        Member findMember = result.get(0);
        Assertions.assertThat(findMember).isEqualTo(member1);
    }
    @Test
    public void findUsernameList()
    {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> useranmeList = memberRepository.findUseranmeList();
        for (String s : useranmeList) {
            System.out.println("s = " + s);
        }

    }
    @Test
    public void findMemberDto()
    {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA",10);
        member1.changeTeam(team);
        memberRepository.save(member1);


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }

    }
    @Test
    public void findByNames()
    {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member byName : byNames) {
            System.out.println("byName = " + byName);
        }

    }
    @Test
    public void returnType()
    {
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("BBB",20);
        memberRepository.save(member1);
        memberRepository.save(member2);

//        List<Member> aaa = memberRepository.findListByUsername("AAA123");
        // List 결과는 null이 나올수 없음 EmptyCollection 반환
//        Member findMember = memberRepository.findMemberByUsername("AAA1235");

        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");
        System.out.println("optionalMember = " + optionalMember);


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
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        //when
        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        //todo DTO로 반환 하는 꿀팁
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));

//        Slice<Member> page2 = memberRepository.findByAge(age, pageRequest);
//        List<Member> page3 = memberRepository.findByAge(age, pageRequest);

        //then
        List<Member> content = page.getContent();
        long totalCount = page.getTotalElements();
        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(totalCount).isEqualTo(5);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();



    }
    @Test
    public void bulkUpdate()
    {
        //given
        Member member1 = new Member("AAA1",10);
        Member member2 = new Member("BBB2",19);
        Member member3 = new Member("BBB3",20);
        Member member4 = new Member("BBB4",21);
        Member member5 = new Member("BBB5",40);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        //DB에 대한 직접 쿼리 실행으로 영속성 컨텍스트는 초기화 해줘야함
        int resultCount = memberRepository.bulkAgePlus(20);
        Member m5 = memberRepository.findMemberByUsername("BBB5");
        System.out.println("m5 = " + m5);
        Assertions.assertThat(resultCount).isEqualTo(3);
    }
    @Test
    public void findMemberLazy()
    {
        //Member 1 -> TeamA
        //Member 2 -> TeamB
        //given
        Team teamA = new Team("TeamA"); 
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        
        em.flush();
        em.clear();
        //when
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
            
        }
    }
    @Test
    public void queryHint()
    {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        findMember.setUsername("member2");

        em.flush();
        
    }

    @Test
    public void lock()
    {
        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername("member1");


        em.flush();

    }
    @Test
    public void callCustom()
    {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    public void specBasic()
    {
        //given
        Team team = new Team("teamA");
        em.persist(team);
        Member m1 = new Member("m1", 0, team);
        Member m2 = new Member("m2", 0, team);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();
        //when
        memberRepository.findAll();
        //then
        Specification<Member> spec = MemberSpec.userName("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }
    @Test
    public void queryByExample() throws Exception
    {
        //given
        Team team = new Team("teamA");
        em.persist(team);
        Member m1 = new Member("m1", 0, team);
        Member m2 = new Member("m2", 0, team);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();
        //when
        //probe
        Member member = new Member("m1");
        Team teamA = new Team("teamA");
        member.changeTeam(teamA);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");
        Example<Member> example = Example.of(member,matcher);
        List<Member> result = memberRepository.findAll(example);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("m1");
        //then
    }
    @Test
    public void projections () throws Exception
    {
        //given
        Team team = new Team("teamA");
        em.persist(team);
        Member m1 = new Member("m1", 0, team);
        Member m2 = new Member("m2", 0, team);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();
        //when
        List<NestedClosedProjection> result = memberRepository.findProjectionByUsername("m1", NestedClosedProjection.class);
        for (NestedClosedProjection nestedClosedProjection : result) {
            String username = nestedClosedProjection.getUsername();
            System.out.println("username = " + username);
            String teamName = nestedClosedProjection.getTeam().getName();
            System.out.println("teamName = " + teamName);
        }

        //then
    }
    @Test
    public void nativeQuery() throws Exception
    {
        //given
        Team team = new Team("teamA");
        em.persist(team);
        Member m1 = new Member("m1", 0, team);
        Member m2 = new Member("m2", 0, team);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();
        //when
//        Member m = memberRepository.findByNativeQuery("m1");
//        System.out.println("m = " + m);
        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        List<MemberProjection> content = result.getContent();
        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
            System.out.println("memberProjection.getTeamName() = " + memberProjection.getTeamName());
        }
        //then
    }
}