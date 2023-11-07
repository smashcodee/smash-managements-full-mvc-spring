package br.com.smashcode.smashmanagements.global;

import br.com.smashcode.smashmanagements.task.TaskEntity;
import br.com.smashcode.smashmanagements.task.TaskPostRequest;
import br.com.smashcode.smashmanagements.task.TaskService;
import br.com.smashcode.smashmanagements.user.UserEntity;
import jakarta.validation.Valid;

import java.util.Optional;

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

@Controller
@RequestMapping("/global")
public class GlobalController {
    @Autowired
    private TaskService service;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String index(Model model,@AuthenticationPrincipal OAuth2User user ) {
        model.addAttribute("globals", service.findAll("GLOBAL"));
        addUserOauth2Atributtes(model, user);
        return "views/global/index";
    }

    @GetMapping("action/delete/{globalId}")
    public String delete(@PathVariable Long globalId, RedirectAttributes redirect) {
        if(service.delete(globalId)) {
            redirect.addFlashAttribute("success", "Global foi apagado com sucesso.");
        } else {
            redirect.addFlashAttribute("error", "Id inválido. A Global não foi deletado.");
        }
        return "redirect:/global";
    }

    @GetMapping("/criar")
    public String cadastroPage(Model model, @AuthenticationPrincipal OAuth2User user) {
        TaskEntity global = new TaskEntity();
        model.addAttribute("global", global);
        addUserOauth2Atributtes(model, user);
        return "views/global/new/cadastro";
    }

    @PostMapping("/action/create")
    public String cadastroAction(@ModelAttribute("global") @Valid TaskPostRequest global,
                                 BindingResult result,
                                 RedirectAttributes redirect) {

        if(result.hasErrors()) {
            return "views/global/new/cadastro";
        }

        if(service.create(global, "GLOBAL")) {
            var message = "Global " + getMessage("task.create.success");
            redirect.addFlashAttribute("success", message);
        } else {
            var message = getMessage("task.create.failure.any");
            redirect.addFlashAttribute("error", message);
        }
        return "redirect:/global";
    }

    @GetMapping("/edit/{id}")
    public String edicaoPage(Model model, @PathVariable("id") Long id, TaskEntity global, RedirectAttributes redirect, @AuthenticationPrincipal OAuth2User user){
        TaskEntity entity;
        Optional<TaskEntity> optional = service.findById(id);
        addUserOauth2Atributtes(model, user);
        if(optional.isPresent()) {
            entity = optional.get();
        } else {
            entity = new TaskEntity();
            var message = "Global " + getMessage("task.notfound");
            redirect.addFlashAttribute("error", message);
            return "redirect:/global";
        }
        model.addAttribute("global", entity);
        return "views/global/edit/editar";
    }

    @PostMapping("action/update/{globalId}")
    public String actionUpdate(@ModelAttribute("global") @Valid TaskPostRequest global,
                               @PathVariable("globalId") Long globalId,
                               BindingResult result,
                               RedirectAttributes redirect) {
        if(result.hasErrors()) {
            return "views/checkpoint/edit/editar";
        }

        TaskEntity entity;
        Optional<TaskEntity> optional = service.findById(globalId);
        if(optional.isPresent()) {
            entity = optional.get();
        } else {
            entity = new TaskEntity();
            var message = "Global " + getMessage("task.notfound");
            redirect.addFlashAttribute("error", message);
            return "redirect:/global";
        }

        boolean wasEdited = service.update(globalId, entity, global);
        if(!wasEdited) {
            var message = getMessage("task.edit.failure.any");
            redirect.addFlashAttribute("error", message);
        } else {
            var message = "Checkpoint " + getMessage("task.edit.success");
            redirect.addFlashAttribute("success", message);
        }
        return "redirect:/global";
    }


    @GetMapping("action/inc/{id}")
    public String increment(@PathVariable Long id) {
        service.increment(id);
        return "redirect:/checkpoint";
    }

    @GetMapping("action/dec/{id}")
    public String decrement(@PathVariable Long id) {
        service.decrement(id);
        return "redirect:/global";
    }

    @GetMapping("action/catch/{id}")
    public String catchCheckpoint(@PathVariable Long id, @AuthenticationPrincipal OAuth2User user) {
        service.catchTask(id, UserEntity.convert(user));
        return "redirect:/global";
    }

    @GetMapping("action/drop/{id}")
    public String dropCheckpoint(@PathVariable Long id, @AuthenticationPrincipal OAuth2User user) {
        service.dropTask(id, UserEntity.convert(user));
        return "redirect:/global";
    }

    private String getMessage(String code){
        return  messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    private void addUserOauth2Atributtes(Model model, OAuth2User user) {
        model.addAttribute("username", user.getAttribute("login"));
        model.addAttribute("avatar", user.getAttribute("avatar_url"));
    }
}
