package br.com.wandaymo.caju.authorizer.service;

import br.com.wandaymo.caju.authorizer.domain.User;
import br.com.wandaymo.caju.authorizer.log.Logged;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Logged
    public String getToken(User user) {
        return JWT.create()
                .withIssuer("Caju-Authorizer")
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withExpiresAt(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("-03:00"))
                ).sign(Algorithm.HMAC256("bWV1bm9tZW5hb2Vqb25ueQ=="));
    }

    public String getSubject(String token) {
        return JWT.require(Algorithm.HMAC256("bWV1bm9tZW5hb2Vqb25ueQ=="))
                    .withIssuer("Caju-Authorizer")
                    .build().verify(token).getSubject();
    }
}
