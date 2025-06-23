package mx.edu.utez.U3_04_OMM.services.security;

import mx.edu.utez.U3_04_OMM.models.User;
import mx.edu.utez.U3_04_OMM.repository.UserRepository;
import mx.edu.utez.U3_04_OMM.repository.security.IUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserServiceImpl {
@Autowired
    UserRepository userRepository;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
