package caso2.service;

import caso2.domain.Usuario;
import caso2.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository ) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorId(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmailUsuario(email);
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosActivos(Boolean soloActivos) {
        if (soloActivos) {
            return usuarioRepository.findByActivoTrue();
        }
        return usuarioRepository.findAll();
    }

    @Transactional
public void guardarUsuario(Usuario usuario, boolean encriptarClave) {
    final Integer idUsuario = usuario.getIdUsuario();
    
    // Validar email duplicado
    Optional<Usuario> usuarioDuplicado = usuarioRepository.findByEmailUsuario(usuario.getEmailUsuario());
    if (usuarioDuplicado.isPresent()) {
        Usuario usuarioEncontrado = usuarioDuplicado.get();
        if (idUsuario == null || !usuarioEncontrado.getIdUsuario().equals(idUsuario)) {
            throw new RuntimeException("Ya existe un usuario con el correo: " + usuario.getEmailUsuario());
        }
    }

    if (usuario.getRol() == null || usuario.getRol().getIdRol() == 0) {
        throw new RuntimeException("Debe seleccionar un rol para el usuario");
    }

    if (usuario.getIdUsuario() == null) {
        if (usuario.getPasswordUsuario() == null || usuario.getPasswordUsuario().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria para un nuevo usuario");
        }

    } else {

        if (usuario.getPasswordUsuario() == null || usuario.getPasswordUsuario().isBlank()) {

            Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("No se encontró el usuario con id: " + usuario.getIdUsuario()));
            usuario.setPasswordUsuario(usuarioExistente.getPasswordUsuario());
        }

    }

    usuarioRepository.save(usuario);
}

    @Transactional
    public void deleteUsuario(Integer idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new IllegalArgumentException("No se encontró el usuario con id: " + idUsuario);
        }
        try {
            usuarioRepository.deleteById(idUsuario);
        } catch (Exception e) {
            throw new IllegalStateException("No se puede eliminar el usuario. Tiene datos asociados.", e);
        }
    }
}