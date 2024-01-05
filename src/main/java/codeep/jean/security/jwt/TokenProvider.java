package codeep.jean.security.jwt;

import codeep.jean.domain.enums.Role;
import codeep.jean.security.jwt.dto.JwtDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {
    private final String secret;
    private static  Key secretKey;
    //access-token
    private static Integer accessTokenExpiredMs;
    //refresh-token
    private static Integer refreshTokenExpiredMs;

    public TokenProvider(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.token-expiration.access}") Integer accessTokenExpiredMs,
            @Value("${spring.jwt.token-expiration.refresh}") Integer refreshTokenExpiredMs
                         ) {
        this.secret = secret;
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiredMs = accessTokenExpiredMs;
        this.refreshTokenExpiredMs = refreshTokenExpiredMs;
    }

    public static JwtDTO createTokens(Long userId, String username, Role role) {
        //access-token 발급
        String accessToken = createAccessJwt(userId,username,role);
        //refresh-token 발급
        String refreshToken = createRefreshJwt();

        return new JwtDTO(accessToken,refreshToken);
    }

    public static String createAccessJwt(Long userId, String userEmail, Role role){
        Claims claims = Jwts.claims();
        claims.put("userId",String.valueOf(userId));
        claims.put("userEmail",userEmail);
        claims.put("role",role);


        return Jwts.builder()
                .setSubject("access-token")
                .setClaims(claims)
                .setIssuer("SKKU.D")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+accessTokenExpiredMs))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public static String createRefreshJwt(){

        return Jwts.builder()
                .setSubject("refresh-token")
                .setIssuer("SKKU.D")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+refreshTokenExpiredMs))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public static String resolveToken(String token){
        return token.split(" ")[1];
    }

    public static Long getExpiration(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(secretKey)
                .build().parseClaimsJws(token).getBody().getExpiration();
        long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    public static String getUserEmail(String token){
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
                .getBody().get("userEmail", String.class);
    }

    public static UsernamePasswordAuthenticationToken getAuthentication(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
    }

}
