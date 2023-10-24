package huylee.BanHang.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {
    @Column(name = "create_at")
    private LocalDateTime createAt;
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    /**
     * @PrePersist => đánh dấu pt onCreate() sẽ đưc thực thi trước khi Entity lưu vào db.||
     * @PreUpdate => đánh dấu pt onUpdate() sẽ đươc thực thi trước khi cập nhật vào db
     *
     * */
    @PrePersist
    protected void onCreate(){
        createAt = LocalDateTime.now(); // đặt thời điểm hiện tại
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updateAt = LocalDateTime.now();
    }
}
