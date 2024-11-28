package aitu.network.aitunetwork.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @GetMapping("/hello")
    public String home(){
        return "Hello World";
    }
    @GetMapping("/secure")
    public String secure(OAuth2AuthenticationToken authentication) {
        return "Authenticated user: " + authentication.getPrincipal().getAttributes();
    }
}
