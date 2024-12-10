package org.example;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("gorillas")
public class Gorilla {
    @Id
    private Long id;
    @Column("color")
    private String color;
    @Column("age")
    private Integer age;
    @Column("angry")
    private Boolean angry;
}
