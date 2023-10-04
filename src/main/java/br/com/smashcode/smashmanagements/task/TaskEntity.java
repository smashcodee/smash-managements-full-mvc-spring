package br.com.smashcode.smashmanagements.task;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // @NotBlank
    private String title;
    //@Size(min = 10)
    private String description;

    @Column(name = "task_type")
    private String taskType;

    // @Postive
    private Integer score;

    // @Min(0) @Max(100)
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TaskEntity() { }
}
