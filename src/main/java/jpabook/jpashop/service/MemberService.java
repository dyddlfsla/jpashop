package jpabook.jpashop.service;

import java.util.List;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;

  @Autowired
  public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  /**
   * 회원 가입
   */
  @Transactional
  public Long join(Member member) {
    validateDuplicateMember(member);
    memberRepository.save(member);
    return member.getId();
  }

  private void validateDuplicateMember(Member member) {
    List<Member> foundMembers = memberRepository.findByName(member.getName());
    //EXCEPTION
    if (!foundMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }

  }

  //회원 전체 조회
  public List<Member> findMembers() {
    return memberRepository.findAll();
  }

  //id로 단건 조회
  public Member findOne(Long memberId) {
    return memberRepository.findOne(memberId);
  }
}
