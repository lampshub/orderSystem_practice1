package com.beyond23.orderSystem.ordering.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderingMyOrdersDto {
    private String productName;
    private String category;
}
