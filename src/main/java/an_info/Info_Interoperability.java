package an_info;

//		Recommendations for Interoperability

//-The maximum utility of the MicroProfile JWT(MP-JWT) as a token format depends on the agreement between both
// identity providers and service providers. 
//-This means identity providers - responsible for issuing tokens - should be able to issue tokens using the MP-JWT format
// in a way that service providers can understand in order to introspect the token and gather information about a subject.
//-To that end, the requirements for the MicroProfile JWT are:
// 1. Be usable as an authentication token.
// 2. Be usable as an authorization token that contains Java EE application level roles indirectly granted via a groups claim.
// 3. Can be mapped to IdentityStore in JSR375.
// 4. Can support additional standard claims described in IANA JWT Assignments as well as non-standard claims.

//-To meet those requirements, we introduce 2 new claims to the MP-JWT:
// • "upn": A human readable claim that uniquely identifies the subject or user principal of the token, across the MicroProfile
//   services the token will be accessed with.
// • "groups": The token subject’s group memberships that be mapped to JEE style app level roles in the MicroProfile service container.


//	Minimum MP-JWT Required Claims
//-The required minimum set of MP-JWT claims is then:
// • typ - This JOSE header parameter identifies the token as an RFC7519 and must be "JWT" RFC7519, Section 5.1
// • alg - This JOSE header parameter identifies the cryptographic algorithm used to secure the JWT. MP-JWT requires the use of the RSASSA-PKCS1-v1_5 SHA-256 algorithm and must be specified as "RS256", RFC7515, Section 4.1.1
// • kid - This JOSE header parameter is a hint indicating which key was used to secure the JWT. RFC7515, Section-4.1.4
// • iss - The MP-JWT issuer. RFC7519, Section 4.1.1
// • sub - Identifies the principal that is the subject of the JWT. See the "upn" claim for how this relates to the container java.security.Principal. RFC7519, Section 4.1.2
// • exp - Identifies the expiration time on or after which the JWT MUST NOT be accepted for processing. Implementers MAY provide for some small leeway, usually no more than a few minutes, to account for clock skew. Its value MUST be a number containing a NumericDate value. RFC7519, Section 4.1.4
// • iat - Identifies the time at which the JWT was issued. This claim can be used to determine the age of the JWT. Its value MUST be a number containing a NumericDate value. RFC7519, Section 4.1.6
// • jti - Provides a unique identifier for the JWT. The identifier value MUST be assigned in a manner that ensures that there is a negligible probability that the same value will be accidentally assigned to a different data object; if the application uses multiple issuers, collisions MUST be prevented among values produced by different issuers as well. The "jti" claim can be used to prevent the JWT from being replayed. The "jti" value is a case-sensitive string. RFC7519, Section 4.1.7
// • upn - This MP-JWT custom claim is the user principal name in the java.security.Principal interface, and is the caller principal name in javax.security.enterprise.identitystore.IdentityStore. If this claim is missing, fallback to the "preferred_username", OIDC Section 5.1 should be attempted, and if that claim is missing, fallback to the "sub" claim should be used.
// • groups - This MP-JWT custom claim is the list of group names that have been assigned to the principal of the MP-JWT. This typically will required a mapping at the application container level to application deployment roles, but a one-to-one between group names and application role names is required to be performed in addition to any other mapping.
//-An example minimal MP-JWT in JSON would be:
//	{											//header
//	     "typ": "JWT",
//	     "alg": "RS256",
//	     "kid": "abc-1234567890"
//	}
//	{											//payload
//       "iss": "https://server.example.com",
//       "jti": "a-123",
//       "exp": 1311281970,
//       "iat": 1311280970,
//       "sub": "24400320",
//       "upn": "jdoe@server.example.com",
//       "groups": ["red-group", "green-group", "admin-group", "admin"],
//	}


//	JsonWebToken - extends Principal
//-This specification defines a JsonWebToken java.security.Principal interface extension that makes this set of required claims
// available via get style accessors. The JsonWebToken interface definition is:
//	public interface JsonWebToken extends Principal {
//	    String getName();
//	    default String getRawToken() {        return getClaim(Claims.raw_token.name());    }
//	    default String getIssuer() {        return getClaim(Claims.iss.name());    }
//	    default Set<String> getAudience() {        return getClaim(Claims.aud.name());    }
//	    default String getSubject() {        return getClaim(Claims.sub.name());    }
//	    default String getTokenID() {        return getClaim(Claims.jti.name());    }
//	    default long getExpirationTime() {        return getClaim(Claims.exp.name());    }
//	    default long getIssuedAtTime() {        return getClaim(Claims.iat.name());    }
//	    default Set<String> getGroups() {        return getClaim(Claims.groups.name());    }
//	    Set<String> getClaimNames();
//	    default boolean containsClaim(String claimName) {        return claim(claimName).isPresent();    }
//	    <T> T getClaim(String claimName);
//	    default <T> Optional<T> claim(String claimName) {        return Optional.ofNullable(getClaim(claimName));    }
//	}


