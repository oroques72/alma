package sicd.infodoc.webservices.usagers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import sicd.alma.Alma;
import sicd.common.Utils;

@Path("quitus")
public class Quitus {

	@Context
	private ServletContext context;

	// valeur dans config.properties
	private static String ALMA_API_USERS_KEY = null;
	private static String ALMA_API_BASE_URL = null;
	private static String GOOGLE_SECRET_CAPTCHA = null;
	private static String HOST_QUITUS_DIR = null;

	// champs optionnels dans config.properties
	private static String SMTP_LOGIN = null;
	private static String SMTP_PASSWD = null;
	private static String SMTP_HOST = null;
	private static Boolean PARSE_USERS_FILE = null;
	private static String USERS_PATH_FILENAME = null;
	private static Boolean GENERATE_PDF = null;

	private static Alma alma = null;
	

	public Quitus() {
		super();

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

		if (HOST_QUITUS_DIR == null)
			HOST_QUITUS_DIR = Utils.getPropertiesByKey("HOST_QUITUS_DIR",
					context.getResourceAsStream("conf/config.properties"));

		if(GENERATE_PDF == null)
		GENERATE_PDF = Utils.getBooleanPropertiesByKey("GENERATE_PDF",
				context.getResourceAsStream("conf/config.properties"));

		if(SMTP_LOGIN==null)
		SMTP_LOGIN = Utils.getPropertiesByKey("SMTP_LOGIN", context.getResourceAsStream("conf/config.properties"));

		if(SMTP_PASSWD==null)
		SMTP_PASSWD = Utils.getPropertiesByKey("SMTP_PASSWD", context.getResourceAsStream("conf/config.properties"));

		if(SMTP_HOST==null)
		SMTP_HOST = Utils.getPropertiesByKey("SMTP_HOST", context.getResourceAsStream("conf/config.properties"));

		if(USERS_PATH_FILENAME==null)
		USERS_PATH_FILENAME = Utils.getPropertiesByKey("USERS_PATH_FILENAME",
				context.getResourceAsStream("conf/config.properties"));

		if(PARSE_USERS_FILE==null)
		PARSE_USERS_FILE = Utils.getBooleanPropertiesByKey("PARSE_USERS_FILE",
				context.getResourceAsStream("conf/config.properties"));

		if (alma == null)
			alma = new Alma(ALMA_API_USERS_KEY, ALMA_API_BASE_URL);

	}

	public static void main(String[] args) {

	}

