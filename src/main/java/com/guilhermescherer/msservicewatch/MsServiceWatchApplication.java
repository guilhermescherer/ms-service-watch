package com.guilhermescherer.msservicewatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsServiceWatchApplication {

    /*
    TODO:
        Implementar testes unitários
     */
    public static void main(String[] args) {
        SpringApplication.run(MsServiceWatchApplication.class, args);
    }

}
