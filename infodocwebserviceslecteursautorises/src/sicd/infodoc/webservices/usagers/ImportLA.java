package sicd.infodoc.webservices.usagers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import sicd.alma.Alma;
import sicd.common.Utils;

@Path("usagerla")
public class ImportLA {

	// valeur dans config.properties
	private static String ALMA_API_USERS_KEY = null;
	private static String ALMA_API_BASE_URL = null;
	private static String GOOGLE_SECRET_CAPTCHA = null;
	private static String URL_BU = null;

	// champs optionnels dans config.properties
	private static String SMTP_LOGIN = null;
	private static String SMTP_PASSWD = null;
	private static String SMTP_HOST = null;

	private static Alma alma = null;

	HashMap<String, String> categories = null;
	HashMap<String, String> categorie_desc = null;
	HashMap<String, String> niveaux = null;
	HashMap<String, String> campus = null;
	HashMap<String, String> bibliotheques = null;

	private static final int HTTP_CODE_BAD_REQUEST = 400;
	private static final int HTTP_CODE_ERR_CAPTCHA = 403;

	@Context
	private ServletContext context;

	public ImportLA() {
		// TODO Auto-generated constructor stub
	}

	private  void initConfig() {
		if (ALMA_API_USERS_KEY == null)
			ALMA_API_USERS_KEY = Utils.getPropertiesByKey("ALMA_API_USERS_KEY",
					context.getResourceAsStream("conf/config.properties"));

		if (ALMA_API_BASE_URL == null)
			ALMA_API_BASE_URL = Utils.getPropertiesByKey("ALMA_API_BASE_URL",
					context.getResourceAsStream("conf/config.properties"));

		if (GOOGLE_SECRET_CAPTCHA == null)
			GOOGLE_SECRET_CAPTCHA = Utils.getPropertiesByKey("GOOGLE_SECRET_CAPTCHA",
					context.getResourceAsStream("conf/config.properties"));

		if (URL_BU == null)
			URL_BU = Utils.getPropertiesByKey("URL_BU", context.getResourceAsStream("conf/config.properties"));

		if (SMTP_LOGIN == null)
			SMTP_LOGIN = Utils.getPropertiesByKey("SMTP_LOGIN", context.getResourceAsStream("conf/config.properties"));

		if (SMTP_PASSWD == null)
			SMTP_PASSWD = Utils.getPropertiesByKey("SMTP_PASSWD",
					context.getResourceAsStream("conf/config.properties"));

		if (SMTP_HOST == null)
			SMTP_HOST = Utils.getPropertiesByKey("SMTP_HOST", context.getResourceAsStream("conf/config.properties"));

		if (alma == null)
			alma = new Alma(ALMA_API_USERS_KEY, ALMA_API_BASE_URL);

		if (categories == null)
			categories = Utils.getProperties(context.getResourceAsStream("conf/affiliation_categorie.properties"));

		if (categorie_desc == null)
			categorie_desc = Utils.getProperties(context.getResourceAsStream("conf/categorie_description.properties"));

		if (niveaux == null)
			niveaux = Utils.getProperties(context.getResourceAsStream("conf/affiliation_etu_niveau.properties"));

		if (campus == null)
			campus = Utils.getProperties(context.getResourceAsStream("conf/campus.properties"));

		if (bibliotheques == null)
			bibliotheques = Utils.getProperties(context.getResourceAsStream("conf/bu.properties"));

	}

