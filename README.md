# UGarden 2025
 On a commencé en modifiant le class 'Level' et on a ajouté les cas pour tous les decors possibles.
 
Puis, on a modifié le class 'SpriteFactory' afin d'afficher tous les éléments, y compris le hérisson et les bonus.

Pour que les nids génère des bugs, on a commencé en ajoutant une liste des bouges comme attribut à la classe 'game' 
et on a ajouté quand un bogue est crée, on l'a ajouté dans cette liste pour bien les gérer.

Comme les frelons se déplacent comme les guêpes sauf qu'elles piqueent 2 fois et plus lente, on 
a crée une nouvelle classe 'bugs' pour implementer les méthods communs et les attributs.
Cette classe est abstraite parce que ce n'est pas possible de creer un objet bug sans specifier.

On a crée des nouvelles classes "sprite" pour les bugs pour gérèr l'affichage des bogues.
On a hérité de la classe 'sprite' dans tous nos classes qu'on a crée pour l'affichage des bogues.

On a ajouté 2 methods 'getWasp' et 'getHornet' dans la classe 'ImageResourceFactory' pour l'appeler depuis la 
classe SpriteHornet selon la direction.

Comme la vitesse des bogues sont déjà définis dans la 'configuration', on les a utilisés en ajoutant des attributs 'speed' 
aux classes bogues, et comme la vitesse est donnée en seconde, on les a multiplié par 1000 afin de le convertir en ms.

Pour nos personnages, bonus et decors on a hérité de la classe 'GameObject' parce que cette classe contient les implementations
des fonctions comme setModified, remove etc. qu'on va utiliser pour intéragir. (ex/ on n'a pas besoin d'implementer la fonction setModified dans la classe Wasp)

On a utilise la methode "checkCollision" de la classe GameEngine seulement pour le "wasp" 
car on avait deja ecrit le cas de collision pour le "hornet" dans sa methode update avant qu'on a 
vu qu'il existe une methode checkCollision et on pense que ajouter ce cas dans update est
efficace donc on n'a pas le modifie

Enfin, on a fait le système de passage par les portes de la manière suivante :
Si le joueur essaie de passer par une porte non marquée comme "précédente" (!door.isPrev()), il accède au level suivant.
À l’inverse, s’il passe par une porte marquée comme "précédente" (door.isPrev()), il revient au level précédent.
Et si le niveau cible existe, le jeu lance la transition vers ce level via requestSwitchLevel.


