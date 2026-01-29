package com.beyond23.orderSystem.member.controller;

import com.beyond23.orderSystem.common.dtos.CommonErrorDto;
import com.beyond23.orderSystem.member.dtos.MemberCreateDto;
import com.beyond23.orderSystem.member.dtos.MemberDetailDto;
import com.beyond23.orderSystem.member.dtos.MemberListDto;
import com.beyond23.orderSystem.member.repository.MemberRepository;
import com.beyond23.orderSystem.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
        Long memberId = memberService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('admin')")
    public List<MemberListDto> list(){
        List<MemberListDto> dtoList = memberService.list();
        return dtoList;
    }

//    @GetMapping("/myinfo")  //로그인된 토큰값으로 ??
//    public ResponseEntity<?> myinfo(){
//    }

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole('admin')")
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







}
