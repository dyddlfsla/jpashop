package jpabook.jpashop.controller;

import java.util.List;
import javax.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

  @Autowired
  private MemberService memberService;

  @GetMapping("/members/new")
  public String createForm(Model model) {
    model.addAttribute("memberForm", new MemberForm());
    return "members/createMemberForm";
  }

  @PostMapping("/members/new")
  public String createMember(@Valid MemberForm memberForm, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "members/createMemberForm";
    }

    Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
    Member member = new Member();
    member.setName(memberForm.getName());
    member.setAddress(address);
    memberService.join(member);
    return "redirect:/";
  }

  @GetMapping("/members")
  public String memberList(Model model) {
    List<Member> members = memberService.findMembers();
    model.addAttribute("members", members);
    return "members/memberList";
  }
}
