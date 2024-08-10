package com.ebi.library_management_system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class AppConfig {
    /*
    @Bean(destroyMethod = "close") // no need, it will call `AutoClosable/Closable::close()` automatically
     */
    @Bean
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/library_db", "root", "root");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
