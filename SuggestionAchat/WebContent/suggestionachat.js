var hostname;
var xmlhttp;
var xmlhttpUNR;
var xmlhttpISBN;

var captchaResponse="KO";
var OKfirstName=false;
var OKlastName =false;
var OKemail =false;
var OKemail2 =false;
var OKtitre=false;
var OKauteur=false;
var OKanneePub=false;
var OKediteur=false;
var OKisbn=false;
var OKremarque=false;
var encours=false;
var etab;
const buCodeDesc = new Map(Object.entries(BU_SUGGESTION_OPTION));
const EtabCodeBu = new Map(Object.entries(ETAB_BU));

var correctCaptcha = function(response) {
	captchaResponse = response;

	document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
	document.getElementById("aideVerifChampObligatoires").style.color = "red";
	// alert("captcha callback");
};



function onloadCallback() {
	   grecaptcha.render('google-recaptcha', {
	        'sitekey': GOOGLE_CAPTCHA_KEY,  // required
	        'expired-callback': 'recaptchaExpired',  // optional
	        'callback': 'correctCaptcha'  // optional
	    });
}

function refreshPage(){
	
	if(navigator.cookieEnabled == true){
		
		console.log("cookies tiers autorises");
	// Rafraichier une fois la page
	if(localStorage.getItem("isRefreshLa") != "refresh"){
		  localStorage.setItem("isRefreshLa","refresh");
		  window.location.reload();
		}
	}else {
			console.log("cookies tiers bloques");
		}
}


function init(){
	
	refreshPage();
	
	xmlhttp = new XMLHttpRequest();
	xmlhttpUNR = new XMLHttpRequest();
	xmlhttpISBN = new XMLHttpRequest();
	
	etab=getParameterByName('view');
	//alert(etab);
	if (etab != null && etab != undefined && EtabCodeBu.get(etab) != undefined){
		remplirBU();
	}
	else{
		document.location = 'https://'+location.hostname+'/'+WAR_NAME+'/suggestionachat_erreur.html';
	}
	
	if (etab == "UT2"){
		carte.style.visibility="hidden";
		carte.disabled=true;
		aide_unr.textContent="Insérez votre numéro de carte pour retrouver vos coordonnées.";
		
	}
	
	
	//Ce champ n'est vérifié que si l'on insère des caractères dedans
	OKisbn=true;
	
	encours=false;
	hostname=location.hostname;
	document.getElementById("Envoyer").disabled = true;
	
	carte.checked=true;
	//boutton disabled car unr vide
	button_search.disabled=true;
	unr.disabled=false;
	
	unr.value="";
	unr.placeholder="Recherchez..";
	
	lastName.disabled=true;
	lastName.value="";
	lastName.placeholder="INCONNU";
	
	firstName.disabled=true;
	firstName.value="";
	firstName.placeholder="INCONNU";
	
	
	email.disabled=true;
	email.value="";
	email.placeholder="INCONNU";
	
	
	email2.disabled=true;
	email2.value="";
	email2.placeholder="INCONNU";
	
	
	
	
	document.getElementById("aideVerifChampObligatoires").textContent="Les champs marqués * sont obligatoires.";
	document.getElementById("aideVerifChampObligatoires").style.color="red";
	
	addControlListener();
}



