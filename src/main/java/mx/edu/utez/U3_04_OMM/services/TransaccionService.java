package mx.edu.utez.U3_04_OMM.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mx.edu.utez.U3_04_OMM.DTO.SalesDto;
import mx.edu.utez.U3_04_OMM.config.ApiResponse;
import mx.edu.utez.U3_04_OMM.models.*;
import mx.edu.utez.U3_04_OMM.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransaccionService {
    private static final Logger logger = LogManager.getLogger(TransaccionService.class);

    private final TransaccionRepository transaccionRepository;
    private final VentaRepository ventaRepository;
    private final RentaRepository rentaRepository;
    private final ClienteRepository clienteRepository;
    private final AlmacenRepository almacenRepository;

    @Transactional
    public ResponseEntity<ApiResponse> create(SalesDto dto) {
        try {
            logger.info("Iniciando creación de transacción para cliente ID: {}", dto.getIdCliente());
            
            Optional<Cliente> cliente = clienteRepository.findById(dto.getIdCliente());
            if (cliente.isEmpty()) {
                logger.warn("Cliente no encontrado con ID: {}", dto.getIdCliente());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Cliente no encontrado"));
            }

            Optional<Almacen> almacenOpt = almacenRepository.findById(dto.getIdAlmacen());
            if (almacenOpt.isEmpty()) {
                logger.warn("Almacén no encontrado con ID: {}", dto.getIdAlmacen());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Almacén no encontrado"));
            }

            Almacen almacen = almacenOpt.get();
            if (!almacen.getStatus()) {
                logger.warn("Intento de usar almacén ocupado ID: {}", dto.getIdAlmacen());
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "El almacén ya está ocupado"));
            }

            almacen.setStatus(false);
            almacenRepository.saveAndFlush(almacen);

            if ("RENTA".equalsIgnoreCase(dto.getTipo())) {
                return crearRenta(dto, cliente.get(), almacen);
            } else if ("VENTA".equalsIgnoreCase(dto.getTipo())) {
                return crearVenta(dto, cliente.get(), almacen);
            } else {
                logger.error("Tipo de transacción inválido: {}", dto.getTipo());
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Tipo de transacción inválido"));
            }
        } catch (Exception e) {
            logger.error("Error al crear transacción: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor"));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> findAll() {
        try {
            logger.info("Consultando todas las transacciones");
            List<Transaccion> list = transaccionRepository.findAll();
            return ResponseEntity.ok(new ApiResponse(list, HttpStatus.OK, "Transacciones recuperadas exitosamente"));
        } catch (Exception e) {
            logger.error("Error al recuperar las transacciones: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al recuperar las transacciones"));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> findById(Long id) {
        try {
            logger.info("Buscando transacción con ID: {}", id);
            Optional<Transaccion> transaccionOpt = transaccionRepository.findById(id);
            if (transaccionOpt.isEmpty()) {
                logger.warn("Transacción no encontrada con ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Transacción no encontrada"));
            }
            return ResponseEntity.ok(new ApiResponse(transaccionOpt.get(), HttpStatus.OK, "Transacción encontrada"));
        } catch (Exception e) {
            logger.error("Error al buscar transacción con ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al buscar la transacción"));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> update(Long id, SalesDto dto) {
        try {
            logger.info("Iniciando actualización de transacción ID: {}", id);
            Optional<Transaccion> transaccionOpt = transaccionRepository.findById(id);
            if (transaccionOpt.isEmpty()) {
                logger.warn("Transacción no encontrada para actualizar, ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Transacción no encontrada"));
            }

            Optional<Cliente> clienteOpt = clienteRepository.findById(dto.getIdCliente());
            if (clienteOpt.isEmpty()) {
                logger.warn("Cliente no encontrado para actualizar, ID: {}", dto.getIdCliente());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Cliente no encontrado"));
            }

            Optional<Almacen> almacenOpt = almacenRepository.findById(dto.getIdAlmacen());
            if (almacenOpt.isEmpty()) {
                logger.warn("Almacén no encontrado para actualizar, ID: {}", dto.getIdAlmacen());
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Almacén no encontrado"));
            }

            return actualizarTransaccion(transaccionOpt.get(), dto, clienteOpt.get(), almacenOpt.get());
        } catch (Exception e) {
            logger.error("Error al actualizar transacción ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar la transacción"));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> delete(Long id) {
        try {
            logger.info("Iniciando eliminación de transacción ID: {}", id);
            Optional<Transaccion> transaccionOpt = transaccionRepository.findById(id);
            if (transaccionOpt.isEmpty()) {
                logger.warn("No se encontró la transacción a eliminar, ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Transacción no encontrada"));
            }
            
            transaccionRepository.deleteById(id);
            logger.info("Transacción eliminada exitosamente, ID: {}", id);
            return ResponseEntity.ok(new ApiResponse(null, HttpStatus.OK, "Transacción eliminada correctamente"));
        } catch (Exception e) {
            logger.error("Error al eliminar transacción ID {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar la transacción"));
        }
    }

    private ResponseEntity<ApiResponse> crearRenta(SalesDto dto, Cliente cliente, Almacen almacen) {
        if (dto.getFechaFin() == null) {
            logger.warn("Intento de crear renta sin fecha de fin");
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La fecha de fin es obligatoria para rentas"));
        }
        
        Renta renta = new Renta();
        renta.setPrecio(dto.getPrecio());
        renta.setFechaInicio(dto.getFechaVenta());
        renta.setFechaFin(dto.getFechaFin());
        renta.setAlmacen(almacen);
        renta.setCliente(cliente);
        
        Renta rentaGuardada = rentaRepository.save(renta);
        logger.info("Renta creada exitosamente con ID: {}", rentaGuardada.getId());
        return ResponseEntity.ok(new ApiResponse(rentaGuardada, HttpStatus.CREATED, "Renta creada correctamente"));
    }

    private ResponseEntity<ApiResponse> crearVenta(SalesDto dto, Cliente cliente, Almacen almacen) {
        Venta venta = new Venta();
        venta.setPrecio(dto.getPrecio());
        venta.setFechaVenta(dto.getFechaVenta());
        venta.setAlmacen(almacen);
        venta.setCliente(cliente);
        
        Venta ventaGuardada = ventaRepository.save(venta);
        logger.info("Venta creada exitosamente con ID: {}", ventaGuardada.getId());
        return ResponseEntity.ok(new ApiResponse(ventaGuardada, HttpStatus.CREATED, "Venta creada correctamente"));
    }

    private ResponseEntity<ApiResponse> actualizarTransaccion(Transaccion transaccion, SalesDto dto, 
                                                            Cliente cliente, Almacen almacen) {
        transaccion.setAlmacen(almacen);
        transaccion.setCliente(cliente);
        transaccion.setPrecio(dto.getPrecio());

        if (transaccion instanceof Venta venta) {
            venta.setFechaVenta(dto.getFechaVenta());
            Venta ventaActualizada = ventaRepository.save(venta);
            logger.info("Venta actualizada exitosamente, ID: {}", ventaActualizada.getId());
            return ResponseEntity.ok(new ApiResponse(ventaActualizada, HttpStatus.OK, "Venta actualizada correctamente"));
        } else if (transaccion instanceof Renta renta) {
            if (dto.getFechaFin() == null) {
                logger.warn("Intento de actualizar renta sin fecha de fin, ID: {}", renta.getId());
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "La fecha de fin es obligatoria para rentas"));
            }
            renta.setFechaInicio(dto.getFechaVenta());
            renta.setFechaFin(dto.getFechaFin());
            Renta rentaActualizada = rentaRepository.save(renta);
            logger.info("Renta actualizada exitosamente, ID: {}", rentaActualizada.getId());
            return ResponseEntity.ok(new ApiResponse(rentaActualizada, HttpStatus.OK, "Renta actualizada correctamente"));
        } else {
            logger.error("Tipo de transacción desconocido para ID: {}", transaccion.getId());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Tipo de transacción desconocido"));
        }
    }
}