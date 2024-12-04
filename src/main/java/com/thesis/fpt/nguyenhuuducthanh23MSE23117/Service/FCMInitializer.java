package com.thesis.fpt.nguyenhuuducthanh23MSE23117.Service;

import java.io.IOException;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Service
public class FCMInitializer {

    @Value("${firebase.config.file.path}")
    private String firebaseConfigPath;
    @PostConstruct
    public void initialize() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(
                        GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))
                .build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase application initialized");
        }
    }
}
