package console;

import token_generator.JwtTokenGenerator;

public class TestEncodedTokenString {
	
	public TestEncodedTokenString() throws Exception {
		
		String generatedJWTString = JwtTokenGenerator.generateJWTString("jwt-token.json");
		
		System.out.println(generatedJWTString);
		
		System.out.println("-- Finished --");
		
	}
	
	public static void main(String[] args) throws Exception {
		new TestEncodedTokenString();

	}
	
}
