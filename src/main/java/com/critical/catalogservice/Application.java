package com.critical.catalogservice;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.jobrunr.scheduling"})
public class Application {

    @Value("${spring.flyway.enabled}")
    private static boolean isFlywayEnabled;

    public static void main(String[] args) {

        if (isFlywayEnabled) {
            Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/bookCatalog", "postgres", "postgres").load();
            flyway.migrate();
        }
        SpringApplication.run(Application.class, args);
    }
}