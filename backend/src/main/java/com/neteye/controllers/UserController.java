package com.neteye.controllers;

import com.neteye.persistence.dto.AccountUpdateDto;
import com.neteye.persistence.dto.LoginDto;
import com.neteye.persistence.dto.UserDto;
import com.neteye.persistence.entities.User;
import com.neteye.services.UserService;
import com.neteye.utils.exceptions.GenericException;
import com.neteye.utils.exceptions.UserAlreadyExistsException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/account")
@Log4j2
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public CurrentUser registerAccount(@RequestBody @Valid UserDto user, HttpServletRequest request) {
        log.info("User with ip {} tries to register new account.", request.getRemoteAddr());
        User registeredUser = userService.createUser(user);
        if (registeredUser == null) {
            throw new UserAlreadyExistsException();
        }
        try {
            request.login(user.getEmail(), user.getPassword());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("User with email {} registered.", registeredUser.getEmail());

        return new CurrentUser(registeredUser.getId(), user.getEmail());
    }

    @PostMapping("/login")
    public CurrentUser login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult, HttpServletRequest request) {
        log.info("User with ip {} tries to log in.", request.getRemoteAddr());
        if(bindingResult.hasErrors()) {
            throw new GenericException("Invalid username or password");
        }
        try {
            request.login(loginDto.getEmail(), loginDto.getPassword());
        } catch (ServletException | BadCredentialsException e) {
            log.error(e.getMessage());
            throw new GenericException("Invalid username or password");
        }
        Authentication auth = (Authentication) request.getUserPrincipal();
        User user = (User) auth.getPrincipal();
        log.info("User with email {} logged in.", user.getEmail());
        return new CurrentUser(user.getId(), user.getEmail());
    }

    @PostMapping("/logout")
    public GenericResponse logout(HttpServletRequest request) {
        log.info("User {} tries to log out.", request.getUserPrincipal().getName());
        try {
            request.logout();
        } catch (ServletException e) {
            log.error(e.getMessage());
            return new GenericResponse("Error during logout");
        }
        return new GenericResponse("Success");
    }

    @PostMapping("/update")
    public GenericResponse updateAccount(@RequestBody AccountUpdateDto accountDto, HttpServletRequest request) {
        Authentication auth = (Authentication) request.getUserPrincipal();
        User user = (User) auth.getPrincipal();
        userService.updateUser(accountDto, user);
        return new GenericResponse("Success");
    }

    public record GenericResponse(String response) {}
    public record CurrentUser(Integer id, String email) {}
}
