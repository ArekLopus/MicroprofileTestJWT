package an_info;

//		Configuration
//-In general we need Public/PrivateKey and configure 2 properties:
//	mp.jwt.verify.issuer
//	mp.jwt.verify.publickey	OR	mp.jwt.verify.publickey.location
//-Parameters are passed using the MicroProfile Config specification or via command-line via -D properties
// META-INF/microprofile-config.properties, ConfigSources, System.getProperties(), System.getenv()
// java -jar movieservice.jar -Dmp.jwt.verify.publickey.location=orange.pem


//-Verification of JSon Web Tokens (JWT) passed to the Microservice in HTTP requests at runtime is done with the RSA Public Key
// corresponding to the RSA Private Key held by the JWT Issuer.

//-At the time of JWT creation, the Issuer will sign the JWT with its Private Key before passing it to the user. 
//-Upon receiving the JWT in future HTTP requests, Microservices can then use the matching Public Key
// to verify the JWT and trust the user info (claims) it contains.

//-MicroProfile JWT leverages the MicroProfile Configuration spec to provide a consistent means of passing all supported configuration options.
//-Prior to MicroProfile JWT 1.1 all configuration options for the Public Key and verification were vendor-specific. 
//-Any vendor-specific methods of configuration are still valid and shall be considered to override any standard config mechanisms.

//	Obtaining the Public Key
//-In practice, the Public Key is often obtained manually from the JWT Issuer and stored in or passed to the binary of the Microservice.
//-If your public Keys do not rotate frequently, then storing them in the binary image or on disk is a realistic option for many env.
//-Alternatively, Public Keys may be obtained by the Microservice at runtime, directly from the JWT Issuer via HTTP request.

//	Supported Public Key Formats
//-Support for RSA Public Keys of 1024 or 2048 bits in length is required.
//-Other key sizes are allowed, but should be considered vendor-specific.
//-Other asymmetric signature algorithms are allowed, but should be considered vendor-specific.
//-Symmetrically signed JWTs such as HMAC-SHA256 (hs256) are explicitly not supported.

//-Public Keys may be formatted in any of the following formats, specified in order of precedence:
// • Public Key Cryptography Standards #8 (PKCS#8) PEM
// • JSON Web Key (JWK)
// • JSON Web Key Set (JWKS)
// • Web Key (JWK) Base64 URL encoded
// • JSON Web Key Set (JWKS) Base64 URL encoded
//-Attempts to parse the Public Key text will proceed in the order specified above until a valid Public Key can be derived.
//-Support for other Public Key formats such as PKCS#1, SSH2, or OpenSSH Public Key format is considered optional.

//	PCKS#8
//-Public Key Cryptography Standards #8 (PKCS#8) PEM format is a plain text format and is the default format for OpenSSL,
// many public/private key tools and is natively supported in Java.
//-The format consists of a Base64 URL encoded value wrapped in a standard -----BEGIN PUBLIC KEY----- header and footer.
//-Base64 URL encoded data can be decoded and the resulting byte array passed directly to java.security.spec.PKCS8EncodedKeySpec.
//-Support for the legacy PKCS#1 format is not required and should be considered vendor-specific.
//-PKCS#1 formatted keys can be identified by the use of the -----BEGIN RSA PUBLIC KEY----- header and footer.


//		Configuration Parameters
//-Parameters are passed using the MicroProfile Config specification.
//	( META-INF/microprofile-config.properties, ConfigSources, System.getProperties(), System.getenv() )
// This spec allows at minimum configuration options to be specified in the microservice binary itself or via command-line via -D properties. 
//	java -jar movieservice.jar -Dmp.jwt.verify.publickey.location=orange.pem


//	mp.jwt.verify.publickey
//-This property allows the Public Key text itself to be supplied as a string.
//		mp.jwt.verify.publickey=eyJrdHkiOiJSU0EiLCJuI...    or   java -jar movieservice.jar -Dmp.jwt.verify.publickey=eyJrdHkiOiJSU0EiLCJuI...
//-When supplied, it will override other standard means to supply the Public Key such as mp.jwt.verify.publickey.location.
//-Vendor-specific options for supplying the key will always take precedence.
//-If neither the mp.jwt.verify.publickey nor mp.jwt.verify.publickey.location are supplied configuration are supplied,
// the MP-JWT signer configuration will default to a vendor specific behavior as was the case for MP-JWT 1.0. 
//-MicroProfile JWT implementations are required to throw a DeploymentException if both
// mp.jwt.verify.publickey and mp.jwt.verify.publickey.location are supplied.


//	mp.jwt.verify.publickey.location
//-It allows for an external or internal location of Public Key to be specified. The value may be a relative path or a URL.
//-MicroProfile JWT implementations are required to check the path at startup or deploy time.
//-Reloading the Public Key from the location at runtime as well as the frequency of any such reloading is beyond the scope
// of this specification and any such feature should be considered vendor-specific.
//
//Relative Path
//-Relative or non-URL paths supplied as the location are resolved in the following order:
// 1. new File(location)
// 2. Thread.currentThread().getContextClassLoader().getResource(location)
//-The following example shows the file orange.pem supplied as either a file in the Microservice’s binary or locally on disk.
//	java -jar movieservice.jar -Dmp.jwt.verify.publickey.location=orange.pem
//-Any non-URL is treated identically and may be a path inside or outside the archive.
//	java -jar movieservice.jar -Dmp.jwt.verify.publickey.location=/META-INF/orange.pem
//
//file: URL Scheme
//-File URL paths supplied as the location allow for explicit externalization of the file via full url.
//	java -jar movieservice.jar -Dmp.jwt.verify.publickey.location=file:///opt/keys/orange.pem
//
//http: URL Scheme
//-HTTP and HTTPS URL paths allow for the Public Key to be fetched from a remote host,
// which may be the JWT Issuer or some other trusted internet or intranet location.
//-The location supplied must respond to an HTTP GET.
//	java -jar movieservice.jar -Dmp.jwt.verify.publickey.location=https://location.dev/widget/issuer
//-Other forms of HTTP requests and responses may be supported, but should be considered vendor-specific.
//
//Other URL Schemes
//-All other locations containing a colon will be considered as URLs and be resolved using the following method:
//	new URL(location).openStream()
//-Thus additional vendor-specific or user-defined options can easily be added.
//-Example custom "smb:" location
//	java -jar movieservice.jar -Dmp.jwt.verify.publickey.location=smb://Host/orange.pem -Djava.protocol.handler.pkgs=org.foo
//-Example stub for custom "smb:" URL Handler
//	public class Handler extends URLStreamHandler {
//	    @Override
//	    protected URLConnection openConnection(URL u) throws IOException {
//	        return // your URLConnection implementation
//	    }
//	}
//-Parsing of the InputStream occurs as defined in Supported Public Key Formats and must return Public Key text in the supported format.


//	mp.jwt.verify.issuer
//-The mp.jwt.verify.issuer config property allows for the expected value of the 'iss' claim to be specified.
//- A MP JWT impl must verify the iss claim of incoming JWTs is present and matches the configured value of mp.jwt.verify.issuer.


public class Info_Config_MP_JWT {}
