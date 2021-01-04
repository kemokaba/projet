# Android-Project-Ecommerce

`MEMBRE DU GROUPE`:
    
    _Tidiani BA DIOP_   GROUPE 2          `email': tdiop1999@gmail.com
    _KEMO DIAITE_       GROUPE 2          `email`: kemokaba@gmail.com
    _Abdou Aziz DIOUF_  GROUPE 1           `email`: abdouazizdiouf75@gmail.com   

 _DESCRIPTION GENERALE DE NOTRE APPLICATION ECOMMERCE_

 `Ce presente application a pour objectif de mettre en place une boutique en ligne permettant aux utilisateurs disposant d'un compte d'en pouvoir consulter et effectuer des achats de different gammes de produit`
 
L'utilisauteur peut amener a effectuer plusieurs actions:

     - ajouter, modifier la quantite ou de supprimer un produit dans le panier
     - creer un compte
     - modifier son profil
     - conltuer la localisation de l'agence 

ON A AUSSI UN ADMINISTRATEUR QUI GERERE LA PARTIE BACKEND DE L'APPLICATION:
        - ajoute des produits en fonction de sa categorie


_LES ACTIVITES DE L'APPLICATION:_ 

    - MainActivity: C'est la page d'accueil de l'apllication 
            : deux bouttons une pour se connecter _LoginActivity_ et une autre pour acceder la _RegisterActivity_ 
    
    - RegisterActivity: C'est la page d'inscription de l'utilisateur 
            
    
    - LoginActivity: C'est la page de connexion de l'utilisateur 

    
    - SettingsActivity: c'est le dashbord de l'utilisateur



    - EmapsActivity: Ca correspond a l'activite de google maps


    - ProductViewHolder: affiche la liste des produits sous format cardView


    - CartActivity: Le panier de l'utilisateur

    
    - CartViewHolder: C'est utiliser dans le CartActivity, ajout d'un produit dans la liste des produits du panier 


    - ProductDetailsActivity: affiche le detail d'un produit


     - AdminCategoryActivity: La liste de category de produit 


    - AdminAddNewProductActivity: Ajout d'un produit par 



    
_BACKGROUND SERVICES / threads_

    - ESERVICE: PERMET DE NOTIFIER L'UTILISATEUR SUR LA LOCALISATION DE L'AGENCE ECOMMERCE

    -  PAPER SERVICE : L'objectif de Paper est de fournir une option de stockage d'objets simple mais rapide pour Android. Il permet d'utiliser les classes Java / Kotlin telles quelles: sans annotations, méthodes d'usine, extensions de classe obligatoires, etc.
            'implementation 'io.paperdb:paperdb:2.7.1'


    
_SENSOR_

    - INTERNET : utilisation google maps


_BIBILITHEQUES EXTERNES_

    implementation 'com.github.rey5137:material:1.2.5'
    implementation 'de.hdodenhof:circleimageview:3.1.0'
    implementation 'com.squareup.picasso:picasso:2.71828'
    implementation 'com.google.android.gms:play-services-maps:17.0.0'
    compileOnly 'org.projectlombok:lombok:1.18.12'
    annotationProcessor 'org.projectlombok:lombok:1.18.12'
    implementation 'io.paperdb:paperdb:2.7.1'
    implementation 'com.theartofdev.edmodo:android-image-cropper:2.8.0' //
    implementation 'com.cepheuen.elegant-number-button:lib:1.0.2'



https://github.com/ArthurHub/Android-Image-Cropper

https://github.com/ashik94vc/ElegantNumberButton

https://github.com/pilgr/Paper

https://square.github.io/picasso/

https://github.com/hdodenhof/CircleImageView

https://github.com/rey5137/material

