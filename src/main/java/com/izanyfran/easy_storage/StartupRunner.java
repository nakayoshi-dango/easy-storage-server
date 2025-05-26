package com.izanyfran.easy_storage;

import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.service.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@EntityScan(basePackages = "com.izanyfran.easy_storage.entity")
public class StartupRunner implements CommandLineRunner {

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Autowired
    private UserService us;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {

        System.out.println("\n\n\n\n\n\n STARTUP RUNNER");
        String passHash = passwordEncoder.encode(adminPassword);

        Optional<User> admin = us.getUserByUsername(adminUsername);
        if (!admin.isPresent()) {
            User userAdmin = new User(adminUsername, passHash);
            userAdmin.setRole("ROLE_ADMIN");
            userAdmin.setImageURL("https://cdn.pixabay.com/photo/2025/05/04/18/04/bird-9578746_1280.jpg");
            admin = Optional.of(us.createUser(userAdmin));
            System.out.println("ADMIN: " + us.toDTO(admin.get()));
        }

        System.out.println("\n\n\n\n\n\n STARTUP RUNNER");
    }

}
