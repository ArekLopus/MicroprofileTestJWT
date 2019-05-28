package an_info;

//		Marking a JAX-RS Application as Requiring MP-JWT Access Control
//-Since the MicroProfile does not specify a deployment format, and currently does not rely on servlet metadata descriptors,
// we have added an org.eclipse.microprofile.jwt.@LoginConfig that provides the same information as the web.xml login-config element.
//-It’s intended usage is to mark a JAX-RS Application as requiring MicroProfile JWT RBAC as shown in the following sample:
//	@LoginConfig(authMethod = "MP-JWT", realmName = "TCK-MP-JWT")
//	@ApplicationPath("/")
//	public class TCKApplication extends Application {}
//-The MicroProfile JWT implementation is responsible for either directly processing this annotation, or mapping it to an equivalent
// form of metadata for the underlying implementation container.

//		Requirements for Rejecting MP-JWT Tokens
//-The MP-JWT spec requires that an MP-JWT impl reject a bearer token as an invalid MP-JWT token if any of the conditions are not met:
// • The JWT must have a JOSE header that indicates the token was signed using the RS256 algorithm.
// • The JWT must have an iss claim representing the token issuer that maps to an MP-JWT impl container runtime configured value.
//   Any issuer other than those issuers that have been whitelisted by the container config must be rejected with an HTTP_UNUATHENTICATED(401) error.
// • The JWT signer must have a public key that maps to an MP-JWT impl container runtime configured value. Any public key other than those
//   that have been whitelisted by the container configuration must be rejected with an HTTP_UNUATHENTICATED(401) error.


//		JAX-RS Container API Integration
//-The behavior of the following JAX-RS security related methods is required for MP-JWT implementations.
// • SecurityContext.getUserPrincipal()
//   The java.security.Principal returned from these methods MUST be an instance of org.eclipse.microprofile.jwt.JsonWebToken.
// • SecurityContext#isUserInRole(String)
//   This method MUST return true for any name that is included in the MP-JWT "groups" claim, as well as for any role name
//   that has been mapped to a group name in the MP-JWT "groups" claim.


//		Using the Common Security Annotations for the Java Platform (JSR-250)
//-The expectations for use of the various security annotations described in sections 2.9 - 2.12 of JSR-250
// (@RolesAllowed, @PermitAll, @DenyAll), is that MP-JWT containers support the behavior as described in those sections.
//
//	Mapping the @RolesAllowed to the MP-JWT group claim
//-In terms of mapping between the MP-JWT claims and role names used in @RolesAllowed, the role names that have been mapped to group names
// in the MP-JWT "groups" claim, MUST result in an allowing authorization decision wherever the security constraint has been applied.


//		A JAX-RS Application Requiring MP-JWT Access Control
//-The requirements of how a JWT should be exposed via the various Java EE container APIs is discussed in this section. 
//-For the 1.0 release, the only mandatory container integration is with the JAX-RS container, and injection of the MP-JWT types.

//	CDI Injection Requirements
//-This section describes the requirements for MP-JWT impls with regard to the injection of MP-JWT tokens and associated claim values.
//
//	Injection of JsonWebToken
//-An MP-JWT impl must support the injection of the currently authenticated caller as a JsonWebToken with @RequestScoped scoping:
//		@Inject
//		private JsonWebToken callerPrincipal;
//
//	Injection of JsonWebToken claims via Raw Type, ClaimValue, javax.enterprise.inject.Instance and JSON-P Types
//-The spec requires support for injection of claims from the current JsonWebToken using the org.eclipse.microprofile.jwt.Claim qualifier:
//
//	@Qualifier
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
//	public @interface Claim {
//	    @Nonbinding    String value() default "";
//	    @Nonbinding    Claims standard() default Claims.UNKNOWN;
//	}
// with @Dependent scoping.
//-MP-JWT implementations are required to throw a DeploymentException when detecting the ambiguous use of a @Claim qualifier
// that includes inconsistent non-default values for both the value and standard elements as is the case shown here:
//		    @Inject
//		    @Claim(value="exp", standard=Claims.iat)
//			private Long timeClaim;
//
//-Injection of a non-proxyable raw type like Long must be in a @RequestScoped bean as the producer will have dependendent scope.
//-A may to specify the name of the claim is using a string or a Claims enum value.
//-The string form would allow for specifying non-standard claims while the Claims enum approach guards against typos.
//	@Inject (1)
//	@Claim(standard=Claims.iat)
//	private Long issuedAt;
//	@Inject (2)
//	@Claim(standard = Claims.raw_token)
//	private ClaimValue<String> rawTokenCV;
//	@Inject (3)
//	@Claim("jti")
//	private ClaimValue<Optional<String>> optJTI;
//	@Inject (4)
//	@Claim(standard=Claims.iat)
//	private ClaimValue<Long> issuedAtCV;
//	@Inject (5)
//	@Claim("custom-missing")
//	private ClaimValue<Optional<Long>> custom;
//	@Inject (6)
//	@Claim(standard = Claims.iat)
//	private Instance<Long> providerIAT;
//	@Inject (7)
//	@Claim("roles")
//	private JsonArray jsonRoles;
//1. Injection of a non-proxyable raw type like Long must be in a @RequestScoped bean as the producer will have dependendent scope.
//2. Injection of the raw MP-JWT token string.
//3. Injection of the jti token id as an Optional<String> wapper.
//4. Injection of the issued at time claim using an @Claim that references the claim name using the Claims.iat enum value.
//5. Injection of a custom claim that does exist will result in an Optional<Long> value for which isPresent() will return false.
//6. Another injection of a non-proxyable raw type like java.lang.Long, but the use of the javax.enterprise.inject.Instance interface allows for injection to occur in non-RequestScoped contexts.
//7. Injection of a JsonArray of role names via a custom "roles" claim.

