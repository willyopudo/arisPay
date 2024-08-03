package org.arispay.controller;

import java.util.List;

import org.arispay.data.ClientDto;
import org.arispay.ports.api.ClientServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    @Autowired
    private ClientServicePort clientServicePort;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDto addClient(@RequestBody ClientDto clientDto) {
        return clientServicePort.addClient(clientDto);
    }

    @PutMapping
    public ClientDto updateCompany(@RequestBody ClientDto clientDto) {
        return clientServicePort.updateClient(clientDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientByID(@PathVariable String id) {
        return ResponseEntity.ok(clientServicePort.getClientById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(clientServicePort.getClients());
    }

    @DeleteMapping("/{id}")
    public void deleteCompanyByID(@PathVariable long id) {
        clientServicePort.deleteClientById(id);
    }
}
