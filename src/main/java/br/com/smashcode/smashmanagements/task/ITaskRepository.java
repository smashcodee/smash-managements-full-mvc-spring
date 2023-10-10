package br.com.smashcode.smashmanagements.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ITaskRepository extends JpaRepository<TaskEntity, Long> {

    @Query(
            "SELECT c FROM TaskEntity c WHERE c.taskType = 'CHECKPOINT'"
    )
    List<TaskEntity> findAllCheckpoints();

    @Query(
            "SELECT c FROM TaskEntity c WHERE c.taskType = 'GLOBAL'"
    )
    List<TaskEntity> findAllGlobals();

    @Query(
            "SELECT c FROM TaskEntity c WHERE c.taskType = 'SPRINT'"
    )
    List<TaskEntity> findAllSprints();
}
