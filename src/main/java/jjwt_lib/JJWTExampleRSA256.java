package jjwt_lib;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

//https://github.com/jwtk/jjwt
public class JJWTExampleRSA256 {

	public JJWTExampleRSA256() throws Exception {
		
		KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		
		String s = Jwts.builder()
				.setSubject("1234567890")
				.setId("e9562827-7e11-4893-96d3-7a8d3c6dc44e")
				.setIssuedAt(Date.from(Instant.ofEpochSecond(1550480348)))
				.setExpiration(Date.from(Instant.ofEpochSecond(1650483948)))
				.claim("name", "John Doe")
				.claim("admin", true)
				.signWith(privateKey)
				.compact();
		
		System.out.println(s);
		
		Jws<Claims> parsedClaimsJws = Jwts.parser()
			.setSigningKey(publicKey)
	        .parseClaimsJws(s);
		 
		 System.out.println(parsedClaimsJws);
	}

	public static void main(String[] args) throws Exception {
		new JJWTExampleRSA256();

	}

}