	@POST
	@Path("/import/{token}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String importLa(@PathParam("token") String token, String requestBody) throws IOException {
		initConfig();
		if (!Utils.verifierCaptcha(GOOGLE_SECRET_CAPTCHA, token))
			return HTTP_CODE_ERR_CAPTCHA + "";

		try {

			InputStreamReader reader = new InputStreamReader(context.getResourceAsStream("user_la.xml"));

			ArrayList<String> l = Utils.file2ArrayString(reader);
			String userxml = "";
			for (int i = 0; i < l.size(); i++) {
				userxml += l.get(i);
			}
			String[] tokens = requestBody.split(":"); // Parse

			if (tokens.length != 20) {
				System.out.println("Erreur de passage de paramètres");
				return HTTP_CODE_BAD_REQUEST + "";
			}

			String last_name = Utils.EnleveAccent(tokens[0]);

			String first_name = Utils.EnleveAccent(tokens[1]);

			String birth_date = tokens[2];
			String gender = tokens[3];
			String email_address = tokens[4];
			String phone = tokens[5];
			String mobile = tokens[6];

			// Obligatoire lors de l'import Alma.
			if (mobile.isEmpty()) {
				mobile = "00000000";
			}

			String line1 = Utils.EnleveAccent(tokens[7]);
			String line2 = Utils.EnleveAccent(tokens[8]);
			String line3 = Utils.EnleveAccent(tokens[9]);
			String line4 = Utils.EnleveAccent(tokens[10]);
			String line5 = Utils.EnleveAccent(tokens[11]);
			String postal_code = tokens[12];
			String city = Utils.EnleveAccent(tokens[13]);
			String country = tokens[14];
			// voir config.js AFFILIATION_xxxx
			String status = tokens[15];
			String status_desc;
			String niveau;

			String categorie = categories.get(status);

			if (categorie == null) {
				System.err.println("Pas de catégorie associée à " + status + " dans affiliation_categorie.properties");
				return HTTP_CODE_BAD_REQUEST + "";
			}

			status_desc = categorie_desc.get(categorie);

			if (status_desc == null) {
				System.err.println(
						"Pas de description associée à " + categorie + " dans categorie_description.properties");
				return HTTP_CODE_BAD_REQUEST + "";
			}

			niveau = niveaux.get(status);

			// Le niveau vaut null si le status ne correspond pas à un étudiant.

			String password = tokens[16];

			String primary_id = tokens[17];

			String duree = tokens[18];
			String duree_str = null;

			int duree_jour = 30;

			try {
				duree_jour = new Integer(duree).intValue();
			} catch (Throwable e) {
				// Nombre invalide dans DUREE_INSCRIPTION_OPTION config.js
				duree_jour = 30;
			}

			if (duree_jour == 30) {
				/* plus utilisé dans le javascript */

				duree_str = "mois";
			} else if (duree_jour == 90) {

				duree_str = "trimestre";
			} else if (duree_jour == 180) {

				duree_str = "semestre";
			} else if (duree_jour == 365) {

				duree_str = "année";
			} else {
				duree_str = duree_jour + " jours";
			}

			String buinscr = tokens[19];

			String purge_date = "";
			String expiry_date = "";

			String campus_code = campus.get(buinscr);
			String budescr = bibliotheques.get(buinscr);

			String month = (Calendar.getInstance().get(Calendar.MONTH) + 1) + "";
			if (month.length() == 1)
				month = "0" + month;

			String day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "";
			if (day.length() == 1)
				day = "0" + day;

			String current_date = day + "/" + month + "/" + Calendar.getInstance().get(Calendar.YEAR);

			XMLGregorianCalendar expiry_date_xml = null;
			try {
				expiry_date_xml = Utils.getXMLGregorianDate(current_date, duree_jour);
				// purge_date=purge_date_xml.getYear()+"-"+purge_date_xml.getMonth()+"-"+purge_date_xml.getDay()+"Z";
				expiry_date = expiry_date_xml.toString().substring(0, 10) + "Z";

			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			XMLGregorianCalendar purge_date_xml = null;
			try {
				purge_date_xml = Utils.getXMLGregorianDate(current_date, 90 + duree_jour);
				// purge_date=purge_date_xml.getYear()+"-"+purge_date_xml.getMonth()+"-"+purge_date_xml.getDay()+"Z";
				purge_date = purge_date_xml.toString().substring(0, 10) + "Z";

			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			XMLGregorianCalendar birth_date_xml = null;
			try {
				birth_date_xml = Utils.getXMLGregorianDate(birth_date, 0);
				// purge_date=purge_date_xml.getYear()+"-"+purge_date_xml.getMonth()+"-"+purge_date_xml.getDay()+"Z";
				birth_date = birth_date_xml.toString().substring(0, 10) + "Z";

			} catch (DatatypeConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String nouveauxml = userxml.replace("_primary_id", primary_id);
			nouveauxml = nouveauxml.replace("_first_name", first_name);
			nouveauxml = nouveauxml.replace("_last_name", last_name);

			nouveauxml = nouveauxml.replace("_birth_date", birth_date);
			nouveauxml = nouveauxml.replace("_expiry_date", expiry_date);
			nouveauxml = nouveauxml.replace("_purge_date", purge_date);
			nouveauxml = nouveauxml.replace("_campus", campus_code);
			nouveauxml = nouveauxml.replace("_line1", line1);
			nouveauxml = nouveauxml.replace("_line2", line2);
			nouveauxml = nouveauxml.replace("_line3", line3);
			nouveauxml = nouveauxml.replace("_line4", line4);
			nouveauxml = nouveauxml.replace("_line5", line5);
			nouveauxml = nouveauxml.replace("_country", country);
			nouveauxml = nouveauxml.replace("_city", city);
			nouveauxml = nouveauxml.replace("_postal_code", postal_code);
			nouveauxml = nouveauxml.replace("_email_address", email_address);
			nouveauxml = nouveauxml.replace("_mobile_phone_number", mobile);
			nouveauxml = nouveauxml.replace("_pwd", password);
			nouveauxml = nouveauxml.replace("_note_ins",
					"Préinscription en ligne: Inscription à finaliser. \n Bibliothèque d'inscription: " + budescr + "("
							+ buinscr + ")" + "\n Catégorie: " + status_desc + "." + "\n Validité: " + duree_str + ".");

			if (niveau != null && !niveau.isEmpty()) {
				nouveauxml = nouveauxml.replaceAll("user_statistic_1", "user_statistic");
				nouveauxml = nouveauxml.replace("statistic_category_1", "statistic_category");
				nouveauxml = nouveauxml.replace("_niveau", niveau);
			} else {
				nouveauxml = nouveauxml.replace("<user_statistic_1>", "");
				nouveauxml = nouveauxml.replace("</user_statistic_1>", "");
				nouveauxml = nouveauxml.replace("<statistic_category_1>", "");
				nouveauxml = nouveauxml.replace("</statistic_category_1>", "");
				nouveauxml = nouveauxml.replace("_niveau", "");
			}

			nouveauxml = nouveauxml.replace("_categorie", categorie);

			System.out.println(nouveauxml);

			int returnCode = alma.createUser(nouveauxml);

			if (returnCode == 200) {

				if (SMTP_LOGIN == null || SMTP_HOST == null || SMTP_PASSWD == null) {
					System.out.println("Erreur paramètres SMTP: pas d'envoie de mail");
				} else {

					String htmlText = Utils.file2String(
							new InputStreamReader(context.getResourceAsStream("conf/templateMail.html"), "UTF-8"));

					htmlText = htmlText.replaceAll("\\{TO\\}", email_address);
					htmlText = htmlText.replaceAll("\\{URL_BU\\}", URL_BU);

					String MAIL_LOGO_PATH = context.getRealPath("conf/logo.png");

					SendMail.getInstance().sendMail("Demande de Préinscription en bibliothèque", email_address,
							htmlText, SMTP_LOGIN, SMTP_PASSWD, SMTP_HOST, MAIL_LOGO_PATH);
				}

			}

			return returnCode + "";
		} catch (Throwable e) {

			e.printStackTrace();
			return HTTP_CODE_BAD_REQUEST + "";
		}

	}

}
