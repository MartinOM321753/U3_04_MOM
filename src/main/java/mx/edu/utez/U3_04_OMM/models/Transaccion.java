package mx.edu.utez.U3_04_OMM.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_transaccion", discriminatorType = DiscriminatorType.STRING)
@Table(name = "transacciones")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public abstract class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double precio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"transacciones", "hibernateLazyInitializer", "handler"})

    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_id", nullable = false)
    @JsonIgnoreProperties({"ventas", "hibernateLazyInitializer", "handler"})

    private Almacen almacen;

}
