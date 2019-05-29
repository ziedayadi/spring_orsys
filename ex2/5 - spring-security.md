# Exercice 2 (partie 4 : utilisation de Spring security)


Objectifs : 
- activation de Spring security.
- définition d'un référentiel utilisateur.
- sécurisation des accès à l'API REST
- utilisation de BCrypt.
*****

1. Ajouter une dépendance vers `spring-boot-starter-security`. Constater l'activation automatique de la sécurité (accès par le navigateur à http://localhost:8080)

2. Ajouter à la classe de configuration (`ApplicationConfig`) une classe statique `SecurityConfig` pour la configuration de la sécurité. Celle-ci doit : 
    - étendre ...
    - étre annotée par ...

3. Dans la classe de configuration créer en 2, redéfinir la méthode 
    ```java
    void configure(AuthenticationManagerBuilder auth)
    ```
    Le référentiel utilisateur est dans la base de données ex2. Début du code d'implémentation : 

    ```java
    DataSource ds = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/ex2", "postgres", null);
	String usersByUsernameQuery = "select username, password, true from Member where username=?";
	String authoritiesByUsernameQuery = "select username, authority from authorities where username=?";
	PasswordEncoder encoder = new BCryptPasswordEncoder();
	```
		
	Essayer ensuite d'accéder à la ressource `http://localhost:8080/books` avec le nom d'utilisateur _jdoe_ et le mot de passe _azerty_

4. Rédéfinir la méthode `void configure(HttpSecurity http)` :

	Configuration pour une API REST : 
	* n'activer que l'authentification basique
	* désactiver la protection CSRF.
	* retourner une erreur 401 si la requête n'est pas authentifiée alors qu'elle porte sur une ressource sécurisée
	* retourner une erreur 403 si la requête porte sur une ressource sécurisée mais que l'initiateur de la requête n'a pas les permissions requises.
	* retourner une réponse 204 suite à la déconnexion.	

	Règles :
	
	* restreindre la réservation d'un livre (`POST` sur `/reservations`) aux titulaires de la permission _borrow-books_
	* autoriser l'accès à tous pour `"/", "/**/*.html", "/**.js", "/favicon.ico"`
	* L'accès aux autres ressources suppose seulement d'être authentifié

5. Dès lors qu'il n'y a plus de formulaire de login ni d'invite de connexion basique, il faut que le frontend puisse soumettre ses identifiants vers une URI. Ajouter à la classe de configuration `ApplicationConfig` le controleur suivant (sans quoi un POST sur `/authentication` de la part du _frontend_ conduirait à une erreur 404) : 

	```java
	@RestController
	public static class AuthenticationController{
		
		@PostMapping("authentication")
		public Map<String, Object> onSucessfulAuthentication(Authentication auth) {
			return Map.of(
				"username", auth.getName(), 
				"authorities", auth.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList())
			);
		}
	}
	```
	Comprendre que cette méthode ne sera appelé que si l'authentification est réussie : c'est le filtre Spring security qui vérifie que l'identifiant et le mot de passe correspondent à un utilisateur valide. 
	
6. Ajouter à la classe `com.acme.ex2.business.MemberRules` une méthode permettant d'encoder en BCrypt le mot de passe lors de la création ou la modification d'un `Member` (voir la méthode `encode` de la classe `BCryptEncoder`).