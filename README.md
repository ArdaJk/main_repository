# UGarden 2025
 On a commencé en modifiant le class 'Level' et on a ajouté les cas pour tous les decors possibles.
 
Puis, on a modifié le class 'SpriteFactory' afin d'afficher tous les éléments, y compris le hérisson et les bonus.


Pour que les nids génère des bugs, on a commencé en ajoutant une liste des bouges comme attribut à la classe 'game' 
et on a ajouté quand un bogue est crée, on l'a ajouté dans cette liste pour bien les gérer.

On a crée des nouvelles classes "sprite" pour les bugs pour gérèr l'affichage des bogues.

Comme la vitesse des bogues sont déjà définis dans la 'configuration', on les a utilisés en ajoutant des attributs 'speed' 
aux classes bogues, et comme la vitesse est donnée en seconde, on les a multiplié par 1000 afin de le convertir en ms.