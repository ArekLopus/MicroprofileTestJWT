package console;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import token_generator.JwtTokenGenerator;

//curl http://localhost:8080/MicroprofileTestJWT-BasicAuthentication/servlet -H"Authorization: Bearer eyJraWQiOiJcL3ByaXZhdGVLZXkucGVtIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiIyNDQwMDMyMCIsImF1ZCI6InM2QmhkUmtxdDMiLCJ1cG4iOiJKb2huIERvZSIsImF1dGhfdGltZSI6MTU1MDM1NjU5OSwiaXNzIjoidGhpc0lzTXlJc3N1ZXIiLCJncm91cHMiOlsidXNlciIsImFkbWluIiwiZGV2Il0sImV4cCI6MTU1MDM1NzU5OSwiaWF0IjoxNTUwMzU2NTk5LCJqdGkiOiJhLTEyMyJ9.f5Mdn9QlKfEaerL9zl8F9huojHWfVBKZoV1YX44Tc2IAuf_aChAyHSJ-siQO35REboWubLLTJ0fujptri0vvno1jI5AkuGj0yvb0xR5gPG2GfTVR7Ye3hWMdAEHpwIsQlpu0ncVhr_TXkAXlmrC_I_bEjuZXxRxRVVHjqEgv5uXImAfLvyRgRyqPltv_6NtAuMluK4GXcr-WoySvmPrAyDvD8NG8MSlYtfqtubbuu4SL66kc65suBqUGpcuV93b1VHIOnBobXNR4BYoJUqa9Icq6u09QK5niGPl1Zhn5ygid32qhy464RR9Hb6NTQpV4GPRe7eC5MUIUS5zGgY_N_A"
public class ClientServletProtected {

	public ClientServletProtected() throws Exception {
		
		Client cl = ClientBuilder.newClient();
		WebTarget target = cl.target("http://localhost:8080/MicroprofileTestJWT/servlet");
		
		String st = target
				.request()
				.header("Authorization", "Bearer " + JwtTokenGenerator.generateJWTString("jwt-token.json"))
				.get(String.class);
		
		System.out.println(st);
	}

	public static void main(String[] args) throws Exception {
		new ClientServletProtected();

	}

}
