package com.dulich.toudulich.component;

import com.dulich.toudulich.Entity.User;
import com.dulich.toudulich.exceptions.InvalidParamException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private int expiration ;

    @Value("${jwt.secretKey}")
    private String secretKey ;

    public String generateToken(User user) throws InvalidParamException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phone", user.getPhone());
        claims.put("userId",user.getId()) ;
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhone())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L)) // expiration tính bằng giây
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Sử dụng HS256 thay vì ES256
                    .compact();
            return token;
        } catch (Exception e) {
            throw new InvalidParamException("Cannot create jwt token, error: " + e.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes); // Sử dụng hmacShaKeyFor cho thuật toán HS256
    }

    private Claims extractAllclaims(String token){
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody() ;

    }

    public  <T> T extractClaim(String token , Function<Claims,T> claimsTFunction){
        final Claims claims = this.extractAllclaims(token) ;
        return  claimsTFunction.apply(claims) ;
    }

    public boolean isTokenExpired(String token){
        Date expirationDate = this.extractClaim(token, Claims::getExpiration) ;
        return expirationDate.before(new Date()) ;
    }

    public String extractPhone(String token){
        return extractClaim(token, Claims::getSubject) ;
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String phone = extractPhone(token) ;
        return (phone.equals(userDetails.getUsername()) && !isTokenExpired(token)) ;
    }
}
