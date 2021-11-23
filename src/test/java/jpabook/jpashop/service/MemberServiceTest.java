package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

  @Autowired
  MemberService memberService;
  @Autowired
  MemberRepository memberRepository;

  @Test
  public void MemberServiceTest() throws Exception {
    //given
    Member member = new Member();
    member.setName("Test");
    //when
    Long saveId = memberService.join(member);
    //then
    assertEquals(member, memberRepository.findOne(saveId));
  }

  @Test
  public void validateDuplicateMemberTest() throws Exception {
    //given
    Member member1 = new Member();
    member1.setName("Test2");
    Member member2 = new Member();
    member2.setName("Test2");
    //when
    memberService.join(member1);
    //then
    assertThrows(IllegalStateException.class, () -> {
      memberService.join(member2); //예외가 발생해야 한다.
    });
  }

  @Test
  public void findMembersTest() {
    //given
    Member member1 = new Member();
    member1.setName("Test3");
    Member member2 = new Member();
    member2.setName("Test4");
    //when
    memberService.join(member1);
    memberService.join(member2);
    int sizeOfFoundMembers = memberService.findMembers().size();
    //then
    assertThat(sizeOfFoundMembers).isEqualTo(2);
  }

  @Test
  public void findOneTest() {
    //given
    Member member1 = new Member();
    member1.setName("Test5");
    //when
    Long saveId = memberService.join(member1);
    //then
    assertThat(member1).isEqualTo(memberService.findOne(saveId));
  }
}