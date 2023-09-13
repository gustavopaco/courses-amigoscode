package com.pacoprojects.controller;

import com.pacoprojects.dto.ClienteDto;
import com.pacoprojects.dto.ClienteDtoList;
import com.pacoprojects.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/clientes")
public class ClienteController {

    @Autowired private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteDtoList>> getClientes() {
        return clienteService.getClientes();
    }

    @PostMapping
    public void addCliente(@RequestBody ClienteDto clienteDto) {
        clienteService.addCliente(clienteDto);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<ClienteDto> updateCliente(@RequestBody ClienteDto clienteDto) {
        return clienteService.updateCliente(clienteDto);
    }

    @DeleteMapping(path = "{id}")
    public void deleteCliente(@PathVariable(name = "id") Integer id) {
        clienteService.deleteCliente(id);
    }
}
