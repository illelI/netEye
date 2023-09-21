package com.neteye.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Log4j2
public class SecurityController {

    @GetMapping("/csrf")
    public CsrfResponse csrf(HttpServletRequest request) {
        log.info("Sending csrf token to ip {}.", request.getRemoteAddr());
        try {
            CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
            return new CsrfResponse(csrf.getToken());
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public record CsrfResponse(String token) {}

}
