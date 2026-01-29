package com.beyond23.orderSystem.member.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

@Builder
@Entity
@Getter @ToString
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

//    private CreatedDate createTime;

}
