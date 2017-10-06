package com.datarepublic.simplecab;

import com.datarepublic.simplecab.repository.JdbcSimpleCabRepository;
import com.datarepublic.simplecab.repository.SimpleCabRepository;
import com.datarepublic.simplecab.service.SimpleCabService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Server {

    @Bean
    public SimpleCabRepository simpleCabRepository() {
        return new JdbcSimpleCabRepository();
    }

    @Bean
    public SimpleCabService simpleCabService() {
        return new SimpleCabService();
    }

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
