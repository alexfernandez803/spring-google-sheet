package com.alexfernandez.googlesheet;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class GoogleSheetConfig {

    private static final List<String> defaultScopes = Arrays.asList(
            "https://www.googleapis.com/auth/drive",
            "https://www.googleapis.com/auth/drive.file",
            "https://www.googleapis.com/auth/spreadsheets");

    @Autowired
    private Environment env;


    @Bean
    ClassPathResource googleSheetCertificate() {
        return new ClassPathResource(env.getProperty("certificate.path"));
    }

    @Bean
    NetHttpTransport httpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    JacksonFactory jacksonFactory() {
        return JacksonFactory.getDefaultInstance();
    }

    @Bean
    GoogleCredential googleCredential() throws GeneralSecurityException, IOException {
        return new GoogleCredential.Builder()
                .setTransport(httpTransport())
                .setJsonFactory(jacksonFactory())
                .setServiceAccountId(env.getProperty("app.serviceaccount"))
                .setServiceAccountPrivateKeyFromP12File(googleSheetCertificate().getFile())
                .setServiceAccountScopes(defaultScopes).build();
    }

    @Bean
    Sheets googleSheetService() throws GeneralSecurityException, IOException {
        return new Sheets.Builder(httpTransport(), jacksonFactory(), googleCredential())
                .setApplicationName(env.getProperty("app.applicationName"))
                .build();
    }

}