package mx.edu.utez.U3_04_OMM.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesDto {

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private Double precio;

    @NotNull(message = "La fecha de venta es obligatoria")
    private LocalDate fechaVenta;

    private LocalDate fechaFin;

    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "VENTA|RENTA", message = "El tipo debe ser 'VENTA' o 'RENTA'")
    private String tipo;

    @NotNull(message = "El id del almacén es obligatorio")
    private Long idAlmacen;

    @NotNull(message = "El id del cliente es obligatorio")
    private Long idCliente;
}
