package sicd.infodoc.webservices.usagers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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

@Path("suggestionachat")
public class SuggestionAchat {

	private static final int HTTP_CODE_OK = 200;
	private static final int HTTP_CODE_BAD_REQUEST = 400;
	private static final int HTTP_CODE_ERR_CAPTCHA = 403;
	private static final String UNKNOWN = "INCONNU";

	// valeur dans config.properties
	private static String ALMA_API_USERS_KEY = null;
	private static String ALMA_API_BASE_URL = null;
	private static String GOOGLE_SECRET_CAPTCHA = null;
	// valeur dans config.properties
	private static String PRIMO_API_BASE_URL = null;
	private static String PRIMO_PROD_KEY = null;
	private static String PRIMO_VIEW = null;

	private static Alma alma = null;
	private static Alma primo = null;
	private static Map<String, String> bibliotheques = null;

	@Context
	private ServletContext context;

	public SuggestionAchat() {
		// TODO Auto-generated constructor stub
		super();
	}

	private void initConfig() {
		if (ALMA_API_USERS_KEY == null)
			ALMA_API_USERS_KEY = Utils.getPropertiesByKey("ALMA_API_USERS_KEY",
					context.getResourceAsStream("conf/config.properties"));

		if (ALMA_API_BASE_URL == null)
			ALMA_API_BASE_URL = Utils.getPropertiesByKey("ALMA_API_BASE_URL",
					context.getResourceAsStream("conf/config.properties"));

		if (GOOGLE_SECRET_CAPTCHA == null)
			GOOGLE_SECRET_CAPTCHA = Utils.getPropertiesByKey("GOOGLE_SECRET_CAPTCHA",
					context.getResourceAsStream("conf/config.properties"));

		if (PRIMO_API_BASE_URL == null)
			PRIMO_API_BASE_URL = Utils.getPropertiesByKey("PRIMO_API_BASE_URL",
					context.getResourceAsStream("conf/config.properties"));

		if (PRIMO_PROD_KEY == null)
			PRIMO_PROD_KEY = Utils.getPropertiesByKey("PRIMO_PROD_KEY",
					context.getResourceAsStream("conf/config.properties"));

		if (PRIMO_VIEW == null)
			PRIMO_VIEW = Utils.getPropertiesByKey("PRIMO_VIEW", context.getResourceAsStream("conf/config.properties"));

		if (alma == null)
			alma = new Alma(ALMA_API_USERS_KEY, ALMA_API_BASE_URL);

		if (primo == null)
			primo = new Alma(PRIMO_PROD_KEY, PRIMO_API_BASE_URL);

		if (bibliotheques == null)
			bibliotheques = Utils.getProperties(context.getResourceAsStream("conf/bu.properties"));
	}

