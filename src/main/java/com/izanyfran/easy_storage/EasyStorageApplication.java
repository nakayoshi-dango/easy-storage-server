package com.izanyfran.easy_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.izanyfran.easy_storage"})

public class EasyStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyStorageApplication.class, args);
    }

}
