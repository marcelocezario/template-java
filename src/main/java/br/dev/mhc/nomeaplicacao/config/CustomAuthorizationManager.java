package br.dev.mhc.nomeaplicacao.config;

import br.dev.mhc.nomeaplicacao.enums.ProfileEnum;
import br.dev.mhc.nomeaplicacao.security.UserSpringSecurity;
import br.dev.mhc.nomeaplicacao.utils.LogHelper;
import br.dev.mhc.nomeaplicacao.utils.Util;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private LogHelper LOG = new LogHelper(this.getClass());

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext request) {
        UserSpringSecurity userSpringSecurity;
        try {
            userSpringSecurity = (UserSpringSecurity) authentication.get().getPrincipal();
        } catch (Exception e) {
            return new AuthorizationDecision(false);
        }

        if (userSpringSecurity.hasRole(ProfileEnum.ADMIN)) {
            return new AuthorizationDecision(true);
        }

        String uri = request.getRequest().getRequestURI();
        List<String> uriParts = Arrays.asList(uri.split("/"));

        boolean isAuthorized = true;

        isAuthorized &= verifyUserId(uriParts, userSpringSecurity);

        return new AuthorizationDecision(isAuthorized);
    }

    private boolean verifyUserId(List<String> uriParts, UserSpringSecurity userSpringSecurity) {
        boolean isAuthorized = true;
        Integer indexUsers = uriParts.indexOf("users");
        if (indexUsers >= 0 && indexUsers + 1 < uriParts.size()) {
            String childPath = uriParts.get(indexUsers + 1);
            if (Util.isIntegerNumber(childPath)) {
                try {
                    Long childId = Long.parseLong(childPath);
                    isAuthorized &= userSpringSecurity.getId().equals(childId);
                } catch (NumberFormatException e) {
                    LOG.error("It was not possible to convert the value into a number", childPath, uriParts);
                    isAuthorized &= true;
                }
            }
        }
        return isAuthorized;
    }

}
