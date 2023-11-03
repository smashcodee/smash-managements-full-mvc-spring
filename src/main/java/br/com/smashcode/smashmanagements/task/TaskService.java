package br.com.smashcode.smashmanagements.task;

import br.com.smashcode.smashmanagements.user.UserEntity;
import br.com.smashcode.smashmanagements.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskService {
    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    private UserService userService;

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
        entity.setTitle(request.title());
        entity.setScore(request.score());
        entity.setDescription(request.description());


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

    public boolean update(Long id, TaskEntity entity, TaskPostRequest request) {
        entity.setId(id);
        entity.setTitle(request.title());
        entity.setScore(request.score());
        entity.setDescription(request.description());
        entity.setUpdatedAt(LocalDateTime.now());
        var persited = taskRepository.saveAndFlush(entity);
        if(persited != null) {
            return true;
        }
        return false;
    }

    public void decrement(Long id) {
        var optional = findById(id);
        if(!optional.isPresent()) {
            throw new RuntimeException("Tarefa não encontrada,");
        }

        var task = optional.get();
        if(task.getStatus() == 0) {
            throw new RuntimeException("O status não pode ser negativo.");
        }

        task.setStatus(task.getStatus() - 10);
        taskRepository.save(task);
    }

    public void increment(Long id) {
        var optional = findById(id);
        if(!optional.isPresent()) {
            throw new RuntimeException("Tarefa não encontrada,");
        }

        var task = optional.get();
        if(task.getStatus() == 100) {
            throw new RuntimeException("O status não pode ser maior do que 100%.");
        }

        task.setStatus(task.getStatus() + 10);
        if(task.getStatus() == 100) {
            var user = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userService.addScore(UserEntity.convert(user), task.getScore());
        }

        taskRepository.save(task);
    }


    public void catchTask(Long id, UserEntity user) {
        var optional = findById(id);
        if(!optional.isPresent()) {
            throw new RuntimeException("Tarefa não encontrada.");
        }

        var task = optional.get();

        if ( task.getUser() != null && task.getUser().equals(user))
            throw new RuntimeException("você já selecionou essa tarefa");

        if (task.getUser() != null)
            throw new RuntimeException("tarefa já atribuída");

        task.setUser(user);
        taskRepository.save(task);
    }

    public void dropTask(Long id, UserEntity user) {
        var optional = findById(id);
        if(!optional.isPresent()) {
            throw new RuntimeException("Tarefa não encontrada.");
        }

        var task = optional.get();
        if(task.getUser() == null ||!task.getUser().getName().equals(user.getName())) {
            throw new RuntimeException("Impossível remover uma associação com uma tarefa não associada a ninguém.");
        }

        task.setUser(null);
        taskRepository.save(task);
    }
}
