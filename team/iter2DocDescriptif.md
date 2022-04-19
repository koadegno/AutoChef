uivi de planification du projet :

Toutes les fonctionnalités des histoires 2, 4 et 5 ont été implémentées.

Difficultés :

Pour la map, il était au début difficile de trouver une map qui nous convenait sur le plan financier (gratuit) mais
finalement nous avions utilisés la librairie argcis qui a ce qu'on avait besoin. Elle nous permet de visualiser la 
carte du monde et de retrouver une adresse.

Choix d'implémentation :

De plus, nous avons implementer notre code en MVC, pour pouvoir diviser la partie qui gère l'interface graphique
(Vue), la partie qui contient les données (modèle) et la partie qui contient la logique (Controleur). C'est
beaucoup plus pratique, ça permet au modele de ne pas dependre de la vue ou du controleur. La base de données 
aussi a été modifiée. Pour y accéder, il faut maintenant passer par des DAO, qui sont des interfaces possédant
les méthodes d'une base de données classique (insert, update,..). Il existe un DAO pour chaque type d'élément
auquel on veut accéder, par exemple, pour insérer une nouvelle recette, on fait appel à la méthode insert 
de RecipeDao. Une autre amélioration de la base de données est la mise en place des prepared statements. Plutôt
que de faire les requêtes direcetement sans vérification, les prepared statements offrent une sécurité 
supplémentaire en vérifiant que l'utilisateur n'essaie pas de corrompre la base de données.