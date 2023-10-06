package br.com.smashcode.smashmanagements.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/global")
public class GlobalController {
    @Autowired
    private GlobalService service;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("globals", service.findAll());
        return "views/global/index";
    }

    @GetMapping("/delete/{globalId}")
    public String delete(@PathVariable Integer globalId, RedirectAttributes redirect) {
        if(service.delete(globalId)) {
            redirect.addFlashAttribute("success", "Global foi apagado com sucesso.");
        } else {
            redirect.addFlashAttribute("error", "Id inválido. A Global não foi deletado.");
        }
        return "redirect:/global";
    }
}
