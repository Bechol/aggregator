package ru.lenni.aggregator.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.lenni.aggregator.dto.RequestStatus;
import ru.lenni.aggregator.dto.RequestType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(schema = "agr", name = "request")
public class Request {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private UUID id = UUID.randomUUID();

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestType type;

    @Column(name = "s3_key", nullable = false)
    private String s3Key;

    @Column(name = "result_s3_key")
    private String resultS3Key;

    @Column(name = "contains_pd")
    private Boolean containsPD = false;

    @CreationTimestamp
    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;

    @UpdateTimestamp
    @Column(name = "last_update_time", nullable = false)
    private LocalDateTime lastUpdateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Version
    private Long version;

    public static Request withType(RequestType requestType) {
        Request request = new Request();
        request.setType(requestType);
        request.setStatus(RequestStatus.NEW);
        return request;
    }

    public boolean isSoftDeleted() {
        return this.deleted;
    }

    public boolean isNotDeleted() {
        return !this.deleted;
    }

}


