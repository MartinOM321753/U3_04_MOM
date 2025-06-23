package mx.edu.utez.U3_04_OMM.repository.security;

import mx.edu.utez.U3_04_OMM.models.User;

import java.util.List;

public interface IUserServiceImpl {

    public List<User> findAllUsers();

}
