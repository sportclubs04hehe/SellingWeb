package huylee.BanHang.service.impl;

import huylee.BanHang.dtos.ProductDTO;
import huylee.BanHang.dtos.ProductImageDTO;
import huylee.BanHang.entity.Category;
import huylee.BanHang.entity.Product;
import huylee.BanHang.entity.ProductImage;
import huylee.BanHang.exception.AppException;
import huylee.BanHang.repository.CategoryRepository;
import huylee.BanHang.repository.ProductImageRepository;
import huylee.BanHang.repository.ProductRepository;
import huylee.BanHang.responses.ProductResponse;
import huylee.BanHang.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static huylee.BanHang.entity.ProductImage.MAXIMUM_IMAGES_PER_PRODUCT;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository imageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) {
        // Kiểm tra xem category tham chiến đến có tồn tại hay không?
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(
                        () -> new AppException(NOT_FOUND, "Category Id Not Found with id= " + productDTO.getCategoryId())
                );
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .categoryId(existingCategory)
                .build();
        return repository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AppException(NOT_FOUND, "Id Not Found with id=" + id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        // lấy d/s sản phẩm theo trang(page) và giới hạn (limit).
        return repository
                .findAll(pageRequest)
                .map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product newProduct = repository.findById(id)
                .orElseThrow(() -> new AppException(NOT_FOUND, "Id Not Found with id=" + id));

        if (newProduct != null) {
            Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(
                            () -> new AppException(NOT_FOUND, "Category Id Not Found" + productDTO.getCategoryId())
                    );
            newProduct.setName(productDTO.getName());
            newProduct.setPrice(productDTO.getPrice());
            newProduct.setThumbnail(productDTO.getThumbnail());
            newProduct.setDescription(productDTO.getDescription());
            newProduct.setCategoryId(existingCategory);
            return repository.save(newProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> productId = repository.findById(id);
        productId.ifPresent(repository::delete);
    }

    @Override
    public boolean existByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(
            Long id,
            ProductImageDTO imageDTO
    ) {
        // kiểm tra xem sản phẩm có hay chưa
        Product sanPhamTonTai = repository
                .findById(id)
                .orElseThrow(() -> new AppException(
                        NOT_FOUND, "Cannot find product with id= " + imageDTO.getProductId()
                ));

        ProductImage newImage = ProductImage.builder()
                .product(sanPhamTonTai)
                .imageUrl(imageDTO.getImageUrl())
                .build();

        int size = imageRepository.findByProductId(id).size();
        if (size > MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new AppException(HttpStatus.UNPROCESSABLE_ENTITY, "Do not exceed 5 photos");
        }
        return imageRepository.save(newImage);
    }
}
