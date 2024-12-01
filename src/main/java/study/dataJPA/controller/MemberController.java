package study.dataJPA.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.dataJPA.dto.MemberDto;
import study.dataJPA.entity.Member;
import study.dataJPA.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

//    @GetMapping("/members/{id}")
//    public String findMember(@PathVariable("id")Long id)
//    {
//        Member member = memberRepository.findById(id).get();
//        return member.getUsername();
//    }
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Member member)
    {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable)
    {
        // entity는 절대 api 스펙에서 외부 노출 말것 그렇기에 dto 변환
        Page<Member> page = memberRepository.findAll(pageable);

        Page<MemberDto> map = page.map(MemberDto::new);
        return map;
    }
    @PostConstruct
    public void init(){
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user"+i,i));
        }

    }


}
