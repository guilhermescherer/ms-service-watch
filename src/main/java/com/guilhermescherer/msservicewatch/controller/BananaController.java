package com.guilhermescherer.msservicewatch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/banana")
@RestController
public class BananaController {

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello Banana");
    }

    @GetMapping("/torangex")
    public ResponseEntity<String> helloTorangex() {
        return ResponseEntity.ok("Hello Torangex");
    }
}
