package mx.edu.utez.U3_04_OMM.controllers;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.utez.U3_04_OMM.DTO.AlmacenDTO;
import mx.edu.utez.U3_04_OMM.config.ApiResponse;
import mx.edu.utez.U3_04_OMM.models.Almacen;
import mx.edu.utez.U3_04_OMM.services.AlmacenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/almacenes")
@RequiredArgsConstructor
public class AlmacenController {

    private final AlmacenService almacenService;

    @GetMapping("/")
    public List<Almacen> getAll() {
        return almacenService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return almacenService.findById(id);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody AlmacenDTO almacen) {
        return almacenService.save(almacen.toEntity());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @Valid @RequestBody AlmacenDTO almacen) {
        return almacenService.update(id, almacen.toEntity());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        return almacenService.delete(id);
    }
}
