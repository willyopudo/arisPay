package org.arispay.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.arispay.auth.JwtUtil;
import org.arispay.data.ClientDto;
import org.arispay.data.GenericFilterDto;
import org.arispay.ports.api.ClientServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController {

    private ClientServicePort clientServicePort;

    private final JwtUtil jwtUtil;

    public ClientController(JwtUtil jwtUtil, ClientServicePort clientServicePort) {
        this.jwtUtil = jwtUtil;
        this.clientServicePort = clientServicePort;
    }

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
    public ResponseEntity<Page<ClientDto>> getAllClients(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int itemsPerPage,
                                                         @RequestParam(name = "status", required = false, defaultValue = "") String status,
                                                         @RequestParam(name = "identifierType", required = false, defaultValue = "") String identifierType,
                                                         HttpServletRequest request) {

        Claims claims = jwtUtil.resolveClaims(request);


        GenericFilterDto filterDto = new GenericFilterDto(
                List.of( status, identifierType),
                null,
                null,
                null
        );

        Long companyId = claims.get("companyId", Long.class);

        Pageable pageable = PageRequest.of(page-1, itemsPerPage);
        return ResponseEntity.ok(clientServicePort.getClients(companyId, pageable, filterDto));
    }

    @DeleteMapping("/{id}")
    public void deleteCompanyByID(@PathVariable long id) {
        clientServicePort.deleteClientById(Long.valueOf(id));
    }

}
