package mx.edu.utez.U3_04_OMM.repository.security;

import mx.edu.utez.U3_04_OMM.config.ApiResponse;
import mx.edu.utez.U3_04_OMM.models.User;
import mx.edu.utez.U3_04_OMM.DTO.LoginDTO;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public interface IAuthService {

    public HashMap<String,String> login(LoginDTO loginDTO) throws Exception;
    public ResponseEntity<ApiResponse> register(User user) throws Exception;

    }
