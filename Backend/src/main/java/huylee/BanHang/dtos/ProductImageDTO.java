package huylee.BanHang.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Builder
@Setter
public class ProductImageDTO {
    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("image_url")
    @Size(min = 5, max = 200, message = "imageUrl must be at least 5 characters and at most 200 characters.")
    private String imageUrl;
}
