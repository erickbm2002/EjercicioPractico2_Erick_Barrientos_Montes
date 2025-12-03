package caso2.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportesController {

    @GetMapping("/reportes")
    public String reportes(Model model, Authentication authentication) {
        model.addAttribute("usuario", authentication.getName());
        return "/pages/reportes";
    }
}