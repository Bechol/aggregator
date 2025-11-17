package ru.lenni.aggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.lenni.aggregator.model.Task;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    @Modifying
    @Query("update Task t set t.deleted = true where t.id = :taskUid")
    void softDeleteById(UUID taskUid);

    List<Task> findByDeletedFalseOrderByLastUpdateTimeDesc();
}
