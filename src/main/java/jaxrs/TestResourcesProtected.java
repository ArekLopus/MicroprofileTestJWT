package jaxrs;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.util.Date;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

//-Injection of a non-proxyable raw type like Long must be in a @RequestScoped bean as the producer will have dependendent scope.
//-A may to specify the name of the claim is using a string or a Claims enum value.
//-The string form would allow for specifying non-standard claims while the Claims enum approach guards against typos.

@RequestScoped
@Path("/sec")
public class TestResourcesProtected {
    
    @Inject
    private JsonWebToken principal;
    
    @Inject
    @Claim(standard=Claims.exp)
	private long timeClaim;
    
    @Inject
    @Claim(standard=Claims.iss)
	private String issClaim;
    
    
    @GET
    @Path("protected")
    @Produces(TEXT_PLAIN)
    @RolesAllowed("admin")
    public String protectedResource() {
    	
    	System.out.println("--- Protected Resource ---");
    	System.out.println("Claims.iss -> " + issClaim);
    	System.out.println("Claims.exp -> " + timeClaim);
    	System.out.println("Expiration Time -> " + new Date(timeClaim * 1000));
        System.out.println("Principal: " + principal);
        System.out.println("Principal groups: " + principal.getGroups());
    	
    	Set<String> claimNames = principal.getClaimNames();
    	StringBuilder sb = new StringBuilder();
    	claimNames.forEach(e -> sb.append(e + ", "));
    	System.out.println("Claim names -> " + sb);
    	
        return
            "This is a protected resource \n" +
            "web username: " + principal.getName() + "\n";
    }
    
}
