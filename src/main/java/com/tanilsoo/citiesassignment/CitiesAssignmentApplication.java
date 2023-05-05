package com.tanilsoo.citiesassignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@SpringBootApplication
public class CitiesAssignmentApplication {

	@Autowired
	private Migrations migrations;

	public static void main(String[] args) {
		SpringApplication.run(CitiesAssignmentApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() throws IOException {
		migrations.runMigrations();
	}
}