//	Handling of Non-RequestScoped Injection of Claim Values
//-MP-JWT impls are required to validate the use of claim value injection into contexts that do not match @RequestScoped. When the target
// context has @ApplicationScoped or @SessionScoped scope, implementations are required to generate a javax.enterprise.inject.spi.DeploymentException.
// For any other context, implementations should issue a warning, that may be suppressed by an implementation specific configuration setting.
//
// Note: If one needs to inject a claim value into a scope with a lifetime greater than @RequestScoped, such as @ApplicationScoped
// or @SessionScoped, one can use the javax.enterprise.inject.Instance interface to do so.


//	Recommendations for Optional Container Integration
//-This section describes the expected behaviors for Java EE container APIs other than JAX-RS.
// • IdentityStore.getCallerGroups(CredentialValidationResult)
//   -This method should return the set of names found in the "groups" claim in the JWT if it exists, an empty set otherwise.
// • SessionContext.getCallerPrincipal()
//   -The java.security.Principal returned from this method MUST be an instance of org.eclipse.microprofile.jwt.JsonWebToken.
// • SessionContext#isCallerInRole(String)
//   -This method MUST return true for any name that is included in the MP-JWT "groups" claim, as well as for any role name that has been mapped to a group name in the MP-JWT "groups" claim.
// • HttpServletRequest.getUserPrincipal()
//   - The java.security.Principal returned from this method MUST be an instance of org.eclipse.microprofile.jwt.JsonWebToken.
// • HttpServletRequest#isUserInRole(String)
//   -This method MUST return true for any name that is included in the MP-JWT "groups" claim, as well as for any role name that has been mapped to a group name in the MP-JWT "groups" claim.
// • javax.security.jacc.PolicyContext.getContext("javax.security.auth.Subject.container")
//   -The Subject returned by the PolicyContext.getContext(String key) with the standard "javax.security.auth.Subject.container" key, MUST return a Subject that has a java.security.Principal of type org.eclipse.microprofile.jwt.JsonWebToken amongst it’s set of Principal`s returned by `getPrincipals()`. Similarly, Subject#getPrincipals(JsonWebToken.class) must return a set with at least one value.
//   -This means that following code snipet must not throw an AssertionError:
//		Subject subject = (Subject) PolicyContext.getContext("javax.security.auth.Subject.container");
//		Set<? extends Principal> principalSet = subject.getPrincipals(JsonWebToken.class);
//		assert principalSet.size() > 0;
// • Overriding @LoginConfig from web.xml login-config
//   -If a deployment with a web.xml descriptor contains a login-config element, an MP-JWT implementation should view the web.xml metadata as an override to the deployment annotation.


//Mapping MP-JWT Token to Other Container APIs
//-For non-Java EE containers that provide access to some form of java.security.Principal representation of an authenticated caller,
// the caller principal MUST be compatible with the org.eclipse.microprofile.jwt.JsonWebToken interface.


public class Info_Config_CDI_and_Server {}