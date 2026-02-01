package com.beyond23.orderSystem.ordering.service;

import com.beyond23.orderSystem.member.domain.Member;
import com.beyond23.orderSystem.member.repository.MemberRepository;
import com.beyond23.orderSystem.ordering.domain.Ordering;
import com.beyond23.orderSystem.ordering.domain.OrderingDetail;
import com.beyond23.orderSystem.ordering.dtos.OrderingCreateDto;
import com.beyond23.orderSystem.ordering.dtos.OrderingDetailDto;
import com.beyond23.orderSystem.ordering.dtos.OrderingListDto;
import com.beyond23.orderSystem.ordering.repository.OrderDetailRepository;
import com.beyond23.orderSystem.ordering.repository.OrderingRepository;
import com.beyond23.orderSystem.product.domains.Product;
import com.beyond23.orderSystem.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderingService {

    private final OrderingRepository orderingRepository;
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderingService(OrderingRepository orderingRepository, MemberRepository memberRepository, OrderDetailRepository orderDetailRepository, ProductRepository productRepository) {
        this.orderingRepository = orderingRepository;
        this.memberRepository = memberRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
    }


//    1. ordering 테이블의 data 저장
//    2. orderDatail에 n개 저장
    public Long create(List<OrderingCreateDto> dtoList){
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("member is not found"));
        Ordering ordering = Ordering.builder()
                .member(member)             //orderStatus()는 엔티티에 디폴트로 저장되어있음
                .build();
//        orderingRepository.save(ordering);
//
//        for(OrderingCreateDto dto : dtoList){
//            Product product = productRepository.findById(dto.getProductId()).orElseThrow(()-> new EntityNotFoundException("entity is not found"));
//            OrderingDetail orderingDetail = OrderingDetail.builder()
//                    .ordering(ordering)
//                    .product(product)
//                    .quantity(dto.getProductCount())
//                    .build();
//            orderDetailRepository.save(orderingDetail);
//        }

//cascaging persist를 사용해서 저장(자식테이블까지 같이 저장)
        for(OrderingCreateDto dto : dtoList){
            Product product = productRepository.findById(dto.getProductId()).orElseThrow(()-> new EntityNotFoundException("entity is not found"));
            OrderingDetail orderingDetail = OrderingDetail.builder()
                    .ordering(ordering)
                    .product(product)
                    .quantity(dto.getProductCount())
                    .build();
            ordering.getOrderingDetailList().add(orderingDetail);   //cascading
        }
        orderingRepository.save(ordering);  //cascading

        return ordering.getId();
    }

    @Transactional(readOnly = true)
    public List<OrderingListDto> findAll(){
        return orderingRepository.findAll().stream().map(o->OrderingListDto.fromEntity(o)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderingListDto> myOrders(){
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("member is not found"));
        return orderingRepository.findAllByMember(member).stream().map(o->OrderingListDto.fromEntity(o)).collect(Collectors.toList());
    }
}
