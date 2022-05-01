# Histoires

Informations récapitulatives concernant les différentes histoires.

#### Quelques précisions

Un point correspond à une heure de travail par binôme (approximatif). Par itération, il faut accomplir 180 points.

----------------------

## Pondération

| Priorité | N°               | Description | Difficulté/3 | Risque/3 | Heures | Points |
|----------|------------------|-------------|--------------|----------|--------|--------|
| 1        | [1](#Histoire-1) | Histoire 1  | 3            | 2        | 80     | 80     |
|          | 2                | Histoire 2  | 2            | 3        | 40     | 40     |
|          | [3](#Histoire-3) | Histoire 3  | 2            | 1        | 100    | 100    |
| 2        | 4                | Histoire 4  | 2            | 2        | 50     | 50     |
|          | 5                | Histoire 5  | 3            | 1        | 100    | 100    |
|          | 6                | Histoire 6  | 3            | 1        | 120    | 120    |
|          | 10               | Histoire 10 | 1            | 1        | 120    | 120    |
|          | 12               | Histoire 12 | 1            | 2        | 60     | 60     |
| 3        | 7                | Histoire 7  | 3            | 3        | 30     | 30     |
|          | 8                | Histoire 8  | 2            | 2        | 25     | 25     |
|          | 9                | Histoire 9  | 1            | 3        | 20     | 20     |
|          | 11               | Histoire 11 | 1            | 2        | 25     | 25     |
|          | 13               | Histoire 13 | 3            | 1        | 180    | 180    |
|          | 14               | Histoire 14 | 1            | 3        | 40     | 40     |

----------------------

## Description

### Itération 1

#### Histoire 1

**Instructions originales:**

- Créer et modifier une liste de courses comportant
  des produits disponibles dans une base de données générique
  - Pour chaque produit apparaissant dans la liste, il faut pouvoir :
  - indiquer le nombre ou la quantité désirée de ce produit
  - La quantité
    doit pouvoir être encodée manuellement ou se faire à l’aide de
    boutons "+" et "-"
- L’utilisateur doit pouvoir archiver une liste de courses.


**Tâches en plus:**

/

:question: **Question:**

- Que se passe t'il lorsqu'on clic sur une épingle "fusionnée" (qui regroupe d'autres épingle) ?
    - On affiche tous les pokemons de cette épingle (possiblement grand :confused:)
    - On ne fait rien (comme s'il n'y avait rien)
    - On zoom pour montrer les différentes épingles
    - Autre ?

### Histoire 3

**Instructions originales:**

- effectuer une recherche dans ses recettes à l’aide de
  filtres, et ajouter ou supprimer des recettes pour chaque jour. Il
  peut y avoir plusieurs recettes prévues pour un même jour,et un
  nombre de personnes différent pour chaque recette.
- l’utilisateur doit pouvoir compléter automatiquement sa
  liste de menus hebdomadaire avec d’autres recettes enregistrées
- La génération automatique doit au minimum prendre en compte
  le nombre de menus végétariens, de viande et de poisson 
- l’utilisateur pourra remplacer une suggestion par
  une nouvelle suggestion
- l’utilisateur doit pouvoir directement générer une liste de
  courses depuis une liste de menus

**Tâches en plus:**

/

:question: **Question:**

- Que se passe t'il lorsqu'on clic sur une épingle "fusionnée" (qui regroupe d'autres épingle) ?
  - On affiche tous les pokemons de cette épingle (possiblement grand :confused:)
  - On ne fait rien (comme s'il n'y avait rien)
  - On zoom pour montrer les différentes épingles
  - Autre ?

### Iteration 2

Les histoires sont les mêmes que de base 

| Numéro | Taches                                                                                      |
|--------|---------------------------------------------------------------------------------------------|
| 2      | 	Requêtes Recettes                                                                          |
| 2      | 	Voir une recette interface                                                                 |
| 2      | 	Modifier une recette                                                                       |
| 2      | 	Créer et supprimer une recette                                                             |
| 2      | 	Setters pour la classe Recipe                                                              |
| 4      | 	Exporter ShoppingList en PDF                                                               |
| 4      | 	Exporter ShoppingList en odt                                                               |
| 4      | 	Envoyer ShoppingList par mail                                                              |
| 5      | 	Classe Magasin                                                                             |
| 5      | 	Requêtes Magasins                                                                          |
| 5      | 	Modifier DB pour Magasins + prix                                                           |
| 5      | 	Interface visualiser la carte                                                              |
| 5      | 	Barre de recherche pour la carte                                                           |
| 5      | 	Interface Ajouter, supprimer magasin                                                       |
| 5      | 	Ajouter produit à un magasin + prix                                                        |
| 5      | 	Clique droit ouvre un menu contextuelle sur la map avec supprimer, detail, ajouter magasin |
| 5      | 	Rechercher magasins sur la carte                                                           |
| 5      | 	Interface Afficher Magasin + liste produits à l'intérieur                                  |
| 4      | 	bdd liste de mail favori (requete aussi)                                                   |
| 4      | 	lié bdd avec le mail favori                                                                |
| 4      | 	Documentation itext pour export pdf                                                        |
| 2      | 	La partie interface pour les boutons json, odt, PDF, mail                                  |
| 2      | 	Connecter la partie interface pr les btn json, PDF                                         |
| 4      | 	connecter la partie interface pour le ODT (qd il sera fini)                                |
| 4      | 	Refactoring JSONReader + PDFCreator                                                        |
| 0      | popup java to fxml                                                                          |
| 0      | Gradle (exe)                                                                                |
| 2      | 	suppression recursive de la recette :  supprimer dans le menu aussi                        |
| 0      | Verifier les doublons dans generateShopList                                                 |
| 2      | 	Bug menu : recette supprimée mais pas dans les menus                                       |
| 4      | 	Interface envoyer liste de course par mail                                                 |
| 0      | refactor	Prepared Statement                                                                 |
| 0      | refactor	Refactoring DB                                                                     |
| 0      | refactor	Refactoring Interface partie 1                                                     |
| 0      | refactor	Refactoring Interface partie 2                                                     |
| 0      | refactor	crée sous package pour bdd (dao)                                                   |
| 0      | refactor	Refactor UniqueProductList                                                         |
| 0      | refactor	MVC mail                                                                           |
| 0      | refactor	MVC Map ( magasin)                                                                 |
| 0      | refactor	MVC Menu                                                                           |
| 0      | refactor	MVC ShoppingList                                                                   |


### Iteration 3

Les histoires sont les mêmes. Sauf qu'on a enlevé la possibilié a un utilisateur 
qui ne possède pas un compte professionnel de faire des recettes dépassant 100 unités par
produit.

| Numéro | Tâches                                                             |
|--------|--------------------------------------------------------------------|
| 8      | 	Calcul du plus court chemin interface + bouton                    |
 | 8      | 	Calcul du plus court chemin algo                                  |
 | 9      | 	Créer un login utilisateur et mdp interface s'inscrire            |
 | 9      | 	Créer un login utilisateur et mdp interface se connecter          |
 | 9      | 	Classe User et Connected User + Tests                             |
 | 9      | 	Créer un login utilisateur et mdp database                        |
 | 1      | 0	Ajout de données des produits d'un magasin interface manuel      |
 | 1      | 0	Ajout de données des produits d'un magasin interface import      |
 | /      | 	Modifier le code pour le faire correspondre a l'utilisateur       |
 | /      | 	button deconnecter sur toutes les pages                           |
 | 1      | 0	parser importation produit json                                  |
 | 1      | 2	Section d'aide sous page Recette 1: create                       |
 | 1      | 2	Section d'aide sous page Recette 2 : modify                      |
 | 1      | 2	Section d'aide :  Menu, liste course, shop, map                  |
 | /      | 	Changer dans tous les fxml Profile par profil                     |
 | /      | 	Refactoring DB (try with resssources)                             |
 | /      | 	Refactoring  MVC :  Réduire le controller listeDecourse           |
 | /      | 	Refactoring  MVC :  Réduire le controller menu                    |
 | /      | 	Refactoring  MVC :  Séparer le controller recette                 |
 | /      | 	Refactoring  MVC : crée les controllers une seule fois            |
 | /      | 	package pour les test                                             |
 | /      | 	refactor nom des fichiers fxml                                    |
 | /      | 	refactor nom des classe User ...                                  |
 | /      | 	JAVADOC                                                           |
 | /      | 	email favori redondant                                            |
 | /      | 	Refact nom des fxml, affiche le JSON recipe                       |
 | /      | 	alertMessage qd une ShoppingList is delete (refresh)              |
 | 9      | 	Table UserFavoriteRecipe DB + methodes d'accès                    |
 | 9      | 	Table UserFavoriteMail DB + methodes d'accès                      |
 | 9      | 	Table UserRecipe DB + methodes d'accès                            |
 | /      | 	Table Magasin DB + methodes d'accès                               |
 | /      | 	Table UserListeDeCourse DB + methodes d'accès                     |
 | 9      | 	Table UserMenu DB + méthodes d'accès                              |
 | 9      | 	Ajout et suppresion de recette favoris ui + BD                    |
 | /      | 	Interface : Recettes favoris de l utilisateur                     |
 | 9      | 	Empecher l'user non profe d'entrer plus de quantité qu'il ne peut |