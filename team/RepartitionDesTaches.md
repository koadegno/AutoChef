# Repartition des taches développeur 


## Itération 1 :

| Histoire n°  | Tache                                                                             |     |
|--------------|-----------------------------------------------------------------------------------|-----|
| Histoire 1   | Architecture BDD ( partie produit , <br/> partie liste de course, partie recette) |     |
| Histoire 1   | Créer requête bdd (ajouter, supp,...)                                             |     |
| Histoire 1   | Interface partie 1 (liste de course, ajout produits)                              |     |
| Histoire 1   | Remplir Bdd avec des recettes                                                     |     |
| Histoire 1   | Remplir Bdd avec des produits,                                                    |     |
| Histoire 1   | Classe Produit                                                                    |     |
| Histoire 1   | Ajout du nombre d'élément à la classe Produit                                     |     |
| Histoire 1   | Classe Liste de Course   <br/>                                                    |     |
| Histoire 1   | Ajout de Quantité à la classe Produit                                             |     |
| Histoire 3   | Interface: recherche et affichage de recette                                      |     |
| Histoire 3   | Interface: Afficher un calendrier de menus                                        |     |
| Histoire 3   | Interface: Afficher et Créer un menu                                              |     |
| Histoire 3   | Complétion automatique des menus                                                  |     |
| Histoire 3   | Classe Menus (repas)                                                              |     |
| Histoire 1/3 | Lier l'interface partie 1 à la BDD                                                |     |
----------------------
## Itération 2
| Numéro   | Tâches                                                                                     | Assignation     | Analystes                  | Nb d'heure |
|----------|--------------------------------------------------------------------------------------------|-----------------|----------------------------|------------|
| 2        | Requêtes Recettes                                                                          | Salim           | Kokou                      | 3          |
| 2        | Voir une recette interface                                                                 | Aïssa           | Nathan                     | 3          |
| 2        | Modifier une recette                                                                       | Aïssa           | Abdessamad, Imane          | 2,5        |
| 2        | Créer et supprimer une recette                                                             | Aïssa           | Helin                      | 5          |
| 2        | Setters pour la classe Recipe                                                              | Abdessamad      |                            | 0,05       |
| 4        | Exporter ShoppingList en PDF                                                               | Salma           | Imane, Salim, Kokou, Samad | 3,5        |
| 4        | Exporter ShoppingList en odt                                                               | Kokou           | Imane,Salim                | 4,5        |
| 4        | Envoyer ShoppingList par mail                                                              | Nathan          | Salma                      | 4,5        |
| 5        | Classe Magasin                                                                             | Valentin        | Kokou                      | 0,5        |
| 5        | Requêtes Magasins                                                                          | Kokou           | Helin, Aissa, Salma        | 8          |
| 5        | Modifier DB pour Magasins + prix                                                           | Aïssa           | Kokou                      | 1          |
| 5        | Interface visualiser la carte (on va decouper que dalle)                                   | Kokou           | Imane, Elsbeth             | 4          |
| 5        | Barre de recherche pour la carte                                                           | Kokou           | Nathan                     | 4          |
| 5        | Interface Ajouter, supprimer magasin                                                       | Kokou           | Kokou, Nathan              | 4          |
| 5        | Ajouter produit à un magasin + prix                                                        | Abdessamad      | Imane                      | 2          |
| 5        | Clique droit ouvre un menu contextuelle sur la map avec supprimer, detail, ajouter magasin | Kokou           | Abdessamad                 | 4          |
| 5        | Rechercher magasins sur la carte                                                           | Kokou           | Abdessamad                 | 2          |
| 5        | Interface Afficher Magasin + liste produits à l'intérieur                                  | Imane           | Nathan                     | 3          |
| 4        | bdd liste de mail favori (requete aussi)                                                   | Kokou           |                            | 1          |
| 4        | lié bdd avec le mail favori                                                                | Imane           |                            |            |
| 4        | Documentation itext pour export pdf                                                        | Salma           | Nathan                     | 1          |
| 2        | La partie interface pour les boutons json, odt, PDF, mail                                  | Imane           |                            | 1          |
| 2        | Connecter la partie interface pr les btn json, PDF                                         | Imane           |                            | 0,5        |
| 4        | connecter la partie interface pour le ODT (qd il sera fini)                                | Kokou           |                            | 1          |
| 4        | Refactoring JSONReader + PDFCreator                                                        | Salma           | Imane                      | 3          |
|          | popup java to fxml                                                                         | Aïssa           | Helin, Imane               | 4          |
|          | Gradle (exe)                                                                               | Nathan          |                            | 6          |
| 2        | suppression recursive de la recette : supprimer dans le menu aussi                         | Helin, Valentin |                            | 0,5        |
|          | Verifier les doublons dans generateShopList                                                | Imane           | Kokou, Nathan              | 1          |
| 2        | Bug menu : recette supprimée mais pas dans les menus                                       | Aïssa           | tout le monde              | 0,5        |
| 4        | Interface envoyer liste de course par mail                                                 | Imane           | Helin, Nathan              | 3          |
| refactor | Prepared Statement                                                                         | Aïssa           | Helin                      | 15         |
| refactor | Refactoring DB                                                                             | Helin           | Valentin                   | 15         |
| refactor | Refactoring Interface partie 1                                                             | Aïssa           | Imane                      | 3          |
| refactor | Refactoring Interface partie 2                                                             | Imane           | Aïssa                      | 3          |
| refactor | creer sous package pour bdd (dao)                                                          | Helin           |                            | 0,05       |
| refactor | Refactor UniqueProductList                                                                 | Nathan          | Salma                      | 4          |
| refactor | MVC mail                                                                                   | Imane           |                            | 2          |
| refactor | MVC Map ( magasin)                                                                         | Kokou           |                            | 4          |
| refactor | MVC Menu                                                                                   | Kokou           |                            | 4          |
| refactor | MVC ShoppingList                                                                           | Imane           |                            | 4          |
| refactor | MVC Recettes                                                                               | Nathan          | Aïssa, Imane, Helin        | 8          |

