package mx.edu.utez.U3_04_OMM.DTO;

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
    private String estado;
    private String municipio;

    public Cede toEntity() {
        Cede cede = new Cede();
        cede.setEstado(this.estado);
        cede.setMunicipio(this.municipio);
        return cede;
    }

}
