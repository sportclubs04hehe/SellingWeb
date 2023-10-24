package huylee.BanHang.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    @JsonProperty("user_id")
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @JsonProperty("full_name")
    @NotBlank(message = "Full name cannot be blank")
    @Size(max = 100, message = "Full name must be less than 100 characters")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\+?\\d+", message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Address cannot be blank")
    @Size(max = 250, message = "Address must be less than 250 characters")
    private String address;

    @Size(max = 500, message = "Note must be less than 500 characters")
    private String note;

    @JsonProperty("total_money")
    // @DecimalMin => inclusive = false: có nghĩa là giá trị chỉ có thể lớn hơn giá trị đặt biên và không được bằng.
    @DecimalMin(value = "0.0", inclusive = false, message = "Total money must be > 0")
    @NotNull(message = "Total money cannot be null")
    private BigDecimal totalMoney;

    @JsonProperty("shipping_method")
    @NotBlank(message = "Shipping method cannot be blank")
    private String shippingMethod;
    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("shipping_address")
    @Size(max = 250, message = "Shipping address must be less than 250 characters")
    private String shippingAddress;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("payment_method")
    @NotBlank(message = "Payment method cannot be blank")
    private String paymentMethod;



}
