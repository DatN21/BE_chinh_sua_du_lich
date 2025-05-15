package com.dulich.toudulich;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.dulich.toudulich.Repositories")
public class TouDuLichApplication {

    public static void main(String[] args) {
        SpringApplication.run(TouDuLichApplication.class, args);
    }

}
