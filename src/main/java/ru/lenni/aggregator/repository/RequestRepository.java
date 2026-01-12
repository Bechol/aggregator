package ru.lenni.aggregator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.lenni.aggregator.model.Request;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {

    @Modifying
    @Query("update Request t set t.deleted = true where t.id = :requestUid")
    void softDeleteById(UUID taskUid);

    List<Request> findByDeletedFalseOrderByLastUpdateTimeDesc();
}
