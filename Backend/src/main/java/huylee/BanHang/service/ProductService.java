package huylee.BanHang.service;

import huylee.BanHang.dtos.ProductDTO;
import huylee.BanHang.dtos.ProductImageDTO;
import huylee.BanHang.entity.Product;
import huylee.BanHang.entity.ProductImage;
import huylee.BanHang.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService {
    Product createProduct(ProductDTO productDTO);
    Product getProductById(Long id);
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(Long id,ProductDTO productDTO);
    void deleteProduct (Long id);
    boolean existByName(String name);
    ProductImage createProductImage(Long id, ProductImageDTO imageDTO);
}
