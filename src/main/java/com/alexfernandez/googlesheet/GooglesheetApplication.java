package com.alexfernandez.googlesheet;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class GooglesheetApplication implements CommandLineRunner {

    @Autowired
    private Environment env;

    @Autowired
    Sheets googleSheetService;

    public static void main(String[] args) {
        SpringApplication.run(GooglesheetApplication.class, args);

    }


    @Override
    public void run(String... args) throws Exception {

        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList("Grocery List"),
                        Arrays.asList("banana", "30"),
                        Arrays.asList("apple", "10")
                      ));


        UpdateValuesResponse result = googleSheetService.spreadsheets().values()
                .update(env.getProperty("app.spreadsheetid"),
                        "Sheet4" + "!" + "A1", body)
                .setValueInputOption("RAW")
                .execute();
    }

}
