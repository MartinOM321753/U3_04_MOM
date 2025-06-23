package mx.edu.utez.U3_04_OMM.repository;

import mx.edu.utez.U3_04_OMM.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "SELECT * FROM users where email = :email",nativeQuery = true)
    Optional<User> findByEmail(String email);




}
