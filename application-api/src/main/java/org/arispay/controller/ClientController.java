package org.arispay.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.arispay.data.ClientDto;
import org.arispay.ports.api.ClientServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController {

    @Autowired
    private ClientServicePort clientServicePort;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDto addClient(@RequestBody ClientDto clientDto) {
        return clientServicePort.addClient(clientDto);
    }

    @PutMapping
    public ClientDto updateClient(@RequestBody ClientDto clientDto) {
        return clientServicePort.updateClient(clientDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientByID(@PathVariable String id) {
        return ResponseEntity.ok(clientServicePort.getClientById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ClientDto>> getAllClients() {
        return ResponseEntity.ok(clientServicePort.getClients());
    }

    @DeleteMapping("/{id}")
    public void deleteCompanyByID(@PathVariable long id) {
        clientServicePort.deleteClientById(Long.valueOf(id));
    }
}
