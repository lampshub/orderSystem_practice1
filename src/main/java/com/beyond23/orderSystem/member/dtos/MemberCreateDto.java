package com.beyond23.orderSystem.member.dtos;

import com.beyond23.orderSystem.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateDto {
    @NotBlank(message = "id값을 입력해주세요")
    private Long id;
    @NotBlank(message = "email을 입력해주세요")
    private String email;
    @NotBlank(message = "password를 입력해주세요")
    private String password;

    public Member toEntity(String encodedPw){
        return Member.builder()
                .id(this.id)
                .email(this.email)
                .password(encodedPw)
                .build();
    }
}
