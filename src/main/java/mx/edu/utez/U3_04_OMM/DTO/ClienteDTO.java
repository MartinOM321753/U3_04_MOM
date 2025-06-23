package mx.edu.utez.U3_04_OMM.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import mx.edu.utez.U3_04_OMM.models.Cliente;
import mx.edu.utez.U3_04_OMM.models.User;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ClienteDTO {

    private String nombreCompleto;
    private String telefono;
    private String correo;
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
