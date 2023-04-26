package ru.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableConfigServer
@EnableWebSecurity
public class ConfigApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(ConfigApp.class, args);
    }
}
