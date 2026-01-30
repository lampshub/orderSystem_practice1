package com.beyond23.orderSystem.ordering.controller;

import com.beyond23.orderSystem.ordering.domain.Ordering;
import com.beyond23.orderSystem.ordering.dtos.OrderingCreateDto;
import com.beyond23.orderSystem.ordering.service.OrderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordering")
public class OrderingController {
    private final OrderingService orderingService;
    @Autowired
    public OrderingController(OrderingService orderingService) {
        this.orderingService = orderingService;
    }

    //    create, list, myorders
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody List<OrderingCreateDto> dto){
        Long id=  orderingService.create(orderingService);

        return ResponseEntity.status()

    }

    @GetMapping("/list")
    public ResponseEntity<?> list(Ordering ordering){
        orderingService.list(ordering);
    }

    @GetMapping("/myorders")
    public ResponseEntity myOrders(){
    }

//    public OrderController(OrderService orderService) {
//        this.orderService = orderService;
//    }
//    @PostMapping("/ordering/create")
//    public ResponseEntity<Long> create(@RequestBody List<OrderCreateDto> dtos){
//        Long orderId = orderService.save(dtos);
//        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
//    }
//    @GetMapping("/ordering/list")
//    @PreAuthorize("hasRole('ADMIN')")
//    public List<OrderListDto> findAll(){
//        List<OrderListDto> dto = orderService.findAll();
//        return dto;
//    }
//    @GetMapping("/ordering/myorders")
//    public List<OrderListDto> myorders(){
//        List<OrderListDto> dto = orderService.myorders();
//        return dto;
//    }
}
