package mx.edu.utez.U3_04_OMM.controllers;

import lombok.AllArgsConstructor;
import mx.edu.utez.U3_04_OMM.DTO.RegisterDTO;
import mx.edu.utez.U3_04_OMM.config.ApiResponse;
import mx.edu.utez.U3_04_OMM.models.Role;
import mx.edu.utez.U3_04_OMM.models.User;
import mx.edu.utez.U3_04_OMM.repository.RoleRepository;
import mx.edu.utez.U3_04_OMM.repository.security.IAuthService;
import mx.edu.utez.U3_04_OMM.DTO.LoginDTO;
import mx.edu.utez.U3_04_OMM.DTO.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {


   private final IAuthService authService;
    private final RoleRepository roleRepository;


    @PostMapping("/registerUser")
    private ResponseEntity<ResponseEntity<ApiResponse>> regsiterUser(@RequestBody RegisterDTO user) throws Exception {
      Optional<Role> role = roleRepository.findByname("ROLE_USER");
        user.setRole(role.get());
        return new ResponseEntity<>(authService.register(user.toEntity()), HttpStatus.OK);
    }
    @PostMapping("/registerAdmin")
    private ResponseEntity<ResponseEntity<ApiResponse>> regsiterAdmin(@RequestBody RegisterDTO user) throws Exception {
        Optional<Role> role = roleRepository.findByname("ADMIN_ROLE");
        user.setRole(role.get());
        return new ResponseEntity<>(authService.register(user.toEntity()), HttpStatus.OK);
    }


    @PostMapping("/login")
    private  ResponseEntity<HashMap<String,String>> login(@RequestBody LoginDTO loginRequest) throws Exception {
        HashMap<String,String> login = authService.login(loginRequest);
        if (login.containsKey("jwt")) {
            return new ResponseEntity<>(login,HttpStatus.OK);

        }else {
            return new ResponseEntity<>(login,HttpStatus.UNAUTHORIZED);
        }


    }
}
