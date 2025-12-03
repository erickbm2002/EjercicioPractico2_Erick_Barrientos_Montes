package caso2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import caso2.domain.Rol;


public interface RolRepository  extends JpaRepository<Rol, Integer> {
    public List<Rol> findAll();

    public Optional<Rol> findByNombreRol(String rol);
}
