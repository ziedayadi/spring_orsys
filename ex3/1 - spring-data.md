# Exercice 2 (partie 1 : Spring data)


Objectifs : 
- utilisation des repositories Spring data.
- compréhension du filtre `OpenEntityManagerInViewFilter`
*****


1. Recopier dans le package `com.acme.ex3.repository` les _repositories_ de l'exercice 2. Supprimer toutes les références à Spring data rest.

2. Compléter le code de la classe `com.acme.ex3.controller.BookController` (suivre les TODO).

3. Lancer l'application (méthode `main` de la classe `com.acme.ex3.ApplicationConfig`), accéder  à http://localhost:8080/books :

	* la recherche par le titre Walden doit retourner un résultat (les films dont le titre contient Walden).
	* la recherche par le titre al doit retourner 4 résultats (les films dont le titre contient al).
	
4. Dans la fiche détail du livre "Walden", constater le bon affichage des commentaires. Pourtant il s'agit d'une relation _lazy_. Constater que l'`EntityManager` est ouvert tout le long du rendu de la vue. Rapprocher ce comportement du message de log _spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning_
