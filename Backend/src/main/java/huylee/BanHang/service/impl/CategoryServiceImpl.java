package huylee.BanHang.service.impl;

import huylee.BanHang.dtos.CategoryDTO;
import huylee.BanHang.entity.Category;
import huylee.BanHang.exception.AppException;
import huylee.BanHang.repository.CategoryRepository;
import huylee.BanHang.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category
                .builder()
                .name(categoryDTO.getName())
                .build();
        return repository.save(newCategory);
    }

    @Override
    public Category getCategoryById(Long id) {
        return repository.findById(id).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, " Id Not Found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existCategory = repository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, " Id Not Found"));
        existCategory.setName(categoryDTO.getName());
        return repository.save(existCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }
}
