package mx.edu.utez.U3_04_OMM.DTO;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.U3_04_OMM.models.Almacen;
import mx.edu.utez.U3_04_OMM.models.Cede;

@Data
@Getter
@Setter
@AllArgsConstructor
public class AlmacenDTO {

    @NotBlank(message = "El tamaño no puede estar vacío")
    @Size(max = 50, message = "El tamaño no puede tener más de 50 caracteres")
    private String tamaño;

    @NotBlank(message = "La clave de la sede no puede estar vacía")
    private String claveCede;

    public Almacen toEntity() {

        Almacen almacen = new Almacen();
        almacen.setTamaño(this.tamaño);

        Cede cedeEntity = new Cede();
        cedeEntity.setClave(this.claveCede);
        almacen.setCede(cedeEntity);

        return almacen;
    }

}
