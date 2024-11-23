package study.dataJPA.repository;

import study.dataJPA.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
