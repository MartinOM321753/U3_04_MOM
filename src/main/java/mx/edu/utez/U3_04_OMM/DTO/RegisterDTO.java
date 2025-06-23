package mx.edu.utez.U3_04_OMM.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.U3_04_OMM.models.Role;
import mx.edu.utez.U3_04_OMM.models.User;

@Data
@Getter
@Setter
@AllArgsConstructor
public class RegisterDTO {
    private String email;
    private String password;
    private Role role;

   public User toEntity() {
        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setRole(this.role);
        return user;
    }
}
