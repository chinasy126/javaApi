package com.example.java.utils;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static long expire = 604800;
    private static String secret = "adsffghj";

    public static String generateToken(String username, String userId, String userAvatar) {
        Date date = new Date();
        Date expiration = new Date(date.getTime() + 1000 * expire);

        Map<String,Object> claims = Maps.newHashMap();
        claims.put("avatar",userAvatar);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setId(userId)
                .setIssuedAt(date)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .addClaims(claims)
                .compact();
    }


    public static Claims getClaimsByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e){
           // android.util.Log.d(TAG, "getClaimsByToken() returned: " + );.error("parseJWT error for {}", token);
            return null;
        }

    }

}
