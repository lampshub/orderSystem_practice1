package com.beyond23.orderSystem.member.service;

import com.beyond23.orderSystem.member.domain.Member;
import com.beyond23.orderSystem.member.dtos.MemberCreateDto;
import com.beyond23.orderSystem.member.dtos.MemberDetailDto;
import com.beyond23.orderSystem.member.dtos.MemberListDto;
import com.beyond23.orderSystem.member.dtos.MemberLoginDto;
import com.beyond23.orderSystem.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    create, doLogin, list, myinfo, detail/1

    public Long save(MemberCreateDto dto) {
        if (memberRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Member member = dto.toEntity(passwordEncoder.encode(dto.getPassword()));    //암호화
        memberRepository.save(member);

        return member.getId();
    }

    @Transactional(readOnly = true)
    public List<MemberDetailDto> list(){
        List<MemberDetailDto> dto = memberRepository.findAll().stream().map(a->MemberDetailDto.fromEntity(a)).collect(Collectors.toList());
        return dto;
    }

    public MemberDetailDto detail(Long id){
        Optional<Member> optMember = memberRepository.findById(id);
        Member member = optMember.orElseThrow(()-> new EntityNotFoundException("찾는 id가 없습니다."));
        MemberDetailDto dto = MemberDetailDto.fromEntity(member);
        return dto;
    }
    @Transactional(readOnly = true)
    public MemberDetailDto myinfo( String email) {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(); //filter에서 가져온 토큰에서 받아온 email
        Optional<Member> optMember = memberRepository.findByEmail(email);
        Member member = optMember.orElseThrow(() -> new NoSuchElementException("없는 entity"));
        MemberDetailDto dto = MemberDetailDto.fromEntity(member);
        return dto;
    }

    public Member login(MemberLoginDto dto) {
        Optional<Member> optMember = memberRepository.findByEmail(dto.getEmail());
        boolean check = true;
        if (!optMember.isPresent()) {
            check = false;
        } else {
            if (!passwordEncoder.matches(dto.getPassword(), optMember.get().getPassword())) {
                check = false;
            }
        }
        if(!check){
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return optMember.get();
    }

}
