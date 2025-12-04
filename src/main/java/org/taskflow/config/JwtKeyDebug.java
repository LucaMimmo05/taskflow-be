package org.taskflow.config;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
public class JwtKeyDebug {

    @PostConstruct
    void checkKeys() {
        try {
            System.out.println(">>> [JWT DEBUG] Current path: " + Path.of("").toAbsolutePath());

            Path privateKey = Path.of("keys/private.pem");
            Path publicKey = Path.of("keys/public.pem");

            System.out.println(">>> [JWT DEBUG] PRIVATE exists: " + Files.exists(privateKey));
            System.out.println(">>> [JWT DEBUG] PUBLIC exists: " + Files.exists(publicKey));

            if (Files.exists(privateKey)) {
                String content = Files.readString(privateKey);
                System.out.println(">>> [JWT DEBUG] PRIVATE KEY CONTENT:");
                System.out.println(content.substring(0, Math.min(100, content.length())) + "...");
            } else {
                System.out.println(">>> [JWT DEBUG] Private key NOT FOUND");
            }
        } catch (Exception e) {
            System.out.println(">>> [JWT DEBUG] ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
