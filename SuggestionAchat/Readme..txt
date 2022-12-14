Les sources sont fournis sous forme de projet Eclipse.

#configuration
Les fichiers présents dans le repertoire WebContent/conf permettent d'adapter le contenu en fonction des bibliothèques.

####config.js
#le repertoire de deploiement de l'archive WAR 
const WAR_NAME='sga';

#La clé publique 
https://developers.google.com/recaptcha/docs/versions#recaptcha_v2_im_not_a_robot_checkbox
const GOOGLE_CAPTCHA_KEY = '';

#le lien où pointe le logo de la page quitus.html
const URL_LOGO='http://bibliotheques.univ-toulouse.fr/';

#les intitulés des BU
const BU_SUGGESTION_OPTION 

Les codes ALMA des BUs par établissement pour remplir la balise owning_library du template xml de suggestion d'achat
const ETAB_BU



####config.properties
La clé pour acceder en lecture/ecriture aux APIS https://developers.exlibrisgroup.com/alma/apis/users/ (Production ou Sandbox)
ALMA_API_USERS_KEY=

l'URL de base des APIS Alma
ALMA_API_BASE_URL=https://api-eu.hosted.exlibrisgroup.com

La clé secrete du recaptcha google (associée à la clé publique Cf config.js)
GOOGLE_SECRET_CAPTCHA=

#l'URL de base de l'API Primo
PRIMO_API_BASE_URL=https://api-eu.hosted.exlibrisgroup.com

https://developers.exlibrisgroup.com/primo/apis/search/
PRIMO_PROD_KEY=

La vue sur laquelle s'effectue la recherche primo
PRIMO_VIEW=