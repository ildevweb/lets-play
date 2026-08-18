package com.example.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.core.env.Environment;


@Component
public class MongoConfigChecker implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;
    private final Environment environment;

    public MongoConfigChecker(
            MongoTemplate mongoTemplate,
            Environment environment) {
        this.mongoTemplate = mongoTemplate;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {

        System.out.println("=================================");
        System.out.println("URI: "
                + environment.getProperty("spring.data.mongodb.uri"));

        System.out.println("DATABASE PROPERTY: "
                + environment.getProperty("spring.data.mongodb.database"));

        System.out.println("MongoTemplate DB: "
                + mongoTemplate.getDb().getName());

        System.out.println("=================================");
    }
}