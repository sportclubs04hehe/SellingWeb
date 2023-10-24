package huylee.BanHang.service;

import huylee.BanHang.dtos.CategoryDTO;
import huylee.BanHang.entity.Category;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CategoryService {
    Category createCategory (CategoryDTO categoryDTO);
    Category getCategoryById (Long id);
    List<Category> getAllCategories ();
    Category updateCategory ( Long id, CategoryDTO category);
    void deleteCategory(Long id);
}
