-Chaque dossier est un projet Eclipse
-Les projets SuggestionAchat,infodocwebserviceslecteursautorises,Quitus dépendent des projets AlmaApi et Common dans le buildpath
-Les projets AlmaApi et Common fournissent des méthodes pour les autres prokets

1)Modifier 
Importer les projets dans Eclipse
Modifier les fichier de configuration dans le repetoire conf (Cf Readme.txt de chaque projet)
Modivier le favicon.ico

2)Créer un archive WAR pour les projets SuggestionAchat,infodocwebserviceslecteursautorises,Quitus


3)Installer un serveur tomcat 9
4)Copier l'archive WAR dans le repetoire /var/lib/tomcat9/webapps (en fonction des installations)

5)
Les projet se trouve à l'url 
https://hostname/WAR_NAME/preinscription.html
https://hostname/WAR_NAME/suggestionachat.html?view=
https://hostname/WAR_NAME/quitus.html

