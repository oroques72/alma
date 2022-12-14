Les sources sont fournis sous forme de projet Eclipse.

##########configuration###############
Les fichiers présents dans le repertoire WebContent/conf permettent d'adapter le contenu en fonction des bibliothèques.

##config.js
#le repertoire de deploiement de l'archive WAR 
const WAR_NAME='quitus';

#La clé publique 
https://developers.google.com/recaptcha/docs/versions#recaptcha_v2_im_not_a_robot_checkbox
const GOOGLE_CAPTCHA_KEY = '';

#le lien où pointe le logo de la page quitus.html
const URL_LOGO='http://bibliotheques.univ-toulouse.fr/';

#le lien de demande de renseignements
const URL_CONTACT='http://bibliotheques.univ-toulouse.fr/nos-services/public/une-question';
//lien à la racine de webapps vers le repertoire où sont générés les quitus

#nom du lien symbolique à créer à la racine du repertoire webapps de tomcat 
#qui pointe vers le reperoire où sont générés les quitus pdf et rtf (Cf HOST_QUITUS_DIR dans config.properties)
const HOST_QUITUS_PATH_LINK='quitus_download';

#le text de présnetation de la page quitus
const
TEXT_ID_PANEL_HEADING='Cette page vous permet de faire <strong>une demande de quitus</strong>.Une fois le quitus réclamé, <strong>il ne vous sera plus possible de faire de prêt</strong> auprès des bibliothèques du réseau de l\'Université de Toulouse Midi-Pyrénées. A la suite de cette demande <strong>vous pourrez télécharger le document</strong>. Une copie de ce document vous sera également <strong>envoyée à votre adresse electronique.</strong>';


##config.properties

#La clé pour acceder en lecture/ecriture aux APIS https://developers.exlibrisgroup.com/alma/apis/users/ (Production ou Sandbox)
ALMA_API_USERS_KEY=

#l'URL de base des APIS Alma
ALMA_API_BASE_URL=https://api-eu.hosted.exlibrisgroup.com

#La clé secrete du recaptcha google (associée à la clé publique Cf config.js)
GOOGLE_SECRET_CAPTCHA=
#repertoire ou est stocke le fichier de quitus genere en rtf et/ou pdf lien quitus_download
HOST_QUITUS_DIR=/root/alma/quitus

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