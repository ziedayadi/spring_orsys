# Exercice 2 (partie 2 : utilisation de Spring boot)


Objectifs : 
- compréhension des dépendances d'un projet Spring boot.
- migration d'un projet vers Spring boot.
- tests unitaires avec Spring boot.
*****

## La gestion des dépendances

1. Ajouter le _bill of materials_ Spring boot (copier/coller depuis le support de cours)

2. Enlever les versions des dépendances, constater qu'elles sont prévues par le BOM `spring-boot-dependencies`
	
	Relancer les tests.

3. Ajouter les dépendances `spring-boot-starter-*` permettant :

	* d'accèder à une base de données avec JPA
	* de coupler Spring à junit ( _starter_  : `spring-boot-starter-test`). Attention à la dépendance transitive vers junit 4...
	
	Ces _starters_ se substituent à un grand nombre de dépendances et simplifier ainsi le pom.xml
	
	Relancer les tests.

## La configuration automatique

4. Reprendre les noms des propriétés du fichier `application.properties` afin qu'ils correspondent à ceux attendus par spring boot. Il suffit de les préfixer par `spring`.

5. Activer la configuration automatique sur la classe `ApplicationConfig` puis
	
	* supprimer les méthodes productrices correspondant à des _beans_ que Spring créera automatiquement.
	* Considérer la possibilité d'utiliser l'annotation `@SpringBootApplication`
	
	Relancer les tests.

## Les tests

6. Dans `BookRepositoryTest` : 
	* remplacer dans les tests `@ExtendWith(SpringExtension.class) @ContextConfiguration(classes=ApplicationConfig.class)` par `@SpringBootTest`
	* référencer le fichier `application-for-tests.properties` via l'annotation `@TestPropertySource`  afin de déléguer à Spring le soin de créer une `EntityManagerFactory` qui utilise une base Derby.

	Relancer les tests.