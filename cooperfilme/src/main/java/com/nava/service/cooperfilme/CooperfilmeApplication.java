package com.nava.service.cooperfilme;

import com.nava.service.cooperfilme.user.RoleRepository;
import com.nava.service.cooperfilme.user.UserRepository;
import com.nava.service.cooperfilme.user.entities.Role;
import com.nava.service.cooperfilme.user.entities.RoleId;
import com.nava.service.cooperfilme.user.entities.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CooperfilmeApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CooperfilmeApplication(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(CooperfilmeApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        createUser("Analista", "analista@cooperfilme.com", "analista@123", "ANALISTA");
        createUser("Revisor", "revisor@cooperfilme.com", "revisor@123", "REVISOR");
        createUser("Aprovador1", "aprovador1@cooperfilme.com", "aprovador1@123", "APROVADOR");
        createUser("Aprovador2", "aprovador2@cooperfilme.com", "aprovador2@123", "APROVADOR");
        createUser("Aprovador3", "aprovador3@cooperfilme.com", "aprovador3@123", "APROVADOR");
    }

    private void createUser(String name, String email, String rawPassword, String roleName) {
        User user = new User(name, email, passwordEncoder.encode(rawPassword));
        userRepository.save(user);

        RoleId roleId = new RoleId(roleName, user.getId());
        Role role = new Role(roleId);
        user.getRoles().add(role);
        roleRepository.save(role);

    }
}
