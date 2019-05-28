package an_info;

//		Generaly
//-Note that for signed tokens the information, though protected against tampering, is readable by anyone.
//-Do not put secret information in the payload or header elements of a JWT unless it is encrypted.

//-For the 1.1 release, the only mandatory container integration is with the JAX-RS container, and injection of the MP-JWT types.

//-Creating JWT token is not a part of Microprofile JWT Propagation.
// We need to create an encrypted JWT token to send from client, afre log-in, for example.
//-When a request comes with a header "Authorization: Bearer tokenHere" token is automatically parsed by PublicKey and user is authenticated.
//-We need access to the PublicKey

//	Generating PCKS#8 Public/PrivateKey
//1. Generate the base key by entering:
//	openssl genrsa -out baseKey.pem
//2. From the base key generate the PKCS#8 private key:
//	openssl pkcs8 -topk8 -inform PEM -in baseKey.pem -out privateKey.pem -nocrypt
//3. And generate the public key:
//	openssl rsa -in baseKey.pem -pubout -outform PEM -out publicKey.pem
//or using JJWT
//	KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
//	PrivateKey privateKey = keyPair.getPrivate();
//	PublicKey publicKey = keyPair.getPublic();


//	Marking a JAX-RS Application as Requiring MP-JWT Access Control
//-Since the MicroProfile does not specify a deployment format, and currently does not rely on servlet metadata descriptors,
// we have added an org.eclipse.microprofile.jwt@.LoginConfig that provides the same information as the web.xml login-config element.
//		@LoginConfig(authMethod = "MP-JWT", realmName = "TCK-MP-JWT")
//		@ApplicationPath("/")
//		public class TCKApplication extends Application {}


//	Injection of JsonWebToken
//-JsonWebToken implements Principal
//-An MP-JWT impl must support the injection of the currently authenticated caller as a JsonWebToken with @RequestScoped scoping:
//		@Inject
//		private JsonWebToken callerPrincipal;


//	Claims
//-The spec requires support for injection of claims from the current JsonWebToken using org.eclipse.microprofile.jwt.Claim qualifier.
//		@Inject	@Claim(standard = Claims.iat)		private Long issuedAt;
//		@Inject	@Claim(standard = Claims.raw_token)	private ClaimValue<String> rawTokenCV;
//		@Inject	@Claim(standard = Claims.iat)		private Instance<Long> providerIAT;
//		@Inject	@Claim(standard = Claims.jti)		private JsonString jsonJTI;
//-Injection of a non-proxyable raw type like Long must be in a @RequestScoped bean as the producer will have dependendent scope.
//-One may to specify the name of the claim is using a string or a Claims enum value.
//-The string form would allow for specifying non-standard claims while the Claims enum approach guards against typos.
//	{
//    "iss": "thisIsMyIssuer",
//    "jti": "a-123",
//    "sub": "24400320",
//    "aud": "s6BhdRkqt3",
//    "exp": 1311281970,
//    "iat": 1311280970,
//    "auth_time": 1311280969,
//    "upn": "John Doe",			//upn is Principal's name
//    "groups": [					//groups is translated into roles
//        "user",
//        "admin",
//        "dev"
//    ]
//	}


//	@RolesAllowed, @PermitAll, @DenyAll 
//-The role names that have been mapped to group names in the MP-JWT "groups" claim, MUST result in an allowing
// authorization decision wherever the security constraint has been applied.


//	APIs Integration
// • SecurityContext#getUserPrincipal()
//The java.security.Principal returned from these methods MUST be an instance of org.eclipse.microprofile.jwt.JsonWebToken.

// • SecurityContext#isUserInRole(String)
//This method MUST return true for any name that is included in the MP-JWT "groups" claim, as well as for any role name that has been mapped to a group name in the MP-JWT "groups" claim.

// • IdentityStore.getCallerGroups(CredentialValidationResult)
//-This method should return the set of names found in the "groups" claim in the JWT if it exists, an empty set otherwise.

// • SessionContext.getCallerPrincipal()
//-The java.security.Principal returned from this method MUST be an instance of org.eclipse.microprofile.jwt.JsonWebToken.

// • SessionContext#isCallerInRole(String)
//-This method MUST return true for any name that is included in the MP-JWT "groups" claim, as well as for any role name that has been mapped to a group name in the MP-JWT "groups" claim.

// • HttpServletRequest.getUserPrincipal()
//-The java.security.Principal returned from this method MUST be an instance of org.eclipse.microprofile.jwt.JsonWebToken.

// • HttpServletRequest#isUserInRole(String)
//-This method MUST return true for any name that is included in the MP-JWT "groups" claim, as well as for any role name that has been mapped to a group name in the MP-JWT "groups" claim.

// • javax.security.jacc.PolicyContext.getContext("javax.security.auth.Subject.container")
//-The Subject returned by the PolicyContext.getContext(String key) with the standard "javax.security.auth.Subject.container" key,
// MUST return a Subject that has a java.security.Principal of type org.eclipse.microprofile.jwt.JsonWebToken amongst it’s set of Principal`s
// returned by `getPrincipals()`. Similarly, Subject#getPrincipals(JsonWebToken.class) must return a set with at least one value.
//-This means that following code snipet must not throw an AssertionError:
//	Subject subject = (Subject) PolicyContext.getContext("javax.security.auth.Subject.container");
//	Set<? extends Principal> principalSet = subject.getPrincipals(JsonWebToken.class);
//	assert principalSet.size() > 0;

// • Overriding @LoginConfig from web.xml login-config
//-If a deployment with a web.xml descriptor contains a login-config element, an MP-JWT implementation
// should view the web.xml metadata as an override to the deployment annotation.


public class Generally {}