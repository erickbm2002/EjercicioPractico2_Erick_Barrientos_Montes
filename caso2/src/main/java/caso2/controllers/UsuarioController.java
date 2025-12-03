package caso2.controllers;

import caso2.domain.Usuario;
import caso2.service.RolService;
import caso2.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final RolService rolService;

    public UsuarioController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }

    @GetMapping()
    public String listaUsuarios(Model model) {
        var usuarios = usuarioService.obtenerUsuariosActivos(false);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "/pages/usuarios";
    }

    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolService.obtenerTodosLosRoles());
        return "/pages/usuarioFormulario";
    }

    @GetMapping("/modificar/{idUsuario}")
    public String modificarUsuario(@PathVariable("idUsuario") Integer idUsuario, Model model, 
            RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El usuario no fue encontrado.");
            return "redirect:/usuarios";
        }
        Usuario usuario = usuarioOpt.get();
        usuario.setPasswordUsuario("");
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.obtenerTodosLosRoles());
        return "/pages/usuarioFormulario";
    }

    @PostMapping("/guardar")
public String guardarUsuario(@Valid Usuario usuario, BindingResult bindingResult,
        RedirectAttributes redirectAttributes, Model model) {
    if (bindingResult.hasErrors()) {
        model.addAttribute("roles", rolService.obtenerTodosLosRoles());
        return "/pages/usuarioFormulario";
    }
    try {
        usuarioService.guardarUsuario(usuario, false); // false = NO encriptar
        redirectAttributes.addFlashAttribute("success", "Usuario guardado exitosamente.");
    } catch (RuntimeException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/usuarios/nuevo";
    }
    return "redirect:/usuarios";
}

    @PostMapping("/eliminar")
    public String eliminarUsuario(@RequestParam Integer idUsuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteUsuario(idUsuario);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "El usuario no existe.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar el usuario. Tiene datos asociados.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error inesperado al eliminar el usuario.");
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/perfil")
    public String verPerfil(Authentication authentication, Model model) {
        String email = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorEmail(email);
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
        }
        return "/pages/perfil";
    }
}