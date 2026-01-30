package com.beyond23.orderSystem.product.domains;

import com.beyond23.orderSystem.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT), nullable = false)
    private Member member;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int price;
    private String category;
    @Column(nullable = false)
    private int stockQuantity;
    private String imagePath;

    @CreationTimestamp
    private LocalDateTime createdTime;

//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    orderingDetail 과 연결

}
