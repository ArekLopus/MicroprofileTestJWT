package jjwt_lib;

import java.security.PrivateKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import token_generator.JwtTokenGenerator;

//https://github.com/jwtk/jjwt
public class JJWTExampleTokenGeneratorParse {

	public JJWTExampleTokenGeneratorParse() throws Exception {
		
		String generatedJWTToken = JwtTokenGenerator.generateJWTString("jwt-token.json");
		PrivateKey key = JwtTokenGenerator.readPrivateKey("privateKey.pem");
		System.out.println(generatedJWTToken);
		
		Jws<Claims> parsedClaimsJws = Jwts.parser()
			.setSigningKey(key)
	        .parseClaimsJws(generatedJWTToken);
		 
		 System.out.println(parsedClaimsJws);
	}

	public static void main(String[] args) throws Exception {
		new JJWTExampleTokenGeneratorParse();
	}

}
