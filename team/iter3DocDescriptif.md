#Document descriptif de l'itération 3

###Suivi de planification du projet :

Toutes les fonctionnalités des histoires 8, 9, 10 et 12 ont été implémentées.

Difficultés :

* Le plus cours chemin pour la map a été assez difficile, dû à des bugs qui n'arrivait que
durant le runtime et qui n'était pas vraiment dû au code.
* L'adaptation de tout le code pour le faire correspondre à 1 seul utilisateur connecté

Choix d'implémentation :

Nous avons implementer notre code en MVC, pour pouvoir diviser la partie qui gère l'interface graphique
(Vue), la partie qui contient les données (modèle) et la partie qui contient la logique (Contrôleur). C'est
beaucoup plus pratique, ça permet au modèle de ne pas dépendre de la vue ou du contrôleur.
La base de données aussi a été modifiée. Pour y accéder, il faut maintenant passer par des DAO, qui sont des interfaces possédant
les méthodes d'une base de données classique (insert, update,..). Il existe un DAO pour chaque type d'élément
auquel on veut accéder, par exemple, pour insérer une nouvelle recette, on fait appel à la méthode insert
de RecipeDao. Une autre amélioration de la base de données est la mise en place des prepared statements. Plutôt
que de faire les requêtes direcetement sans vérification, les prepared statements offrent une sécurité
supplémentaire en vérifiant que l'utilisateur n'essaie pas de corrompre la base de données.