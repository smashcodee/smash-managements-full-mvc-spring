package br.com.smashcode.smashmanagements.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ITaskRepository extends JpaRepository<TaskEntity, Integer> {
}
