package com.beyond23.orderSystem.ordering.service;

import com.beyond23.orderSystem.member.domain.Member;
import com.beyond23.orderSystem.member.repository.MemberRepository;
import com.beyond23.orderSystem.ordering.domain.Ordering;
import com.beyond23.orderSystem.ordering.dtos.OrderingCreateDto;
import com.beyond23.orderSystem.ordering.dtos.OrderingListDto;
import com.beyond23.orderSystem.ordering.repository.OrderingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderingService {

    private final OrderingRepository orderingRepository;
    private final MemberRepository memberRepository;
    @Autowired
    public OrderingService(OrderingRepository orderingRepository, MemberRepository memberRepository) {
        this.orderingRepository = orderingRepository;
        this.memberRepository = memberRepository;
    }


    public Long create( List<OrderingCreateDto> orderCreateDtoList){
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("member is not found"));
        Ordering ordering = Ordering.builder()
                .member(member)
                .build();
    }

    public void list(OrderingListDto dto){

    }
}
