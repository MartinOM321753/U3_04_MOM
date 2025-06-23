package mx.edu.utez.U3_04_OMM.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mx.edu.utez.U3_04_OMM.config.ApiResponse;
import mx.edu.utez.U3_04_OMM.models.Almacen;
import mx.edu.utez.U3_04_OMM.models.Cede;
import mx.edu.utez.U3_04_OMM.repository.AlmacenRepository;
import mx.edu.utez.U3_04_OMM.repository.CedeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlmacenService {

    private final AlmacenRepository almacenRepository;
    private final CedeRepository cedeRepository;

    @Transactional
    public List<Almacen> findAll() {
        return almacenRepository.findAll();
    }

    @Transactional
    public ResponseEntity<ApiResponse> save(Almacen almacen) {
        try {
            Optional<Cede> cedeOpt = cedeRepository.findCedeByClave(almacen.getCede().getClave());

            if (cedeOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Cede no encontrado"));
            }

            Cede cede = cedeOpt.get();
            almacen.setCede(cede);
            almacen.setFechaRegistro(LocalDate.now());
            almacen.setStatus(true);
            
            Almacen savedAlmacen = almacenRepository.save(almacen);
            
            String claveGenerada = String.format("%s-A%d", cede.getClave(), savedAlmacen.getId());
            savedAlmacen.setClave(claveGenerada);
            
            return ResponseEntity.ok(new ApiResponse(
                almacenRepository.save(savedAlmacen), 
                HttpStatus.OK, 
                "Almacén guardado correctamente"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el almacén"));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> findById(Long id) {
        return almacenRepository.findById(id)
                .map(almacen -> ResponseEntity.ok(new ApiResponse(almacen, HttpStatus.OK, "")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Almacén no encontrado")));
    }

    @Transactional
    public ResponseEntity<ApiResponse> update(Long id, Almacen almacen) {
        Optional<Almacen> optional = almacenRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Almacén no encontrado"));
        }

        // Validar que el Cede exista
        Optional<Cede> cedeOpt = cedeRepository.findById(almacen.getCede().getId());
        if (cedeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Cede no encontrado"));
        }

        Almacen existente = optional.get();
        existente.setTamaño(almacen.getTamaño());
        existente.setStatus(almacen.getStatus());
        existente.setCede(cedeOpt.get());

        return ResponseEntity.ok(new ApiResponse(almacenRepository.save(existente), HttpStatus.OK, "Almacén actualizado correctamente"));
    }

    @Transactional
    public ResponseEntity<ApiResponse> delete(Long id) {
        Optional<Almacen> almacenOpt = almacenRepository.findById(id);
        if (almacenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Almacén no encontrado"));
        }

        Almacen almacen = almacenOpt.get();
        if (Boolean.FALSE.equals(almacen.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "No se puede eliminar un almacén ocupado"));
        }

        almacenRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(null, HttpStatus.OK, "Eliminado correctamente"));
    }
}