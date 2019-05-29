# Exercice 2 (partie 1 : couplage Spring - JPA, Spring Data)

**Objectifs** : 

* couplage Spring - JPA (Java persistence API)
* utilisation de Spring Data

*****
## configuration

Sur la classe `com.acme.ex2.ApplicationConfig`

1. Référencer le fichier _application.properties_

2. Compléter les méthodes afin que le  contexte applicatif contienne : 
	* une `DataSource` (compléter la méthode `ds`)
	* un `EntityManagerFactory` (compléter la méthode `emf`)
	* un `PlatformTransactionManager` (implémentation à utiliser : `JpaTransactionManager`)

	Les deux premières méthodes ont bien sûr besoin d'accéder aux propriétés du fichier application.properties

## surcouche spring data

1. Ajouter la dépendance à spring-data-jpa : 

	```xml
	<dependency>
		<groupId>org.springframework.data</groupId>
		<artifactId>spring-data-jpa</artifactId>
		<version>2.1.8.RELEASE</version>
	</dependency>
	```
	
2. Ajouter 4 _repositories_ dans le package `com.acme.ex2.repository` : 

	* `AuthorRepository`
	* `ReservationRepository`
	* `BookRepository`
	* `MemberRepository`
		
	Pour chacun le type de l'`ID` est `Integer`

	Définir dans `MemberRepository` une méthode permettant de rechercher un `Member` par son nom d'utilisateur (rappel : un `Member` possède un `acccount` qui lui même possède une propriété `username`)
	
3. Activer sur la classe de configuration `ApplicationConfig` la prise en charge de _repositories_ JPA

4. Décommenter les tests unitaires de la classe `com.acme.ex2.repository.BookRepositoryTest` (puis organiser les imports) et coupler Spring et Junit.

5. Lancer les tests unitaires (un par un pour bien observer les logs).
