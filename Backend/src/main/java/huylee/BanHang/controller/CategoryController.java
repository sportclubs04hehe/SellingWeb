package huylee.BanHang.controller;

import huylee.BanHang.dtos.CategoryDTO;
import huylee.BanHang.entity.Category;
import huylee.BanHang.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/category")
@RequiredArgsConstructor
//@Validated
// sử dụng ở cấp độ class nó kích hoạt các tính năng khác example: validation nhóm hoặc xử lí phương thức chưa hỗ trợ
public class CategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult result
    ) {
        if(result.hasErrors()){
            List<String> errorMessage = result.getFieldErrors()
                    .stream().map(
                            FieldError::getDefaultMessage
                    ).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        service.createCategory(categoryDTO);
        return ResponseEntity.ok(String.format("Thêm thành công Category = %s", categoryDTO.getName()));
    }

    @GetMapping // http://localhost:8080/api/v1/category?page=1&limit=10
    public ResponseEntity<?> getAllCategories(
            @RequestParam("page") int page, // example: page_of_category => pageOfCategory đó là quy ước
            @RequestParam("limit") int limit
    ) {
        List<Category> categories = service.getAllCategories();
        return ResponseEntity.ok().body(categories);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO categoryDTO
    ) {
        Category newCategory = service.updateCategory(id, categoryDTO);
        return ResponseEntity.ok().body(newCategory.toString() + " _Success");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        service.deleteCategory(id);
        return ResponseEntity.ok("Delete a Category Success id= " + id);
    }

}
