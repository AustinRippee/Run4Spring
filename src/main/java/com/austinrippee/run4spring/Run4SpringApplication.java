package com.austinrippee.run4spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Run4SpringApplication {

    String whatNot = "hello, this is a sample message";
    int five = 5;

    @RequestMapping("/")
    String home() {
        return whatNot + " " + five;
    }

    public static void main(String[] args) {
        SpringApplication.run(Run4SpringApplication.class, args);
    }

}
