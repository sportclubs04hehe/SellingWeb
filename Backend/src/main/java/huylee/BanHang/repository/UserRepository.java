package huylee.BanHang.repository;

import huylee.BanHang.entity.Category;
import huylee.BanHang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByPhoneNumber (String phoneNumber);
    Optional<User> findByPhoneNumber (String phoneNumber);
}
