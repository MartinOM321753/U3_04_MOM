package mx.edu.utez.U3_04_OMM.controllers;

import lombok.RequiredArgsConstructor;
import mx.edu.utez.U3_04_OMM.DTO.SalesDto;
import mx.edu.utez.U3_04_OMM.config.ApiResponse;
import mx.edu.utez.U3_04_OMM.services.TransaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Validated @RequestBody SalesDto dto) {
        return transaccionService.create(dto);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> findAll() {
        return transaccionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable Long id) {
        return transaccionService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@Validated @PathVariable Long id, @RequestBody SalesDto dto) {
        return transaccionService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        return transaccionService.delete(id);
    }
}
