package com.beyond23.orderSystem.member.dtos;


import com.beyond23.orderSystem.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberListDto {
    private Long id;
    private String name;
    private String email;

    public static MemberListDto fromEntity(Member member){
        return MemberListDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}

