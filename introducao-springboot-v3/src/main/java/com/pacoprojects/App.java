package com.pacoprojects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }

//    @GetMapping("/greet")
//    public GreetResponse greet() {
//        GreetResponse response = new GreetResponse("Ola Mundo",
//                List.of("Java", "Angular", "Javascript", "CSS"),
//                new Pessoa("Gustavo", 34, 10_000));
//        return response;
//    }
//
//    record Pessoa(String nome, int idade, double poupanca) {}
//
//    record GreetResponse(String greet, List<String> linguagensFavoritas, Pessoa pessoa) {}

//    class GreetResponse {
//        private final String nome;
//
//        public GreetResponse(String nome) {
//            this.nome = nome;
//        }
//
//        public String getNome() {
//            return nome;
//        }
//
//        @Override
//        public String toString() {
//            return "GreetResponse{" +
//                    "greet='" + nome + '\'' +
//                    '}';
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            GreetResponse that = (GreetResponse) o;
//            return Objects.equals(nome, that.nome);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(nome);
//        }
//    }
}