## Itération 3 :

| Histoire n°   | Tache                                                                      | Assignation   | Analystes             | Heure    |
|---------------|----------------------------------------------------------------------------|---------------|-----------------------|----------|
| 8             | 	Calcul du plus court chemin interface + bouton	                           | Abdessamad	   | Imane	                | 10h      |
| 8             | 	Calcul du plus court chemin algo	                                         | Abdessamad	   | Kokou	                | 40h      |
| 9             | 	Créer un login utilisateur et mdp interface s'inscrire                    | 	Helin	       | Aïssa	                | 4h       |
| 9             | 	Créer un login utilisateur et mdp interface se connecter                  | 	Valentin     | 		                    | 2h       |
| 9             | 	Classe User et Connected User + Tests	                                    | Aïssa	        | Aissa	                | 3h       |
| 9             | 	Créer un login utilisateur et mdp database                                | 	Kokou	       | 	                     | 2h       |
| 10            | 	Ajout de données des produits d'un magasin interface manuel               | 	Imane	       | 	                     | 2h       |
| 10            | 	Ajout de données des produits d'un magasin interface import               | 	Imane	       | 	                     | 1h       |
| /             | 	Modifier le code pour le faire correspondre a l'utilisateur               | 	Aïssa        | 	Aissa	               | 2h       |
| /             | 	button deconnecter sur toutes les pages	                                  | Aïssa	        | Aissa	                | 2h       |
| 10            | 	parser importation produit json	                                          | Salma	        | 	                     | 1h       |
| 12            | 	Section d'aide sous page Recette 1: create	                               | Elsbeth       | 	Imane	               | 2h       |
| 12            | 	Section d'aide sous page Recette 2 : modify	                              | Salim		       |                       |
| 12            | 	Section d'aide :  Menu, liste course, shop, map                           | 	Imane	       | Salim, Elsbeth        | 	15h     |
| /             | 	Changer dans tous les fxml Profile par profil	                            | Helin	        | 	                     | 5 min    |
| /             | 	Refactoring  MVC :  Réduire le controller listeDecourse	                  | Imane		       |                       | 4h30     |
| /             | 	Refactoring  MVC :  Réduire le controller menu			                         | Imene         | Aissa                 | 5h       |
| /             | 	Refactoring  MVC :  Séparer le controller recette	                        | Valentin	     | Kokou                 | 	8h      |
| /             | 	Refactoring  MVC : crée les controllers une seule fois                    | 	Valentin     | 	Kokou,Nathan         | 	5h      |
| /             | 	refactor nom des fichiers fxml	                                           | Kokou	        |                       | 	15min   |
| /             | 	JAVADOC			                                                                |               |                       |          |
| /             | 	email favori redondant	                                                   | Kokou		       |                       | 3h       |
| /             | 	Refact nom des fxml, affiche le JSON recipe 	                             | Imane         | 	Imane	               | 10 min   |
| /             | 	alertMessage qd une ShoppingList is delete (refresh)	                     | Imane         | 	Imane	               | 15 min   |
| 9             | 	Table UserFavoriteRecipe DB + methodes d'accès	                           | Kokou         | 	Aissa	               | 2h       |
| 9             | 	Table UserFavoriteMail DB + methodes d'accès	                             | Kokou         | 		                    | 4h       |
| 9             | 	Table UserRecipe DB + methodes d'accès	                                   | Kokou         | 		                    | 4h       |
| /             | 	Table Magasin DB + methodes d'accès	                                      | Kokou	        | Aissa       	         | 2h       |
| /             | 	Table UserListeDeCourse DB + methodes d'accès	                            | Kokou         | 	Aissa	               | 8h       |
| 9             | 	Table UserMenu DB + méthodes d'accès	                                     | Salma 	       | Kokou                 | 	1h      |
| 9             | 	Ajout et suppresion de recette favoris ui + BD	                           | Aïssa         | 	Helin, Imane	        | 2h       |
| /             | 	Interface : Recettes favoris de l utilisateur	                            | Aïssa         | 	Aissa	               | 2h       |
| 9             | 	Empecher l'user non profe d'entrer plus de quantité qu'il ne peut	        | Imane         | 		                    | 1h       |
