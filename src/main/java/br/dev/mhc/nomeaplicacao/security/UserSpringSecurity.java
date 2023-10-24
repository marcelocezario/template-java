package br.dev.mhc.nomeaplicacao.security;

import br.dev.mhc.nomeaplicacao.entities.User;
import br.dev.mhc.nomeaplicacao.enums.ProfileEnum;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserSpringSecurity implements UserDetails {

    @Getter
    private final Long id;
    private final String username;
    private final String password;
    private final boolean active;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserSpringSecurity(User user) {
        super();
        id = user.getId();
        username = user.getEmail();
        password = user.getPassword();
        active = user.isActive();
        authorities = user.getProfiles().stream().map(p -> new SimpleGrantedAuthority("ROLE_" + p.getDescription())).toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public boolean hasRole(ProfileEnum profile) {
        return getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + profile.getDescription()));
    }
}