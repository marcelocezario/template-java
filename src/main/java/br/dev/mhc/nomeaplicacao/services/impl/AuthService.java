package br.dev.mhc.nomeaplicacao.services.impl;

import br.dev.mhc.nomeaplicacao.security.UserSpringSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthService {

    public static UserSpringSecurity getAuthenticatedUser() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

}
