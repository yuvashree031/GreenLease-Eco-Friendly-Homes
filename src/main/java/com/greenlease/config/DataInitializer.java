package com.greenlease.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Initialize database with schema and sample data on first run only
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        // Check if users table exists
        boolean tablesExist = checkIfTablesExist();
        
        if (!tablesExist) {
            System.out.println("Initializing database with schema and sample data...");
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("schema.sql"));
            populator.setContinueOnError(false);
            populator.execute(dataSource);
            System.out.println("Database initialized successfully!");
        } else {
            System.out.println("Database already initialized. Skipping schema creation.");
        }
    }

    private boolean checkIfTablesExist() {
        try {
            jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'USERS'",
                Integer.class
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
