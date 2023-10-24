package br.dev.mhc.nomeaplicacao.controllers;

import br.dev.mhc.nomeaplicacao.security.JWTUtil;
import br.dev.mhc.nomeaplicacao.security.UserSpringSecurity;
import br.dev.mhc.nomeaplicacao.services.impl.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public record AuthController(JWTUtil jwtUtil) {

    @PostMapping(value = "/refresh-token")
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        UserSpringSecurity userSS = AuthService.getAuthenticatedUser();
        String token = jwtUtil.generateToken(userSS.getUsername(), userSS.getId(), userSS.getAuthorities());
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        return ResponseEntity.noContent().build();
    }

}
