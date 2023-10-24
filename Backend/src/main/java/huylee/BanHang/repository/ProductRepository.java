package huylee.BanHang.repository;

import huylee.BanHang.entity.Category;
import huylee.BanHang.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByName (String name);
}