	@GET
	@Path("/getNotice/{isbn}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getNotice(@PathParam("isbn") String isbn) {
		BufferedReader rd = null;
		HttpURLConnection conn = null;
		System.out.println(isbn);
		try {
			initConfig();

			String notice = primo.searchPrimo(isbn, PRIMO_VIEW);

			if (notice == null)
				return Response.status(Status.NOT_FOUND).build();
			else {

				String titre = getField("title", notice, false);
				if (titre == null || titre.isEmpty())
					return Response.status(Status.NOT_FOUND).build();

				String auteur = getField("author", notice, false);
				if (auteur == null || auteur.isEmpty())
					auteur = UNKNOWN;

				String date = getField("date", notice, false);
				if (date == null || date.isEmpty())
					date = UNKNOWN;

				String editeur = getField("publisher", notice, false);
				if (editeur == null || editeur.isEmpty())
					editeur = UNKNOWN;

				String library = getField("library", notice, true);
				if (library == null || library.isEmpty())
					library = UNKNOWN;

				String msg = "<notice><titre>" + titre + "</titre><auteur>" + auteur + "</auteur><date>" + date
						+ "</date><editeur>" + editeur + "</editeur><library>" + library + "</library></notice>";
				System.out.println(msg);
				return Response.status(Status.OK).entity(msg).build();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} finally {
			if (rd != null)
				try {
					rd.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			if (conn != null)
				conn.disconnect();

		}
	}

	public String getField(String field, String s, boolean list) {

		try {
			int debutField = s.indexOf("\"" + field + "\"");
			if (debutField < 0)
				return null;

			int debutValueField = s.indexOf("[", debutField);
			if (debutValueField < 0)
				return null;

			int finValueField = s.indexOf("]", debutField);
			if (finValueField < 0)
				return null;

			String value = s.substring(debutValueField + 1, finValueField);

			if (value.contains(",") && list == false)
				value = value.substring(0, value.indexOf(","));

			if (field.equals("library")) {
				Set<String> buCodes = bibliotheques.keySet();
				for (String buCode : buCodes) {

					value = value.replaceAll(buCode, bibliotheques.get(buCode));
				}
			}
			value = value.replaceAll(",", ", ");
			value = value.replaceAll("\"", "");
			return value;
		} catch (Throwable e) {
			return null;
		}

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
				if (xml.indexOf("email preferred=\"true\"") > 0) {
					xml = xml.replaceAll("<email preferred=\"false\"((?!</email>).)*</email>", "");
				}
				return Response.status(Status.OK).entity(xml).build();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	@POST
	@Path("/suggestionachat/{token}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String suggestionachat(@PathParam("token") String token, String requestBody) {

		try {
			initConfig();
			System.out.println("appel suggestionachat:" + token);

			if (!Utils.verifierCaptcha(GOOGLE_SECRET_CAPTCHA, token))
				return HTTP_CODE_ERR_CAPTCHA + "";

			String[] tokens = requestBody.split(":", -1);

			// Create path components to save the file
			final String titre = tokens[0];
			final String auteur = tokens[1];
			final String anneepub = tokens[2];
			final String editeur = tokens[3];
			final String isbn = tokens[4];

			String bu = tokens[5];

			String unr = tokens[6];
			final String nom = tokens[7];
			final String prenom = tokens[8];
			final String email = tokens[9];
			final String volume = "";
			final String remarque = tokens[10];

			System.out.println("titre = " + titre);
			System.out.println("auteur = " + auteur);
			System.out.println("anneepub = " + anneepub);
			System.out.println("editeur = " + editeur);
			System.out.println("isbn = " + isbn);

			System.out.println("bu = " + bu);
			System.out.println("unr = " + unr);
			System.out.println("nom = " + nom);
			System.out.println("prenom = " + prenom);
			System.out.println("email = " + email);

			String f = null;

			if (unr.isEmpty()) {

				// UNR d'un dossier fictif pour les suggestions d'achat.
				// dossier primary_id = sga
				unr = "0000000000000";

			}

			InputStreamReader reader = new InputStreamReader(
					context.getResourceAsStream("demande_achat_template_book.xml"));
			ArrayList<String> l = Utils.file2ArrayString(reader);
			String achatxml = "";
			for (int i = 0; i < l.size(); i++) {
				achatxml += l.get(i);
			}

			String nouveauxml = achatxml.replace("_bu_", bu);
			nouveauxml = nouveauxml.replace("_titre_", titre);
			nouveauxml = nouveauxml.replace("_auteur_", auteur);
			nouveauxml = nouveauxml.replace("_editeur_", editeur);
			nouveauxml = nouveauxml.replace("_anneepub_", anneepub);

			// le champ isbn du formulaire sert à la fois à passer l'isbn ou
			// l'issn

			nouveauxml = nouveauxml.replace("_isbn_", isbn);

			nouveauxml = nouveauxml.replace("_volume_", volume);
			nouveauxml = nouveauxml.replace("_note_address",
					"nom:" + nom + " " + "prenom:" + prenom + " " + "email:" + email);
			nouveauxml = nouveauxml.replace("_note_demandeur", remarque);

			int returnCode = creerSuggestionAchat(unr, nouveauxml);
			System.out.println("fin appel suggestionachat");
			return returnCode + "";

		} catch (Throwable e) {
			System.out.println("fin appel suggestionachat exception");
			e.printStackTrace();
			return HTTP_CODE_BAD_REQUEST + "";
		}
	}

	public int creerSuggestionAchat(String unr, String xml) {

		boolean res = alma.createUserPurchaseRequest(unr, xml);
		if (res)
			return HTTP_CODE_OK;
		else
			return HTTP_CODE_BAD_REQUEST;

	}

}
