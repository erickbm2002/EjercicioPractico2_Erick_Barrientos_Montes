package caso2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import caso2.domain.Usuario;

public interface UsuarioRepository  extends JpaRepository<Usuario, Integer> {
    public List<Usuario> findByActivoTrue();

    public Optional<Usuario> findByEmailUsuario(String correo);

}
