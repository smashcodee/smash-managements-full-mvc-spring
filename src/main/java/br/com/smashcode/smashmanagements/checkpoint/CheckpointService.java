package br.com.smashcode.smashmanagements.checkpoint;

import br.com.smashcode.smashmanagements.task.ITaskRepository;
import br.com.smashcode.smashmanagements.task.TaskEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CheckpointService {
    @Autowired
    private ITaskRepository taskRepository;

    public boolean create(TaskEntity entity) {
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setTaskType("CHECKPOINT"); // ** importante **
        entity.setStatus(0);

        taskRepository.saveAndFlush(entity);
        log.info("[ DB Persist ] - Checkpoint cadastrado com sucesso!");
        return true;
    }

    public Optional<TaskEntity> findById(Integer id) {
        return taskRepository.findById(id);
    }

    public List<TaskEntity> findAll() {
        return taskRepository.findAllCheckpoints();
    }

    public boolean delete(Integer checkpointId) {
        var checkpoint = taskRepository.findById(checkpointId);
        if(checkpoint.isEmpty()) return false;
        taskRepository.delete(checkpoint.get());
        return true;
    }

}
