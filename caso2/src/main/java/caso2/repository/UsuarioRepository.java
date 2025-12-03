package caso2.repository;

import caso2.domain.Usuario;
import caso2.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    List<Usuario> findByActivoTrue();
    Optional<Usuario> findByEmailUsuario(String correo);

    List<Usuario> findByRol(Rol rol);
    
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombreRol = :nombreRol")
    List<Usuario> findByNombreRol(@Param("nombreRol") String nombreRol);

    @Query("SELECT u FROM Usuario u WHERE u.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Usuario> findByFechaCreacionBetween(
        @Param("fechaInicio") LocalDateTime fechaInicio,
        @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.emailUsuario) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR LOWER(u.nombreUsuario) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR LOWER(u.apellidoUsuario) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Usuario> buscarPorTexto(@Param("busqueda") String busqueda);

    long countByActivo(boolean activo);

    List<Usuario> findAllByOrderByFechaCreacionDesc();

    @Query("SELECT u FROM Usuario u WHERE u.rol.nombreRol = :nombreRol AND u.activo = :activo")
    List<Usuario> findByRolAndActivo(
        @Param("nombreRol") String nombreRol, 
        @Param("activo") boolean activo
    );
}