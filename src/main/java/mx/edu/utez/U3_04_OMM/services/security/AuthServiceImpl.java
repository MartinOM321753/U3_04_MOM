package mx.edu.utez.U3_04_OMM.services.security;

import lombok.AllArgsConstructor;
import mx.edu.utez.U3_04_OMM.config.ApiResponse;
import mx.edu.utez.U3_04_OMM.models.User;
import mx.edu.utez.U3_04_OMM.repository.UserRepository;
import mx.edu.utez.U3_04_OMM.DTO.LoginDTO;
import mx.edu.utez.U3_04_OMM.repository.security.IAuthService;
import mx.edu.utez.U3_04_OMM.repository.security.IJWTUtilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final IJWTUtilityService jwtUtilityService;

    @Override
    public HashMap<String,String> login(LoginDTO loginDTO) throws Exception {
        try {
            HashMap<String,String> jwt = new HashMap<>();
            Optional<User> user =userRepository.findByEmail(loginDTO.getEmail());

            if (user.isEmpty()){
                jwt.put("error","Usuario no registrado");
                return jwt;
            }
            if (veriifyPassword(loginDTO.getPassword(),user.get().getPassword() )){

                jwt.put("jwt",jwtUtilityService.genareteJWT(user.get().getId()));
            } else {

                jwt.put("error","La autenticacion fallo");

            }
            return jwt;
        }catch (Exception e){

            throw  new Exception(e.toString());

        }
    }

    @Override
    public ResponseEntity<ApiResponse> register(User user) {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse(null, HttpStatus.CONFLICT, "El correo ya está registrado", true));
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));

            User savedUser = userRepository.save(user);

            savedUser.setPassword("*****************");

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(savedUser, HttpStatus.CREATED, "Usuario creado correctamente", false));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar usuario", true));
        }
    }

    private boolean veriifyPassword(String enteredPassword,String storePassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return  encoder.matches(enteredPassword,storePassword);

    }


}
