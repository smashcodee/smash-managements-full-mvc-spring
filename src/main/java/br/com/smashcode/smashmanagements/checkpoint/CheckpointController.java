package br.com.smashcode.smashmanagements.checkpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
    public String delete(@PathVariable  Integer checkpointId, RedirectAttributes redirect) {
        if(service.delete(checkpointId)) {
            redirect.addFlashAttribute("success", "Checkpoint foi apagado com sucesso.");
        } else {
            redirect.addFlashAttribute("error", "Id inválido. O checkpoint não foi deletado.");
        }
        return "redirect:/checkpoint";
    }
}
