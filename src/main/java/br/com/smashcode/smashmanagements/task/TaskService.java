package br.com.smashcode.smashmanagements.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskService {
    @Autowired
    private ITaskRepository taskRepository;

    public boolean create(TaskPostRequest request, String taskType) {
        TaskEntity entity = new TaskEntity();

        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        switch (taskType) {
            case "CHECKPOINT": {
                entity.setTaskType("CHECKPOINT");
                break;
            }
            case "GLOBAL": {
                entity.setTaskType("GLOBAL");
                break;
            }
            case "SPRINT": {
                entity.setTaskType("SPRINT");
                break;
            }
            default: {
                entity.setTaskType("TASK");
            }
        }

        entity.setStatus(0);

        taskRepository.saveAndFlush(entity);
        log.info("[ DB Persist ] - Checkpoint cadastrado com sucesso!");
        return true;
    }

    public Optional<TaskEntity> findById(Long id) {
        return taskRepository.findById(id);
    }

    public List<TaskEntity> findAll(String taskType) {
        List<TaskEntity> list;
        switch (taskType) {
            case "CHECKPOINT": {
                list = taskRepository.findAllCheckpoints();
                break;
            }
            case "GLOBAL": {
                list = taskRepository.findAllGlobals();
                break;
            }
            case "SPRINT": {
                list = taskRepository.findAllSprints();
                break;
            }
            default: {
                list = taskRepository.findAll();
            }
        }
        return list;
    }

    public boolean delete(Long checkpointId) {
        var checkpoint = taskRepository.findById(checkpointId);
        if(checkpoint.isEmpty()) return false;
        taskRepository.delete(checkpoint.get());
        return true;
    }
}
