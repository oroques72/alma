const
WAR_NAME = "la_test";
const
GOOGLE_CAPTCHA_KEY = "";
const URL_MENTIONS_LEGALES="http://bibliotheques.univ-toulouse.fr/archipel-cnil";
const URL_LOGO="http://bibliotheques.univ-toulouse.fr/";

const
TEXT_ID_PANEL_HEADING = "Cette page est destinée aux lecteurs extérieurs au réseau des bibliothèques de l'Université de Toulouse. Elle leur permet de se préinscrire. Si vous êtes étudiant, personnel ou enseignant d'un des établissements suivants, vous êtes déjà inscrit au sein du réseau des bibliothèques de l'Université de Toulouse : Université Toulouse-1 Capitole, Université Toulouse Jean Jaurès, Université Toulouse-3 Paul Sabatier, INP de Toulouse, INSA de Toulouse, ISAE-SUPAERO, INU Champollion, ENAC, IMT Mines Albi. Vous n'avez donc rien à faire.";
const
TEXT_ID_ALERT = 'Vous rencontrez un problème ou vous avez une question ?</strong> Veuillez vous rendre dans votre bibliothèque ou contactez le service <a href="http://bibliotheques.univ-toulouse.fr/nos-services/public/une-question>une-question"</a>.';
const BU_HTML_MAP={
	INSA : "http://bib.insa-toulouse.fr/fr/infos-pratiques/tarifs/inscriptions-bib-insa.html",
	MAN : "https://www.ut-capitole.fr/bibliotheques/services/services-pour-tous/bu-s-inscrire-305326.kjsp?RH=1466774735707",
	ARS : "https://www.ut-capitole.fr/bibliotheques/services/services-pour-tous/bu-s-inscrire-305326.kjsp?RH=1466774735707",
	ALBI : "https://scd.univ-jfc.fr/inscription",
	RODEZ : "https://scd.univ-jfc.fr/inscription",
	CASTRES : "https://scd.univ-jfc.fr/inscription",
	JUL : "https://bibliotheques.univ-tlse3.fr/regles-de-pret-inscription",
	SAN : "https://bibliotheques.univ-tlse3.fr/regles-de-pret-inscription",
	SCI : "https://bibliotheques.univ-tlse3.fr/regles-de-pret-inscription",
	IUTR : "https://bibliotheques.univ-tlse3.fr/regles-de-pret-inscription",
	IUTP : "https://bibliotheques.univ-tlse3.fr/regles-de-pret-inscription",
	MINALBI : "https://jason.univ-toulouse.fr/la/BU/MINALBI.html"
};
const
AFFILIATION_LABEL_ETU_OPTGROUP = "Etudiant d'un établissement extérieur au réseau des bibliothèques de l'Université de Toulouse";
const
AFFILIATION_ETU_OPTION = {
	11 : "Etudiant niveau BAC+1 (L1)",
	12 : "Etudiant niveau BAC+2 (L2)",
	13 : "Etudiant niveau BAC+3 (L3)",
	14 : "Etudiant niveau BAC+4 (M1)",
	15 : "Etudiant niveau BAC+5 (M2)",
	16 : "Etudiant niveau BAC+6 (D1)",
	17 : "Etudiant niveau BAC+7 (D2)",
	18 : "Etudiant niveau BAC+8 (D3)"
};

const AFFILIATION_PERSO_OPTION={
	2 : "Personnel d'un établissement extérieur au réseau des bibliothèques de l'Université de Toulouse",
	3 : "Enseignant d'un établissement extérieur au réseau des bibliothèques de l'Université de Toulouse",
	4 : "Professeur d'un établissement de l'enseignement primaire",
	5 : "Professeur d'un établissement de l'enseignement secondaire",
	6 : "Membre d'une profession juridique",
	7 : "Professionnel de santé",
	8 : "Demandeur d'emploi",
	9 : "Autre profession",
};
const
BU_INSCRIPTION_OPTION = {
	INSA : "BIB'INSA",
	MAN : "Bibliothèque de la Manufacture",
	ARS : "Bibliothèque de l'Arsenal",
	ALBI : "BU Albi",
	RODEZ : "BU Rodez",
	CASTRES : "Maison de campus Castres(INUC)",
	JUL : "BU Santé Allées",
	SAN : "BU Santé Rangueil",
	SCI : "BU Sciences",
	IUTP : "CRDoc Ponsan",
	IUTR : "CRDoc Rangueil",
	MINALBI : "IMT Mines Albi"
};
const
DUREE_INSCRIPTION_OPTION = {
	//NB_JOURS : "DESCRIPTION"
	90 : "3 mois",
	180 : "6 mois",
	365 : "1 an"
};
