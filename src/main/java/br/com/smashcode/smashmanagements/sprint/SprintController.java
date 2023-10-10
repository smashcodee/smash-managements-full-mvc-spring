package br.com.smashcode.smashmanagements.sprint;

import br.com.smashcode.smashmanagements.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sprint")
public class SprintController {
    @Autowired
    private TaskService service;
    @GetMapping
    public String index(Model model) {
        model.addAttribute("sprints", service.findAll("SPRINT"));
        return "views/sprint/index";
    }

    @GetMapping("/delete/{sprintId}")
    public String delete(@PathVariable Long sprintId, RedirectAttributes redirect) {
        if(service.delete(sprintId)) {
            redirect.addFlashAttribute("success", "Sprint foi apagada com sucesso.");
        } else {
            redirect.addFlashAttribute("error", "Id inválido. A Sprint não foi deletada.");
        }
        return "redirect:/sprint";
    }
}
