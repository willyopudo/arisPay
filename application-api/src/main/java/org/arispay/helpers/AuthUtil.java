package org.arispay.helpers;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Logger;
import org.arispay.data.*;
import org.arispay.data.dtoauth.EmailDetails;
import org.arispay.data.dtoauth.RegistrationDto;
import org.arispay.ports.api.CompanyAccountServicePort;
import org.arispay.ports.api.CompanyServicePort;
import org.arispay.ports.api.UserServicePort;
import org.arispay.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.context.Context;

@Service
public class AuthUtil {

    @Autowired
    private EmailService emailService;

    public ResponseEntity<GenericHttpResponse<UserDto>> getGenericHttpResponseResponseEntity(@RequestBody @Valid UserDto userDto, BindingResult result, GenericHttpResponse<UserDto> response, UserDto existingUser, Logger logger, PasswordEncoder passwordEncoder, UserServicePort userServicePort) {
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }
//        if (userDto.getPassword().isEmpty()) {
//            result.rejectValue("password", null,
//                    "Password cannot be empty");
//        }
        if (result.hasErrors()) {
            logger.debug("There were errors");
            StringBuilder errors = new StringBuilder();
            for (ObjectError element : result.getAllErrors()) {
                errors.append(element.toString()).append("\n");
                logger.debug("{} ", element.toString());
            }
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("There were errors : " + errors.toString());
            response.setData(userDto);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

//        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if (userDto.getRole() == null)
            userDto.setRole("ROLE_COMPANY_USER");
        UserDto savedUser =  userServicePort.saveUser(userDto);
        savedUser.setPassword(null);

        String message = String.format("Dear  %s, you have been created on ArisPay by your Company Admin.", savedUser.getFirstName());
        String subject = "Update your user details";

        Context context = new Context();
        // Set variables for the template
        context.setVariable("message", message);
        context.setVariable("subject", subject);

        EmailDetails emailDetails = new EmailDetails(savedUser.getEmail(), "", subject, "");

        try {
            emailService.sendHTMLMail(emailDetails, "companyUserEmailTemplate", context);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("User registered successfully");
        response.setData(savedUser);

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @Transactional
    public static ResponseEntity<GenericHttpResponse<?>> registerCompanyAdmin(@RequestBody @Valid RegistrationDto registrationDto,
                                                                                              BindingResult result, GenericHttpResponse<RegistrationDto> response,
                                                                                              UserDto existingUser,
                                                                                              Logger logger,
                                                                                              PasswordEncoder passwordEncoder,
                                                                                              UserServicePort userServicePort,
                                                                                              CompanyServicePort companyServicePort,
                                                                                              CompanyAccountServicePort<CompanyAccountDto> companyAccountServicePort) {
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("userDto.email", null,
                    "There is already an account registered with the same email");
        }
        if (registrationDto.getUserDto().getPassword().isEmpty()) {
            result.rejectValue("userDto.password", null,
                    "Password cannot be empty");
        }
        if (result.hasErrors()) {
            logger.debug("There were errors");
            StringBuilder errors = new StringBuilder();
            for (ObjectError element : result.getAllErrors()) {
                errors.append(element.toString()).append("\n");
                logger.debug("{} ", element.toString());
            }
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("There were errors : " + errors.toString());
            response.setData(registrationDto);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        registrationDto.getUserDto().setPassword(passwordEncoder.encode(registrationDto.getUserDto().getPassword()));
        if (registrationDto.getUserDto().getRole().contains("ROLE_COMPANY_USER")) {
            UserDto savedUser = userServicePort.saveUser(registrationDto.getUserDto());
            savedUser.setPassword(null);
            registrationDto.setUserDto(savedUser);
        }
        else if (registrationDto.getUserDto().getRole().contains("ROLE_COMPANY_ADMIN")) {
            CompanyDto savedCompany = companyServicePort.addCompany(registrationDto.getCompanyDto());
            UserDto savedUser = registrationDto.getUserDto();
            UserCompanyDto userCompanyDto = new UserCompanyDto(null, savedCompany.getId(), true);
            savedUser.getUserCompanies().add(userCompanyDto);
            savedUser = userServicePort.saveUser(savedUser);
            savedUser.setPassword(null);
            CompanyAccountDto savedCompanyAccount = registrationDto.getCompanyAccountDto();
            savedCompanyAccount.setCompanyId(savedCompany.getId());
            savedCompanyAccount = (CompanyAccountDto) companyAccountServicePort.add(savedCompanyAccount);
            registrationDto.setCompanyDto(savedCompany);
            registrationDto.setUserDto(savedUser);
            registrationDto.setCompanyAccountDto(savedCompanyAccount);
        }

        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("User registered successfully");
        response.setData(registrationDto);

        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
