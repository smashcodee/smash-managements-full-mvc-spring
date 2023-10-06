package br.com.smashcode.smashmanagements.sprint;

import br.com.smashcode.smashmanagements.task.ITaskRepository;
import br.com.smashcode.smashmanagements.task.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SprintService {
    @Autowired
    private ITaskRepository taskRepository;

    public List<TaskEntity> findAll() {
        return taskRepository.findAllSprints();
    }

    public boolean delete(Integer sprintId) {
        var sprint = taskRepository.findById(sprintId);
        if(sprint.isEmpty()) return false;
        taskRepository.delete(sprint.get());
        return true;
    }
}
