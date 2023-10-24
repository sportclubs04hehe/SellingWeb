package huylee.BanHang.controller;

import com.github.javafaker.Faker;
import huylee.BanHang.dtos.ProductDTO;
import huylee.BanHang.dtos.ProductImageDTO;
import huylee.BanHang.entity.Product;
import huylee.BanHang.entity.ProductImage;
import huylee.BanHang.exception.AppException;
import huylee.BanHang.responses.ProductListResponse;
import huylee.BanHang.responses.ProductResponse;
import huylee.BanHang.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static huylee.BanHang.entity.ProductImage.MAXIMUM_IMAGES_PER_PRODUCT;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping // http://localhost:8080/api/v1/products?page=1&limit=10
    public ResponseEntity<?> getAllProducts(
            @RequestParam("page") int page, // example: page_of_category => pageOfCategory đó là quy ước
            @RequestParam("limit") int limit
    ) {
        if (page < 1) { // đảm bảo luôn bắt đầu từ trang 1
            return new ResponseEntity<>("Invalid page number", BAD_REQUEST);
        }

        PageRequest pageRequest = PageRequest.of(
                page - 1, // đảm bảo trừ về chỉ mục của JPA
                limit,
                Sort.by("createAt").descending()
        );

        Page<ProductResponse> products = service.getAllProducts(pageRequest);
        int totalsPage = products.getTotalPages(); // lấy tổng số trang
        List<ProductResponse> productList = products.getContent();
        if (productList.isEmpty()) {
            return new ResponseEntity<>("No records found", NO_CONTENT);
        }
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(productList)
                        .totalPage(totalsPage)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") Long id
    ) {
        try{
            Product product =service.getProductById(id);
            if(product == null){
                return new ResponseEntity<>(NO_CONTENT);
            }
            return ResponseEntity.ok(ProductResponse.fromProduct(product));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * value = "" định nghĩa rằng nó URL sẽ đi theo base URL cụ thể là @RequestMapping ||
     * consumes = MediaType.MULTIPART_FORM_DATA_VALUE => phương thức sẽ chỉ xử lí loại y/c content-type (multipart/form-data) ||
     * MultipartFile là một giao diện đại diện cho tệp tin được tải lên trong một yêu cầu HTTP.
     * Giao diện này cung cấp các phương thức để truy cập thông tin của tệp (như tên, loại nội dung, kích thước)
     * và cũng cho phép đọc nội dung của tệp.
     */
    @PostMapping
    public ResponseEntity<?> insertProducts(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream().map(
                                FieldError::getDefaultMessage
                        ).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Product newProduct = service.createProduct(productDTO);
            return new ResponseEntity<>(newProduct, CREATED);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable Long id,
            @ModelAttribute("files") List<MultipartFile> files
    ) {
        try {
            Product existingProduct = service.getProductById(id); // lấy id product nếu tồn tại
            files = files == null ? new ArrayList<MultipartFile>() : files; // nếu upload file trống thì không trả về gì

            if (files.size() > MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("Can only upload a maximum of 5 photos per upload");
            }

            List<ProductImage> productImages = new ArrayList<>(); // khởi tạo một mảng rỗng
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {  // nếu không có ảnh nào
                    continue;
                }
                // Check kích thước file ảnh và định dạng
                if (file.getSize() > 10 * 1024 * 1024) { // kích thước 10mb
                    return ResponseEntity.status(PAYLOAD_TOO_LARGE)
                            .body("File tải quá lớn! Chỉ có thể tải dưới > 10MB");
                }

                // image/png => MIME Type
                Set<String> anhHoTro = new HashSet<>(Arrays.asList(
                        "image/png",
                        "image/jpeg",
                        "image/jpg"
                ));

                String contentType = file.getContentType(); // lấy ra định dạng file
                if (contentType == null || !anhHoTro.contains(contentType)) { //1211 => 2 tồn tại trong dãy 1 => true => phủ định (!)=> false -> thoát vòng if
                    return ResponseEntity.status(UNSUPPORTED_MEDIA_TYPE)
                            .body("Tệp phải là hình ảnh có định dạng PNG, JPG hoặc JPEG");
                }

                // lưu file và cập nhật thumbnail trong dtos
                String fileName = storeFile(file);
                ProductImage productImage = service.createProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(fileName)
                                .build()
                );
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProducts(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO
    ) {
        try{
            Product product = service.updateProduct(id,productDTO);
            return new ResponseEntity<>(product, OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducts(@PathVariable Long id) {
        try{
            service.deleteProduct(id);
            return ResponseEntity.ok().body("Delete Product with id= " + id +" successfully.");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * file.getOriginalFilename() => lấy tên gốc của tệp tải lên ||
     * Objects.requireNonNull => kiểm tra đối tượng truyền vào có null không nếu null trả về NullPointerException với thông báo mặc định ||
     * StringUtils.cleanPath => làm sạch đường dẫn loại bỏ các phần tử như ".." để bảo mật ||
     * UUID.randomUUID().toString() => Viết tắt của Universal Unique Identifier) là một chuỗi 128-bit được sử dụng để định danh thông tin duy nhất.
     * UUID.randomUUID() => sinh ra một UUID ngẫu nhiên còn toString() => chuyển nó thành chuỗi dạng "550e8400-e29b-41d4-a716-446655440000" ví dụ
     */
    private String storeFile(MultipartFile file) throws IOException {

        if (!isValidImage(file) || file.getOriginalFilename() == null) {
            throw new AppException(
                    UNSUPPORTED_MEDIA_TYPE, "Uploaded file is not a valid image or is not supported."
            );
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = UUID.randomUUID().toString() + "_" + fileName; // "_" + fileName => thêm một dấu gạch dưới trước fileName;
        Path uploadDir = Paths.get("uploads"); // đường dẫn đến thư mục lưu file

        if (!Files.exists(uploadDir)) { // Kiểm tra và tạo thư mục nếu nó không tồn tại
            Files.createDirectories(uploadDir);
        }

        //Đường dẫn đến file Example: uploads/abc_xyz.png
        Path viTri = Paths.get(uploadDir.toString(), uniqueFilename);
        /* sao chép file vào thư mục đích
         * file.getInputStream() => trả về một InputStream cho phép đọc nội dung của tệp đã tải lên.
         * StandardCopyOption.REPLACE_EXISTING => là một tùy chọn chỉ định rằng nếu một tệp đã tồn tại tại vị trí đích (viTri), nó sẽ được thay thế bằng tệp mới */
        Files.copy(file.getInputStream(), viTri, REPLACE_EXISTING);
        return uniqueFilename;
    }

    public boolean isValidImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("/image");
    }

    @PostMapping("/generateFakeProducts")
    public ResponseEntity<?> generateFakeProducts(){
        Faker faker = new Faker();
        for(int i = 0; i < 1000; i ++){
            String productName = faker.commerce().productName();
            if(service.existByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price(BigDecimal.valueOf(faker.number().numberBetween(10,100000)))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long)faker.number().numberBetween(1,5))
                    .build();
            service.createProduct(productDTO);
        }

        return ResponseEntity.ok("fake ok");
    }
}
