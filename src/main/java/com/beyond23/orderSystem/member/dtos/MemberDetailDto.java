package com.beyond23.orderSystem.member.dtos;

import com.beyond23.orderSystem.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDetailDto {
    private Long id;
    private String name;
    private String email;

    public static MemberDetailDto fromEntity(Member member){
        return MemberDetailDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}
