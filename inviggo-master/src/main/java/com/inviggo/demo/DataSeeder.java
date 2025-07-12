package com.inviggo.demo;

import com.inviggo.demo.model.Category;
import com.inviggo.demo.model.User;
import com.inviggo.demo.model.Ad;
import com.inviggo.demo.repository.AdRepository;
import com.inviggo.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        // Only seed if database is empty
        if (userRepository.count() == 0) {
            seedUsers();
            seedAds();
            System.out.println("Database seeded successfully!");
        }
    }

    private void seedUsers() {
        List<User> users = Arrays.asList(
                createUser("john_doe", "555-0101"),
                createUser("jane_smith", "555-0102"),
                createUser("bob_johnson", "555-0103"),
                createUser("alice_brown", "555-0104"),
                createUser("charlie_davis", "555-0105"),
                createUser("diana_wilson", "555-0106"),
                createUser("eve_miller", "555-0107"),
                createUser("frank_garcia", "555-0108"),
                createUser("grace_lee", "555-0109"),
                createUser("henry_taylor", "555-0110")
        );

        userRepository.saveAll(users);
        System.out.println("Seeded " + users.size() + " users");
    }
    private User createUser(String username, String phone) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setPhoneNumber(phone);
        user.setRegistrationDate(LocalDateTime.now().minusDays(random.nextInt(365)));
        return user;
    }
    private void seedAds() {
        List<User> users = userRepository.findAll();
        List<Ad> ads = new ArrayList<>();

        // Categories and their associated items - updated to match your specifications
        String[][] categoryItems = {
                {"Clothing", "Designer Jacket", "Vintage Jeans", "Evening Dress", "Sneakers", "Handbag", "Winter Coat", "Suit", "Boots", "Sunglasses", "Watch"},
                {"Tools", "Power Drill", "Hammer Set", "Screwdriver Kit", "Saw", "Wrench Set", "Toolbox", "Pliers", "Level", "Tape Measure", "Socket Set"},
                {"Sports", "Mountain Bike", "Tennis Racket", "Yoga Mat", "Basketball", "Golf Clubs", "Running Shoes", "Dumbbells", "Treadmill", "Soccer Ball", "Skateboard"},
                {"Accessories", "Phone Case", "Jewelry Box", "Belt", "Wallet", "Keychain", "Bag", "Scarf", "Hat", "Gloves", "Umbrella"},
                {"Furniture", "Vintage Chair", "Coffee Table", "Bookshelf", "Dining Set", "Sofa", "Bed Frame", "Desk", "Wardrobe", "Ottoman", "Side Table"},
                {"Pets", "Dog Bed", "Cat Tree", "Bird Cage", "Fish Tank", "Pet Carrier", "Dog Leash", "Cat Toys", "Pet Food Bowl", "Hamster Cage", "Pet Collar"},
                {"Games", "Board Game", "Chess Set", "Card Game", "Puzzle", "Video Game", "Dice Set", "Monopoly", "Scrabble", "Poker Set", "Jenga"},
                {"Books", "Programming Book", "Novel Collection", "Textbook", "Art Book", "Cookbook", "History Book", "Self-Help Book", "Comic Books", "Dictionary", "Travel Guide"},
                {"Technology", "iPhone 14 Pro", "Samsung Galaxy", "MacBook Pro", "iPad Air", "Gaming Laptop", "Wireless Headphones", "Smart TV", "PlayStation 5", "Xbox Series X", "Tablet"}
        };

        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose", "Austin", "Jacksonville", "Fort Worth", "Columbus", "Charlotte"};

        // Generate 100 ads
        for (int i = 0; i < 100; i++) {
            String[] categoryData = categoryItems[random.nextInt(categoryItems.length)];
            String category = categoryData[0];
            String title = categoryData[1 + random.nextInt(categoryData.length - 1)];

            Ad ad = new Ad();
            ad.setTitle(title);
            ad.setDescription(generateDescription(title, category));
            ad.setPrice(generatePrice(category));
            ad.setCategory(Category.valueOf(category)); // Convert String to Category enum
            ad.setCity(cities[random.nextInt(cities.length)]);
            ad.setUser(users.get(random.nextInt(users.size())));
            ad.setImageUrl("https://picsum.photos/400/300?random=" + (i + 1));
            ad.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(180)));

            ads.add(ad);
        }

        adRepository.saveAll(ads);
        System.out.println("Seeded " + ads.size() + " ads");
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }


    private String generateDescription(String title, String category) {
        String[] descriptions = {
                "Excellent condition " + title.toLowerCase() + ". Barely used, comes with original packaging.",
                "Great " + title.toLowerCase() + " for sale. Perfect for anyone looking for quality " + category + " items.",
                "Used " + title.toLowerCase() + " in good working condition. Some signs of wear but fully functional.",
                "Like new " + title.toLowerCase() + ". Purchased recently but no longer needed.",
                "Quality " + title.toLowerCase() + " at a great price. Must sell due to moving.",
                "Well-maintained " + title.toLowerCase() + ". Has served me well, time for an upgrade.",
                "Authentic " + title.toLowerCase() + " in excellent condition. No damage or defects.",
                "Gently used " + title.toLowerCase() + ". Perfect for students or professionals.",
                "Premium " + title.toLowerCase() + " available. Selling to make space.",
                "Reliable " + title.toLowerCase() + " with minimal wear. Ready for immediate use."
        };
        return descriptions[random.nextInt(descriptions.length)];
    }

    private double generatePrice(String category) {
        switch (category) {
            case "technology":
                return Math.ceil(50 + random.nextDouble() * 1950); // $50-$2000
            case "furniture":
                return Math.ceil(25 + random.nextDouble() * 975); // $25-$1000
            case "sports":
                return Math.ceil(10 + random.nextDouble() * 490); // $10-$500
            case "clothing":
                return Math.ceil(15 + random.nextDouble() * 485); // $15-$500
            case "books":
                return Math.ceil(5 + random.nextDouble() * 95); // $5-$100
            case "tools":
                return Math.ceil(20 + random.nextDouble() * 480); // $20-$500
            case "accessories":
                return Math.ceil(10 + random.nextDouble() * 290); // $10-$300
            case "games":
                return Math.ceil(5 + random.nextDouble() * 195); // $5-$200
            case "pets":
                return Math.ceil(15 + random.nextDouble() * 285); // $15-$300
            default:
                return Math.ceil(10 + random.nextDouble() * 490); // $10-$500
        }
    }
}
