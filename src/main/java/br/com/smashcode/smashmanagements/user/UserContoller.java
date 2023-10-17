package br.com.smashcode.smashmanagements.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserContoller {
    @GetMapping("/login")
    public String loginPage() {
        return "views/auth/login";
    }

    @GetMapping("/logout")
    public String logautPage() {
        return "views/auth/logout";
    }

}
