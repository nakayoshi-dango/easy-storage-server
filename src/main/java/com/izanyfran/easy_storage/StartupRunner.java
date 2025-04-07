package com.izanyfran.easy_storage;

import com.izanyfran.easy_storage.entity.Product;
import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.service.ServiceProduct;
import com.izanyfran.easy_storage.service.ServiceUser;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@EntityScan(basePackages = "com.izanyfran.easy_storage.entity")
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private ServiceUser su;

    @Autowired
    private ServiceProduct sp;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {
        
        System.out.println("\n\n\n\n\n\n STARTUP RUNNER");
        String pass = "admin";
        System.out.println("admin's password before hash: " + pass);
        String passHash = passwordEncoder.encode(pass);
        System.out.println("admin's password after hash: " + passHash);
        
        Optional<User> admin = su.getUserByUsername("admin");
        if (!admin.isPresent()) {
            // Create the admin user if it doesn't exist
            User userAdmin= new User("admin", passHash);
            userAdmin.setRole("ROLE_ADMIN");
            admin = Optional.of(su.createUser(userAdmin));
        }
        
        System.out.println("Admin: " + admin.get());

        // Ensure the admin is present before using it
        if (admin.isPresent()) {
            Product dumbbell = new Product("1234A", "20kg dumbbells", "A pair of dumbbells weighing 20kg each.", admin.get(), "Amazon");
            dumbbell = sp.createProduct(dumbbell);
            System.out.println("Dumbbell: " + dumbbell);
        } else {
            System.out.println("Admin user creation failed.");
        }
        System.out.println("\n\n\n\n\n\n STARTUP RUNNER");
    }

}
