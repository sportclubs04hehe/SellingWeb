package huylee.BanHang.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import huylee.BanHang.entity.BaseEntity;
import huylee.BanHang.entity.Category;
import huylee.BanHang.entity.Product;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString @Builder
public class ProductResponse extends BaseResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private Long categoryId;

    public static ProductResponse fromProduct(Product product){
        ProductResponse p = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategoryId().getId())
                .build();
        p.setCreateAt(product.getCreateAt());
        p.setUpdateAt(product.getUpdateAt());
        return p;
    }
}
