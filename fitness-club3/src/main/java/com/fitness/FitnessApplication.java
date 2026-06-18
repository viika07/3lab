package com.fitness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FitnessApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessApplication.class, args);
        System.out.println("Приложение Фитнес-клуб успешно запущено!");
        System.out.println("Откройте браузер: http://localhost:8080");
    }

    @Bean
    public CommandLineRunner initData(FitnessService fitnessService) {
        return args -> {
            System.out.println(">>> Инициализация тестовых данных");
            fitnessService.initTestData();
            System.out.println(">>> Приложение работает и готово к использованию");
        };                                                                                                                                                                                                                                                                                                                                                                                                                                                        
    }
}