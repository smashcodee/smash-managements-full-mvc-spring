package br.com.smashcode.smashmanagements.globalchat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/globalchat")
public class GlobalChatController {

    @GetMapping
    public ModelAndView indexPage() {
        ModelAndView mv = new ModelAndView("/views/globalchat/index");
        return mv;
    }
}
