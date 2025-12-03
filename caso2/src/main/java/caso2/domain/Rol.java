package caso2.domain;

import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "rol")
public class Rol implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int idRol;

    @NotBlank
    @Column(name = "nombre", length = 100)
    private String nombreRol;
    
    @NotBlank
    @Column(name = "descripcion", length = 255)
    private String descripcionRol;

}
