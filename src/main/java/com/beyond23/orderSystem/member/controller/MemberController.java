package com.beyond23.orderSystem.member.controller;

import com.beyond23.orderSystem.member.domain.Member;
import com.beyond23.orderSystem.member.dtos.MemberCreateDto;
import com.beyond23.orderSystem.member.repository.MemberRepository;
import com.beyond23.orderSystem.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    @Autowired
    public MemberController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MemberCreateDto dto){
        memberService.save(dto);
//        Long memberId = memberRepository.findAll().get(dto.getEmail()).getId();
        return ResponseEntity.status(HttpStatus.CREATED).body(" ");

    }


}
