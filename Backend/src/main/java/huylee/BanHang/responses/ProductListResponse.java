package huylee.BanHang.responses;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter @Builder
public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPage;
}
