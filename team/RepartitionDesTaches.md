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
| Numéro   | Tâches                                                                                     | Assignation     | Analystes                   | Nb d'heure |
| -------- | ------------------------------------------------------------------------------------------ | --------------- | --------------------------- | ---------- |
| 2        | Requêtes Recettes                                                                          | Salim           | Vinove                      | 3          |
| 2        | Voir une recette interface                                                                 | Aïssa           | Nathan                      | 3          |
| 2        | Modifier une recette                                                                       | Aïssa           | Abdessamad, Imane           | 2,5        |
| 2        | Créer et supprimer une recette                                                             | Aïssa           | Helin                       | 5          |
| 2        | Setters pour la classe Recipe                                                              | Abdessamad      |                             | 0,05       |
| 4        | Exporter ShoppingList en PDF                                                               | Salma           | Imane, Salim, Vinove, Samad | 3,5        |
| 4        | Exporter ShoppingList en odt                                                               | Vinove          | Imane,Salim                 | 4,5        |
| 4        | Envoyer ShoppingList par mail                                                              | Nathan          | Salma                       | 4,5        |
| 5        | Classe Magasin                                                                             | Valentin        | Vinove                      | 0,5        |
| 5        | Requêtes Magasins                                                                          | Vinove          | Helin, Aissa, Salma         | 8          |
| 5        | Modifier DB pour Magasins + prix                                                           | Aïssa           | Vinove                      | 1          |
| 5        | Interface visualiser la carte (on va decouper que dalle)                                   | Vinove          | Imane, Elsbeth              | 4          |
| 5        | Barre de recherche pour la carte                                                           | Vinove          | Nathan                      | 4          |
| 5        | Interface Ajouter, supprimer magasin                                                       | Vinove          | Vinove, Nathan              | 4          |
| 5        | Ajouter produit à un magasin + prix                                                        | Abdessamad      | Imane                       | 2          |
| 5        | Clique droit ouvre un menu contextuelle sur la map avec supprimer, detail, ajouter magasin | Vinove          | Abdessamad                  | 4          |
| 5        | Rechercher magasins sur la carte                                                           | Vinove          | Abdessamad                  | 2          |
| 5        | Interface Afficher Magasin + liste produits à l'intérieur                                  | Imane           | Nathan                      | 3          |
| 4        | bdd liste de mail favori (requete aussi)                                                   | Vinove          |                             | 1          |
| 4        | lié bdd avec le mail favori                                                                | Imane           |                             |            |
| 4        | Documentation itext pour export pdf                                                        | Salma           | Nathan                      | 1          |
| 2        | La partie interface pour les boutons json, odt, PDF, mail                                  | Imane           |                             | 1          |
| 2        | Connecter la partie interface pr les btn json, PDF                                         | Imane           |                             | 0,5        |
| 4        | connecter la partie interface pour le ODT (qd il sera fini)                                | Vinove          |                             | 1          |
| 4        | Refactoring JSONReader + PDFCreator                                                        | Salma           | Imane                       | 3          |
|          | popup java to fxml                                                                         | Aïssa           | Helin, Imane                | 4          |
|          | Gradle (exe)                                                                               | Nathan          |                             | 6          |
| 2        | suppression recursive de la recette : supprimer dans le menu aussi                         | Helin, Valentin |                             | 0,5        |
|          | Verifier les doublons dans generateShopList                                                | Imane           | Vinove, Nathan              | 1          |
| 2        | Bug menu : recette supprimée mais pas dans les menus                                       | Aïssa           | tout le monde               | 0,5        |
| 4        | Interface envoyer liste de course par mail                                                 | Imane           | Helin, Nathan               | 3          |
| refactor | Prepared Statement                                                                         | Aïssa           | Helin                       | 15         |
| refactor | Refactoring DB                                                                             | Helin           | Valentin                    | 15         |
| refactor | Refactoring Interface partie 1                                                             | Aïssa           | Imane                       | 3          |
| refactor | Refactoring Interface partie 2                                                             | Imane           | Aïssa                       | 3          |
| refactor | creer sous package pour bdd (dao)                                                          | Helin           |                             | 0,05       |
| refactor | Refactor UniqueProductList                                                                 | Nathan          | Salma                       | 4          |
| refactor | MVC mail                                                                                   | Imane           |                             | 2          |
| refactor | MVC Map ( magasin)                                                                         | Vinove          |                             | 4          |
| refactor | MVC Menu                                                                                   | Vinove          |                             | 4          |
| refactor | MVC ShoppingList                                                                           | Imane           |                             | 4          |
| refactor | MVC Recettes                                                                               | Nathan          | Aïssa, Imane, Helin         | 8          |