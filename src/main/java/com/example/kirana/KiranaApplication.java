package com.example.kirana;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Kirana Application.
 */
@SpringBootApplication
public class KiranaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KiranaApplication.class, args);
	}

	private static void performFlywayMigration() {
		Flyway flyway = Flyway.configure().dataSource(
				"dbc:postgresql://localhost:5432/Kirana",
				"postgres",
				"postgres"
		).load();
		flyway.migrate();
	}

}
