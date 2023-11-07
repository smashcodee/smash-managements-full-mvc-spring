package br.com.smashcode.smashmanagements.sprint;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sprint")
public class SprintController {
    @Autowired
    private TaskService service;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal OAuth2User user) {
        model.addAttribute("sprints", service.findAll("SPRINT"));
        addUserOauth2Atributtes(model, user);
        return "views/sprint/index";
    }

    @GetMapping("action/delete/{sprintId}")
    public String delete(@PathVariable Long sprintId, RedirectAttributes redirect) {
        if(service.delete(sprintId)) {
            var message = "A Sprint "+ getMessage("task.delete.success");
            redirect.addFlashAttribute("success", message);
        } else {
            var message = "A Sprint "+ getMessage("task.delete.failure");
            redirect.addFlashAttribute("error", message);
        }
        return "redirect:/sprint";
    }

    @GetMapping("/criar")
    public String cadastroPage(Model model, @AuthenticationPrincipal OAuth2User user){
        TaskEntity sprint = new TaskEntity();
        model.addAttribute("sprint", sprint);
        addUserOauth2Atributtes(model, user);
        return "views/sprint/new/cadastro";
    }

    @PostMapping("/action/create")
    public String cadastroAction(@ModelAttribute("sprint") @Valid TaskPostRequest sprint,
                                 BindingResult result,
                                 RedirectAttributes redirect) {

        if(result.hasErrors()) {
            return "views/sprint/new/cadastro";
        }

        if(service.create(sprint, "SPRINT")) {
            var message = "Sprint " + getMessage("task.create.success");
            redirect.addFlashAttribute("success", message);
        } else {
            var message = getMessage("task.create.failure.any");
            redirect.addFlashAttribute("error", message);
        }
        return "redirect:/sprint";
    }

    @GetMapping("/edit/{id}")
    public String edicaoPage(Model model, @PathVariable("id") Long id, TaskEntity sprint, RedirectAttributes redirect, @AuthenticationPrincipal OAuth2User user) {
        TaskEntity entity;
        Optional<TaskEntity> optional = service.findById(id);
        addUserOauth2Atributtes(model, user);
        if(optional.isPresent()) {
            entity = optional.get();
        } else {
            entity = new TaskEntity();
            var message = "Sprint " + getMessage("task.notfound");
            redirect.addFlashAttribute("error", message);
            return "redirect:/sprint";
        }
        model.addAttribute("sprint", entity);
        return "views/sprint/edit/editar";
    }

    
    @PostMapping("/action/update/{checkpointId}")
    public String actionUpdate(@ModelAttribute("sprint") @Valid TaskPostRequest sprint,
                               @PathVariable("sprintId") Long sprintId,
                               BindingResult result,
                               RedirectAttributes redirect) {
        if(result.hasErrors()) {
            return "views/sprint/edit/editar";
        }

        TaskEntity entity;
        Optional<TaskEntity> optional = service.findById(sprintId);
        if(optional.isPresent()) {
            entity = optional.get();
        } else {
            entity = new TaskEntity();
            var message = "Sprint " + getMessage("task.notfound");
            redirect.addFlashAttribute("error", message);
            return "redirect:/sprint";
        }

        boolean wasEdited = service.update(sprintId, entity, sprint);
        if(!wasEdited) {
            var message = getMessage("task.edit.failure.any");
            redirect.addFlashAttribute("error", message);
        } else {
            var message = "Sprint " + getMessage("task.edit.success");
            redirect.addFlashAttribute("success", message);
        }
        return "redirect:/sprint";
    }


    @GetMapping("action/inc/{id}")
    public String increment(@PathVariable Long id) {
        service.increment(id);
        return "redirect:/sprint";
    }

    @GetMapping("action/dec/{id}")
    public String decrement(@PathVariable Long id) {
        service.decrement(id);
        return "redirect:/sprint";
    }

    @GetMapping("action/catch/{id}")
    public String catchCheckpoint(@PathVariable Long id, @AuthenticationPrincipal OAuth2User user) {
        service.catchTask(id, UserEntity.convert(user));
        return "redirect:/sprint";
    }

    @GetMapping("action/drop/{id}")
    public String dropCheckpoint(@PathVariable Long id, @AuthenticationPrincipal OAuth2User user) {
        service.dropTask(id, UserEntity.convert(user));
        return "redirect:/sprint";
    }

    private String getMessage(String code){
        return  messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    private void addUserOauth2Atributtes(Model model, OAuth2User user) {
        model.addAttribute("username", user.getAttribute("login"));
        model.addAttribute("avatar", user.getAttribute("avatar_url"));
    }
}
