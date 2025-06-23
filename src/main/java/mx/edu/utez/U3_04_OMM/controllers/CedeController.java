package mx.edu.utez.U3_04_OMM.controllers;


import lombok.RequiredArgsConstructor;
import mx.edu.utez.U3_04_OMM.DTO.CedeDTO;
import mx.edu.utez.U3_04_OMM.config.ApiResponse;
import mx.edu.utez.U3_04_OMM.models.Cede;
import mx.edu.utez.U3_04_OMM.services.CedeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/cedes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CedeController {

    private final CedeService cedeService;

    @GetMapping("/")
    public List<Cede> getAll() {
        return cedeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return cedeService.findById(id);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> create(@Validated @RequestBody CedeDTO cede) {
        return cedeService.save(cede.toEntity());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@Validated @PathVariable Long id, @RequestBody CedeDTO cede) {
        return cedeService.update(id, cede.toEntity());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        return cedeService.delete(id);
    }
}