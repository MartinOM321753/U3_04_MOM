package mx.edu.utez.U3_04_OMM.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints. *;
import mx.edu.utez.U3_04_OMM.models.Cliente;
import mx.edu.utez.U3_04_OMM.models.User;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ClienteDTO {

    @NotBlank(message = "El nombre completo no puede estar vacío")
    @Size(max = 100, message = "El nombre completo no puede tener más de 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo puede contener letras y espacios")
    private String nombreCompleto;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener 10 dígitos")
    private String telefono;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 50, message = "El correo no puede tener más de 50 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    private String password;


    public Cliente toEntity() {

        Cliente cliente = new Cliente();
        cliente.setNombreCompleto(this.nombreCompleto);
        cliente.setTelefono(this.telefono);
        cliente.setCorreo(this.correo);

        User userEntity = new User();
        userEntity.setEmail(this.correo);
        userEntity.setPassword(this.password);
        cliente.setUser(userEntity);

        return cliente;
    }
}
