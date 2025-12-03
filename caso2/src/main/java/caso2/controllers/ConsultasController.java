package caso2.controllers;

import caso2.domain.Usuario;
import caso2.repository.UsuarioRepository;
import caso2.service.RolService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/consultas")
public class ConsultasController {
    private final UsuarioRepository usuarioRepository;
    private final RolService rolService;

    public ConsultasController(UsuarioRepository usuarioRepository, RolService rolService) {
        this.usuarioRepository = usuarioRepository;
        this.rolService = rolService;
    }

    @GetMapping()
    public String mostrarConsultas(Model model) {
        model.addAttribute("roles", rolService.obtenerTodosLosRoles());
        long usuariosActivos = usuarioRepository.countByActivo(true);
        long usuariosInactivos = usuarioRepository.countByActivo(false);
        
        model.addAttribute("usuariosActivos", usuariosActivos);
        model.addAttribute("usuariosInactivos", usuariosInactivos);
        model.addAttribute("totalUsuarios", usuariosActivos + usuariosInactivos);
        
        return "/pages/consultas";
    }

    @GetMapping("/por-rol")
    public String consultarPorRol(@RequestParam("nombreRol") String nombreRol, Model model) {
        List<Usuario> usuarios = usuarioRepository.findByNombreRol(nombreRol);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("tipoConsulta", "Usuarios con rol: " + nombreRol);
        model.addAttribute("roles", rolService.obtenerTodosLosRoles());
        
        long usuariosActivos = usuarioRepository.countByActivo(true);
        long usuariosInactivos = usuarioRepository.countByActivo(false);
        model.addAttribute("usuariosActivos", usuariosActivos);
        model.addAttribute("usuariosInactivos", usuariosInactivos);
        model.addAttribute("totalUsuarios", usuariosActivos + usuariosInactivos);
        
        return "/pages/consultas";
    }

    @GetMapping("/por-fechas")
    public String consultarPorFechas(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            Model model) {
        List<Usuario> usuarios = usuarioRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("tipoConsulta", "Usuarios creados entre " + fechaInicio + " y " + fechaFin);
        model.addAttribute("roles", rolService.obtenerTodosLosRoles());
        
        long usuariosActivos = usuarioRepository.countByActivo(true);
        long usuariosInactivos = usuarioRepository.countByActivo(false);
        model.addAttribute("usuariosActivos", usuariosActivos);
        model.addAttribute("usuariosInactivos", usuariosInactivos);
        model.addAttribute("totalUsuarios", usuariosActivos + usuariosInactivos);
        
        return "/pages/consultas";
    }

    @GetMapping("/buscar")
    public String buscarPorTexto(@RequestParam("busqueda") String busqueda, Model model) {
        List<Usuario> usuarios = usuarioRepository.buscarPorTexto(busqueda);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("tipoConsulta", "Búsqueda: '" + busqueda + "'");
        model.addAttribute("roles", rolService.obtenerTodosLosRoles());
        
        long usuariosActivos = usuarioRepository.countByActivo(true);
        long usuariosInactivos = usuarioRepository.countByActivo(false);
        model.addAttribute("usuariosActivos", usuariosActivos);
        model.addAttribute("usuariosInactivos", usuariosInactivos);
        model.addAttribute("totalUsuarios", usuariosActivos + usuariosInactivos);
        
        return "/pages/consultas";
    }

    @GetMapping("/recientes")
    public String usuariosRecientes(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAllByOrderByFechaCreacionDesc();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("tipoConsulta", "Usuarios ordenados por fecha de creación (más recientes primero)");
        model.addAttribute("roles", rolService.obtenerTodosLosRoles());
        
        long usuariosActivos = usuarioRepository.countByActivo(true);
        long usuariosInactivos = usuarioRepository.countByActivo(false);
        model.addAttribute("usuariosActivos", usuariosActivos);
        model.addAttribute("usuariosInactivos", usuariosInactivos);
        model.addAttribute("totalUsuarios", usuariosActivos + usuariosInactivos);
        
        return "/pages/consultas";
    }
}