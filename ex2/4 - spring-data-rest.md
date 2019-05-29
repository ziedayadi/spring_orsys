# Exercice 2 (partie 3 : utilisation de Spring data rest)


Objectifs : 
- exposition de _repositories_ en REST
- tests d'intégration avec MockMvc
*****

1. Ajouter une dépendance vers `spring-boot-starter-web`. L'application embarque désormais un serveur web, elle peut désormais être une application web autonome.

2. Remplacer le corps de la méthode `main` de la classe `ApplicationConfig` par l'instruction de lancement d'une application Spring boot:

	```java
	SpringApplication.run(ApplicationConfig.class, args);
	```
		
	Lancer l'application.

3. Ajouter une dépendance vers `spring-boot-devtools` pour bénéficier du rechargement à chaud :

	```xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-devtools</artifactId>
	</dependency>
	```
	
4. Ajouter une dépendance à `spring-boot-data-rest` pour exposer les _repositories_ en REST : 

	```xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-rest</artifactId>
	</dependency>
	```
	Annoter `BookRepository` le rendre accessible en REST.
	
	Relancer l'application puis accéder depuis le navigateur (ou Postman) à http://localhost:8080/books puis http://localhost:8080/books/1
	
5. Créer une projection pour l'entité `Book`, n'exposant que les propriétés suivantes : 

	* id
	* titre
	* nom complet de l'auteur.
	* nom de la categorie.
	
	Constater la différence en accédant à nouveau à http://localhost:8080/books puis http://localhost:8080/books/1
	
6. Ajouter dans `BookRepository` une méthode permettant de rechercher les livres par leur titre (opérateur : contient). Annoter cette méthode afin qu'elle soit accessible en REST par un GET sur `/books/search/byTitle`

8. Redéfinir les méthodes `deleteById` des _repositories_ afin qu'elles ne soient pas accessible en REST.

9. Lancer les tests `BookRepositoryRestTest` et `MemberRepositoryRestTest` après avoir ajouter l'annotation `@SpringBootTest` et décommenter le code (attention à l'organisation des imports).