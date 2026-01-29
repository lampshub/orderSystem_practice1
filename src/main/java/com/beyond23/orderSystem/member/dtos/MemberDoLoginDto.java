package com.beyond23.orderSystem.member.dtos;

import com.beyond23.orderSystem.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDoLoginDto {
    @NotBlank(message = "email을 입력해주세요")
    private String email;
    @NotBlank(message = "password를 입력해주세요")
    private String password;


}
