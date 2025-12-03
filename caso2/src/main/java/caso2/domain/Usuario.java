package caso2.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idUsuario;

    @NotBlank
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombreUsuario;

    @NotBlank
    @Column(name = "apellido", nullable = false, length = 150)
    private String apellidoUsuario;

    @NotBlank
    @Column(name = "email", nullable = false, length = 200, unique = true)
    private String emailUsuario;

    @NotBlank
    @Column(name = "password", nullable = false, length = 255)
    private String passwordUsuario;

    @NotBlank
    @Column(name = "activo", nullable = false)
    private boolean activo;


    //Si falla la base de datos valdiar acá
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rol_id",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    ) 
    private Set<Rol> roles = new HashSet<>();
}
