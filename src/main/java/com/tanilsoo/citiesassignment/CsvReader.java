package com.tanilsoo.citiesassignment;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class CsvReader {

    public List<List<String>> readFile(String resourceFile) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourceFile);
        File file = resource.getFile();

        List<List<String>> rows = new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(1)
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                    .build();

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                rows.add(Arrays.asList(line[0], line[1], line[2]));
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return rows;
    }

}
