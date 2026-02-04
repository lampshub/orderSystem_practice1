package com.beyond23.orderSystem.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RabbitMqStockDto {
    private Long productId;
    private int productCount;


}
