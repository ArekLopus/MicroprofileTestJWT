package jjwt_lib;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import token_generator.JwtTokenGenerator;

//https://github.com/jwtk/jjwt
public class JJWTExampleTokenGenerator {

	public JJWTExampleTokenGenerator() throws Exception {
		
		PrivateKey key = JwtTokenGenerator.readPrivateKey("privateKey.pem");
		
		String s = Jwts.builder()
				.setSubject("1234567890")
				.setId("e9562827-7e11-4893-96d3-7a8d3c6dc44e")
				.setIssuedAt(Date.from(Instant.ofEpochSecond(1550480348)))
				.setExpiration(Date.from(Instant.ofEpochSecond(1650483948)))
				.claim("name", "John Doe")
				.claim("admin", true)
				.signWith(key)
				.compact();
		
		System.out.println(s);
		
		Jws<Claims> parsedClaimsJws = Jwts.parser()
			.setSigningKey(key)
	        .parseClaimsJws(s);
		 
		 System.out.println(parsedClaimsJws);
	}

	public static void main(String[] args) throws Exception {
		new JJWTExampleTokenGenerator();

	}

}
