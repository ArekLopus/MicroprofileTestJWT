package jaxrs;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.jwt.JsonWebToken;

import token_generator.JwtTokenGenerator;

@Path("/sec")
public class TestResourcesPublic {
    
    @Inject
    private JsonWebToken principal;
    
    
    @GET
    @Path("public")
    @Produces(TEXT_PLAIN)
    public String publicResource() {
        return
            "This is a public resource \n" +
            "web username: " + principal.getName() + "\n";
    }
    
    
    @GET
    @Path("token")
    @Produces(TEXT_PLAIN)
    public String getToken() throws Exception {
        return JwtTokenGenerator.generateJWTString("jwt-token.json"); 
    }
}
