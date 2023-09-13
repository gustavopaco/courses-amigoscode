package com.pacoprojects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@RestController
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @GetMapping(path = "hello")
//    public List<String> hello() {
//        return List.of("Hello World!", "Bye");
//    }

}
