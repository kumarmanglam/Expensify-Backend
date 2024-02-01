package com.expensia.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Secret key for signing the JWT token
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    // Expiration time of the JWT token in milliseconds
    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    // Utility method to generate JWT token
    public String generateToken(Authentication authentication){
        // Extracting username from authentication
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
        // Building JWT token
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())// Signing the token with HMAC
                .compact();
        return token;
        //the form expire date is ->
    }
    // Utility method to get the username from a JWT token
    public String getUsername(String token){
        // Parsing the JWT token and extracting claims
        Claims body = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        // Extracting the username from the claims
        String username = body.getSubject();
        return username;
    }
    // Utility method to validate JWT token
    public boolean validateToken(String token){
        // Parsing and validating the JWT token
        Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parse(token);
        // If parsing and validation are successful, return true
        return true;
    }
    // Utility method to get the key used for signing
    private Key key() {
        // Decoding the secret key from Base64 and returning it
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
//generateToken: This method creates a JWT token using the provided Authentication object. It sets the subject (username), issue date, expiration date, and signs the token using the HMAC key.
//key: This method generates an HMAC key from the base64-decoded JWT secret.
//getUsername: This method extracts the username (subject) from a given JWT token.
//validateToken: This method parses the JWT token to check its validity. If parsing is successful, the token is considered valid.

//Jwts:
//
//Jwts is a class provided by the io.jsonwebtoken library, which is used for creating and parsing JWTs.
//It provides a builder pattern for constructing JWTs with various claims.

//HMAC (Hash-based Message Authentication Code):
//
//Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)): This line generates an HMAC key using the provided secret (jwtSecret),
// which is then used for signing the JWT.
//HMAC is a cryptographic function that combines a secret key with the message to produce a hash, providing a way to verify both the
// data integrity and authenticity

//Claims:
//
//Claims represents the payload of the JWT, containing information such as the subject (username), expiration date, and other custom claims.
//Claims are statements about an entity (typically, the user) and additional metadata

//The JwtTokenProvider class is a utility for generating, parsing, and validating JWT tokens,
// using HMAC for signature verification. It leverages the io.jsonwebtoken library for JWT handling,
// providing methods for token generation, username extraction, and token validation

//the JwtTokenProvider class provides utility methods to generate, validate, and extract information from JWT tokens,
// making it a crucial component in authenticating users using JWTs within a Spring Security context