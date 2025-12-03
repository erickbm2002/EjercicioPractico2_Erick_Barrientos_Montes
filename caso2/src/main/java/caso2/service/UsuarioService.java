package caso2.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import caso2.domain.Rol;
import caso2.domain.Usuario;
import caso2.repository.RolRepository;
import caso2.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;


@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorId(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosActivos(Boolean activo) {
        if (activo)
            return usuarioRepository.findByActivoTrue();
        return usuarioRepository.findAll();
    }



    @Transactional
    public void guardarUsuario(Usuario usuario, boolean encriptaclave) {
        final Integer idUsuario = usuario.getIdUsuario();
        Optional<Usuario> usuarioDuplicado = usuarioRepository.findByEmailUsuario(usuario.getEmailUsuario());
        if (usuarioDuplicado.isPresent()) {
            Usuario usuarioEncontrado = usuarioDuplicado.get();
            if (idUsuario == null || !usuarioEncontrado.getIdUsuario().equals(idUsuario)) {
                throw new RuntimeException("Ya existe un usuario con el correo: " + usuario.getEmailUsuario());
            }
        }

        var asignarRol = false;
        if (usuario.getIdUsuario() == null) {
            if (usuario.getPasswordUsuario() == null || usuario.getPasswordUsuario().isBlank()) {
                throw new RuntimeException("La clave es obligatoria para un nuevo usuario");
            }
            usuario.setPasswordUsuario(encriptaclave ? passwordEncoder.encode(usuario.getPasswordUsuario())
                    : usuario.getPasswordUsuario());
            asignarRol = true;

        } else {
            if (usuario.getPasswordUsuario() != null && !usuario.getPasswordUsuario().isBlank()) {
                usuario.setPasswordUsuario(encriptaclave ? passwordEncoder.encode(usuario.getPasswordUsuario())
                        : usuario.getPasswordUsuario());
            } else {
                if (usuario.getPasswordUsuario() == null || usuario.getPasswordUsuario().isBlank()) {
                    Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario()).orElseThrow(
                            () -> new RuntimeException("No se encontró el usuario con id: " + usuario.getIdUsuario()));
                    usuario.setPasswordUsuario(
                            encriptaclave ? passwordEncoder.encode(usuarioExistente.getPasswordUsuario())
                                    : usuarioExistente.getPasswordUsuario());
                } else {
                    usuario.setPasswordUsuario(encriptaclave ? passwordEncoder.encode(usuario.getPasswordUsuario())
                            : usuario.getPasswordUsuario());
                }
            }
        }

        usuarioRepository.save(usuario);
        if (asignarRol) {
            asignarRolUsuario(usuario.getEmailUsuario(), "USER");
        }
    }

    @Transactional
    public void deleteUsuario(Integer idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RuntimeException("No se encontró el usuario con id: " + idUsuario);
        }
        try {
            usuarioRepository.deleteById(idUsuario);
        } catch (Exception e) {
            throw new IllegalStateException("No se puede eliminar el usuario. Tiene datos asociados.", e);
        }
    }

    @Transactional
    public Usuario asignarRolUsuario(String correo, String rolName) {
        Optional<Usuario> usuariOpt = usuarioRepository.findByEmailUsuario(correo);
        if (usuariOpt.isEmpty()) {
            throw new RuntimeException("No se encontró el usuario con correo: " + correo);
        }
        Usuario usuario = usuariOpt.get();
        Optional<Rol> rolOpt = rolRepository.findByNombreRol(rolName);
        if (rolOpt.isEmpty()) {
            throw new RuntimeException("No se encontró el rol con nombre: " + rolName);
        }
        Rol rol = rolOpt.get();
        usuario.getRoles().add(rol);
        return usuarioRepository.save(usuario);
    }
    
}
