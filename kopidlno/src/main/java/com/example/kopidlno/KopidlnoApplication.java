package com.example.kopidlno;

import com.example.kopidlno.data.KopidlnoController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KopidlnoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(KopidlnoApplication.class, args);
    }

    @Autowired
    KopidlnoController kopidlnoController;

    @Override
    public void run(String... args) throws Exception {
        kopidlnoController.processXml();
        System.exit(0);
    }
}
