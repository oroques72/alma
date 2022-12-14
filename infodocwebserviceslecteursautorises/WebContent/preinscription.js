var hostname;
var xmlhttp;

var captchaResponse="KO";

var OKfirstName=false;
var OKlastName =false;
var OKbirthDate =false;
var OKemail =false;
var OKemail2 =false;
var OKpasswd =false;
var OKpasswd2 =false;
var OKmobile=false;
var OKaddress1 =false;
var OKaddress2 =true;
var OKaddress3 =true;
var OKaddress4 =true;
var OKaddress5 =true;
var OKpostalCode =false;
var OKcity =false;
var encours=false;
const buTohtmlpage_map = new Map(Object.entries(BU_HTML_MAP));
const affiliation_etu_map = new Map(Object.entries(AFFILIATION_ETU_OPTION));
const affiliation_perso_map = new Map(Object.entries(AFFILIATION_PERSO_OPTION));
const businscr_map= new Map(Object.entries(BU_INSCRIPTION_OPTION));
const duree_map=new Map(Object.entries(DUREE_INSCRIPTION_OPTION));

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
	
	$('#ID_PANEL_HEADING').html(TEXT_ID_PANEL_HEADING);
	$('#ID_ALERT').html('<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'+TEXT_ID_ALERT);
	
	
	var optgroup = "<optgroup label='" + AFFILIATION_LABEL_ETU_OPTGROUP + "'>";
	affiliation_etu_map.forEach((value,key) => {
		optgroup += "<option value='" + key + "'>" + value + "</option>";
	});
	
	 optgroup += "</optgroup>";
	$('#affiliation').append(optgroup);
	
	
	 affiliation_perso_map.forEach((value,key) => {
		 $('#affiliation').append("<option value='" + key + "'>" + value + "</option>");
		});
	 
	
	 businscr_map.forEach((value,key) => {
		 $('#buinscr').append("<option value='" + key + "'>" + value + "</option>");
		});
	
	 duree_map.forEach((value,key) => {
		
		 $('#duree').append("<option value='" + key + "'>" + value + "</option>");
		 
	 });
	
	 
	 
	 
	
	addControlListener();
	
	xmlhttp = new XMLHttpRequest();
	encours=false;
	document.getElementById("Envoyer").disabled = true;
	hostname=location.hostname;
	document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
    document.getElementById("aideVerifChampObligatoires").style.color="red";
	
    
}





function recaptchaExpired() {
	captchaResponse = "KO";
	document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
	document.getElementById("aideVerifChampObligatoires").style.color = "red";

}

function ecrireFichierUserLa() {

	$("#Envoyer")
			.html(
					'<i class="fa fa-circle-o-notch fa-spin"></i>&nbsp;&nbsp;&nbsp;Envoyer la demande');

	encours = true;
	document.getElementById('Envoyer').style.cursor = 'wait';
	document.getElementById('createForm').style.cursor = 'wait';
	document.getElementById('Envoyer').disabled = true;

	var url = "https://" + hostname + "/"+WAR_NAME+"/rest/usagerla/import/"
			+ captchaResponse;
	var primaryId = email.value;

	// alert("ici2");
	xmlhttp.open('POST', url, true);
	xmlhttp.setRequestHeader("Content-type", "text/plain");

	xmlhttp.send(lastName.value + ":" + firstName.value + ":" + birthDate.value
			+ ":" + "genderpasutil" + ":" + email.value + ":" + "phonepasutil"
			+ ":" + mobile.value + ":" + address1.value + ":" + address2.value
			+ ":" + address3.value + ":" + address4.value + ":"
			+ address5.value + ":" + postalCode.value + ":" + city.value + ":"
			+ country.value + ":" + affiliation.value + ":" + passwd.value
			+ ":" + primaryId + ":" + duree.value + ":" + buinscr.value);

	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4) {
			var userLaResponse = xmlhttp.responseText;

			// alert(xmlhttp.responseText);
			if (userLaResponse == "200") {
				window.location.replace('https://' + hostname + "/"+WAR_NAME+"/"
						+ '/demande.html?email=' + email.value + '&'
						+ 'id=' + primaryId);

			} else {
				$("#Envoyer")
						.html(
								'<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>&nbsp;&nbsp;&nbsp;Envoyer la demande');
				encours = false;
				document.getElementById('Envoyer').disabled = false;
				document.getElementById('Envoyer').style.cursor = 'auto';
				document.getElementById('createForm').style.cursor = 'auto';
				grecaptcha.reset();
				recaptchaExpired();

				if (userLaResponse == "401") {
					bootbox.alert("L'usager existe déja.");
				} else if (userLaResponse == "402") {
					bootbox
							.alert("Le mot de passe ne doit pas contenir de nom commun.");
				} else if (userLaResponse == "403") {
					bootbox.alert("Le captcha a expiré.")
				} else {
					bootbox
							.alert("Erreur du service. Veuillez Réessayer plus tard ou vous rendre en bibliothèque");
				}
			}
		}
	}
}

