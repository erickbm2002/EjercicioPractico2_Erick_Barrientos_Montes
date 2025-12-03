package caso2.controllers;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import caso2.domain.Usuario;
import caso2.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    public UsuarioController(UsuarioService usuarioService, MessageSource messageSource) {
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    @GetMapping()
    public String listaUsuarios(Model model) {
        var usuarios = usuarioService.obtenerUsuariosActivos(false);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "/pages/usuarios";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid Usuario usuario, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("Usuario.error04", null, Locale.getDefault()));
            if (usuario.getIdUsuario() == null) {
                return "redirect:/usuario";
            }
            return "redirect:/usuario/modifcar" + usuario.getIdUsuario();
        }
        usuarioService.guardarUsuario(usuario, true);
        redirectAttributes.addFlashAttribute("success",
                messageSource.getMessage("Usuario.success01", null, Locale.getDefault()));
        return "redirect:/usuarios";
    }
    
    @PostMapping("/eliminar") 

    public String eliminarUsuario(@RequestParam Integer idUsuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteUsuario(idUsuario);
            redirectAttributes.addFlashAttribute("success",
                    messageSource.getMessage("Usuario.success02", null, Locale.getDefault()));
        } catch (IllegalArgumentException e) {
            // Captura argumento inválido para el mensaje de "no existe"
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error01", null,
                            Locale.getDefault()));
        } catch (IllegalStateException e) {
            // Captura estado ilegal para el mensaje de "datos asociados"
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error02", null,
                            Locale.getDefault()));
        } catch (NoSuchMessageException e) {
            // Captura cualquier otra excepción inesperada
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error03", null,
                            Locale.getDefault()));
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/modificar/{idUsuario}")
    public String modificarUsuario(@PathVariable("idUsuario") Integer idUsuario, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(idUsuario);
        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("Error", "El usuario no fue encontrado.");
            return "redirect:/usuarios";
        }
        Usuario usuario = usuarioOpt.get();
        usuario.setPasswordUsuario("");
        model.addAttribute("usuario", usuario);
        return "/pages/usuarioModificar";
    }
    
}
