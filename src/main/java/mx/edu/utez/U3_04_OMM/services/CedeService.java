package mx.edu.utez.U3_04_OMM.services;


import lombok.AllArgsConstructor;
import mx.edu.utez.U3_04_OMM.config.ApiResponse;
import mx.edu.utez.U3_04_OMM.models.Cede;
import mx.edu.utez.U3_04_OMM.repository.CedeRepository;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CedeService {

    private final CedeRepository cedeRepository;

    @Transactional(readOnly = true)
    public List<Cede> findAll() {
        return cedeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById(Long id) {
        Optional<Cede> cede = cedeRepository.findById(id);
        if (cede.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(cede.get(), HttpStatus.OK, ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Cede no encontrada"));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> save(Cede cede) {
        try {
            Cede saved = cedeRepository.save(cede);

            String claveGenerada = generarClaveCede(saved.getId());
            saved.setClave(claveGenerada);

            cedeRepository.saveAndFlush(saved);

            return ResponseEntity.ok(new ApiResponse(saved, HttpStatus.OK, "Cede guardada correctamente"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar la cede"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> update(Long id, Cede cede) {
        Optional<Cede> cedeOpt = cedeRepository.findById(id);
        if (cedeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Cede no encontrada"));
        }
        Cede existente = cedeOpt.get();

        existente.setEstado(cede.getEstado());
        existente.setMunicipio(cede.getMunicipio());


        cedeRepository.save(existente);
        return ResponseEntity.ok(new ApiResponse(existente, HttpStatus.OK, "Cede actualizada correctamente"));
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> delete(Long id) {
        Optional<Cede> cedeOpt = cedeRepository.findById(id);
        if (cedeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Cede no encontrada"));
        }
        cedeRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(null, HttpStatus.OK, "Cede eliminada correctamente"));
    }



    private String generarClaveCede(Long id) {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        int randomNum = 1000 + new Random().nextInt(9000);
        return "C" + id + "-" + fecha + "-" + randomNum;
    }
}