package com.beyond23.orderSystem.ordering.service;

import com.beyond23.orderSystem.common.service.RabbitMqStockService;
import com.beyond23.orderSystem.common.service.SseAlarmService;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderingService {

    private final OrderingRepository orderingRepository;
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final SseAlarmService sseAlarmService;
    private final RedisTemplate<String, String> redisTemplate;
    private final RabbitMqStockService rabbitMqStockService;

    @Autowired
    public OrderingService(OrderingRepository orderingRepository, MemberRepository memberRepository, OrderDetailRepository orderDetailRepository, ProductRepository productRepository, SseAlarmService sseAlarmService, @Qualifier("stockInventory") RedisTemplate<String, String> redisTemplate, RabbitMqStockService rabbitMqStockService) {
        this.orderingRepository = orderingRepository;
        this.memberRepository = memberRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
        this.sseAlarmService = sseAlarmService;
        this.redisTemplate = redisTemplate;
        this.rabbitMqStockService = rabbitMqStockService;
    }


//    1. ordering 테이블에 data 저장
//    2. orderDetail에 n개 저장

//    동시성제어방법1. 특정 메서드에 한해 격리수준 올리기.
//    @Transactional(isolation = Isolation.SERIALIZABLE)
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

//cascading persist를 사용해서 저장(자식테이블까지 같이 저장)
        for(OrderingCreateDto dto : dtoList){
            Product product = productRepository.findById(dto.getProductId()).orElseThrow(()-> new EntityNotFoundException("entity is not found"));
//              동시성제어방법2. select for update를 통한 락 설정 이후 조회
//            Product product = productRepository.findByIdForUpdate(dto.getProductId()).orElseThrow(()-> new EntityNotFoundException("entity is not found"));
//              동시성제어방법3. redis에서 재고수량 확인 및 재고수량 감소처리
//              단점 : 조회와 감소요청이 분리되다보니, 동시성문제 발생 -> 해결책 : 루아(lua)스크립트를 통해 여러작업을 단일요청으로 묶어 해결
            String remain = redisTemplate.opsForValue().get(String.valueOf(dto.getProductId()));
            int remainQuantity = Integer.parseInt(remain);
            if(remainQuantity < dto.getProductCount()){
                throw new IllegalArgumentException("재고가 부족합니다");
            }else {
                redisTemplate.opsForValue().decrement(String.valueOf(dto.getProductId()), dto.getProductCount());   //dto.getProductCount만큼 감소시키겠다
            }
//            if(product.getStockQuantity() < dto.getProductCount()) {         //All or Nothing
//                throw new IllegalArgumentException("재고가 부족합니다.");       //재고 사과3, 바나나3 일때, 한주문안에 사과3,바나나5이면 주문전체 rollback
//            }

//            product.updateStockQuantity(dto.getProductCount()); //주문시 재고수량 변경

            OrderingDetail orderingDetail = OrderingDetail.builder()
                    .ordering(ordering)
                    .product(product)
                    .quantity(dto.getProductCount())
                    .build();
            ordering.getOrderingDetailList().add(orderingDetail);   //cascading

//            rdb 동기화를 위한 작업1 : 스케쥴러 활용
//            rdb 동기화를 위한 작업2 : rabbitmq에 rdb 재고감소 메세지 발행
            rabbitMqStockService.publish(dto.getProductId(), dto.getProductCount());

        }
        orderingRepository.save(ordering);  //cascading

//        주문성공시 admin 유저에게 알림메시지 전송
        String message = ordering.getId() + "번 주문이 들어왔습니다.";
//        메세지 전파
        sseAlarmService.sendMessage("admin@naver.com",email, message);
////        알림DB저장 시, 따로 설계해줘야함
//        alarmRepository.save(alarm객체)

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
