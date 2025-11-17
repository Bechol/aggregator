package ru.lenni.aggregator.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.lenni.aggregator.resource.common.TaskType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(schema = "agr", name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "task_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "result_file_path")
    private String resultFilePath;

    @Column(name = "contains_pd", nullable = false)
    private Boolean containsPD = false;

    @CreationTimestamp
    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;

    @UpdateTimestamp
    @Column(name = "last_update_time", nullable = false)
    private LocalDateTime lastUpdateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    @Column(name = "deleted", nullable = true)
    private Boolean deleted = false;

    @Version
    private Long version;

    public static Task withType(TaskType taskType) {
        Task task = new Task();
        task.setTaskType(taskType);
        return task;
    }

    public boolean isSoftDeleted() {
        return this.deleted;
    }

    public boolean isNotDeleted() {
        return !this.deleted;
    }

}


