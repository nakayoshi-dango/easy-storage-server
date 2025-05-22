package com.izanyfran.easy_storage;

import com.izanyfran.easy_storage.entity.Collection;
import com.izanyfran.easy_storage.entity.Product;
import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.service.CollectionService;
import com.izanyfran.easy_storage.service.ProductCollectionService;
import com.izanyfran.easy_storage.service.ProductService;
import com.izanyfran.easy_storage.service.UserCollectionService;
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

    @Autowired
    private ProductService ps;

    @Autowired
    private CollectionService cs;

    @Autowired
    private ProductCollectionService pcs;

    @Autowired
    private UserCollectionService ucs;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) throws Exception {

        System.out.println("\n\n\n\n\n\n STARTUP RUNNER");
        String passHash = passwordEncoder.encode(adminPassword);

        Optional<User> admin = us.getUserByUsername("admin");
        if (!admin.isPresent()) {
            User userAdmin = new User(adminUsername, passHash);
            userAdmin.setRole("ROLE_ADMIN");
            userAdmin.setImageURL("https://cdn.pixabay.com/photo/2025/05/04/18/04/bird-9578746_1280.jpg");
            admin = Optional.of(us.createUser(userAdmin));
            System.out.println("ADMIN: " + us.toDTO(admin.get()));
        }

        if (admin.isPresent()) {
            Optional<Collection> optGymStuff = cs.getCollectionByName("Gym Stuff");
            if (!optGymStuff.isPresent()) {
                Collection gymStuff = new Collection("Gym Stuff", "Equipment and supplements for hypertrophy or exercise.", admin.get());
                gymStuff = cs.createCollection(gymStuff);
                System.out.println("GYM: " + gymStuff);
                ucs.addUserToCollection(1, 1);
                Optional<Product> optDumbbell = ps.getProductById("1234A");
                if (!optDumbbell.isPresent()) {
                    Product dumbbell = new Product("1234A", "20kg dumbbells", "A pair of dumbbells weighing 20kg each.", "Amazon", "https://www.ukgymequipment.com/images/2-20kg-premium-rubber-dumbbell-set-p5807-75449_image.jpg");
                    dumbbell.setUploader(admin.get());
                    dumbbell = ps.createProduct(dumbbell);
                    System.out.println("DUMBBELL: " + dumbbell);
                    try {
                        pcs.addProductToCollection(dumbbell.getId(), gymStuff.getName(), 2);
                    } catch (Exception e) {
                        System.out.println("Error: could not add product " + dumbbell.getName() + " to collection " + gymStuff.getName() + ".");
                    }
                }

                Optional<Product> optCreatine = ps.getProductById("A965X");
                if (!optCreatine.isPresent()) {
                    Product creatine = new Product("A965X", "500g Creatine powder", "A creatine container with 500 grams of powder", "Renaissance Periodization", "https://www.extremenutritions.com/cdn/shop/files/71rPih5keSL._AC_SL1500.jpg?v=1721910655&width=720");
                    creatine.setUploader(admin.get());
                    creatine = ps.createProduct(creatine);
                    System.out.println("CREATINE: " + creatine);
                    try {
                        pcs.addProductToCollection(creatine.getId(), gymStuff.getName(), 2);
                    } catch (Exception e) {
                        System.out.println("Error: could not add product " + creatine.getName() + " to collection" + gymStuff.getName() + "");
                    }
                }

                Optional<Product> optPepera = ps.getProductById("pepera7492");
                if (!optPepera.isPresent()) {
                    Product pepera = new Product("pepera7492", "Izan VS calvicie", "Izan pierde 100%", "Amazon", "https://media.tenor.com/9K0bOcaUG3gAAAAM/smiling-friends-pim.gif");
                    pepera.setUploader(admin.get());
                    pepera = ps.createProduct(pepera);
                    System.out.println("PEPERA: " + pepera);
                    try {
                        pcs.addProductToCollection(pepera.getId(), gymStuff.getName(), 50);
                    } catch (Exception e) {
                        System.out.println("Error: could not add product " + pepera.getName() + " to collection" + gymStuff.getName() + "");
                    }
                }

            }
        } else {
            System.out.println("No se pudo crear al usuario administrador.");
        }
        System.out.println("\n\n\n\n\n\n STARTUP RUNNER");
    }

}
