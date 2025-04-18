package com.izanyfran.easy_storage;

import com.izanyfran.easy_storage.entity.Collection;
import com.izanyfran.easy_storage.entity.Product;
import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.service.CollectionService;
import com.izanyfran.easy_storage.service.ProductCollectionService;
import com.izanyfran.easy_storage.service.ProductService;
import com.izanyfran.easy_storage.service.UserService;
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
    private UserService us;

    @Autowired
    private ProductService ps;

    @Autowired
    private CollectionService cs;

    @Autowired
    private ProductCollectionService pcs;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {

        System.out.println("\n\n\n\n\n\n STARTUP RUNNER");
        String pass = "admin";
        System.out.println("ADMIN'S PASSWORD BEFORE HASH: " + pass);
        String passHash = passwordEncoder.encode(pass);
        System.out.println("ADMIN'S PASSWORD BEFORE HASH: " + passHash);

        Optional<User> admin = us.getUserByUsername("admin");
        if (!admin.isPresent()) {
            // Create the admin user if it doesn't exist
            User userAdmin = new User("admin", passHash);
            userAdmin.setRole("ROLE_ADMIN");
            admin = Optional.of(us.createUser(userAdmin));
        }

        System.out.println("ADMIN: " + admin.get());

        // Ensure the admin is present before using it
        if (admin.isPresent()) {
            Product dumbbell = new Product("1234A", "20kg dumbbells", "A pair of dumbbells weighing 20kg each.", "Amazon");
            dumbbell.setUploader(admin.get());
            dumbbell = ps.createProduct(dumbbell);
            System.out.println("DUMBBELL: " + dumbbell);
            Product creatine = new Product("A965X", "500g Creatine powder", "A creatine container with 500 grams of powder", "Renaissance Periodization");
            creatine.setUploader(admin.get());
            creatine = ps.createProduct(creatine);
            System.out.println("CREATINE: " + creatine);
            Collection gymStuff = new Collection("Gym Stuff", "Equipment and supplements for hypertrophy or exercise.", admin.get());
            gymStuff = cs.createCollection(gymStuff);
            System.out.println("GYM: " + gymStuff);
            try {
                pcs.addProductToCollection(dumbbell.getId(), gymStuff.getName(), 2);
            } catch (Exception e) {
                System.out.println("Error: could not add product " + dumbbell.getName() + " to collection " + gymStuff.getName() + ".");
            }
            try {
                pcs.addProductToCollection(creatine.getId(), gymStuff.getName(), 2);
            } catch (Exception e) {
                System.out.println("Error: could not add product " + creatine.getName() + " to collection" + gymStuff.getName() + "");
            }
        } else {
            System.out.println("Admin user creation failed.");
        }
        System.out.println("\n\n\n\n\n\n STARTUP RUNNER");
    }

}
