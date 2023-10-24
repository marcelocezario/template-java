package br.dev.mhc.nomeaplicacao.testconfig;

import br.dev.mhc.nomeaplicacao.entities.User;
import br.dev.mhc.nomeaplicacao.security.UserSpringSecurity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Set;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = User.builder()
                .id(annotation.id())
                .email(annotation.username())
                .password(annotation.password())
                .active(annotation.active())
                .profiles(Set.of(annotation.profile().getCod()))
                .build();

        UserSpringSecurity principal = new UserSpringSecurity(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }

}