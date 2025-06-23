package mx.edu.utez.U3_04_OMM.services;

import lombok.AllArgsConstructor;
import mx.edu.utez.U3_04_OMM.config.ApiResponse;
import mx.edu.utez.U3_04_OMM.models.Cliente;
import mx.edu.utez.U3_04_OMM.models.User;
import mx.edu.utez.U3_04_OMM.repository.ClienteRepository;
import mx.edu.utez.U3_04_OMM.repository.UserRepository;
import mx.edu.utez.U3_04_OMM.services.security.AuthServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UserRepository userRepository;
    private final AuthServiceImpl authService;

    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse> findById(Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> ResponseEntity.ok(new ApiResponse(cliente, HttpStatus.OK, "")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Cliente no encontrado")));
    }

    @Transactional
    public ResponseEntity<ApiResponse> save(Cliente cliente) {
        try {
            if (clienteRepository.findByCorreo(cliente.getCorreo()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse(null, HttpStatus.CONFLICT, "El correo ya está registrado"));
            }

            User user = cliente.getUser();
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(null, HttpStatus.BAD_REQUEST, "Usuario requerido"));
            }

            if (user.getId() == null) {
               ResponseEntity<ApiResponse> userRegister =  authService.register(user);
                user = (User) userRegister.getBody().getData();
            } else {

                Optional<User> userOpt = userRepository.findById(user.getId());
                if (userOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Usuario asociado no encontrado"));
                }
                user = userOpt.get();
            }

            cliente.setUser(user);

            Cliente saved = clienteRepository.save(cliente);
            return ResponseEntity.ok(new ApiResponse(saved, HttpStatus.OK, "Cliente guardado correctamente"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar cliente"));
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse> update(Long id, Cliente cliente) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        }

        Optional<Cliente> existingByCorreo = clienteRepository.findByCorreo(cliente.getCorreo());
        if (existingByCorreo.isPresent() && !existingByCorreo.get().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(null, HttpStatus.CONFLICT, "El correo ya está registrado por otro cliente"));
        }

        // Validar si el usuario existe
        Optional<User> userOpt = userRepository.findById(cliente.getUser().getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Usuario asociado no encontrado"));
        }

        Cliente existente = clienteOpt.get();
        existente.setNombreCompleto(cliente.getNombreCompleto());
        existente.setTelefono(cliente.getTelefono());
        existente.setCorreo(cliente.getCorreo());
        existente.setUser(userOpt.get());

        clienteRepository.save(existente);
        return ResponseEntity.ok(new ApiResponse(existente, HttpStatus.OK, "Cliente actualizado correctamente"));
    }

    @Transactional
    public ResponseEntity<ApiResponse> delete(Long id) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(null, HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(null, HttpStatus.OK, "Cliente eliminado correctamente"));
    }
}
