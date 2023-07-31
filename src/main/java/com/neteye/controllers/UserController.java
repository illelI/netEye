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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin() //to change later
@RequestMapping("/account")
public class UserController {
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    public final Logger logger;

    UserController(UserService userService, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        logger = LogManager.getLogger(UserController.class);
    }


    @PostMapping("/registration")
    public GenericResponse registerAccount(@RequestBody @Valid UserDto user, HttpServletRequest request) {
        logger.debug("Registering user account with information: {}", user);
        User registeredUser = userService.createUser(user);
        if (registeredUser == null) {
            throw new UserAlreadyExistsException();
        }
        logger.info("User with email {} registered.", registeredUser.getEmail());
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
        if(bindingResult.hasErrors()) {
            throw new GenericException("Invalid username or password");
        }

        try {
            request.login(loginDto.getEmail(), loginDto.getPassword());
        } catch (ServletException e) {
            logger.error(e.getMessage(), e.getCause());
            throw new GenericException("Invalid username or password");
        }

        Authentication auth = (Authentication) request.getUserPrincipal();
        User user = (User) auth.getPrincipal();
        logger.info("User with email {} logged in.", user.getEmail());
        return new CurrentUser(user.getId(), user.getEmail());
    }

    @PostMapping("/logout")
    public LogoutResponse logout(HttpServletRequest request) {
        try {
            request.logout();
        } catch (ServletException e) {
            logger.error(e.getMessage(), e.getCause());
            throw new GenericException("Error during logout.");
        }
        return new LogoutResponse();
    }

    @GetMapping("/csrf")
    public CsrfResponse csrf(HttpServletRequest request) {
        try {
            CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
            return new CsrfResponse(csrf.getToken());
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return null;
        }
    }


    public record CsrfResponse(String token) {}
    public record LogoutResponse() {}
    public record CurrentUser(UUID id, String email) {}
}
