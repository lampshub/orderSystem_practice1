package com.beyond23.orderSystem.member.controller;

import com.beyond23.orderSystem.common.auth.JwtTokenProvider;
import com.beyond23.orderSystem.common.dtos.CommonErrorDto;
import com.beyond23.orderSystem.member.domain.Member;
import com.beyond23.orderSystem.member.dtos.MemberCreateDto;
import com.beyond23.orderSystem.member.dtos.MemberDetailDto;
import com.beyond23.orderSystem.member.dtos.MemberListDto;
import com.beyond23.orderSystem.member.dtos.MemberLoginDto;
import com.beyond23.orderSystem.member.repository.MemberRepository;
import com.beyond23.orderSystem.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public MemberController(MemberService memberService, MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

//    create, doLogin, list, myinfo, detail/1

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody MemberCreateDto dto){
        Long memberId = memberService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MemberListDto> list(){
        List<MemberListDto> dtoList = memberService.list();
        return dtoList;
    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> detail(@PathVariable Long id){
        try {
            MemberDetailDto dto = memberService.detail(id);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (NoSuchElementException e){
            CommonErrorDto dto = CommonErrorDto.builder()
                    .status_code(404)
                    .error_message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
        }
    }

    @GetMapping("/myinfo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> myinfo(@AuthenticationPrincipal String principal) {
        System.out.println(principal);
        MemberDetailDto dto = memberService.myinfo();
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> login(@RequestBody MemberLoginDto dto){
        Member member = memberService.login(dto);
        String token = jwtTokenProvider.createToken(member);    //토큰생성 및 리턴
        return ResponseEntity.ok().body("accessToken : "+ token);
    }






}
