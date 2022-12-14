var hostname;
var xmlhttp;
var xmlhttpUNR;
var xmlhttpISBN;

var captchaResponse = "KO";
var OKfirstName = false;
var OKlastName = false;
var OKemail = false;

var encours = false;

var correctCaptcha = function(response) {
	captchaResponse = response;

	document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
	document.getElementById("aideVerifChampObligatoires").style.color = "red";
};

function recaptchaExpired() {

	captchaResponse = "KO";
	document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
	document.getElementById("aideVerifChampObligatoires").style.color = "red";
}


function onloadCallback() {
	grecaptcha.render('google-recaptcha', {
		'sitekey' : GOOGLE_CAPTCHA_KEY, // required
		'expired-callback' : 'recaptchaExpired', // optional
		'callback' : 'correctCaptcha' // optional
	});
}

function refreshPage() {

	if (navigator.cookieEnabled == true) {

		console.log("cookies tiers autorises");
		// Rafraichier une fois la page
		if (localStorage.getItem("isRefreshQuitus") != "refresh") {
			localStorage.setItem("isRefreshQuitus", "refresh");
			window.location.reload();
		}
	} else {
		console.log("cookies tiers bloques");
	}
}

function init() {
	refreshPage();

	xmlhttp = new XMLHttpRequest();
	xmlhttpUNR = new XMLHttpRequest();
	xmlhttpISBN = new XMLHttpRequest();

	encours = false;
	hostname = location.hostname;
	document.getElementById("Envoyer").disabled = true;
	
	$('#ID_PANEL_HEADING').html(TEXT_ID_PANEL_HEADING);

	// boutton disabled car unr vide
	button_search.disabled = true;
	unr.value = "";
	unr.placeholder = "Recherchez..";

	lastName.disabled = true;
	lastName.value = "";
	lastName.placeholder = "INCONNU";

	firstName.disabled = true;
	firstName.value = "";
	firstName.placeholder = "INCONNU";

	email.disabled = true;
	email.value = "";
	email.placeholder = "INCONNU";

	// document.getElementById("image").style.display="none";

	document.getElementById("aideVerifChampObligatoires").textContent = "Les champs marqués * sont obligatoires.";
	document.getElementById("aideVerifChampObligatoires").style.color = "red";

	addControlListener();
}

function getParameterByName(p) {
	var str = document.location.href;
	var url = new URL(str);
	var name = url.searchParams.get(p);
	return name;
}

