package an_info;

//		JWT RBAC (Role Based Access Control) for MicroProfile

//-This specification outlines a proposal for using OpenID Connect(OIDC) based JSON Web Tokens(JWT) for
// role based access control (RBAC) of microservice endpoints.

//-One of the main strategies to propagate the security state from clients to services or
// even from services to services involves the use of security tokens. 
//-In fact, the main security protocols in use today are based on security tokens such as OAuth2, OpenID Connect, SAML, WS-Trust,
// WS-Federation and others. While some of these standards are more related with identity federation,
// they share a common concept regarding security tokens and token based authentication.

//-For microservices, security tokens offer a very lightweight and interoperable way to propagate identities across diffrnt services, where:
// • Services don’t need to store any state about clients or users
// • Services can verify the token validity if token follows a well known format. Otherwise, services may invoke a separated service.
// • Services can identify the caller by introspecting the token. If the token follows a well known format, services are capable to introspect the token by themselves, locally. Otherwise, services may invoke a separated service.
// • Services can enforce authorization policies based on any information within a security token.
// • Support for both delegation and impersonation of identities.


//	Token Based Authentication
//-Token Based Authentication mechanisms allow systems to authenticate, authorize and verify identities based on a security token. -Usually, the following entities are involved:
// • Issuer - Responsible for issuing security tokens as a result of successfully asserting an identity (authentication). Issuers are usually related with Identity Providers.
// • Client - Represented by an application to which the token was issued for. Clients are usually related with Service Providers. A client may also act as an intermediary between a subject and a target service (delegation).
// • Subject - The entity to which the information in a token refers to.
// • Resource Server - Represented by an application that is going to actually consume the token in order to check if a token gives access or not to a protected resource.

//-Independent of the token format or protocol in use, from a service perspective, token based authentication is based on the steps:
// • Extract security token from the request - For REST services, it is usually achieved by obtaining a token from the Authorization header.
// • Perform validation checks against the token - This step usually depends on the token format and security protocol in use. The objective
//   is make sure the token is valid and can be consumed by the application. It may involve signature, encryption and expiration checks.
// • Introspect the token and extract information about the subject - This step usually depends on the token format and security protocol
//   in use. The objective is to obtain all the necessary information about the subject from the token.
// • Create a security context for the subject - Based on the information extracted from the token, the application creates
//   a security context for the subject in order to use the information wherever necessary when serving protected resources.


//	Using JWT Bearer Tokens to Protect Services
// 1. A client sends a HTTP request to Service A including the JWT as a bearer token:
//	    GET /resource/1 HTTP/1.1
//	    Host: example.com
//	    Authorization: Bearer mF_9.B5f-4.1JqM
// 2. On the server, a token-based authentication mechanism in front of Service A perform all steps described on the Token Based Authentication
//    section. As part of the security context creation, the server establishes role and group mappings for the subject based on 
//    the JWT claims. The role to group mapping is fully configurable by the server along the lines of the Java EE RBAC security model.
//
//-JWT tokens follow a well defined and known standard that is becoming the most common token format to protect services.
//-It not only provides a token format but additional security aspects like signature and encryption based on another set of standards
// like JSON Web Signature (JWS), JSON Web Encryption (JWE) and others.
//
//-There are few reasons why JWT is becoming so widely adopted:
// • Token validation doesn’t require an additional trip and can be validated locally by each service
// • Given its JSON nature, it is solely based on claims or attributes to carry authentication and authorization information about a subject.
// • Makes easier to support different types of access control mechanisms such as ABAC, RBAC, Context-Based Access Control, etc.
// • Message-level security using signature and encryption as defined by both JWS and JWE standards
// • Given its JSON nature, processing JWT tokens becomes trivial and lightweight. 
//   Especially if considering Java EE standards such as JSON-P or different third-party libraries out there such as Nimbus, Jackson, etc.
// • Parties can easily agree on a specific set of claims in order to exchange both authentication and authorization information.
//   Defining this along with the Java API and mapping to JAX-RS APIs are the primary tasks of the MP-JWT specification.
// • Widely adopted by different Single Sign-On solutions and well known standards such as OpenID Connect given its small overhead 
//   and ability to be used across different different security domains (federation)





public class Info_Basics {}