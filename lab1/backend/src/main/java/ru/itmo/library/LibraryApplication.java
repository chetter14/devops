package ru.itmo.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryApplication {

    // Unused field
//    private static final String UNUSED_FIELD = "This is never used";

    public static void main(String[] args) {
//        // Duplicate strings
//        String s1 = "SonarQube";
//        String s2 = "SonarQube";
//
//        // Empty catch block
//        try {
//            int num = Integer.parseInt("123");
//        } catch (NumberFormatException e) {
//            // Do nothing
//        }
//
//        // Potential NPE
//        String nullString = null;
//        if (nullString.equals("test")) {
//            System.out.println("This will never happen");
//        }
//
//        // Unused variable
//        int unusedVar = 42;
//
//        // Inefficient string concatenation in loop
//        String result = "";
//        for (int i = 0; i < 10; i++) {
//            result += i;
//        }
//
//        // Hardcoded password - security hotspot
//        String password = "admin123";
//
//        // Random unused method call
//        meaninglessMethod();

        SpringApplication.run(LibraryApplication.class, args);
    }

//    // Method with multiple issues
//    private void neverCalled() {
//        // Duplicated code
//        System.out.println("Hello");
//        System.out.println("Hello");
//
//        // Magic number
//        for (int i = 0; i < 100; i++) {
//            if (i == 99) {
//                System.out.println("Magic number reached");
//            }
//        }
//    }
//
//    // Dead code
//    private static void meaninglessMethod() {
//        System.out.println("This will never be executed");
//    }
}