function testChaineCarEspace(nom, valeur, taille) {
	var regex = new RegExp(/([^A-Za-zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ\-' ])/);
	var regexespace = RegExp(/([^\s])/);
	var msg = "";

	if (valeur.length == 0) {
		return "";
	}

	if (regex.test(valeur)) {
		msg = nom
				+ " ne doit contenir que des caractères alphabétiques, le tiret ou l'apostrophe";
		return msg;
	}

	if (!regexespace.test(valeur)) {
		msg = nom + " ne doit pas contenir que des espaces";
		return msg;

	}

	if (valeur.length > taille) {
		msg = nom + " ne doit pas dépasser " + taille + " caractères";
		return msg;
	}

	return msg;
}

function testChaineAlphaNum(nom, valeur, taille, required) {
	var regex = new RegExp(/([^A-Za-zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ0-9\- ,'])/);
	var regexespace = RegExp(/([^\s])/);
	var msg = "";

	if (valeur.length == 0) {
		return "";
	}

	if (!regexespace.test(valeur)) {
		msg = nom + " ne doit pas contenir que des espaces";
		return msg;

	}
	if (regex.test(valeur)) {
		msg = nom
				+ " ne doit contenir que des caractères alphanumériques, le tiret, la virgule, l'apostrophe ou l'espace";
		return msg;
	}

	if (valeur.length > taille) {
		msg = nom + " ne doit pas dépasser " + taille + " caractères";
		return msg;
	}
	return msg;
}

function testChaineNum(nom, valeur, taille) {
	var regex = new RegExp(/([^0-9])/);
	var msg = "";

	// alert(valeur);

	if (valeur.length == 0) {
		return "";
	}

	if (regex.test(valeur)) {
		msg = nom + " ne doit contenir que des chiffres";
		return msg;
	}

	if (valeur.length > taille) {
		msg = nom + " doit être composé de " + taille + " chiffres au maximum";
		return msg;
	}
	return msg;
}

function testChaineMail(nom, valeur, taille) {
	// var regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	var regex = /\S+@\S+\.\S+/;
	var msg = "";
	// alert(valeur);

	if (valeur.length == 0) {
		return "";
	}

	if (!regex.test(valeur)) {
		msg = valeur + " n'est pas une adresse email valide";
		return msg;
	}

	return msg;
}

function testChaineMail2() {

	if (document.getElementById('email').value.length > 0
			&& document.getElementById('email2').value.length > 0
			&& document.getElementById('email').value != document
					.getElementById('email2').value)
		return "Les adresses email sont différentes";
	else
		return "";

}
function testChainePassword2() {

	if (document.getElementById('passwd').value.length > 0
			&& document.getElementById('passwd2').value.length > 0
			&& document.getElementById('passwd').value != document
					.getElementById('passwd2').value) {

		return "Les mots de passes sont différents";
	} else
		return "";
}
function testChainePassword(nom, valeur, taille) {
	var regexMaj = new RegExp(/([A-Z])/);
	var regexMin = new RegExp(/([a-z])/);
	var regexChiffre = new RegExp(/([0-9])/);
	var regexCarspe = new RegExp(/([\\\-,;.!?#$*@])/);
	var regexdeuxpoints = new RegExp(/([:<>])/);
	var msg = "";

	// Si le mot de passe est vide il y a aura un message au niveau de la verif
	// des champs requis mais pas du password

	if (valeur.length == 0) {
		return "";

	}
	var erreurMsg = " doit contenir au moins 8 caractères non accentués dont une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial \-,;.!?#$*@";

	if (valeur.length < taille) {
		msg = nom + " doit contenir au moins " + taille + " caractères";
		return msg;
	}

	if (!regexMaj.test(valeur)) {
		msg = nom + erreurMsg;
		return msg;
	}

	if (!regexMin.test(valeur)) {
		msg = nom + erreurMsg;
		return msg;
	}
	if (!regexChiffre.test(valeur)) {
		msg = nom + erreurMsg;
		return msg;
	}
	if (!regexCarspe.test(valeur)) {
		msg = nom + erreurMsg;
		return msg;
	}

	if (regexdeuxpoints.test(valeur)) {
		msg = "Les caractères : < >  ne sont pas autorisés";
		return msg;
	}

	if (lastName.value.length > 0) {

		regex = new RegExp(lastName.value);
		if (regex.test(valeur)) {
			msg = nom + " ne doit pas contenir le nom";
			return msg;

		}
	}

	if (firstName.value.length > 0) {

		regex = new RegExp(firstName.value);

		if (regex.test(valeur)) {
			msg = nom + " ne doit pas contenir le prénom";
			return msg;

		}
	}
	return msg;
}

function verifierDateNaissance() {
	var ok = false;
	var msg = "";

	if (birthDate.value.length > 0
			&& !birthDate.value.match(/^(\d{1,2})\/(\d{1,2})\/(\d{4})$/)) {
		msg = "Date invalide : le format attendu est JJ/MM/AAAA.";
	} else if (birthDate.value.length > 0 && birthDate.value.length != 10) {

		msg = "Date invalide : le format attendu est JJ/MM/AAAA.";
	} else if (birthDate.value.length == 10) {

		var sdate1 = birthDate.value;

		var jour = sdate1.substr(0, 2);
		var mois = sdate1.substr(3, 2);
		var annee = sdate1.substr(6, 4);

		var date1 = new Date(annee + "-" + mois + "-" + jour);

		var d1 = date1.getTime();
		var dd = parseInt(sdate1.substr(0, 2), 10);
		var mm = parseInt(sdate1.substr(3, 2), 10);
		var yy = parseInt(sdate1.substr(6, 4), 10);

		if (isNaN(dd) || isNaN(mm) || isNaN(yy)) {
			msg = "Date invalide : le format attendu est JJ/MM/AAAA.";
		} else if (isNaN(d1)) {
			msg = "Date invalide : le format attendu est JJ/MM/AAAA.";
		} else if (parseInt(annee, 10) < 1900) {
			msg = "Votre date de naissance doit être postérieure à 1900.";
		} else if (mm == 2 && dd > 29) {
			msg = "Date invalide : le format attendu est JJ/MM/AAAA.";
		} else {
			var dateCourante = new Date();
			var d2 = dateCourante.getTime();
			if (d1 >= d2) {
				msg = "Votre date de naissance doit être antérieure à la date courante.";
			} else {
				ok = true;
			}
		}

	}

	return msg;
}



function verifLastName(){
	
	var msg= testChaineCarEspace("Nom",document.getElementById("lastName").value,40);
	 document.getElementById("aideLastName").textContent = msg;
	 document.getElementById("aideLastName").style.color="red";
	if(lastName.value.length >0 && msg == ""){
		OKlastName=true;
		
	}
	else
		OKlastName=false;
	
}

function verifFirstName(){
	
	var msg=  testChaineCarEspace("Prénom",document.getElementById("firstName").value,40);
    document.getElementById("aideFirstName").textContent = msg;
    document.getElementById("aideFirstName").style.color="red";
    
    if(firstName.value.length >0 && msg == ""){
    	OKfirstName=true;
    
    }
    else
    	OKfirstName=false;
}


// Contrôle de la date de naissance en fin de saisie

function verifBirthDate(){
	var msg= verifierDateNaissance();
	   
    document.getElementById("aideBirthdate").textContent = msg;
    document.getElementById("aideBirthdate").style.color="red";
    
    if(birthDate.value.length >0 && msg == "")
    	OKbirthDate=true;
    else
    	OKbirthDate=false;
	
}

function verifPassword(){
	var msg=  testChainePassword("Mot de passe",document.getElementById("passwd").value,8);
	   
    document.getElementById("aidePassword").textContent =msg;
    document.getElementById("aidePassword").style.color="red";
    
    
    if(passwd.value.length >0 && msg == "")
    	OKpasswd=true;
    else
    	OKpasswd=false;
	
	
}		

function verifPassword2(){
	
	var msg= testChainePassword2();
    document.getElementById("aidePassword2").textContent = msg;
    document.getElementById("aidePassword2").style.color="red";
    
    
    if(passwd2.value.length >0 && msg == "")
    	OKpasswd2=true;
    else
    	OKpasswd2=false;
}

function verifEmail(){
	var msg= testChaineMail("Email",document.getElementById("email").value,70);
	   
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

function verifMobile(){
	
	var msg=  testChaineNum("Télephone portable",document.getElementById("mobile").value,14);
	   
    document.getElementById("aideMobile").textContent =msg;
    document.getElementById("aideMobile").style.color="red";
   

    // pas obligatoire donc on teste pas la taille
    if(msg == "")
    	OKmobile=true;
    else
    	OKmobile=false;
	
}

function verifAddress1(){
	var msg= testChaineAlphaNum("Adresse 1",document.getElementById("address1").value,40,true);
    document.getElementById("aideAddress1").textContent = msg;
    document.getElementById("aideAddress1").style.color="red";
    
    if(address1.value.length >0 && msg == "")
    	OKaddress1=true;
    else
    	OKaddress1=false;
	
	
}


function verifAddress2(){
	var msg= testChaineAlphaNum("Adresse 2",document.getElementById("address2").value,40,false);
	   
    document.getElementById("aideAddress2").textContent = msg;
    document.getElementById("aideAddress2").style.color="red";
    
  // pas obligatoire donc on teste pas la taille
    if(msg == "")
    	OKaddress2=true;
    else
    	OKaddress2=false;
}


function verifAddress3(){
	
	var msg= testChaineAlphaNum("Adresse 3",document.getElementById("address3").value,40,false);
	   
    document.getElementById("aideAddress3").textContent = msg;
    document.getElementById("aideAddress3").style.color="red";
    
    
    if(msg == "")
    	OKaddress3=true;
    else
    	OKaddress3=false;
	
}

function verifAddress4(){
	var msg= testChaineAlphaNum("Adresse 4",document.getElementById("address4").value,40,false);
    document.getElementById("aideAddress4").textContent = msg;
    document.getElementById("aideAddress4").style.color="red";
    
    if(msg == "")
    	OKaddress4=true;
    else
    	OKaddress4=false;
	
}


function verifAddress5(){
	
	var msg=  testChaineAlphaNum("Adresse 5",document.getElementById("address5").value,40,false);
	   
    document.getElementById("aideAddress5").textContent = msg;
    document.getElementById("aideAddress5").style.color="red";
    
    if(msg == "")
    	OKaddress5=true;
    else
    	OKaddress5=false;
}

function verifPostalCode(){
	
	var msg= testChaineNum("Code postal",document.getElementById("postalCode").value,5);
    document.getElementById("aidePostalCode").textContent = msg;
    document.getElementById("aidePostalCode").style.color="red";
    
    if(postalCode.value.length >0 && msg == "")
    	OKpostalCode=true;
    else
    	OKpostalCode=false;
	
}

function verifCity(){
	
	var msg= testChaineCarEspace("Ville",document.getElementById("city").value,20);
	   
    document.getElementById("aideCity").textContent = msg;
    document.getElementById("aideCity").style.color="red";
    
    if(city.value.length >0 && msg == "")
    	OKcity=true;
    else
    	OKcity=false;
    
	
}




function verifChampObligatoires (){
	
	if (encours==true)
		return "";
	
	verifLastName();
	verifFirstName();
	verifBirthDate();
	verifPassword();
	verifPassword2();
	verifEmail();
	verifEmail2();
	verifAddress1();
	verifAddress2();
	verifAddress3();
	verifAddress4();
	verifAddress5();
	verifMobile();
	verifCity();
	verifPostalCode();
    
	 var   msg="";
 
	
	if (!OKlastName || !OKfirstName || !OKbirthDate || !OKemail || !OKemail2 || !OKpasswd || !OKpasswd2 || !OKaddress1  || !OKpostalCode || !OKcity || affiliation.value == "" || buinscr.value == "")
		{
		
		msg="Les champs marqués * sont obligatoires.";
	}
	else if (!OKaddress2 || !OKaddress3 || !OKaddress4 || !OKaddress5 || !OKmobile){

		msg="Des champs d'adresses optionnels sont erronés.";
		
	}else if (document.getElementById('accept').checked == false){
	msg="Veuillez accepter les conditions générales.";
	}else if (captchaResponse == "KO"){
		 msg="Veuillez Cocher la case : Je ne suis pas un robot.";
			
	}
	
	
	else {
	// alert("ici");
		msg="";
	
	}

	
	if (msg == "")
		document.getElementById("Envoyer").disabled = false;
	else
		document.getElementById("Envoyer").disabled = true;
	return msg;

}


	function handleSelect(elm)
{
	if(elm.value != ""){
		
		document.getElementById("bulien").innerHTML='Vous pouvez consulter les tarifs de la bibliothèque en cliquant ' + '<a href=\"javascript: void(0);\" onclick=\"javascript:window.open(\''+buTohtmlpage_map.get(buinscr.value)+'\',\'\',\'\');\">ici</a>';
		document.getElementById("bulien").style.color="blue";
	}
	else{
		document.getElementById("bulien").innerHTML='';
	}
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




