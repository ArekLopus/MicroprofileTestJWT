package jjwt_lib;

import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JJWTCreateToken {
	
	private static Key key;
	
	public JJWTCreateToken() throws Exception {
		
		String token = createToken();
		System.out.println(token);
		
		Jws<Claims> parsedClaimsJws = Jwts.parser()
			.setSigningKey(JJWTCreateToken.key)
	        .parseClaimsJws(token);
		 
		 System.out.println(parsedClaimsJws);
		 
	}

	public static String createToken() throws Exception {
		
		//PrivateKey key = JwtTokenGenerator.readPrivateKey("privateKey.pem");
		JJWTCreateToken.key = Keys.hmacShaKeyFor("mySecretPasword1mySecretPasword2".getBytes("UTF-8"));
		
		Instant now = Instant.ofEpochSecond(System.currentTimeMillis());
		
		String s = Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("kid", "/privateKey.pem")
				.setIssuer("thisIsMyIssuer")
				.claim("jti", "a-123")
				.setSubject("24400320")
				.setAudience("s6BhdRkqt3")
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plusSeconds(60 * 10)))
				.claim("auth_time", now.getEpochSecond())
				.claim("upn", "John Doe")
				.claim("groups", Arrays.asList("user", "admin", "dev"))
				.signWith(key)
				.compact();
		return s;
	}
	
	public static void main(String[] args) throws Exception {
		new JJWTCreateToken();

	}
}
//Header
//{
//"kid": "/privateKey.pem",
//"typ": "JWT",
//"alg": "RS256"
//}

//Claims
//{
//"iss": "thisIsMyIssuer",
//"jti": "a-123",
//"sub": "24400320",
//"aud": "s6BhdRkqt3",
//"exp": 1311281970,
//"iat": 1311280970,
//"auth_time": 1311280969,
//"upn": "John Doe",
//"groups": [
//  "user",
//  "admin",
//  "dev"
//]
//}
