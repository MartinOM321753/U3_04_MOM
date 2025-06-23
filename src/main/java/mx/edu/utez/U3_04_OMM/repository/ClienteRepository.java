package mx.edu.utez.U3_04_OMM.repository;
import mx.edu.utez.U3_04_OMM.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCorreo(String correo);

}
