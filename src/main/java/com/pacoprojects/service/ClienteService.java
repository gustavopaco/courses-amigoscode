package com.pacoprojects.service;

import com.pacoprojects.dto.ClienteDto;
import com.pacoprojects.dto.ClienteDtoList;
import com.pacoprojects.mapper.ClienteMapper;
import com.pacoprojects.model.Cliente;
import com.pacoprojects.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ClienteService{

    private final ClienteMapper clienteMapper;
    private final ClienteRepository clienteRepository;

    public ResponseEntity<List<ClienteDtoList>> getClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        List<ClienteDtoList> clienteDtoLists = clientes.stream().map(clienteMapper::toDto1).collect(Collectors.toList());
        return ResponseEntity.ok(clienteDtoLists);

    }

    public void addCliente(ClienteDto clienteDto) {
        Cliente cliente = clienteMapper.toEntity(clienteDto);
        clienteRepository.save(cliente);

    }

    public void deleteCliente(Integer id) {
        clienteRepository.deleteById(id);
    }

    public ResponseEntity<ClienteDto> updateCliente(ClienteDto clienteDto) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(clienteDto.id());
        clienteOptional.orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não foi encontrado");
        });

        Cliente cliente = clienteRepository.save(clienteMapper.toEntity(clienteDto));

        return ResponseEntity.ok(clienteMapper.toDto(cliente));
    }
}
