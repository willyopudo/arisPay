package org.arispay.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.arispay.data.SelectDto;
import org.arispay.ports.api.BankServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banks")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000")
public class BankController {
    @Autowired
    private BankServicePort bankService;

    // Endpoint to get all banks
    // This method will be implemented in the BankServicePort interface
    // and the corresponding adapter will handle the logic to fetch banks from the database.
    @GetMapping("/select-list")
    public List<SelectDto> getBanksOptionsList() {
        // Return the list of banks
        return bankService.getBanks();
    }

}
