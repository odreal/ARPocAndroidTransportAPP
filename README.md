# Paris et ses transports en AR

## Présentation du projet
Les consignes du projet sont simples : Trouver une utilisation de l'AR qui n'a pas encore été imaginée et essayer de fournir un POC.

Après quelques minutes de réflexion, notre idée fut de réaliser une carte 3D de la ville de Paris et d'y afficher les transports en communs en temps réel.

Cette application peut avoir une certaine utilité auprès des touristes par exemple afin qu'ils se repèrent plus facilement dans l'immense fourmilière que peut être Paris pour eux. Grâce à cette représentation 3D ils pourront bouger Paris du bout des doigts et mieux se repérer. Ils pourront également mieux comprendre où se dirige chaque ligne de métro ou de train.

Notre application peut également avoir une utilité plus ludique, pour les curieux qui aiment savoir ce qui les entours, ils pourront avoir accès à tout ce qui se passe autour d'eux.

Voici une vidéo montrant le POC en fonctionnement.
[Vidéo d'exemple du POC](https://youtu.be/A7WR0aE8jPc)

## Création de la map en 3D
Afin de réaliser au mieux notre projet nous avons dû trouver une map de Paris modélisée en 3D. Malheureusement, toutes les cartes que nous avons trouvé étaient payantes.

Il a alors fallut trouver une solution afin de modéliser Paris (ou quelques quartiers) nous-mêmes. Deux solutions s'offraient à nous:
- Ouvrir Blender, ouvrir Google Maps et tout faire à la main... Beaucoup trop laborieux
- Créer un script python qui récupère les datas d'une ville sur OpenStreetMap et ensuite créé un modèle 3D.

Nous avons donc adapté certains outils déjà existant pour automatiser ce processus et ainsi arriver à modéliser une carte de Paris en 3D.

Après avoir fait tourner le script pendant quelques heures, nous avons pu générer une carte 3D de Paris. Le problème est que celle-ci faisait 1Go.
Nous avons alors choisi de ne représenter que 4 arrondissements pour la première version du projet. Cette carte fait cette fois-ci approximativement 80Mo, ce qui est bien plus optimisé que la carte 3D entière de Paris.

## API transports en communs
Afin de connaitre le déplacement en direct des trains et métros nous utilisons l'API de la RATP afin d'avoir les horaires théoriques de passages. Suite à cela nous pouvons déterminer la position du train en direct.
L'API Navitia est adaptée à notre projet car elle offre toutes les ressources d'un point de vue données que nous aurions besoin pour les horaires et positions des trains.
## Sample de code utilisé
Notre projet se base sur un sample de code [disponible ici](https://github.com/cgathergood/Your-First-AR-App-with-Sceneform/tree/master/app)

## Assets
Nous utilisons un modèle de train et d'arrêt de bus provenant de la bibliothèque [Poly de Google](https://poly.google.com/).
Le modèle de train choisi est [celui-ci](https://poly.google.com/view/e9pQgEqPGir). 
###
![alt text](https://zupimages.net/up/19/49/t8ce.gif)
###
Le modèle de l'arrêt de bus choisi est [celui-ci](https://poly.google.com/view/4qZtHf-opkG).
###
![alt text](https://zupimages.net/up/19/50/w48v.gif)
## Auteurs
* Maxime
* Josselin

2019

