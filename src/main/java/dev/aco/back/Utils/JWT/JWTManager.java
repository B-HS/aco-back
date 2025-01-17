package dev.aco.back.Utils.JWT;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component

public class JWTManager {
    @Value("${dev.aco.secretkey}")
    private String secretKey;
    private final long DAY = 259200000 / 3;

    public List<String> AccessRefreshGenerator(Long memberId, String email){
        List<String> tokenAry = new ArrayList<>();
        tokenAry.add(tokenGenerate(memberId, email, 1L));
        tokenAry.add(tokenGenerate(memberId, email, 7L));

        return tokenAry;

    }

    public String tokenGenerate(Long memberId, String email, Long expireRate) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        Date today = new Date();
        String jwt = Jwts.builder()
                .setIssuer("hyns.dev")
                .setIssuedAt(today)
                .setExpiration(new Date(today.getTime() + ((DAY / 24 / 6) * expireRate)))
                .claim("email", email)
                .claim("userNumber", memberId)
                .setSubject("bblog token")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return "Bearer " + jwt;
    }

    public Boolean tokenValidator(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).build().parseClaimsJws(token.split("Bearer ")[1]);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 토큰", e);
        } catch (ExpiredJwtException e) {
            log.info("유효기간이 지난 토큰", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 토큰", e);
        } catch (IllegalArgumentException e) {
            log.info("claims가 비어있음", e);
        }
        return false;
    }

    public Claims tokenParser(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)).build().parseClaimsJws(token.split("Bearer ")[1]).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
