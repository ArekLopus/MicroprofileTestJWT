package console;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import token_generator.JwtTokenGenerator;

@SuppressWarnings("unused")
public class ClientProtected {

	public ClientProtected() throws Exception {
		
		Client cl = ClientBuilder.newClient();
		WebTarget target = cl.target("http://localhost:8080/MicroprofileTestJWT/res/sec/protected");
		
		String expiredToken = "eyJraWQiOiJcL3ByaXZhdGVLZXkucGVtIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiIyNDQwMDMyMCIsImF1ZCI6InM2QmhkUmtxdDMiLCJ1cG4iOiJKb2huIERvZSIsImF1dGhfdGltZSI6MTU1MDgyNDEzMywiaXNzIjoidGhpc0lzTXlJc3N1ZXIiLCJncm91cHMiOlsidXNlciIsImFkbWluIiwiZGV2Il0sImV4cCI6MTU1MDgyNDEzNCwiaWF0IjoxNTUwODI0MTMzLCJqdGkiOiJhLTEyMyJ9.C4AouCVCVLRMj4X4C5QepcOtAVqn9HtsZpmGbrSCe-hqgzhoio6oagbu9bx12i3jmmLuBn2PXa6CLWOYH2nxsG6h7YbsfCnQa1qAqSdjbMRpHB2G-2WyuMUFECrmmSD9X-WL3X8a0JxiXEqSq9lf1CVW2sjdkIyModOm9xTse8kYGmmOjpxpD04UjFxkt3CAfmiXieS8O0L_sszwemW2cY54Svnqcahg_OUOu5hJWVTpZBevvTYcCZxh9xoEMDxJ-BQHo4KMW647QMc2q9z-_4obeavQnuw060hte4hqYJPVsg6etWbw0KIFhaFleeBKZLPmAgXLHtY7D6bdIYMR4A";
		
		String st = target
				.request()
				.header("Authorization", "Bearer " + JwtTokenGenerator.generateJWTString("jwt-token.json"))
				//.header("Authorization", "Bearer " + expiredToken)												// HTTP 401 Unauthorized
				.get(String.class);
		
		System.out.println(st);
	}

	public static void main(String[] args) throws Exception {
		new ClientProtected();

	}

}
