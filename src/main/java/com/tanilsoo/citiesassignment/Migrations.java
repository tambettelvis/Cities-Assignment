package com.tanilsoo.citiesassignment;

import com.tanilsoo.citiesassignment.city.City;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class Migrations {

    private final Db db;

    private final CsvReader csvReader;

    @Value("${app.resources.cities}")
    private String filePath;

    @Autowired
    public Migrations(
            Db db,
            CsvReader csvReader
    ) {
        this.db = db;
        this.csvReader = csvReader;
    }

    public void runMigrations() throws IOException {
        if (db.hasCityData()) {
            log.info("Skipping migrations");
            return;
        }
        log.info("Adding data to db...");

        var rows = csvReader.readFile(filePath);
        rows.remove(0);

        rows.forEach(row -> {
            db.insertCity(
                    City.builder()
                            .id(Integer.parseInt(row.get(0)))
                            .name(row.get(1))
                            .url(row.get(2))
                            .build()
            );
        });
    }

}
