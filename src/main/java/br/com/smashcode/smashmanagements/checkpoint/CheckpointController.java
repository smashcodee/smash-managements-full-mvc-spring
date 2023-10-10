package br.com.smashcode.smashmanagements.checkpoint;

import br.com.smashcode.smashmanagements.task.TaskEntity;
import br.com.smashcode.smashmanagements.task.TaskPostRequest;
import br.com.smashcode.smashmanagements.task.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/checkpoint")
public class CheckpointController {
    @Autowired
    private TaskService service;
    @GetMapping
    public String index(Model model) {
        model.addAttribute("checkpoints", service.findAll("CHECKPOINT"));
        return "views/checkpoint/index";
    }

    @GetMapping("action/delete/{checkpointId}")
    public String delete(@PathVariable Long checkpointId, RedirectAttributes redirect) {
        if(service.delete(checkpointId)) {
            redirect.addFlashAttribute("success", "Checkpoint foi apagado com sucesso.");
        } else {
            redirect.addFlashAttribute("error", "Id inválido. O checkpoint não foi deletado.");
        }
        return "redirect:/checkpoint";
    }

    @GetMapping("/criar")
    public String cadastroPage(Model model) {
        TaskEntity checkpoint = new TaskEntity();
        model.addAttribute("checkpoint", checkpoint);
        return "views/checkpoint/new/cadastro";
    }

    @PostMapping("/action/create")
    public String cadastroAction(@ModelAttribute("checkpoint") @Valid TaskPostRequest checkpoint,
                                 BindingResult result,
                                 RedirectAttributes redirect) {

        if(result.hasErrors()) {
            return "views/checkpoint/new/cadastro";
        }

        if(service.create(checkpoint, "CHECKPOINT")) {
            redirect.addFlashAttribute("success", "Checkpoint criado com sucesso!");
        } else {
            redirect.addFlashAttribute("error", "Erro ao criar o checkpoint.");
        }
        return "redirect:/checkpoint";
    }

    @GetMapping("/edit/{id}")
    public String edicaoPage(Model model, @PathVariable("id") Long id, TaskEntity checkpoint, RedirectAttributes redirect) {
        TaskEntity entity;
        Optional<TaskEntity> optional = service.findById(id);
        if(optional.isPresent()) {
            entity = optional.get();
        } else {
            entity = new TaskEntity();
            redirect.addFlashAttribute("error", "Id do checkpoint inválido.");
            return "redirect:/checkpoint";
        }
        model.addAttribute("checkpoint", entity);
        return "views/checkpoint/edit/edit";
    }
}
