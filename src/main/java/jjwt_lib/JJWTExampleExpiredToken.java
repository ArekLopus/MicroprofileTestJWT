package jjwt_lib;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

//https://github.com/jwtk/jjwt
public class JJWTExampleExpiredToken {
	
	private String token;
	
	public JJWTExampleExpiredToken() {
		
		try {
			
			SecretKey key = Keys.hmacShaKeyFor("mySecretPasword1mySecretPasword2".getBytes("UTF-8"));
			long currentTimeInSecs = (System.currentTimeMillis() / 1000);
	        long expirationTime = currentTimeInSecs + 3;
			
	        if(token == null) {			// To check if invalid after X seconds
	        	token = Jwts.builder()
	        		.setSubject("1234567890")
					.setId("e9562827-7e11-4893-96d3-7a8d3c6dc44e")
					.setIssuedAt(Date.from(Instant.ofEpochSecond(currentTimeInSecs)))
					.setExpiration(Date.from(Instant.ofEpochSecond(expirationTime)))
					.claim("name", "John Doe")
					.claim("admin", true)
					.signWith(key)
					.compact();
			}
			
			System.out.println("Token: " + token);
			
			Jws<Claims> parsedClaimsJws = Jwts.parser()
				.setSigningKey(key)
		        .parseClaimsJws(token);
			 
			System.out.println("Jws Claims: " + parsedClaimsJws);
			
			
			System.out.println("\nWaiting for token getting expired...");
			Thread.sleep(3000);
			 
			Jws<Claims> parsedClaimsJws2 = Jwts.parser()
				.setSigningKey(key)
				.parseClaimsJws(token);
					 
			System.out.println("Jws Claims: " + parsedClaimsJws2);
			 
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
		
		
	}

	public static void main(String[] args) {
		new JJWTExampleExpiredToken();

	}

}
