package com.grihom.backend.config;

import com.grihom.backend.model.Improvement;
import com.grihom.backend.model.Role;
import com.grihom.backend.model.User;
import com.grihom.backend.repository.ImprovementRepository;
import com.grihom.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ImprovementRepository improvementRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedImprovements();
    }

    private void seedAdmin() {
        if (!userRepository.existsByEmail("admin@grihom.com")) {
            User admin = User.builder()
                    .name("GriHom Admin")
                    .email("admin@grihom.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
            log.info("✅ Default admin seeded — email: admin@grihom.com | password: admin123");
        }

        if (!userRepository.existsByEmail("decor@grihom.com")) {
            User decor = User.builder()
                    .name("Decorator User")
                    .email("decor@grihom.com")
                    .password(passwordEncoder.encode("decor123"))
                    .role(Role.DECOR)
                    .active(true)
                    .build();
            userRepository.save(decor);
            log.info("✅ Default decorator seeded — email: decor@grihom.com | password: decor123");
        }
    }

    private void seedImprovements() {
        if (improvementRepository.count() == 0) {
            User admin = userRepository.findByEmail("admin@grihom.com").orElse(null);

            improvementRepository.save(Improvement.builder()
                    .title("False Ceiling with LED Lighting")
                    .description("Install a POP false ceiling with recessed LED lights for a modern, elegant look. Popular in Indian urban homes.")
                    .room("Living Room")
                    .cost("Medium")
                    .effort("Medium")
                    .roi("High")
                    .impact(8)
                    .duration("3-5 days")
                    .budgetRange("₹50,000 - ₹2,00,000")
                    .indianSpecific(true)
                    .source("admin")
                    .createdByUser(admin)
                    .build());

            improvementRepository.save(Improvement.builder()
                    .title("Modular Kitchen Upgrade")
                    .description("Replace old platform kitchen with a modular design featuring soft-close drawers, granite countertops and chimney.")
                    .room("Kitchen")
                    .cost("High")
                    .effort("High")
                    .roi("High")
                    .impact(12)
                    .duration("7-14 days")
                    .budgetRange("₹1,50,000 - ₹5,00,000")
                    .indianSpecific(true)
                    .source("admin")
                    .createdByUser(admin)
                    .build());

            improvementRepository.save(Improvement.builder()
                    .title("Bathroom Tiles & Fixtures Replacement")
                    .description("Replace old tiles with vitrified tiles, install modern sanitary ware and LED mirror lights.")
                    .room("Bathroom")
                    .cost("Medium")
                    .effort("Medium")
                    .roi("High")
                    .impact(9)
                    .duration("5-7 days")
                    .budgetRange("₹40,000 - ₹1,50,000")
                    .indianSpecific(true)
                    .source("admin")
                    .createdByUser(admin)
                    .build());

            improvementRepository.save(Improvement.builder()
                    .title("Vastu-Compliant Main Door Upgrade")
                    .description("Replace main entrance with a solid wood or steel door with auspicious Vastu alignment and brass fittings.")
                    .room("All")
                    .cost("Low")
                    .effort("Low")
                    .roi("Medium")
                    .impact(5)
                    .duration("1-2 days")
                    .budgetRange("₹15,000 - ₹60,000")
                    .indianSpecific(true)
                    .source("admin")
                    .createdByUser(admin)
                    .build());

            improvementRepository.save(Improvement.builder()
                    .title("Wardrobe with Sliding Doors")
                    .description("Build a floor-to-ceiling wardrobe with sliding mirrors in the master bedroom. Maximizes storage in Indian apartment layouts.")
                    .room("Bedroom")
                    .cost("Medium")
                    .effort("Medium")
                    .roi("Medium")
                    .impact(7)
                    .duration("3-5 days")
                    .budgetRange("₹35,000 - ₹1,20,000")
                    .indianSpecific(true)
                    .source("admin")
                    .createdByUser(admin)
                    .build());

            improvementRepository.save(Improvement.builder()
                    .title("Balcony Garden & Waterproofing")
                    .description("Add planters, artificial grass and waterproof coating on balcony slab. Popular in Indian high-rises.")
                    .room("All")
                    .cost("Low")
                    .effort("Low")
                    .roi("Medium")
                    .impact(4)
                    .duration("2-3 days")
                    .budgetRange("₹10,000 - ₹40,000")
                    .indianSpecific(true)
                    .source("admin")
                    .createdByUser(admin)
                    .build());

            log.info("✅ Sample improvements seeded (6 records)");
        }
    }
}
