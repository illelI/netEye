package com.neteye.controllers;

import com.neteye.persistence.dto.LoginDto;
import com.neteye.persistence.dto.UserDto;
import com.neteye.persistence.entities.User;
import com.neteye.services.UserService;
import com.neteye.utils.GenericResponse;
import com.neteye.utils.exceptions.GenericException;
import com.neteye.utils.exceptions.UserAlreadyExistsException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    UserController(UserService userService, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }


    @PostMapping("/registration")
    public GenericResponse registerAccount(@RequestBody @Valid UserDto user, HttpServletRequest request) {
        log.info("User with ip {} tries to register new account.", request.getRemoteAddr());
        User registeredUser = userService.createUser(user);
        if (registeredUser == null) {
            throw new UserAlreadyExistsException();
        }
        log.info("User with email {} registered.", registeredUser.getEmail());
        StringBuilder appUrl = new StringBuilder();
        appUrl.append("https://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(request.getContextPath());

        return new GenericResponse("Success");
    }

    @PostMapping("/login")
    public CurrentUser login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult, HttpServletRequest request) {
        log.info("User with ip {} tries to log in.", request.getRemoteAddr());
        if(bindingResult.hasErrors()) {
            throw new GenericException("Invalid username or password");
        }
        try {
            request.login(loginDto.getEmail(), loginDto.getPassword());
        } catch (ServletException e) {
            log.error(e.getMessage(), e.getCause());
            throw new GenericException("Invalid username or password");
        }

        Authentication auth = (Authentication) request.getUserPrincipal();
        User user = (User) auth.getPrincipal();
        log.info("User with email {} logged in.", user.getEmail());
        return new CurrentUser(user.getId(), user.getEmail());
    }

    @PostMapping("/logout")
    public LogoutResponse logout(HttpServletRequest request) {
        log.info("User {} tries to log out.", request.getUserPrincipal().getName());
        try {
            request.logout();
        } catch (ServletException e) {
            log.error(e.getMessage(), e.getCause());
            return new LogoutResponse("Error during logout");
        }
        return new LogoutResponse("Success");
    }

    @GetMapping("/csrf")
    public CsrfResponse csrf(HttpServletRequest request) {
        log.info("Sending csrf token to ip {}.", request.getRemoteAddr());
        try {
            CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
            return new CsrfResponse(csrf.getToken());
        } catch (Exception e) {
            log.error(e.getMessage(), e.getCause());
            return null;
        }
    }


    public record CsrfResponse(String token) {}
    public record LogoutResponse(String response) {}
    public record CurrentUser(UUID id, String email) {}
}
