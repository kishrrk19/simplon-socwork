package co.simplon.socwork.config;

import javax.crypto.spec.SecretKeySpec;

import java.util.Arrays;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.auth0.jwt.algorithms.Algorithm;

@Configuration
public class WebConfig {
	
	@Value("${co.simplon.socwork.tousBcrypt}")
	private int tours;
	
	@Value("${co.simplon.socwork.secretJWT}")
	private String secret;
	
	@Value("${co.simplon.socwork.hasExpiration}")
	private boolean hasExpiration;
	
	@Value("${co.simplon.socwork.expirationMinutes}")
	private int expirationMinutes;
	
	@Value("${co.simplon.socwork.issuer}")
	private String issuer;

	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {

			@Value("${co.simplon.socwork.cors}")
			private String origins;
			
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("POST", "GET", "PATCH", "PUT", "DELETE").allowedOrigins(origins);
			}
		};
	}
	//en decembre khahoot sur Bcrypt et JWT et algorithme
	//cle prive= pour signer token
	//cle publique = pour verifier signature
	
	//ces 2 bean = serveur d'authorisation
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(tours);
	}
	
    @Bean
    JwtProvider jwtProvider() {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    return new JwtProvider(algorithm, hasExpiration,expirationMinutes, issuer);
    }
    
    //ici partie de serveur de ressource
    //moyens de verifier la signature de token
    @Bean
    JwtDecoder jwtDecoder() { // Tell Spring how to verify JWT signature
    SecretKey secretKey = new SecretKeySpec(secret.getBytes(),
        "HMACSHA256");
 
    // アクセストークンのカスタム検証
    OAuth2TokenValidator<Jwt> validators = JwtValidators.createDefaultWithIssuer(issuer);
    
    NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey)
        .macAlgorithm(MacAlgorithm.HS256)
        .build();
    decoder.setJwtValidator(validators);
    //par default verifies expiration et signature
    // Issuer: 
    //Add in properties, token
    //Here : verify issuer / Why verify the issuer? jwtissuervalidator
    
    
    return decoder;
    }
    
    @Bean // Change default behaviour
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	//le fait de enlever (exclude = SecurityAutoConfiguration.class) dans le class main, il pose des problemes de cors
    	//car il active la securite par Spring et il y a un filtre grace a SecurityFilterChain sera applique. 
    	//Ce filtre, par default, pour moindre privillege, il n'accepte pas les requettes et si on ne configure pas pour dire qu'il y a deja un cors custom configue,il n'en prend pas compte
    	//http C'est un builder pour builder un securityFilterChain
    	
    	//preflight c'est un requette HTTP Option
    // Default Spring behaviour: PoLP (no authorization at all)
    // Relies on JWT
    // authorize some requests or not
    // ???
    	// Multiple matchers to map verbs + paths + authorizations
        // "authorizations": anonymous, permit, deny and more...
        // By configuration (filterChain), also by annotations...

    	//withDefaults prends notre cors customized
    	//Il y a bcp de petits comportements par default de Spring Security
    	//par exemple par default il n'active pas Cors, et active CSRF que je n'ai pas besoin, il verrouille tous les endpoints, il ne sait pas qu'on veut utiliser JWT
    	// par default il n'a pas de serveur d'authentification
    	// 3 sujets de veilles de securites a connaitre : injection SQL(LF), XXS(Fab et Deb) et CSRF
    	//nous c'est stateless c'est a dire que on ne conserve aucune information sur la session sur l'identification de l'utilisateur car on aura toutes les informations dans le token. On aura aucun besoin d'aller appeler la base de donnees ou d'aller chercher aupres d'un autre serveur
    	// si on ne deactive pas csrf, la security attends une information de la part de front concernant csrf et s'il n'y a pas, il rejette la requette, c'est comme token
    	// si on configure au minimum cors et deactive csrf, il arrive a acceder a endpoint et faire marcher la requette mqis c'est a dire que on accepte tous les endpoints, c'est a dire pas du tout securise
    	
		return http.cors(Customizer.withDefaults()).csrf((csrf) -> csrf.disable())
				.authorizeHttpRequests((req) -> req
						.requestMatchers(HttpMethod.GET, "/accounts/with-role")
						.hasRole("MANAGER"))
				.authorizeHttpRequests((req) -> req
						.requestMatchers(HttpMethod.POST, "/accounts", "/accounts/login").anonymous()) //.anonymousとすることですでにログインしているひとではなく(qqun qui a un token valable)、アプリケーションにとってanonymeのユーザーのみこのリクエストを許可する。.anonymousにしたことで、有効なtokenを一緒に送ったアカウント作成若しくはログインリクエストは403forbiddenに
//				.authorizeHttpRequests((req) -> req
//						.requestMatchers(HttpMethod.GET, "/accounts/get-account").anonymous())
				// Always last rule:
				.authorizeHttpRequests((reqs) -> reqs.anyRequest().authenticated())//上記以外の場合認証(authenticate)が必要ということ
				.oauth2ResourceServer((srv) -> srv.jwt(Customizer.withDefaults())) // ce serveur verifie la signature de
																					// token(si il n'est pas modifie) et
																					// l'expiration. ici decoder est apple
				// pour Spring, il est tellement evident qu'il doit veirifier la siganture et
				// l'expiration donc dans le jwtDecoder ne dit pas de verifier mais on dit dans
				// jwtProvider comment il peut verifier surtout avec quel secret
				// ici meme si on n'a pas mis le nom de methode jwtProvider, grace a injection
				// de dependence @Bean, jwtDecoder sera apple. Spring va trouver tout seul Bean
				// adapte. et c'est base sur le nom de methode
				// par default le nom de Bean c'est nom de methode
				.build();
    	
    }
    //derniere version de spring security 6 version a autre ca change
    //期限切れのtokenをcreer-compteで送信した場合、tokenを受け取ったら必ず検証されてしまい、401が返る。
    //有効なtokenをcreer-compteで送信した場合、匿名ルールに違反するため403が返る。
    //つまり、期限切れのtokenを送った場合はまずtokenが検証され、その結果tokenが有効でないので401が返り（つまり、匿名性は検証されていない）、有効なtokenを送った場合もまずtokenが検証され、tokenが有効だと分かり、そのあと、anonymous()の匿名性が検証され、匿名でないので403が返る
    
}
