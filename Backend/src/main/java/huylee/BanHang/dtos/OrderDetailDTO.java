package huylee.BanHang.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetailDTO {
    /** Positive => số phải hoàn toàn dương và lớn hơn 0
     * PositiveOrZero => số là dương hoặc có thể bằng 0
     * */

    @JsonProperty("order_id")
    @NotNull(message = "Order ID cannot be null")
    @Positive(message = "Order ID must be a positive number")
    private Long orderId;

    @JsonProperty("product_id")
    @NotNull(message = "Product ID cannot be null")
    @Positive(message = "Product ID must be a positive number")
    private Long productId;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    @JsonProperty("number_of_product")
    @PositiveOrZero(message = "Number of products must be zero or positive")
    private int numberOfProduct;

    @JsonProperty("total_money")
    @NotNull(message = "Total money cannot be null")
    @Positive(message = "Total money must be greater than 0")
    private BigDecimal totalMoney;

    @Size(max = 50, message = "Color must not exceed 50 characters")
    private String color;
}
