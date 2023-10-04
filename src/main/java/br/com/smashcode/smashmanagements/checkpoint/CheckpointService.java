package br.com.smashcode.smashmanagements.checkpoint;

import br.com.smashcode.smashmanagements.task.ITaskRepository;
import br.com.smashcode.smashmanagements.task.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckpointService {
    @Autowired
    private ITaskRepository taskRepository;

    public List<TaskEntity> findAll() {
        return taskRepository.findAll();
    }

    public boolean delete(Integer checkpointId) {
        var checkpoint = taskRepository.findById(checkpointId);
        if(checkpoint.isEmpty()) return false;
        taskRepository.delete(checkpoint.get());
        return true;
    }
}
