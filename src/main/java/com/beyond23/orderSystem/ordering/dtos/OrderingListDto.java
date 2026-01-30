package com.beyond23.orderSystem.ordering.dtos;

import com.beyond23.orderSystem.ordering.domain.Ordering;
import com.beyond23.orderSystem.ordering.domains.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderingListDto {

    private Long id;
    private String memberEmail;
    private OrderStatus orderStatus;
    private List<OrderingListDto> orderDetails;

    public static OrderingListDto fromEntity(Ordering ordering){
        List<OrderingDetailDto> orderingDetailDtos = new ArrayList<>();

        for(OrderDetail orderDetail = ordering.getOrderDetailList()){
            orderDetail
        }

        OrderingListDto orderingListDto = OrderingListDto.builder()
                .id(ordering.getId)
                .orderStatus(ordering.getOrderStatus())
                .memberEmail(ordering.getMember().getEmail())
                .orderDetails(orderingDetailDtos)
                .build();

    }
}
