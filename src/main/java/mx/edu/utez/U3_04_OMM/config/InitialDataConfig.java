package mx.edu.utez.U3_04_OMM.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import mx.edu.utez.U3_04_OMM.models.Almacen;
import mx.edu.utez.U3_04_OMM.models.Cede;
import mx.edu.utez.U3_04_OMM.models.Cliente;
import mx.edu.utez.U3_04_OMM.models.Role;
import mx.edu.utez.U3_04_OMM.models.User;
import mx.edu.utez.U3_04_OMM.repository.RoleRepository;
import mx.edu.utez.U3_04_OMM.services.AlmacenService;
import mx.edu.utez.U3_04_OMM.services.CedeService;
import mx.edu.utez.U3_04_OMM.services.ClienteService;
import mx.edu.utez.U3_04_OMM.services.security.AuthServiceImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InitialDataConfig {
    private final RoleRepository roleRepository;
    private final AlmacenService almacenService;
    private final CedeService cedeService;
    private final ClienteService clienteService;
    private final AuthServiceImpl authService;

    @PostConstruct
    public void init() {
        Role userRole = roleRepository.findByname("USER_ROLE")
                .orElseGet(() -> roleRepository.save(new Role(null, "USER_ROLE")));
        Role adminRole = roleRepository.findByname("ADMIN_ROLE")
                .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN_ROLE")));

        Cede cede1 = new Cede();
        cede1.setEstado("Estado1");
        cede1.setMunicipio("Municipio1");

        Cede cede2 = new Cede();
        cede2.setEstado("Estado2");
        cede2.setMunicipio("Municipio2");

        Cede cede3 = new Cede();
        cede3.setEstado("Estado3");
        cede3.setMunicipio("Municipio3");

        cede1 = (Cede) cedeService.save(cede1).getBody().getData();
        cede2 = (Cede) cedeService.save(cede2).getBody().getData();
        cede3 = (Cede) cedeService.save(cede3).getBody().getData();

        Almacen almacen1 = Almacen.builder()

                .tamaño("M")
                .status(true)
                .fechaRegistro(LocalDate.now())
                .cede(cede1)
                .build();

        Almacen almacen2 = Almacen.builder()

                .tamaño("G")
                .status(true)
                .fechaRegistro(LocalDate.now())
                .cede(cede2)
                .build();

        Almacen almacen3 = Almacen.builder()

                .tamaño("M")
                .status(true)
                .fechaRegistro(LocalDate.now())
                .cede(cede3)
                .build();

        almacenService.save(almacen1);
        almacenService.save(almacen2);
        almacenService.save(almacen3);

        User user1 = User.builder()
                .email("juan@mail.com")
                .password("password1")
                .role(userRole)
                .build();

        User user2 = User.builder()
                .email("ana@mail.com")
                .password("password2")
                .role(userRole)
                .build();

        User user3 = User.builder()
                .email("carlos@mail.com")
                .password("password3")
                .role(userRole)
                .build();
        User admin = User.builder()
                .email("admin@mail.com")
                .password("password4")
                .role(adminRole)
                .build();

        user1 = (User) authService.register(user1).getBody().getData();
        user2 = (User) authService.register(user2).getBody().getData();
        user3 = (User) authService.register(user3).getBody().getData();

       authService.register(admin);


        Cliente cliente1 = Cliente.builder()
                .nombreCompleto("Juan Pérez")
                .telefono("5551112222")
                .correo("juan@mail.com")
                .user(user1)
                .build();

        Cliente cliente2 = Cliente.builder()
                .nombreCompleto("Ana López")
                .telefono("5553334444")
                .correo("ana@mail.com")
                .user(user2)
                .build();

        Cliente cliente3 = Cliente.builder()
                .nombreCompleto("Carlos Ruiz")
                .telefono("5555556666")
                .correo("carlos@mail.com")
                .user(user3)
                .build();

        clienteService.save(cliente1);
        clienteService.save(cliente2);
        clienteService.save(cliente3);
    }
}
