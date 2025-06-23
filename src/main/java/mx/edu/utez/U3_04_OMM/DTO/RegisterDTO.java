package mx.edu.utez.U3_04_OMM.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.U3_04_OMM.models.Role;
import mx.edu.utez.U3_04_OMM.models.User;
import jakarta.validation.constraints. *;
@Data
@Getter
@Setter
@AllArgsConstructor
public class RegisterDTO {
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 50, message = "El correo no puede tener más de 50 caracteres")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    private String password;

    @NotNull(message = "El rol no puede ser nulo")
    private Role role;

   public User toEntity() {
        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setRole(this.role);
        return user;
    }
}
