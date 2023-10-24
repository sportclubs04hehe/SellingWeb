package huylee.BanHang.repository;

import huylee.BanHang.entity.Category;
import huylee.BanHang.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}
