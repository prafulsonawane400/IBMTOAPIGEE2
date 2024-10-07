package com.ibmtoapigee.ibmToApigee.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

@Configuration
public class GoogleAuthConfig {

    @Value("${google.cloud.credentials.file}")
    private String credentialsFilePath;

    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        try (FileInputStream serviceAccountStream = new FileInputStream(credentialsFilePath)) {
            // Load the service account credentials
            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(serviceAccountStream)
                    .createScoped(Collections.singleton("https://www.googleapis.com/auth/cloud-platform"));
            return credentials;
        }
    }

    @Bean
    public AccessToken accessToken(GoogleCredentials googleCredentials) throws IOException {
        // Refresh credentials to obtain an access token
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken();
    }
}
