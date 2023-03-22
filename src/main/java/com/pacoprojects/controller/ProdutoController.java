package com.pacoprojects.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "produtos")
public class ProdutoController {

    @GetMapping
    public ResponseEntity<List<String>> getProdutos() {
        return ResponseEntity.ok(List.of("Angular","Java", "React"));
    }
}
