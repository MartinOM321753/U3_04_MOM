package mx.edu.utez.U3_04_OMM.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@DiscriminatorValue("VENTA")
public class Venta extends Transaccion {

    private LocalDate fechaVenta;

}
