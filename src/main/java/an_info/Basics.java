package an_info;

//-MP-JWT is used for Role Based Access Control (RBAC).
//-For the 1.1 release, the only mandatory container integration is with the JAX-RS container, and injection of the MP-JWT types.
//-If Resource is protected by @RolesAllowed, MP-JWT automatically try to convert Authorization header's Bearer token into JWT:  
// • Extract security token from the request.
// • Perform validation checks against the token.
// • Introspect the token and extract information about the subject.
// • Create a security context for the subject. As part of the security context creation, the server establishes role and group mappings for the subject
//   based on the JWT claims. The role to group mapping is fully configurable by the server along the lines of the Java EE RBAC security model.

//-In general we need Public/PrivateKey and configure 2 properties (Fe in META_INF/microprofile-config.properties):
//	mp.jwt.verify.issuer											- allows for the expected value of the 'iss' claim to be specified.
//	mp.jwt.verify.publickey	OR	mp.jwt.verify.publickey.location

//Marking a JAX-RS Application as Requiring MP-JWT Access Control
//-@LoginConfig provides the same information as the web.xml 'login-config' element.
//-It’s intended usage is to mark a JAX-RS Application as requiring MicroProfile JWT RBAC
//	@LoginConfig(
//	  authMethod = "MP-JWT",
//	  // Even though specified being only for HTTP Basic auth, JBoss/WildFly/ mandates this to refer to its proprietary "security domain" concept.
//	  realmName = "MP-JWT"
//	)
//	@ApplicationPath("res")
//	public class JAXRSConfig extends Application {}


//-Verification of Json Web Tokens (JWT) passed to the Microservice in HTTP requests at runtime is done with the RSA Public Key
// corresponding to the RSA Private Key held by the JWT Issuer.
//-At the time of JWT creation, the Issuer will sign the JWT with its Private Key before passing it to the user. 
//-Upon receiving the JWT in future HTTP requests, Microservices can then use the matching Public Key
// to verify the JWT and trust the user info (claims) it contains.

//Supported Public Key Formats
//-Support for RSA Public Keys of 1024 or 2048 bits in length is required.
//-Other key sizes are allowed, but should be considered vendor-specific.
//-Other asymmetric signature algorithms are allowed, but should be considered vendor-specific.
//-Symmetrically signed JWTs such as HMAC-SHA256 (hs256) are explicitly not supported.

// Requirements for Rejecting MP-JWT Tokens
//-The MP-JWT spec requires that an MP-JWT impl reject a bearer token as an invalid MP-JWT token if any of the conditions are not met:
// • The JWT must have a JOSE header that indicates the token was signed using the RS256 algorithm.
// • The JWT must have an iss claim representing the token issuer that maps to an MP-JWT impl container runtime configured value.
// • The JWT signer must have a public key that maps to an MP-JWT impl container runtime configured value.


//	Injection of JsonWebToken
//-An MP-JWT impl must support the injection of the currently authenticated caller as a JsonWebToken with @RequestScoped scoping:
//	@Inject
//	private JsonWebToken callerPrincipal;

//	Injection of JsonWebToken claims via Raw Type, ClaimValue, javax.enterprise.inject.Instance and JSON-P Types
//-Injection of a non-proxyable raw type like Long must be in a @RequestScoped bean as the producer will have dependendent scope.
//-MP-JWT impls are required to validate the use of claim value injection into contexts that do not match @RequestScoped.
// Note: If one needs to inject a claim value into a scope with a lifetime greater than @RequestScoped, such as @ApplicationScoped
// or @SessionScoped, one can use the javax.enterprise.inject.Instance interface to do so.
//-A may to specify the name of the claim is using a string or a Claims enum value.
//-The string form would allow for specifying non-standard claims while the Claims enum approach guards against typos.

//@RequestScoped
//@Path("/sec")
//public class TestResourcesProtected {
//  
//  @Inject
//  private JsonWebToken principal;
//  
//  @Inject
//  @Claim(standard=Claims.exp)
//	private long timeClaim;
//  
//  @Inject
//  @Claim(standard=Claims.iss)
//	private String issClaim;


public class Basics {}
