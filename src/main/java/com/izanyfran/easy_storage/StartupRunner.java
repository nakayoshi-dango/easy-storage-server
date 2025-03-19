package com.izanyfran.easy_storage;

import com.izanyfran.easy_storage.entity.Product;
import com.izanyfran.easy_storage.entity.User;
import com.izanyfran.easy_storage.service.ServiceProduct;
import com.izanyfran.easy_storage.service.ServiceUser;
import java.sql.Date;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Component;

@Component
@EntityScan(basePackages = "com.izanyfran.easy_storage.entity")
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private ServiceUser su;
    @Autowired
    private ServiceProduct sp;

    @Override
    public void run(String... args) throws Exception {
        String pass = "admin";
        System.out.println("admin's password before hash: " + pass);
        System.out.println("admin's password after hash: " + pass.hashCode());
        Date today = Date.valueOf(LocalDate.now());
        User admin = su.getUserByUsername("admin");
        if (admin == null) {
            admin = new User("admin", pass.hashCode(), "admin", 444444444, today);
            admin = su.createUser(admin);
        }
        System.out.println("Sample User.toString():" + admin);
        Product dumbbell = new Product("1234A", "20kg dumbbells", "A pair of dumbbells weighing 20kg each.", admin, "Amazon", today);
        dumbbell = sp.createProduct(dumbbell);
        System.out.println("Sample Product.toString():" + dumbbell);
    }

}
