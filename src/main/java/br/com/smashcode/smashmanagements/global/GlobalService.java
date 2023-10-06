package br.com.smashcode.smashmanagements.global;

import br.com.smashcode.smashmanagements.task.ITaskRepository;
import br.com.smashcode.smashmanagements.task.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalService {
    @Autowired
    private ITaskRepository taskRepository;

    public List<TaskEntity> findAll() {
        return taskRepository.findAllGlobals();
    }

    public boolean delete(Integer globalId) {
        var global = taskRepository.findById(globalId);
        if(global.isEmpty()) return false;
        taskRepository.delete(global.get());
        return true;
    }
}
