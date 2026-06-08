package org.sid.exam.config;

import org.sid.exam.entities.AppUser;
import org.sid.exam.repositories.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            createUserIfMissing(appUserRepository, passwordEncoder, "admin", "admin123", Set.of("ROLE_ADMIN"));
            createUserIfMissing(appUserRepository, passwordEncoder, "employe", "employe123", Set.of("ROLE_EMPLOYE"));
            createUserIfMissing(appUserRepository, passwordEncoder, "client", "client123", Set.of("ROLE_CLIEN"));
        };
    }

    private void createUserIfMissing(AppUserRepository appUserRepository,
                                     PasswordEncoder passwordEncoder,
                                     String username,
                                     String password,
                                     Set<String> roles) {
        if (!appUserRepository.existsByUsername(username)) {
            AppUser appUser = new AppUser();
            appUser.setUsername(username);
            appUser.setPassword(passwordEncoder.encode(password));
            appUser.setRoles(roles);
            appUserRepository.save(appUser);
        }
    }
}
