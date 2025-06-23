package mx.edu.utez.U3_04_OMM.repository;

import mx.edu.utez.U3_04_OMM.models.Cede;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CedeRepository extends JpaRepository<Cede, Long> {

    Optional<Cede> findCedeByClave(String clave);
}
