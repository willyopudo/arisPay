package org.arispay.controller;

import org.arispay.data.dtoauth.RegistrationDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test-controller")
public class RegistrationController {

    @RequestMapping("/")
    public RegistrationDto returnObjectInBrowser() {
        RegistrationDto someClass = new RegistrationDto();
        return someClass;
    }
}