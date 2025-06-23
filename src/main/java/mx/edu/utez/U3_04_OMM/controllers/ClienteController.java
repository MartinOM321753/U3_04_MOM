package mx.edu.utez.U3_04_OMM.controllers;


import lombok.RequiredArgsConstructor;
import mx.edu.utez.U3_04_OMM.DTO.ClienteDTO;
import mx.edu.utez.U3_04_OMM.config.ApiResponse;
import mx.edu.utez.U3_04_OMM.models.Cliente;
import mx.edu.utez.U3_04_OMM.services.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping("/")
    public List<Cliente> getAll() {
        return clienteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        return clienteService.findById(id);
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse> create(@Validated @RequestBody ClienteDTO cliente) {
        return clienteService.save(cliente.toEntity());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@Validated @PathVariable Long id, @RequestBody ClienteDTO cliente) {
        return clienteService.update(id, cliente.toEntity());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        return clienteService.delete(id);
    }
}