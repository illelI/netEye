package com.neteye.controllers;

import com.neteye.persistence.dto.UserDto;
import com.neteye.persistence.entities.User;
import com.neteye.services.UserService;
import com.neteye.utils.GenericResponse;
import com.neteye.utils.exceptions.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        StringBuilder appUrl = new StringBuilder();
        appUrl.append("https://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort())
                .append(request.getContextPath());
        return new GenericResponse("Success");
    }

    @PostMapping("/login")
    public void login() {

    }

    @GetMapping("/csrf")
    public CsrfResponse csrf(HttpServletRequest request) {
        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        return new CsrfResponse(csrf.getToken());
    }
    public record CsrfResponse(String token) {}
}
