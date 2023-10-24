package br.dev.mhc.nomeaplicacao.config;

import br.dev.mhc.nomeaplicacao.entities.User;
import br.dev.mhc.nomeaplicacao.security.UserSpringSecurity;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomAuthorizationManagerTest {

    private CustomAuthorizationManager authorizationManager;

    @BeforeEach
    public void setup() {
        authorizationManager = new CustomAuthorizationManager();
    }

    @Test
    public void shouldCheckIfUserCanAccessSpecificId(){
        var decision = authorizationManager.check(mockAuthentication(1L), mockRequest("/users/1"));
        Assertions.assertTrue(decision.isGranted());

        decision = authorizationManager.check(mockAuthentication(1L), mockRequest("/users/2"));
        Assertions.assertFalse(decision.isGranted());

        decision = authorizationManager.check(mockAuthentication(1L), mockRequest("/test/1"));
        Assertions.assertTrue(decision.isGranted());

        decision = authorizationManager.check(mockAuthentication(1L), mockRequest("users/test/2"));
        Assertions.assertTrue(decision.isGranted());

        decision = authorizationManager.check(mockAuthentication(1L), mockRequest("users/2/teste"));
        Assertions.assertFalse(decision.isGranted());
    }

    private Supplier<Authentication> mockAuthentication(Long userId) {
        Authentication authentication = Mockito.mock(Authentication.class);
        UserSpringSecurity userSpringSecurity = new UserSpringSecurity(User.builder().id(userId).build());
        Mockito.when(authentication.getPrincipal()).thenReturn(userSpringSecurity);
        return () -> authentication;
    }

    private RequestAuthorizationContext mockRequest(String path) {
        RequestAuthorizationContext request = Mockito.mock(RequestAuthorizationContext.class);
        HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(httpRequest.getRequestURI()).thenReturn(path);
        Mockito.when(request.getRequest()).thenReturn(httpRequest);
        return request;
    }

}
