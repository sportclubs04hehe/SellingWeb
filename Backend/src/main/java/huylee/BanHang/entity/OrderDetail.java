package huylee.BanHang.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor @Builder
@Table(name = "_order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "number_of_product", nullable = false)
    private int numberOfProduct;

    @Column(name = "total_money", nullable = false)
    private BigDecimal totalMoney;

    @Column(name = "color")
    private String color;
}
