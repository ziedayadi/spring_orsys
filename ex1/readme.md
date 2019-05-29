# Exercice 1 :

Objectif : 
- compréhension du pom.xml
- détection des dépendances
- préparation des injections
- déclaration des beans auprès de Spring

*****

1. Ecrire deux implémentations de l'interface `MovieRepository` : 
	* `com.acme.ex1.dao.impl.FoxMovieRepositoryImpl`
	* `com.acme.ex1.dao.impl.WarnerMovieRepositoryImpl`

	la méthode `findByTitle` doit retourner un `Stream<Movie>` filtrée d'après le `title` reçu en argument. A propos des streams : https://stackoverflow.com/a/24679745

2. Ecrire une implémentation `MovieServiceImpl` de `MovieService`, la méthode `find` utilise une des implémentations de `MovieRepository` pour renvoyer un `Stream` de `Movie`
	* Comprendre la dépendance entre les implémentations de `MovieService` et les implémentations de `MovieRepository`
	* Rendre possible l'injection d'une `MovieRepository` dans la classe `MovieServiceImpl`

5. Ecrire une deuxième implémentation `SuperMovieServiceImpl` de l'interface `MovieService`. Cette deuxième implémentation doit être capable de recevoir plusieurs `MovieRepository` et non plus une seul

6. Utiliser la classe `ApplicationConfig` pour déclarer à Spring la gestion de nos composants : Spring va désormais jouer le rôle de *factory*. Nous voulons disposer de 4 _beans_ : 

	* 1 FoxMovieRepositoryImpl
	* 1 WarnerMovieRepositoryImpl
	* 1 SuperMovieServiceImpl
	* 2 MovieServiceImpl :
		* 1 qui se voit injecté une référence vers le _bean_ `FoxMovieRepositoryImpl`
		* 1 qui se voit injecté une référence vers le _bean_ `WarnerMovieRepositoryImpl`
		
9. Compléter et lancer les tests unitaires

	* `SuperMovieServiceImplTest`
	* `MovieServiceImplTest`.
