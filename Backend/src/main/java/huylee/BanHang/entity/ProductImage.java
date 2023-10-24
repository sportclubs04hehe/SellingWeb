package huylee.BanHang.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "product_image") @Builder
public class ProductImage {

    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 6;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "image_url", length = 300)
    private String imageUrl;
}
