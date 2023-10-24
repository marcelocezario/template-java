package br.dev.mhc.nomeaplicacao.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static java.util.Objects.nonNull;

@Component
public class JWTUtil {

    // TODO criar variavel de ambiente com secret keyword
    private final String secretKeyword = "a1e87b93e29368288407cf9884f7bf55f4f424d1796e844f83a5d0d7420f8e77697472d5c0e3c05ce08f5c453804ac274f6472774c4f168c1f0ca26a6a0cb002";
    // TODO criar variavel de ambiente com expiration token
    private final Long expirationToken = 86400000L;

    public String generateToken(String username, Long userId, Collection<? extends GrantedAuthority> authorities) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(currentTimeMillis() + expirationToken))
                .claim("userId", userId)
                .claim("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList())
                .signWith(SignatureAlgorithm.HS512, getSecretKey()).compact();
    }

    private SecretKey getSecretKey() {
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyword.getBytes());
        return secretKey;
    }

    public boolean isValidToken(String token) {
        Claims claims = getClaims(token);
        if (nonNull(claims)) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(currentTimeMillis());
            return nonNull(username) && nonNull(expirationDate) && now.before(expirationDate);
        }
        return false;
    }

    public String getUsername(String token) {
        return nonNull(getClaims(token)) ? getClaims(token).getSubject() : null;
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKeyword.getBytes()).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

}
