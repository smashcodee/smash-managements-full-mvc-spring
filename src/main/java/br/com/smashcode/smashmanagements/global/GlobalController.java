package br.com.smashcode.smashmanagements.global;

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

@Controller
@RequestMapping("/global")
public class GlobalController {
    @Autowired
    private TaskService service;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("globals", service.findAll("GLOBAL"));
        return "views/global/index";
    }

    @GetMapping("/delete/{globalId}")
    public String delete(@PathVariable Long globalId, RedirectAttributes redirect) {
        if(service.delete(globalId)) {
            redirect.addFlashAttribute("success", "Global foi apagado com sucesso.");
        } else {
            redirect.addFlashAttribute("error", "Id inválido. A Global não foi deletado.");
        }
        return "redirect:/global";
    }

    @GetMapping("/criar")
    public String cadastroPage(Model model) {
        TaskEntity global = new TaskEntity();
        model.addAttribute("global", global);
        return "views/global/new/cadastro";
    }

    @PostMapping("/action/create")
    public String cadastroAction(@ModelAttribute("global") @Valid TaskPostRequest checkpoint,
                                 BindingResult result,
                                 RedirectAttributes redirect) {

        if(result.hasErrors()) {
            return "views/global/new/cadastro";
        }

        if(service.create(checkpoint, "GLOBAL")) {
            redirect.addFlashAttribute("success", "Global criado=a com sucesso!");
        } else {
            redirect.addFlashAttribute("error", "Erro ao criar a global.");
        }
        return "redirect:/global";
    }
}