//	The Claims Enumeration Utility Class, and the Set of Claim Value Types
//-The org.eclipse.microprofile.jwt.Claims utility class encapsulate an enumeration of all the standard JWT related claims
// along with a description and the required Java type for the claim as returned from the JsonWebToken#getClaim(String) method.
//	public enum Claims {
//    // The base set of required claims that MUST have non-null values in the JsonWebToken
//    iss("Issuer", String.class),
//    sub("Subject", String.class),
//    exp("Expiration Time", Long.class),
//    iat("Issued At Time", Long.class),
//    jti("JWT ID", String.class),
//    upn("MP-JWT specific unique principal name", String.class),
//    groups("MP-JWT specific groups permission grant", Set.class),
//    raw_token("MP-JWT specific original bearer token", String.class),
//
//    // The IANA registered, but MP-JWT optional claims
//    aud("Audience", Set.class),
//    nbf("Not Before", Long.class),
//    auth_time("Time when the authentication occurred", Long.class),
//    updated_at("Time the information was last updated", Long.class),
//    azp("Authorized party - the party to which the ID Token was issued", String.class),
//    nonce("Value used to associate a Client session with an ID Token", String.class),
//    at_hash("Access Token hash value", Long.class),
//    c_hash("Code hash value", Long.class),
//
//    full_name("Full name", String.class),
//    family_name("Surname(s) or last name(s)", String.class),
//    middle_name("Middle name(s)", String.class),
//    nickname("Casual name", String.class),
//    given_name("Given name(s) or first name(s)", String.class),
//    preferred_username("Shorthand name by which the End-User wishes to be referred to", String.class),
//    email("Preferred e-mail address", String.class),
//    email_verified("True if the e-mail address has been verified; otherwise false", Boolean.class),
//
//    gender("Gender", String.class),
//    birthdate("Birthday", String.class),
//    zoneinfo("Time zone", String.class),
//    locale("Locale", String.class),
//    phone_number("Preferred telephone number", String.class),
//    phone_number_verified("True if the phone number has been verified; otherwise false", Boolean.class),
//    address("Preferred postal address", JsonObject.class),
//    acr("Authentication Context Class Reference", String.class),
//    amr("Authentication Methods References", String.class),
//    sub_jwk("Public key used to check the signature of an ID Token", JsonObject.class),
//    cnf("Confirmation", String.class),
//    sip_from_tag("SIP From tag header field parameter value", String.class),
//    sip_date("SIP Date header field value", String.class),
//    sip_callid("SIP Call-Id header field value", String.class),
//    sip_cseq_num("SIP CSeq numeric header field parameter value", String.class),
//    sip_via_branch("SIP Via branch header field parameter value", String.class),
//    orig("Originating Identity String", String.class),
//    dest("Destination Identity String", String.class),
//    mky("Media Key Fingerprint String", String.class),
//
//    jwk("JSON Web Key Representing Public Key", JsonObject.class),
//    jwe("Encrypted JSON Web Key", String.class),
//    kid("Key identifier", String.class),
//    jku("JWK Set URL", String.class),
//
//    UNKNOWN("A catch all for any unknown claim", Object.class)
//    ;
//	  ...
//
//    public String getDescription() {        return description;    }
//    public Class<?> getType() {        return type;    }
//	}
//
//-Custom claims not handled by the Claims enum are required to be valid JSON-P javax.json.JsonValue subtypes.
//-The current complete set of valid claim types is therefore, (excluding the invalid Claims.UNKNOWN Void type):
// • java.lang.String
// • java.lang.Long
// • java.lang.Boolean
// • java.util.Set<java.lang.String>
// • javax.json.JsonValue.TRUE/FALSE
// • javax.json.JsonString
// • javax.json.JsonNumber
// • javax.json.JsonArray
// • javax.json.JsonObject


public class Info_Interoperability {}