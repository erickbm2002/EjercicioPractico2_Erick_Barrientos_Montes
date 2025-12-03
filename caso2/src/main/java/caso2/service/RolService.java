package caso2.service;

import caso2.domain.Rol;
import caso2.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {
    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Rol> obtenerRolPorId(Integer idRol) {
        return rolRepository.findById(idRol);
    }

    @Transactional
    public void guardarRol(Rol rol) {
        // Validar que no exista otro rol con el mismo nombre
        Optional<Rol> rolDuplicado = rolRepository.findByNombreRol(rol.getNombreRol());
        if (rolDuplicado.isPresent()) {
            Rol rolEncontrado = rolDuplicado.get();
            if (rol.getIdRol() == 0 || rolEncontrado.getIdRol() != rol.getIdRol()) {
                throw new RuntimeException("Ya existe un rol con el nombre: " + rol.getNombreRol());
            }
        }
        rolRepository.save(rol);
    }

    @Transactional
    public void eliminarRol(Integer idRol) {
        if (!rolRepository.existsById(idRol)) {
            throw new IllegalArgumentException("No se encontró el rol con id: " + idRol);
        }
        try {
            rolRepository.deleteById(idRol);
        } catch (Exception e) {
            throw new IllegalStateException("No se puede eliminar el rol. Tiene usuarios asociados.", e);
        }
    }
}