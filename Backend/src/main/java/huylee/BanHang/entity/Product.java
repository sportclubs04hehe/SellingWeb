package huylee.BanHang.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @AllArgsConstructor @NoArgsConstructor
@Getter
@Setter
@Table(name = "_product") @Builder
public class Product extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    private BigDecimal price;
    @Column(name = "thumbnail", length = 300)
    private String thumbnail;
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryId;

}
