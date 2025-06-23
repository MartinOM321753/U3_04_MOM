package mx.edu.utez.U3_04_OMM.repository;

import mx.edu.utez.U3_04_OMM.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByname(String name);

}