function remplirBU (){
	
	
	const bus= EtabCodeBu.get(etab).split(",");
	
	for (let i=0; i < bus.length;i++){
	let opt = document.createElement('option');
	opt.value=bus[i];
	opt.text=buCodeDesc.get(bus[i]);
	document.getElementById("buinscr").options.add(opt);
	}
	document.getElementById("buinscr").selectedIndex=0;
	
}
function getParameterByName(p)
{
	var str = document.location.href;
	var url = new URL(str);
	var name = url.searchParams.get(p);
	return name;
}
	
	function envoyer(){
		
		
		$("#Envoyer").html(
				'<i class="fa fa-circle-o-notch fa-spin"></i>&nbsp;&nbsp;&nbsp;Envoyer la demande'
		);
		
		encours=true;
		document.getElementById('Envoyer').style.cursor='wait';
		document.getElementById('createForm').style.cursor='wait';
		document.getElementById('Envoyer').disabled=true;
		
		var url = "https://" + hostname + "/"+WAR_NAME+"/rest/suggestionachat/suggestionachat/"+captchaResponse;

			xmlhttp.open('POST', url, true);
			xmlhttp.setRequestHeader("Content-type", "text/plain");
	
			xmlhttp.send(titre.value.replaceAll(":","-")+":"+auteur.value.replaceAll(":","-")+":"+anneepub.value+":"+editeur.value.replaceAll(":","-")+":"+isbn.value+":"+document.getElementById('buinscr').options[buinscr.selectedIndex].value+":"+unr.value+":"+lastName.value+":"+firstName.value+":"+email.value+":"+remarque.value.replaceAll(":","-"));
	
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4){
			
					
			var reponse=xmlhttp.responseText;
	
			
	
			if (reponse == "200"){
				
				
				
				window.location.replace('https://' + hostname + "/"+WAR_NAME+"/"
						+ 'demande.html?titre='+titre.value+
						'&'+'auteur='+auteur.value+
						'&'+'anneepub='+anneepub.value+
						'&'+'editeur='+editeur.value+
						'&'+'isbn='+isbn.value+
						'&'+'bu='+document.getElementById('buinscr').options[buinscr.selectedIndex].text+
						'&'+'nom='+lastName.value+
						'&'+'prenom='+firstName.value+
						'&'+'email='+email.value+
						'&'+'view='+etab);
						
						
			
			} else {
				$("#Envoyer").html(
						'<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>&nbsp;&nbsp;&nbsp;Envoyer la demande'
				);
				
				encours=false;
				document.getElementById('Envoyer').disabled=false;
				document.getElementById('Envoyer').style.cursor='auto';
				document.getElementById('createForm').style.cursor='auto';
				grecaptcha.reset();
				recaptchaExpired();
				
				if (reponse == "403"){
					bootbox.alert("Le captcha a expiré.")	
				}
				else{
				bootbox.alert("Erreur du service. Veuillez Réessayer plus tard ou vous rendre en bibliothèque");
				}
			}
				}
	}
	
			
			
	
	}


