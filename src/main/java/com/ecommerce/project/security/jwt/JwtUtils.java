package com.ecommerce.project.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;
    
    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }
    
   /* public String generateTokenFromUserName(UserDetails userDetails) {
    
    }*/
}
