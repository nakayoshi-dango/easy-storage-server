/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author nakayoshi_dango
 */
@Component
@EntityScan(basePackages = "com.izanyfran.easy_storage.entity")
public class SampleDataCreation implements CommandLineRunner {

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

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if ("create".equalsIgnoreCase(ddlAuto) || "create-drop".equalsIgnoreCase(ddlAuto)) {
            System.out.println("\n\n\n\n\n\n SAMPLE DATA CREATION ");

            Optional<User> admin = us.getUserByUsername(adminUsername);
            if (!admin.isPresent()) {
                String passHash = passwordEncoder.encode(adminPassword);
                User userAdmin = new User(adminUsername, passHash);
                userAdmin.setRole("ROLE_ADMIN");
                userAdmin.setImageURL("https://cdn.pixabay.com/photo/2025/05/04/18/04/bird-9578746_1280.jpg");
                admin = Optional.of(us.createUser(userAdmin));
                System.out.println("ADMIN: " + us.toDTO(admin.get()));
            }

            Collection gymStuff = new Collection("Gym Stuff", "Equipment and supplements for hypertrophy or exercise.", admin.get());
            gymStuff.setImageURL("https://cdn.pixabay.com/photo/2018/04/16/15/40/dumbbell-3324976_1280.jpg");
            gymStuff = cs.createCollection(gymStuff);
            System.out.println("GYM: " + gymStuff);

            Product dumbbell = new Product("1234A", "20kg dumbbells", "A pair of dumbbells weighing 20kg each.", "Amazon", "https://www.ukgymequipment.com/images/2-20kg-premium-rubber-dumbbell-set-p5807-75449_image.jpg");
            dumbbell.setUploader(admin.get());
            dumbbell = ps.createProduct(dumbbell);
            System.out.println("DUMBBELL: " + dumbbell);
            pcs.addProductToCollection(dumbbell.getId(), gymStuff.getName(), 2);

            Product creatine = new Product("A965X", "500g Creatine powder", "A creatine container with 500 grams of powder", "Renaissance Periodization", "https://www.extremenutritions.com/cdn/shop/files/71rPih5keSL._AC_SL1500.jpg?v=1721910655&width=720");
            creatine.setUploader(admin.get());
            creatine = ps.createProduct(creatine);
            System.out.println("CREATINE: " + creatine);
            pcs.addProductToCollection(creatine.getId(), gymStuff.getName(), 2);

            String passHashIzan = passwordEncoder.encode("izan");
            User izan = new User("izan", passHashIzan);
            izan.setRole("ROLE_USER");
            izan.setImageURL("https://cdn.pixabay.com/photo/2016/12/13/21/20/alien-1905155_1280.png");
            izan = (us.createUser(izan));
            System.out.println("IZAN: " + us.toDTO(izan));
            ucs.addUserToCollection(izan.getId(), gymStuff.getId());

            Collection fruitShop = new Collection("Fruit Shop", "Fresh fruit on sale.", izan);
            fruitShop.setImageURL("https://cdn.pixabay.com/photo/2018/04/12/09/29/market-3312908_1280.jpg");
            fruitShop = cs.createCollection(fruitShop);
            System.out.println("FRUITSHOP: " + fruitShop);

            Product pear = new Product("1a", "Pear", "Fresh pears from the land nextdoor", "Fruit Shop", "https://cdn.pixabay.com/photo/2018/10/12/18/31/autumn-3742720_1280.jpg");
            pear.setUploader(izan);
            pear = ps.createProduct(pear);
            System.out.println("PEAR: " + pear);
            pcs.addProductToCollection(pear.getId(), fruitShop.getName(), 32);

            Product apple = new Product("2a", "Apple", "Fresh apples from the west", "Fruit Shop", "https://cdn.pixabay.com/photo/2017/09/11/21/23/fruit-2740499_1280.jpg");
            apple.setUploader(izan);
            apple = ps.createProduct(apple);
            System.out.println("APPLE: " + apple);
            pcs.addProductToCollection(apple.getId(), fruitShop.getName(), 64);

            Product orange = new Product("3a", "Orange", "Fresh oranges from the east", "Fruit Shop", "https://cdn.pixabay.com/photo/2016/01/02/02/03/orange-1117645_1280.jpg");
            orange.setUploader(admin.get());
            orange = ps.createProduct(orange);
            System.out.println("ORANGE: " + orange);
            pcs.addProductToCollection(orange.getId(), fruitShop.getName(), 11);

            String passHashFran = passwordEncoder.encode("fran");
            User fran = new User("fran", passHashFran);
            fran.setRole("ROLE_USER");
            fran.setImageURL("https://cdn.pixabay.com/photo/2021/02/12/01/49/daruma-doll-6006957_1280.jpg");
            fran = (us.createUser(fran));
            System.out.println("FRAN: " + us.toDTO(fran));
            ucs.addUserToCollection(fran.getId(), fruitShop.getId());

            System.out.println("\n\n\n\n\n\n SAMPLE DATA CREATION ");
        }
    }
}