function testChaineCarEspace (nom,valeur,taille){
	var regex = new RegExp(/([^A-Za-zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ\-' ])/);
	var regexespace =  RegExp(/([^\s])/);
	var msg="";
	
	if (valeur.length == 0){
		  return "";   
	}
	
	
	 if (regex.test (valeur)){
		  msg=nom + " ne doit contenir que des caractères alphabétiques, le tiret ou l'apostrophe";
	  	  return msg;
	  }
	 
		if (!regexespace.test (valeur)){
			 msg=nom + " ne doit pas contenir que des espaces";
			return msg;
		
		}
	 
	  if (valeur.length > taille){
		  msg=nom + " ne doit pas dépasser " + taille + " caractères";
	  	  return msg;
	  }
	
	  return msg;
}

function testChaineAlphaNum (nom,valeur,taille,required){
	//One teste pas à cause des accents UTF8 renvoyer par la recherche par ISBN
	//	var regex = new RegExp(/([^A-Za-zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ0-9\- ,'])/);
	var regexespace =  RegExp(/([^\s])/);
	var msg="";
	
	
	  
	if (valeur.length == 0){
		  return "";   
	}
	
	if (!regexespace.test (valeur)){
		 msg=nom + " ne doit pas contenir que des espaces";
		return msg;
	
	}
	/* 
	if (regex.test (valeur)){
		  msg=nom + " ne doit contenir que des caractères alphanumériques, le tiret, la virgule, l'apostrophe ou l'espace";
	  	  return msg;
	  }
	*/
	  
	  if (valeur.length > taille){
		  msg=nom + " ne doit pas dépasser " + taille + " caractères";
	  	  return msg;
	  }
	  return msg;
}

function testChaineNum (nom,valeur,taille){
	var regex = new RegExp(/([^0-9])/);
	var msg="";
	
//	alert(valeur);
	
	if (valeur.length == 0){
		  return "";   
	}
	
	
	if (regex.test (valeur)){
		  msg=nom + " ne doit contenir que des chiffres";
	  	  return msg;
	  }
	  if (valeur.length != taille){
		  msg=nom + " doit être composé de " + taille + " chiffres";
	  	  return msg;
	  }
	  return msg;
}


function testChaineISBN (nom,valeur){
	//C-CCCC-CCCC-C
	//var regexISBN10 = new RegExp(/([0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9])/);
	
	var regexISBN = new RegExp(/([^0-9\-])/);
	var regexISBN2 = new RegExp(/([^0-9\-xX])/);
	//CCC-C-CC-CCCCCC-C
	//var regexISBN13 = new RegExp(/([0-9][0-9][0-9]-[0-9]-[0-9][0-9]-[0-9][0-9][0-9][0-9][0-9][0-9]-[0-9])/);
	
	var msg="";
	
//	alert(valeur);

	document.getElementById("noticetrouve").innerHTML="";
	//document.getElementById("image").setAttribute("src","");
	
	if (valeur.length == 0){
		  return "";   
	}
	
	
	
	  if (valeur.length > 17 || valeur.length < 10 || regexISBN.test(valeur.substring(0,valeur.length-1)) || regexISBN2.test(valeur))  {
		  msg="<small class=\"text-body\">Le code ISBN doit contenir soit <ul><li><strong>10 chiffres</strong> (tirets optionnels) pour le format <strong>ISBN-10</strong> (Exemple: 2-7654-1005-4 ),</li> <li><strong>13 chiffres</strong> (tirets optionnels) pour le format <strong>ISBN-13</strong> (Exemple: 978-2-02-130452-7).</li></ul> </small>";
	  	  
	  }else {
		  
		  getNotice(valeur);
		  
	  }
	  
	
	  return msg;
}

function getNotice(isbn){
	
	//document.getElementById("noticetrouve").innerHTML="";
	// Remplacer / par tirets dans date
	var url = 'https://'+hostname+'/'+WAR_NAME+'/rest/suggestionachat/getNotice/'+isbn;
	
	xmlhttpISBN.open('GET', url, true);
	xmlhttpISBN.send(null);

	
	xmlhttpISBN.onreadystatechange = function() {
		if (xmlhttpISBN.readyState == 4){
			
			if(xmlhttpISBN.status == 200) {
				var res=xmlhttpISBN.responseXML.getElementsByTagName("titre")[0].childNodes[0].nodeValue;
				titre.value = res;
				
				res=xmlhttpISBN.responseXML.getElementsByTagName("auteur")[0].childNodes[0].nodeValue;
				auteur.value=res;	
			
				res=xmlhttpISBN.responseXML.getElementsByTagName("date")[0].childNodes[0].nodeValue;
				anneepub.value=res;	
			
				res=xmlhttpISBN.responseXML.getElementsByTagName("editeur")[0].childNodes[0].nodeValue;
				editeur.value=res;	
			
				res=xmlhttpISBN.responseXML.getElementsByTagName("library")[0].childNodes[0].nodeValue;
				
				
				if (res != "INCONNU"){
				document.getElementById("noticetrouve").innerHTML = "Cet ouvrage est disponible à "+ res+".";
				document.getElementById("noticetrouve").style.color="green";
				} else {
					document.getElementById("noticetrouve").innerHTML = "Cet ouvrage n'est pas disponible dans les bibliothèques du réseau.";
					document.getElementById("noticetrouve").style.color="red";
				}
					
			}else {
				//document.getElementById("image").style.display="none";
				document.getElementById("noticetrouve").innerHTML = "Cet ouvrage n'est pas disponible dans les bibliothèques du réseau.";
				 document.getElementById("noticetrouve").style.color="red";
			
			}
		}
	}
}

function testChaineMail (nom,valeur,taille){
	var regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	
	var msg="";
	//alert(valeur);
	
	
	if (valeur.length == 0){
		  return "";   
	}
	
	
	if (!regex.test (valeur)){
		  msg=valeur + " n'est pas une adresse email valide";
	  	  return msg;
	  }
	  
	  return msg;
}

function testChaineMail2 (){
	
if (document.getElementById('email').value.length > 0 && document.getElementById('email2').value.length >0 && document.getElementById('email').value != document.getElementById('email2').value)
	return "Les adresses email sont différentes";
	else
		return "";
		
}

function carteselect(){
	
	unr.value="";
	unr.placeholder="Recherchez..";
	
	if(carte.checked == true){
		
		
		
		unr.disabled=false;
		
		lastName.disabled=true;
		lastName.value="";
		lastName.placeholder="INCONNU";
		
		firstName.disabled=true;
		firstName.value="";
		firstName.placeholder="INCONNU";
		
		
		email.disabled=true;
		email.value="";
		email.placeholder="INCONNU";
		
		
		email2.disabled=true;
		email2.value="";
		email2.placeholder="INCONNU";
		
		
	}else{
		button_search.disabled=true;
		
		unr.disabled=true;
		
		
		lastName.disabled=false;
		lastName.value="";
		lastName.placeholder="Entrez votre nom";
		
		
		firstName.disabled=false;
		firstName.value="";
		firstName.placeholder="Entrez votre prénom";
		
		email.disabled=false;
		email.value="";
		email.placeholder="Entrez votre email";
		
		email2.disabled=false;
		email2.value="";
		email2.placeholder="Entrez une deuxième fois votre e-mail";
		
		
	}
}


function maj(){
	
	if (unr.value==""){
		button_search.disabled=true;
		}
	else
		button_search.disabled=false;
		
	lastName.value="";
	firstName.value="";
	email.value="";
	email2.value="";
	
}


function rechercherUNR(){
	
	// Remplacer / par tirets dans date

	var m="";
	
	if(unr.value.startsWith("299")){
	m=testChaineNum("Le numéro de carte",unr.value,12);
	}else{
		m=testChaineNum("Le numéro UNR",unr.value,13);
	}
	
	
	if (m==""){
	var url = 'https://'+hostname+'/'+WAR_NAME+'/rest/suggestionachat/recherche_unr_alma/'+unr.value;
	
	document.getElementById('createForm').style.cursor='wait';
	document.getElementById('button_search').disabled=true;
	document.getElementById('button_search').style.cursor='wait';
	
	
	xmlhttpUNR.open('GET', url, true);
	xmlhttpUNR.send(null);

	
	xmlhttpUNR.onreadystatechange = function() {
		if (xmlhttpUNR.readyState == 4){
			document.getElementById('button_search').disabled=false;
			document.getElementById('button_search').style.cursor='auto';
			document.getElementById('createForm').style.cursor='auto';
	
			
			if(xmlhttpUNR.status == 200) {
				lastName.value=xmlhttpUNR.responseXML.getElementsByTagName("last_name")[0].childNodes[0].nodeValue;
				firstName.value=xmlhttpUNR.responseXML.getElementsByTagName("first_name")[0].childNodes[0].nodeValue;
				email.value=xmlhttpUNR.responseXML.getElementsByTagName("email_address")[0].childNodes[0].nodeValue;
				email2.value=xmlhttpUNR.responseXML.getElementsByTagName("email_address")[0].childNodes[0].nodeValue;
			} else if (xmlhttpUNR.status == 404){
				lastName.value="";
				firstName.value="";
				email.value="";
				email2.value="";
				
				lastName.placeholder="INCONNU";
				firstName.placeholder="INCONNU";
				email.placeholder="INCONNU";
				email2.placeholder="INCONNU";
				bootbox.alert('<i class="fa fa-exclamation-circle" style="font-size:48px;color:red"></i> Ce numéro ne correspond à aucun dossier usager.')
				
			}else{
				bootbox.alert("Erreur du service. Veuillez Réessayer plus tard ou vous rendre en bibliothèque.");
			}
			
		
		
		
		
		document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
	    document.getElementById("aideVerifChampObligatoires").style.color="red";
		
	}
		
}			
	
	}else {
		m="Si vous avez une carte multiservices le numéro de carte est votre numéro UNR sur 13 chiffres, si vous avec une carte lecteur c'est un code à 12 chiffres commençant par 299.";
		bootbox.alert(m);
	}
	
}


function verifISBN(){

var msg= testChaineISBN("ISBN",document.getElementById("isbn").value); 

document.getElementById("aideIsbn").innerHTML = msg;
document.getElementById("aideIsbn").style.color="red";

if(msg == "")
 OKisbn=true;
else
 OKisbn=false;
}	



function verifAnneePub(){

var msg= testChaineNum("Année de publication",document.getElementById("anneepub").value,4);
document.getElementById("aideAnneePub").textContent = msg;
document.getElementById("aideAnneePub").style.color="red";

if(msg == "")
OKanneePub=true;
else
OKanneePub=false;
}

function verifAuteur(){

var msg= testChaineAlphaNum("Auteur",document.getElementById("auteur").value,80);
document.getElementById("aideAuteur").textContent = msg;
document.getElementById("aideAuteur").style.color="red";
if(msg == ""){
OKauteur=true;

}
else
OKauteur=false;

}



function verifEditeur(){

var msg= testChaineAlphaNum("Editeur",document.getElementById("editeur").value,40);
document.getElementById("aideEditeur").textContent = msg;
document.getElementById("aideEditeur").style.color="red";

if(msg == "")
OKediteur=true;
else
OKediteur=false;
}



function verifRemarque(){

var msg=testChaineAlphaNum("Remarque",document.getElementById("remarque").value,150);
document.getElementById("aideRemarque").textContent = msg;
document.getElementById("aideRemarque").style.color="red";
if(titre.value.length >0 && msg == ""){
OKremarque=true;

}
else
OKremarque=false;

}


function verifTitre(){

var msg=testChaineAlphaNum("Titre",document.getElementById("titre").value,100);
document.getElementById("aideTitre").textContent = msg;
document.getElementById("aideTitre").style.color="red";
if(titre.value.length >0 && msg == ""){
OKtitre=true;

}
else
OKtitre=false;

}

function verifLastName(){
var msg="";
// ON NE FAIT PAS DE VERIF A CAUSE DES ACCENTS RECUPERES D 'ALMA
if (carte.checked == false){
msg= testChaineCarEspace("Nom",document.getElementById("lastName").value,40);
document.getElementById("aideLastName").textContent = msg;
document.getElementById("aideLastName").style.color="red";

}

if(lastName.value.length >0 && msg == ""){
OKlastName=true;

}
else
OKlastName=false;

}


function verifFirstName(){
var msg="";

if (carte.checked == false){
msg=  testChaineCarEspace("Prénom",document.getElementById("firstName").value,40);
document.getElementById("aideFirstName").textContent = msg;
document.getElementById("aideFirstName").style.color="red";
}

if(firstName.value.length >0 && msg == ""){
OKfirstName=true;

}
else
OKfirstName=false;
}

function verifEmail(){
var msg= testChaineMail("Email",document.getElementById("email").value,40);

document.getElementById("aideEmail").textContent = msg;
document.getElementById("aideEmail").style.color="red";

if(email.value.length >0 && msg == "")
OKemail=true;
else
OKemail=false;


}

function verifEmail2(){
var msg= testChaineMail2();

document.getElementById("aideEmail2").textContent = msg;
document.getElementById("aideEmail2").style.color="red";

if(email2.value.length >0 && msg == "")
OKemail2=true;
else
OKemail2=false;


}



var correctCaptcha = function(response) {
captchaResponse=response;

document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
document.getElementById("aideVerifChampObligatoires").style.color="red";
};

function recaptchaExpired(){

captchaResponse="KO";
document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
document.getElementById("aideVerifChampObligatoires").style.color="red";
}

function verifChampObligatoires (){

if (encours==true)
return "";

verifTitre();
verifAuteur();
verifEditeur();
verifAnneePub();
//Verifier lorsqu'on modifie ce champ pour ne pas aller chercher la notice inutilement
//verifISBN();
verifRemarque();

verifLastName();
verifFirstName();
verifEmail();
verifEmail2();

var   msg="";

if (!OKlastName || !OKfirstName || !OKemail || !OKemail2 || !OKtitre)
{

msg="Les champs marqués * sont obligatoires.";
}
else if (!OKanneePub || !OKediteur || !OKisbn || !OKauteur || !OKremarque){

msg="Des champs optionnels sont erronés.";


}
else if (captchaResponse == "KO"){
msg="Veuillez Cocher la case : Je ne suis pas un robot.";

}
else {

msg="";

}


if (msg == "")
document.getElementById("Envoyer").disabled = false;
else
document.getElementById("Envoyer").disabled = true;
return msg;

}

function addControlListener(){
	/* click */
	document.addEventListener("change", function (e) {
		  document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
	    document.getElementById("aideVerifChampObligatoires").style.color="red";
	    });

	document.addEventListener("input", function (e) {
		  document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
	    document.getElementById("aideVerifChampObligatoires").style.color="red";
	    });

	
	
}