	@GET
	@Path("/recherche_unr_alma/{unrid}")
	@Produces(MediaType.APPLICATION_XML)
	public Response rechercheUsagersAlma(@PathParam("unrid") String unr) {
		try {

			initConfig();

			String xml = alma.getUser(unr);

			if (xml == null) {
				return Response.status(Status.NOT_FOUND).build();
			} else {

				xml = xml.replaceAll("<email preferred=\"false\"((?!</email>).)*</email>", "");
				return Response.status(Status.OK).entity(xml).build();

			}

		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	@POST
	@Path("/demandequitus/{token}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response demandeQuitus(@PathParam("token") String token, String requestBody) {
		int nbLoans = -1;

		try {
			initConfig();

			System.out.println("appel quitus:" + token);
			if (!Utils.verifierCaptcha(GOOGLE_SECRET_CAPTCHA, token)) /* 403 */
				return Response.status(Status.FORBIDDEN).build();

			String[] tokens = requestBody.split(":", -1);

			final String unr = tokens[0];
			final String nom = tokens[1];
			final String prenom = tokens[2];
			final String email = tokens[3];

			System.out.println("unr = " + unr);
			System.out.println("nom = " + nom);
			System.out.println("prenom = " + prenom);
			System.out.println("email = " + email);

			String s = alma.getLoans(unr);
			if (s != null) {
				String chaine = "item_loans total_record_count=";
				int debut = s.indexOf(chaine) + chaine.length() + 1;
				int fin = s.indexOf("\"", debut);
				nbLoans = Integer.parseInt(s.substring(debut, fin));

			}

			if (nbLoans < 0) {
				// impossible de trouver les prets ou echec appel getLoans

				// return "400";
				return Response.status(Status.BAD_REQUEST).build();
			} else if (nbLoans > 0) {
				// pret en cours
				// return "401";
				return Response.status(Status.UNAUTHORIZED).build();
			} else {
				// Creation quitus
				;

				String fileName = "demande_quitus" + "_" + prenom.substring(0, 1) + nom + "_"
						+ Utils.getCurrentDateTime() + ".rtf";

				fileName = fileName.replaceAll(" ", "");
				fileName = fileName.replaceAll("'", "");

				System.out.println(fileName);
				// String quitus_template = hostPath +
				// "/template/demande_quitus_template.rtf";

				String quitus_template = context.getRealPath("conf/demande_quitus_template.rtf");
				BufferedWriter quitus_demande = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(HOST_QUITUS_DIR + "/" + fileName, false), "ISO-8859-1"));

				// ArrayList<String> l = file2ArrayString(quitus_template);
				String quitusxml = "";
				String nouveauxml = "";

				quitusxml = Utils.readFile(quitus_template, Charset.forName("ISO-8859-1"));
				nouveauxml = quitusxml.replaceAll("_NOM", nom);
				nouveauxml = nouveauxml.replaceAll("_PRENOM", prenom);
				nouveauxml = nouveauxml.replaceAll("_NUM_UNR", unr);
				nouveauxml = nouveauxml.replaceAll("_NUM_INE", getINE(unr));

				nouveauxml = nouveauxml.replaceAll("_CURRENT_DATE", Utils.getCurrentDate());

				System.out.println("DEBUT Quitus:GetUser");
				String xmluser = alma.getUser(unr);
				System.out.println("END Quitus:GetUser");
				if (xmluser == null) {
					quitus_demande.close();
					return Response.status(Status.INTERNAL_SERVER_ERROR).build();

				}

				nouveauxml = nouveauxml.replaceAll("_DATE_NAISSANCE", getDateNaissance(xmluser));

				String xml = xmluser.replaceAll("<address preferred=\"false\"((?!</address>).)*</address>", "");

				ArrayList<String> r = Utils.getTagsValue("line1", xml);
				String line1 = r.size() > 0 ? r.get(0) : "";

				r = Utils.getTagsValue("line2", xml);
				String line2 = r.size() > 0 ? r.get(0) : "";

				r = Utils.getTagsValue("postal_code", xml);
				String postal_code = r.size() > 0 ? r.get(0) : "";

				r = Utils.getTagsValue("city", xml);
				String city = r.size() > 0 ? r.get(0) : "";

				r = Utils.getTagsValue("state_province", xml);
				String state_province = r.size() > 0 ? r.get(0) : "";

				String country = Utils.getTagsValueDesc("country", xml);

				nouveauxml = nouveauxml.replaceAll("_ADRESSE",
						line1 + ", " + line2 + ", " + postal_code + " " + city + ", " + country);
				quitus_demande.write(nouveauxml);
				quitus_demande.flush();
				quitus_demande.close();

				// Ajout note

				boolean update = false;
				String noteQuitus = "<user_note segment_type=\"Internal\"><note_type>BARCODE</note_type><note_text>"
						+ "Document de quitus envoyé le " + Utils.getCurrentDate() + " à " + email
						+ "</note_text><user_viewable>false</user_viewable><popup_note>true</popup_note></user_note>";
				if (xmluser.contains("</user_notes>")) {
					update = true;
					xmluser = xmluser.replaceFirst("</user_notes>", noteQuitus + "</user_notes>");
				} else if (xmluser.contains("<user_notes/>")) {
					update = true;
					xmluser = xmluser.replaceFirst("<user_notes/>", "<user_notes>" + noteQuitus + "</user_notes>");

				} else
					return Response.status(Status.INTERNAL_SERVER_ERROR).build();

				if (update) {
					xmluser = xmluser.replaceFirst("<user_roles>.*</user_roles>", "");
					xmluser = xmluser.replaceFirst("<line1>[ ]*</line1>", "<line1>A REMPLIR</line1>");

					String statusDate = Utils.getXMLGregorianDate(Utils.getCurrentDate(), 0).toString().substring(0, 10)
							+ "Z";
					String purgeDate = Utils.getXMLGregorianDate(Utils.getCurrentDate(), 90).toString().substring(0, 10)
							+ "Z";

					// On termine par status_date car il y a plusieurs balises
					// status ailleur dans le xml
					xmluser = xmluser.replaceFirst("<status[^<]*ACTIVE</status><status_date>[^<]*</status_date>",
							"<status>INACTIVE</status><status_date>" + statusDate + "</status_date>");
					xmluser = xmluser.replaceFirst("<expiry_date>[^<]*</expiry_date>",
							"<expiry_date>" + statusDate + "</expiry_date>");

					xmluser = xmluser.replaceFirst("<purge_date>[^<]*</purge_date>",
							"<purge_date>" + purgeDate + "</purge_date>");

					if (!alma.updateUser(unr, xmluser)) {
						return Response.status(Status.INTERNAL_SERVER_ERROR).build();

					}

				} else
					return Response.status(Status.INTERNAL_SERVER_ERROR).build();

				System.out.println("GENERATE_PDF=" + GENERATE_PDF);

				if (GENERATE_PDF) {
					if (testPdfgenere(HOST_QUITUS_DIR + "/" + fileName.replace(".rtf", ".pdf")) == false)
						return Response.status(Status.INTERNAL_SERVER_ERROR).build();
				}
				// Envoi mail

				String MAIL_LOGO_PATH = context.getRealPath("conf/logo.png");
				System.out.println("MAIL_LOGO_PATH" + MAIL_LOGO_PATH);
				;

				String htmlText = Utils.file2String(
						new InputStreamReader(context.getResourceAsStream("conf/templateMail.html"), "UTF-8"));

				String quitus_filename;
				if (GENERATE_PDF)
					quitus_filename = fileName.replace(".rtf", ".pdf");
				else
					quitus_filename = fileName;

				if (SMTP_LOGIN == null || SMTP_HOST == null || SMTP_PASSWD == null) {
					System.out.println("Erreur paramètres SMTP: pas d'envoie de mail");
				} else {

					SendMail.getInstance().sendMail("Demande de quitus", email, htmlText, quitus_filename,
							HOST_QUITUS_DIR, SMTP_LOGIN, SMTP_PASSWD, SMTP_HOST, MAIL_LOGO_PATH);
				}

				return Response.status(Status.OK).entity(quitus_filename).build();
			}

		} catch (Throwable e) {
			System.out.println("fin appel quitus exception");

			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	public boolean testPdfgenere(String fileName) {

		try {
			int c = 0;
			File f = new File(fileName);
			// On attends 5 min au pire
			while (c < 300) {
				if (f.exists()) {
					return true;
				} else {
					c++;
					Thread.sleep(1000);
				}
			}

		} catch (Throwable e) {

			e.printStackTrace();
		}

		return false;
	}

	public String getINE(String unr) {

		if (!PARSE_USERS_FILE)
			return "";

		try {
			// 0210020806548;06O9IE013W2

			if (USERS_PATH_FILENAME == null)
				return "";

			String res = Utils.readFile(USERS_PATH_FILENAME, Charset.forName("ISO-8859-1"));

			if (res.indexOf(unr) == -1)
				return "";

			int debut = res.indexOf(unr) + unr.length() + 1;

			int fin = res.indexOf(";", debut);
			return res.substring(debut, fin);

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return "";

	}

	public String getDateNaissance(String xml) {

		try {

			if (xml != null) {

				String date = Utils.getTagsValue("birth_date", xml).get(0);
				return date.substring(8, 10) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4);

			} else
				return "";

		} catch (Throwable e) {
			return "";
		}

	}

}
