package mx.edu.utez.U3_04_OMM.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mx.edu.utez.U3_04_OMM.DTO.SalesDto;
import mx.edu.utez.U3_04_OMM.config.ApiResponse;
import mx.edu.utez.U3_04_OMM.models.*;
import mx.edu.utez.U3_04_OMM.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final VentaRepository ventaRepository;
    private final RentaRepository rentaRepository;
    private final ClienteRepository clienteRepository;
    private final AlmacenRepository almacenRepository;

    @Transactional
    public ResponseEntity<ApiResponse> create(SalesDto dto) {
        Optional<Cliente> cliente = clienteRepository.findById(dto.getIdCliente());
        if (cliente.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        Optional<Almacen> almacenOpt = almacenRepository.findById(dto.getIdAlmacen());
        if (almacenOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Almacén no encontrado"));

        Almacen almacen = almacenOpt.get();
        if (!almacen.getStatus())
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El almacén ya está ocupado"));

        almacen.setStatus(false);
        almacenRepository.saveAndFlush(almacen);

        if ("RENTA".equalsIgnoreCase(dto.getTipo())) {
            if (dto.getFechaFin() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La fecha de fin es obligatoria para rentas"));
            }
            Renta renta = new Renta();
            renta.setPrecio(dto.getPrecio());
            renta.setFechaInicio(dto.getFechaVenta());
            renta.setFechaFin(dto.getFechaFin());
            renta.setAlmacen(almacen);
            renta.setCliente(cliente.get());
            return ResponseEntity.ok(new ApiResponse(rentaRepository.save(renta), HttpStatus.CREATED, "Renta creada correctamente"));

        } else if ("VENTA".equalsIgnoreCase(dto.getTipo())) {
            Venta venta = new Venta();
            venta.setPrecio(dto.getPrecio());
            venta.setFechaVenta(dto.getFechaVenta());
            venta.setAlmacen(almacen);
            venta.setCliente(cliente.get());
            return ResponseEntity.ok(new ApiResponse(ventaRepository.save(venta), HttpStatus.CREATED, "Venta creada correctamente"));
        } else {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Tipo de transacción inválido"));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> findAll() {
        List<Transaccion> list = transaccionRepository.findAll();
        return ResponseEntity.ok(new ApiResponse(list, HttpStatus.OK, ""));
    }

    @Transactional
    public ResponseEntity<ApiResponse> findById(Long id) {
        Optional<Transaccion> transaccionOpt = transaccionRepository.findById(id);
        if (transaccionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Transacción no encontrada"));
        }
        return ResponseEntity.ok(new ApiResponse(transaccionOpt.get(), HttpStatus.OK, ""));
    }

    @Transactional
    public ResponseEntity<ApiResponse> update(Long id, SalesDto dto) {
        Optional<Transaccion> transaccionOpt = transaccionRepository.findById(id);
        if (transaccionOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Transacción no encontrada"));

        Optional<Cliente> clienteOpt = clienteRepository.findById(dto.getIdCliente());
        if (clienteOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        Optional<Almacen> almacenOpt = almacenRepository.findById(dto.getIdAlmacen());
        if (almacenOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Almacén no encontrado"));

        Almacen almacen = almacenOpt.get();

        Transaccion transaccion = transaccionOpt.get();

        transaccion.setAlmacen(almacen);
        transaccion.setCliente(clienteOpt.get());
        transaccion.setPrecio(dto.getPrecio());

        if (transaccion instanceof Venta venta) {
            venta.setFechaVenta(dto.getFechaVenta());
            return ResponseEntity.ok(new ApiResponse(ventaRepository.save(venta), HttpStatus.OK, "Venta actualizada correctamente"));
        } else if (transaccion instanceof Renta renta) {
            if (dto.getFechaFin() == null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La fecha de fin es obligatoria para rentas"));
            }
            renta.setFechaInicio(dto.getFechaVenta());
            renta.setFechaFin(dto.getFechaFin());
            return ResponseEntity.ok(new ApiResponse(rentaRepository.save(renta), HttpStatus.OK, "Renta actualizada correctamente"));
        } else {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Tipo de transacción desconocido"));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> delete(Long id) {
        Optional<Transaccion> transaccionOpt = transaccionRepository.findById(id);
        if (transaccionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Transacción no encontrada"));
        }
        transaccionRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(null, HttpStatus.OK, "Transacción eliminada correctamente"));
    }
}
