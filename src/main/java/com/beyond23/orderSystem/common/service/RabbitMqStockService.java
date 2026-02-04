package com.beyond23.orderSystem.common.service;

import com.beyond23.orderSystem.common.dtos.RabbitMqStockDto;
import com.beyond23.orderSystem.product.domains.Product;
import com.beyond23.orderSystem.product.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//상품주문시 redis로 처리하고. db에 rabbitMq를 이용하여 재고 감소 테스트

@Component
public class RabbitMqStockService {
    private final RabbitTemplate rabbitTemplate;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    @Autowired
    public RabbitMqStockService(RabbitTemplate rabbitTemplate, ProductRepository productRepository, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

//      publish메서드
    public void publish(Long productId, int productCount){
        RabbitMqStockDto dto = RabbitMqStockDto.builder()
                .productId(productId)
                .productCount(productCount)
                .build();
        rabbitTemplate.convertAndSend("stockQueue", dto);   //stockQueue공간에 dto를 발행
    }

    //    RabbitListener rabbitmq에 특정 큐에 대해 listen, subscribe 하는 어노테이션
//    RabbitListener는 낟ㄴ일스레드로 메세지를 처리하므로, 동시성이슈발생X. 다만, 멀티서버환경에서는 문제발생할 수 있음
    @RabbitListener(queues = "stockQueue")  //stockQueue를 바라보고 있음
    @Transactional
    public void subscribe(Message message) throws JsonProcessingException {
        String messageBody = new String(message.getBody());
        System.out.println("message : " + messageBody);     //queue
        RabbitMqStockDto dto = objectMapper.readValue(messageBody, RabbitMqStockDto.class) ;
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(()-> new EntityNotFoundException("없는 상품id입니다"));
        product.updateStockQuantity(dto.getProductCount());
    }





}
