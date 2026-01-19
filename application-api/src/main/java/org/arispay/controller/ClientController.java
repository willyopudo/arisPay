package org.arispay.controller;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.arispay.auth.JwtUtil;
import org.arispay.data.ActivityEventDto;
import org.arispay.data.ClientDto;
import org.arispay.data.GenericFilterDto;
import org.arispay.ports.api.ActivityServicePort;
import org.arispay.ports.api.ClientServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class ClientController {

    private final ClientServicePort clientServicePort;
    private final JwtUtil jwtUtil;
    private final ActivityServicePort activityService;

    private static final Logger logger = LogManager.getLogger(ClientController.class);

    public ClientController(JwtUtil jwtUtil, ClientServicePort clientServicePort, ActivityServicePort activityService) {
        this.jwtUtil = jwtUtil;
        this.clientServicePort = clientServicePort;
        this.activityService = activityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDto addClient(@RequestBody ClientDto clientDto, Authentication authentication) {
        clientDto.setCreatedBy(authentication.getName());
        ClientDto savedClient = clientServicePort.addClient(clientDto);

        // Log activity
        String userName = authentication.getName();
        ActivityEventDto event = activityService.clientCrudEvent(savedClient, "CLIENT_CREATED", userName);
        activityService.broadcastActivityToCompany(savedClient.getCompany(), event);

        return savedClient;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@RequestBody ClientDto clientDto,
                                          @PathVariable long id,
                                          Authentication authentication) {
        if(clientDto.getId() != id) {
            return ResponseEntity.badRequest().body("Id in path and body do not match");
        }
        try {
            ClientDto updatedClient = clientServicePort.updateClient(clientDto);

            // Log activity
            String userName = authentication.getName();
            ActivityEventDto event = activityService.clientCrudEvent(updatedClient, "CLIENT_UPDATED", userName);
            activityService.broadcastActivityToCompany(updatedClient.getCompany(), event);

            return ResponseEntity.ok(updatedClient);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
                                                         @RequestParam(name = "search", required = false) String search,
                                                         @RequestParam(name = "sortBy", defaultValue = "clientName", required = false) String sortBy,
                                                         @RequestParam(name = "orderBy", defaultValue = "asc", required = false) String orderBy,
                                                         HttpServletRequest request,
                                                         Authentication authentication) {

        logger.info("Authentication: {}", authentication.getAuthorities());

        Claims claims = jwtUtil.resolveClaims(request);

        // Determine sort direction
        Sort.Direction direction = "desc".equalsIgnoreCase(orderBy)
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        GenericFilterDto filterDto = new GenericFilterDto(
                List.of( status, identifierType),
                search,
                direction,
                sortBy
        );

        Long companyId = claims.get("companyId", Long.class);

        Pageable pageable = PageRequest.of(page-1, itemsPerPage);
        return ResponseEntity.ok(clientServicePort.getClients(companyId, pageable, filterDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClientById(@PathVariable long id, Authentication authentication) {
        try {
            // Get client details before deletion for activity log
            ClientDto client = clientServicePort.getClientById(String.valueOf(id));
            if (client == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found");
            }

            Long companyId = client.getCompany();
            clientServicePort.deleteClientById(id);

            // Log activity
            String userName = authentication.getName();
            ActivityEventDto event = activityService.clientCrudEvent(client, "CLIENT_DELETED", userName);
            activityService.broadcastActivityToCompany(companyId, event);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Client deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
