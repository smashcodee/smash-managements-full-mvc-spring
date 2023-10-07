package br.com.smashcode.smashmanagements.checkpoint;

import br.com.smashcode.smashmanagements.task.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/checkpoint")
public class CheckpointController {
    @Autowired
    private CheckpointService service;
    @GetMapping
    public String index(Model model) {
        model.addAttribute("checkpoints", service.findAll());
        return "views/checkpoint/index";
    }

    @GetMapping("/delete/{checkpointId}")
    public String delete(@PathVariable Integer checkpointId, RedirectAttributes redirect) {
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
    public String cadastroAction(@ModelAttribute("checkpoint") TaskEntity checkpoint, RedirectAttributes redirect) {
        if(service.create(checkpoint)) {
            redirect.addFlashAttribute("success", "Checkpoint criado com sucesso!");
        } else {
            redirect.addFlashAttribute("error", "Erro ao criar o checkpoint.");
        }
        return "redirect:/checkpoint";
    }

    @GetMapping("/editar/{id}")
    public String edicaoPage(Model model, @PathVariable Integer id, TaskEntity checkpoint, RedirectAttributes redirect) {
        TaskEntity entity;
        System.out.println(id);
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
