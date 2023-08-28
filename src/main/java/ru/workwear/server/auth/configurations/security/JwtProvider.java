package ru.workwear.server.auth.configurations.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.workwear.server.auth.dto.UserDTO;
import ru.workwear.server.auth.models.User;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
public class JwtProvider {
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.expiration.access}")
    private int accessExpirationMin;
    @Value("${jwt.expiration.refresh}")
    private int refreshExpirationDay;
    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    public JwtProvider(
            @Value("${jwt.secret.access}") String access,
            @Value("${jwt.secret.refresh}") String refresh
    ){
        this.accessKey = Keys.hmacShaKeyFor(access.getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(refresh.getBytes());
    }

    public String generateAccessToken(User user){
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(accessExpirationMin).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(user.getUsername())
                .setExpiration(accessExpiration)
                .claim("authorities", user.getAuthorities())
                .signWith(accessKey)
                .compact();
    }

    public String generateRefreshToken(UserDTO userDTO){
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(refreshExpirationDay).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(userDTO.getUsername())
                .claim("digitalSignature",userDTO.getDigitalSignature())
                .setExpiration(refreshExpiration)
                .signWith(refreshKey)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessKey);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, refreshKey);
    }

    private boolean validateToken(String token, Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, accessKey);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, refreshKey);
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