function envoyer() {

	$("#Envoyer")
			.html(
					'<i class="fa fa-circle-o-notch fa-spin"></i>&nbsp;&nbsp;&nbsp;Envoyer la demande');

	encours = true;
	document.getElementById('Envoyer').style.cursor = 'wait';
	document.getElementById('createForm').style.cursor = 'wait';
	document.getElementById('Envoyer').disabled = true;

	var url = 'https://' + hostname + '/' + WAR_NAME +'/rest/quitus/demandequitus/'
			+ captchaResponse;

	xmlhttp.open('POST', url, true);
	xmlhttp.setRequestHeader("Content-type", "text/plain");

	xmlhttp.send(unr.value + ":" + lastName.value + ":" + firstName.value + ":"
			+ email.value);

	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4) {

			if (xmlhttp.status == 200) {
				window.location
						.replace('https://' + hostname + '/' + WAR_NAME +'/demande.html?nom='
								+ lastName.value
								+ '&'
								+ 'prenom='
								+ firstName.value
								+ '&'
								+ 'email='
								+ email.value
								+ '&'
								+ 'filename='
								+ xmlhttp.responseText);

			} else if (xmlhttp.status == 401) {

				window.location
						.replace('https://' + hostname + '/' + WAR_NAME +'/demandeEchec.html?nom='
								+ lastName.value
								+ '&'
								+ 'prenom='
								+ firstName.value
								+ '&'
								+ 'email='
								+ email.value);

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

				if (xmlhttp.status == 403) {
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
	// One teste pas à cause des accents UTF8 renvoyer par la recherche par ISBN
	// var regex = new RegExp(/([^A-Za-zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ0-9\- ,'])/);
	var regexespace = RegExp(/([^\s])/);
	var msg = "";

	if (valeur.length == 0) {
		return "";
	}

	if (!regexespace.test(valeur)) {
		msg = nom + " ne doit pas contenir que des espaces";
		return msg;

	}
	/*
	 * if (regex.test (valeur)){ msg=nom + " ne doit contenir que des caractères
	 * alphanumériques, le tiret, la virgule, l'apostrophe ou l'espace"; return
	 * msg; }
	 */

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
	if (valeur.length != taille) {
		msg = nom + " doit être composé de " + taille + " chiffres";
		return msg;
	}
	return msg;
}

function testChaineMail(nom, valeur, taille) {
	var regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

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

function maj() {

	if (unr.value == "") {
		button_search.disabled = true;
	} else
		button_search.disabled = false;

	lastName.value = "";
	firstName.value = "";
	email.value = "";

}

function rechercherUNR() {

	// Remplacer / par tirets dans date

	var m = testChaineNum("Le numéro UNR", unr.value, 13);

	if (m == "") {

		var url = 'https://' + hostname + '/' + WAR_NAME + '/rest/quitus/recherche_unr_alma/' + unr.value;
		
		

		document.getElementById('createForm').style.cursor = 'wait';
		document.getElementById('button_search').disabled = true;
		document.getElementById('button_search').style.cursor = 'wait';

		xmlhttpUNR.open('GET', url, true);
		xmlhttpUNR.send(null);

		xmlhttpUNR.onreadystatechange = function() {
			if (xmlhttpUNR.readyState == 4) {
				document.getElementById('button_search').disabled = false;
				document.getElementById('button_search').style.cursor = 'auto';
				document.getElementById('createForm').style.cursor = 'auto';

				if (xmlhttpUNR.status == 200) {
					lastName.value = xmlhttpUNR.responseXML
							.getElementsByTagName("last_name")[0].childNodes[0].nodeValue;
					firstName.value = xmlhttpUNR.responseXML
							.getElementsByTagName("first_name")[0].childNodes[0].nodeValue;
					email.value = xmlhttpUNR.responseXML
							.getElementsByTagName("email_address")[0].childNodes[0].nodeValue;

				} else if (xmlhttpUNR.status == 404) {
					lastName.value = "";
					firstName.value = "";
					email.value = "";

					lastName.placeholder = "INCONNU";
					firstName.placeholder = "INCONNU";
					email.placeholder = "INCONNU";

					// alert("Ce numéro ne correspond à aucun dossier usager.")

					bootbox
							.alert('<i class="fa fa-exclamation-circle" style="font-size:48px;color:red"></i> Ce numéro ne correspond à aucun dossier usager.');

				} else {
					bootbox
							.alert("Erreur du service. Veuillez Réessayer plus tard ou vous rendre en bibliothèque.");
				}

				document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
				document.getElementById("aideVerifChampObligatoires").style.color = "red";

			}

		}

	} else {
		m = "Le numéro à utiliser est l'identifiant à 13 chiffres inscrit sur votre carte d'étudiant.";
		bootbox.alert(m);
	}

}

function verifLastName() {

	// ON NE FAIT PAS DE VERIF A CAUSE DES ACCENTS RECUPERES D 'ALMA

	/*
	 * var msg=
	 * testChaineCarEspace("Nom",document.getElementById("lastName").value,40);
	 * document.getElementById("aideLastName").textContent = msg;
	 * document.getElementById("aideLastName").style.color="red";
	 */
	var msg = "";

	if (lastName.value.length > 0 && msg == "") {
		OKlastName = true;

	} else
		OKlastName = false;

}

function verifFirstName() {
	// ON NE FAIT PAS DE VERIF A CAUSE DES ACCENTS RECUPERES D 'ALMA

	/*
	 * var msg=
	 * testChaineCarEspace("Prénom",document.getElementById("firstName").value,40);
	 * document.getElementById("aideFirstName").textContent = msg;
	 * document.getElementById("aideFirstName").style.color="red";
	 */
	var msg = "";

	if (firstName.value.length > 0 && msg == "") {
		OKfirstName = true;

	} else
		OKfirstName = false;
}

function verifEmail() {
	var msg = testChaineMail("Email", document.getElementById("email").value,
			40);

	document.getElementById("aideEmail").textContent = msg;
	document.getElementById("aideEmail").style.color = "red";

	if (email.value.length > 0 && msg == "")
		OKemail = true;
	else
		OKemail = false;

}



function verifChampObligatoires() {

	if (encours == true)
		return "";

	verifLastName();
	verifFirstName();
	verifEmail();

	var msg = "";

	if (!OKlastName || !OKfirstName || !OKemail) {

		msg = "Les champs marqués * sont obligatoires.";
	} else if (captchaResponse == "KO") {
		msg = "Veuillez Cocher la case : Je ne suis pas un robot.";

	} else {

		msg = "";

	}

	if (msg == "")
		document.getElementById("Envoyer").disabled = false;
	else
		document.getElementById("Envoyer").disabled = true;
	return msg;

}

function addControlListener() {
	/* click */
	document
			.addEventListener(
					"change",
					function(e) {
						document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
						document.getElementById("aideVerifChampObligatoires").style.color = "red";
					});

	document
			.addEventListener(
					"input",
					function(e) {
						document.getElementById("aideVerifChampObligatoires").textContent = verifChampObligatoires();
						document.getElementById("aideVerifChampObligatoires").style.color = "red";
					});

}
