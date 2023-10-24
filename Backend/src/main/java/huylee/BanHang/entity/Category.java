package huylee.BanHang.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_categories")
@Builder @ToString
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name",nullable = false)
    private String name;
}
