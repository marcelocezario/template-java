package br.dev.mhc.nomeaplicacao.testconfig;

import br.dev.mhc.nomeaplicacao.enums.ProfileEnum;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    long id() default 1L;
    String username() default "test@test.com";
    String password() default "test@123";
    boolean active() default true;
    ProfileEnum profile() default ProfileEnum.BASIC;

}
