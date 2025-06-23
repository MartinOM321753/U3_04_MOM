package mx.edu.utez.U3_04_OMM.repository;

import mx.edu.utez.U3_04_OMM.models.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {}
