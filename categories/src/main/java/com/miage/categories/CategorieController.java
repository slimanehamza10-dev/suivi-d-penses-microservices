package com.miage.categories;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategorieController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello depuis categories";
    }

    @GetMapping("/categories")
    public List<String> categories() {
        return List.of("Courses", "Transport", "Loisirs", "Logement");
    }
}