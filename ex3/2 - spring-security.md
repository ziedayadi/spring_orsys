# Exercice 3 (partie 2 : Spring security)


Objectifs : 
- activation de Spring security.
- définition d'un référentiel utilisateur.
- sécurisation des routes d'une IHM.
*****


1. Ajouter une dépendance vers `spring-boot-starter-security`. Constater l'activation automatique de la sécurité (accès par le navigateur à http://localhost:8080)

2. Ajouter à la classe de configuration (`ApplicationConfig`) une classe statique `SecurityConfig` pour la configuration de la sécurité. Celle-ci doit : 
    - étendre ...
    - étre annotée par ...

3. Dans la classe de configuration créer en 2, redéfinir la méthode 
    ```java
    void configure(AuthenticationManagerBuilder auth)
    ```
    Le référentiel utilisateur est dans la base de données ex3. Début du code d'implémentation : 

    ```java
    DataSource ds = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/ex2", "postgres", null);
	String usersByUsernameQuery = "select username, password, true from Member where username=?";
	String authoritiesByUsernameQuery = "select username, authority from authorities where username=?";
	PasswordEncoder encoder = new BCryptPasswordEncoder();
	```
		
	Essayer ensuite d'accéder à la ressource `http://localhost:8080/books` avec le nom d'utilisateur _jdoe_ et le mot de passe _azerty_

4. Rédéfinir la méthode `void configure(HttpSecurity http)` :

	* Configuration pour une IHM : n'activer que l'authentification par formulaire de login
	* Règles : l'accès aux autres routes est autorisé pour tous les utilisateurs.

5. Dans le contrôleur `com.acme.ex3.controller.BookController` : 

	* obtenir le contexte d'authentification de l'utilisateur, utiliser le `username` pour valoriser la propriété `command.username`, le champ `username` peut être supprimé du formulaire.
	* interdire l'invocation de la méthode `borrow` si l'utilisateur n'a pas la permission `borrow-books`.
	
	
6. Ajouter une dépendance vers `spring-security-taglibs` et modifier la vue `src/main/resources/META-INF/resources/books/detail.jsp` :
	
	* si l'utilisateur à la permission _borrow-books_ : afficher le formulaire de réservation
	* sinon : afficher un lien vers le formulaire de login (`/login`).
	
