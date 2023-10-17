package br.com.smashcode.smashmanagements.checkpoint;

import br.com.smashcode.smashmanagements.task.TaskEntity;
import br.com.smashcode.smashmanagements.task.TaskPostRequest;
import br.com.smashcode.smashmanagements.task.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    @Autowired
    private MessageSource messageSource;


    @GetMapping
    public String index(Model model, @AuthenticationPrincipal OAuth2User user) {
        model.addAttribute("checkpoints", service.findAll("CHECKPOINT"));
        addUserOauth2Atributtes(model, user);
        return "views/checkpoint/index";
    }

    @GetMapping("action/delete/{checkpointId}")
    public String delete(@PathVariable Long checkpointId, RedirectAttributes redirect) {
        if(service.delete(checkpointId)) {
            var message = "O Checkpoint "+ getMessage("task.delete.success");
            redirect.addFlashAttribute("success", message);
        } else {
            var message = "O Checkpoint "+ getMessage("task.delete.failure");
            redirect.addFlashAttribute("error", message);
        }
        return "redirect:/checkpoint";
    }

    @GetMapping("/criar")
    public String cadastroPage(Model model, @AuthenticationPrincipal OAuth2User user) {
        TaskEntity checkpoint = new TaskEntity();
        model.addAttribute("checkpoint", checkpoint);
        addUserOauth2Atributtes(model, user);
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
            var message = "Checkpoint " + getMessage("task.create.success");
            redirect.addFlashAttribute("success", message);
        } else {
            var message = getMessage("task.create.failure.any");
            redirect.addFlashAttribute("error", message);
        }
        return "redirect:/checkpoint";
    }

    @GetMapping("/edit/{id}")
    public String edicaoPage(Model model, @PathVariable("id") Long id, TaskEntity checkpoint, RedirectAttributes redirect, @AuthenticationPrincipal OAuth2User user) {
        TaskEntity entity;
        Optional<TaskEntity> optional = service.findById(id);
        addUserOauth2Atributtes(model, user);
        if(optional.isPresent()) {
            entity = optional.get();
        } else {
            entity = new TaskEntity();
            var message = "Checkpoint " + getMessage("task.notfound");
            redirect.addFlashAttribute("error", message);
            return "redirect:/checkpoint";
        }
        model.addAttribute("checkpoint", entity);
        return "views/checkpoint/edit/editar";
    }

    @PostMapping("/action/update/{checkpointId}")
    public String actionUpdate(@ModelAttribute("checkpoint") @Valid TaskPostRequest checkpoint,
                               @PathVariable("checkpointId") Long checkpointId,
                               BindingResult result,
                               RedirectAttributes redirect) {
        if(result.hasErrors()) {
            return "views/checkpoint/edit/editar";
        }

        TaskEntity entity;
        Optional<TaskEntity> optional = service.findById(checkpointId);
        if(optional.isPresent()) {
            entity = optional.get();
        } else {
            entity = new TaskEntity();
            var message = "Checkpoint " + getMessage("task.notfound");
            redirect.addFlashAttribute("error", message);
            return "redirect:/checkpoint";
        }

        boolean wasEdited = service.update(checkpointId, entity, checkpoint);
        if(!wasEdited) {
            var message = getMessage("task.edit.failure.any");
            redirect.addFlashAttribute("error", message);
        } else {
            var message = "Checkpoint " + getMessage("task.edit.success");
            redirect.addFlashAttribute("success", message);
        }
        return "redirect:/checkpoint";
    }



    private String getMessage(String code){
        return  messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    private void addUserOauth2Atributtes(Model model, OAuth2User user) {
        model.addAttribute("username", user.getAttribute("login"));
        model.addAttribute("avatar", user.getAttribute("avatar_url"));
    }
}
