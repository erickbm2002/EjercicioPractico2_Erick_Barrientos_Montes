package caso2.controllers;

import caso2.domain.Rol;
import caso2.service.RolService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/roles")
public class RolController {
    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;

    }

    @GetMapping()
    public String listaRoles(Model model) {
        var roles = rolService.obtenerTodosLosRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("totalRoles", roles.size());
        return "/pages/roles";
    }

    @GetMapping("/nuevo")
    public String nuevoRol(Model model) {
        model.addAttribute("rol", new Rol());
        return "/pages/rolFormulario";
    }

    @GetMapping("/modificar/{idRol}")
    public String modificarRol(@PathVariable("idRol") Integer idRol, Model model, RedirectAttributes redirectAttributes) {
        Optional<Rol> rolOpt = rolService.obtenerRolPorId(idRol);
        if (rolOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El rol no fue encontrado.");
            return "redirect:/roles";
        }
        model.addAttribute("rol", rolOpt.get());
        return "/pages/rolFormulario";
    }

    @PostMapping("/guardar")
    public String guardarRol(@Valid Rol rol, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Error en la validación del formulario.");
            return "redirect:/roles/nuevo";
        }
        try {
            rolService.guardarRol(rol);
            redirectAttributes.addFlashAttribute("success", "Rol guardado exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/roles/nuevo";
        }
        return "redirect:/roles";
    }

    @PostMapping("/eliminar")
    public String eliminarRol(@RequestParam Integer idRol, RedirectAttributes redirectAttributes) {
        try {
            rolService.eliminarRol(idRol);
            redirectAttributes.addFlashAttribute("success", "Rol eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "El rol no existe.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el rol. Tiene usuarios asociados.");
        }
        return "redirect:/roles";
    }
}