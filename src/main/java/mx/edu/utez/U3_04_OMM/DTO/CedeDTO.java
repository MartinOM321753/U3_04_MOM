package mx.edu.utez.U3_04_OMM.DTO;

import jakarta.validation.constraints. *;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utez.U3_04_OMM.models.Cede;

@Data
@Getter
@Setter
@AllArgsConstructor
public class CedeDTO {
    @NotBlank(message = "El estado no puede estar vacío")
    @Size(max = 50, message = "El estado no puede tener más de 50 caracteres")
    private String estado;

    @NotBlank(message = "El municipio no puede estar vacío")
    @Size(max = 50, message = "El municipio no puede tener más de 50 caracteres")
    private String municipio;
    public Cede toEntity() {
        Cede cede = new Cede();
        cede.setEstado(this.estado);
        cede.setMunicipio(this.municipio);
        return cede;
    }

}
