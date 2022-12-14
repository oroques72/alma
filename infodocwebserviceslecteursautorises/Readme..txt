Les sources sont fournis sous forme de projet Eclipse.

##########configuration###############
Les fichiers présents dans le repertoire WebContent/conf permettent d'adapter le contenu en fonction des bibliothèques.

##config.js
#le repertoire de deploiement de l'archive WAR 
const WAR_NAME='la';

#La clé publique 
https://developers.google.com/recaptcha/docs/versions#recaptcha_v2_im_not_a_robot_checkbox
const GOOGLE_CAPTCHA_KEY = '';

#le text de présnetation de la page de preinscription
const
TEXT_ID_PANEL_HEADING='';

#les liens vers les bibliothèques indiquant les tarifs
const BU_HTML_MAP=''

const
AFFILIATION_LABEL_ETU_OPTGROUP

const
AFFILIATION_LABEL_ETU_OPTGROUP

le menu des affiliation
const AFFILIATION_PERSO_OPTION=

Le menu bibliothèques d'inscription
const
BU_INSCRIPTION_OPTION = 

Le menu durées d'inscription
const
DUREE_INSCRIPTION_OPTION 


##config.properties

#La clé pour acceder en lecture/ecriture aux APIS https://developers.exlibrisgroup.com/alma/apis/users/ (Production ou Sandbox)
ALMA_API_USERS_KEY=

#l'URL de base des APIS Alma
ALMA_API_BASE_URL=https://api-eu.hosted.exlibrisgroup.com

#La clé secrete du recaptcha google (associée à la clé publique Cf config.js)
GOOGLE_SECRET_CAPTCHA=

#champs optionnels peuvent etre vide ou inexistant
#Les paramètres de connexion du serveur SMTP pour l'envoi de mail des QUITUS
#Si vide pas d'envoie de mail
SMTP_LOGIN=
SMTP_PASSWD=
SMTP_HOST=

#Pour recuperer l'INE (Universite Toulouse UNIQUEMENT)
PARSE_USERS_FILE=false
USERS_PATH_FILENAME=

#Si une tache Shell génère les PDFs associés aux RTF déposés dans HOST_QUITUS_DIR
GENERATE_PDF=false


#########Deploiement############