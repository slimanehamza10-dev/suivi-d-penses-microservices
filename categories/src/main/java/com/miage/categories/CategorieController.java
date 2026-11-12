package com.miage.categories;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategorieController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello depuis categories";
    }
}