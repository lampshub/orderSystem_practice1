package com.beyond23.orderSystem.member.service;

import com.beyond23.orderSystem.member.domain.Member;
import com.beyond23.orderSystem.member.dtos.MemberCreateDto;
import com.beyond23.orderSystem.member.dtos.MemberDetailDto;
import com.beyond23.orderSystem.member.dtos.MemberListDto;
import com.beyond23.orderSystem.member.dtos.MemberLoginDto;
import com.beyond23.orderSystem.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long save(MemberCreateDto dto){
        if(memberRepository.findByEmail(dto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Member member = dto.toEntity(passwordEncoder.encode(dto.getPassword()));
        Member memberDb = memberRepository.save(member);

        return memberDb.getId();
    }

    public void login(MemberLoginDto dto){

    }

    public List<MemberListDto> list(){
        List<MemberListDto> dto = memberRepository.findAll().stream().map(a->MemberListDto.fromEntity(a)).collect(Collectors.toList());
        return dto;
    }

    public MemberDetailDto detail(Long id){
        Optional<Member> optMember = memberRepository.findById(id);
        Member member = optMember.orElseThrow(()-> new NoSuchElementException("찾는 id가 없습니다."));
        MemberDetailDto dto = MemberDetailDto.fromEntity(member);
        return dto;
    }


}
