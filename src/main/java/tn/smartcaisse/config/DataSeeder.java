package tn.smartcaisse.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tn.smartcaisse.entity.*;
import tn.smartcaisse.repository.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdmin();
        seedCategories();
        seedMenuItems();
    }

    private void seedAdmin() {
        if (userRepository.existsByUsername("admin")) return;

        userRepository.save(User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin123"))
            .role(User.Role.ADMIN)
            .fullName("Administrateur")
            .build());

        userRepository.save(User.builder()
            .username("caissier")
            .password(passwordEncoder.encode("caisse123"))
            .role(User.Role.CASHIER)
            .fullName("Caissier Principal")
            .build());

        log.info("Default users created: admin / admin123 — CHANGE THIS IN PRODUCTION");
    }

    private void seedCategories() {
        if (categoryRepository.count() > 0) return;

        categoryRepository.save(Category.builder().name("Burgers").nameAr("برغر").sortOrder(1).build());
        categoryRepository.save(Category.builder().name("Sandwichs").nameAr("ساندويتش").sortOrder(2).build());
        categoryRepository.save(Category.builder().name("Boissons").nameAr("مشروبات").sortOrder(3).build());
        categoryRepository.save(Category.builder().name("Extras").nameAr("إضافات").sortOrder(4).build());

        log.info("Demo categories seeded");
    }

    private void seedMenuItems() {
        if (menuItemRepository.count() > 0) return;

        var burgers   = categoryRepository.findAll().get(0);
        var sandwichs = categoryRepository.findAll().get(1);
        var boissons  = categoryRepository.findAll().get(2);
        var extras    = categoryRepository.findAll().get(3);

        menuItemRepository.save(MenuItem.builder().category(burgers).name("Double Burger").nameAr("دبل برغر").price(8.5).build());
        menuItemRepository.save(MenuItem.builder().category(burgers).name("Chicken Burger").nameAr("تشيكن برغر").price(7.0).build());
        menuItemRepository.save(MenuItem.builder().category(burgers).name("Classic Burger").nameAr("كلاسيك برغر").price(6.0).build());
        menuItemRepository.save(MenuItem.builder().category(sandwichs).name("Shawarma Poulet").nameAr("شاورما دجاج").price(6.5).build());
        menuItemRepository.save(MenuItem.builder().category(sandwichs).name("Sandwich Thon").nameAr("ساندويتش تون").price(4.5).build());
        menuItemRepository.save(MenuItem.builder().category(boissons).name("Coca-Cola 33cl").nameAr("كوكاكولا").price(2.5).build());
        menuItemRepository.save(MenuItem.builder().category(boissons).name("Eau Minérale").nameAr("ماء معدني").price(1.5).build());
        menuItemRepository.save(MenuItem.builder().category(extras).name("Frites M").nameAr("بطاطا م").price(3.0).build());
        menuItemRepository.save(MenuItem.builder().category(extras).name("Frites L").nameAr("بطاطا ك").price(4.0).build());

        log.info("Demo menu items seeded");
    }
}
