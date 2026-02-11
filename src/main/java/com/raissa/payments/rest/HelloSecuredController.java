package com.raissa.payments.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloSecuredController {
    @GetMapping("/api/hello")
    public String hello() {
        return "OK, autenticado";
    }

    @GetMapping("/api/whoami")
    public String whoami(org.springframework.security.core.Authentication auth) {
        return "Usuario: " + auth.getName() + " | Authorities: " + auth.getAuthorities();
    }
}
