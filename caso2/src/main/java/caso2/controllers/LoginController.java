package caso2.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "/pages/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();
            
            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/usuarios";
            } else if (role.equals("ROLE_PROFESOR")) {
                return "redirect:/reportes";
            } else if (role.equals("ROLE_ESTUDIANTE")) {
                return "redirect:/usuarios/perfil";
            }
        }
        return "redirect:/Home";
    }
}