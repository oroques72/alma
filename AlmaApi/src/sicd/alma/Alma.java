package sicd.alma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.text.Utilities;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.Response;

import sicd.alma.gen.users.Parameter;
import sicd.alma.gen.users.Parameters;
import sicd.alma.gen.users.User;
import sicd.alma.gen.users.UserRole;
import sicd.alma.gen.users.UserRoles;
import sicd.common.Utils;

public class Alma {


	
	private static final String URL_BIB = "/almaws/v1/bibs/{mms_id}";
	private static final String URL_HOLDING = "/almaws/v1/bibs/{mms_id}/holdings/{holding_id}";
	private static final String URL_ITEM = "/almaws/v1/bibs/{mms_id}/holdings/{holding_id}/items/{item_pid}";
	private static final String URL_USER = "/almaws/v1/users/{user_id}";
	private static final String URL_USER_CREATE_PURCHASE_REQUESTS = "/almaws/v1/users/{user_id}/purchase-requests";
	private static final String URL_USER_LOANS = "/almaws/v1/users/{user_id}/loans";
	private static final String URL_CREATE_USER = "/almaws/v1/users";
	private static final String URL_CREATE_ITEM = "/almaws/v1/bibs/{mms_id}/holdings/{holding_id}/items";
	private static final String URL_POLINES = "/almaws/v1/acq/po-lines/{po_line_id}";
	private static final String URL_RETRIEVE_POLINES = "/almaws/v1/acq/po-lines";
	private static final String URL_PRIMO_SEARCH = "/primo/v1/search";

	// The keys from the developper network determinates the environment
	// (Production or SandBox) and the scope (BIB,USER, etc ...)
	private String apiKey;
	//pas final car configurable
	private String ALMA_SERVER = "https://api-eu.hosted.exlibrisgroup.com";
	private class Response {
		boolean status;
		String msg;

		private Response(boolean status, String msg) {
			super();
			this.status = status;
			this.msg = msg;
		}

	}

	public Alma(String apiKey) {
		this.apiKey = apiKey;
		
	}
	
	public Alma(String apiKey,String url) {
		this.apiKey = apiKey;
		this.ALMA_SERVER=url;
	}

	public String getBib(String mmsId) {
		return getApi(URL_BIB, Stream.of(new String[][] { { "mms_id", mmsId } })
				.collect(Collectors.toMap(data -> data[0], data -> data[1])), null);

	}

	public boolean updateBib(String mmsId, String xml) {
		return putApi(URL_BIB, Stream.of(new String[][] { { "mms_id", mmsId } })
				.collect(Collectors.toMap(data -> data[0], data -> data[1])), xml);

	}

	public String getHolding(String mmsId, String holdingId) {
		return getApi(URL_HOLDING, Stream.of(new String[][] { { "mms_id", mmsId }, { "holding_id", holdingId } })
				.collect(Collectors.toMap(data -> data[0], data -> data[1])), null);

	}

	public boolean updateHolding(String mmsId, String holdingId, String xml) {
		return putApi(URL_HOLDING, Stream.of(new String[][] { { "mms_id", mmsId }, { "holding_id", holdingId } })
				.collect(Collectors.toMap(data -> data[0], data -> data[1])), xml);

	}

	public String getItem(String mms_id, String holding_id, String item_pid) {
		return getApi(URL_ITEM,
				Stream.of(
						new String[][] { { "mms_id", mms_id }, { "holding_id", holding_id }, { "item_pid", item_pid } })
						.collect(Collectors.toMap(data -> data[0], data -> data[1])),
				null);

	}

	public boolean updateItem(String mms_id, String holding_id, String item_pid, String xml) {
		return putApi(URL_ITEM,
				Stream.of(
						new String[][] { { "mms_id", mms_id }, { "holding_id", holding_id }, { "item_pid", item_pid } })
						.collect(Collectors.toMap(data -> data[0], data -> data[1])),
				xml);

	}

	public boolean createItem(String mms_id, String holding_id, String xml) {

		Response rep = postApi(URL_CREATE_ITEM,
				Stream.of(new String[][] { { "mms_id", mms_id }, { "holding_id", holding_id } })
						.collect(Collectors.toMap(data -> data[0], data -> data[1])),
				xml);

		return rep.status;
	}

	public boolean deleteItem(String mms_id, String holding_id, String item_pid) {
		return deleteApi(URL_ITEM,
				Stream.of(
						new String[][] { { "mms_id", mms_id }, { "holding_id", holding_id }, { "item_pid", item_pid } })
						.collect(Collectors.toMap(data -> data[0], data -> data[1])));

	}

	public String getUser(String id) {
		return getApi(URL_USER, Stream.of(new String[][] { { "user_id", id } })
				.collect(Collectors.toMap(data -> data[0], data -> data[1])), null);

	}
	
	public String getLoans(String id) {
		return getApi(URL_USER_LOANS, Stream.of(new String[][] { { "user_id", id } })
				.collect(Collectors.toMap(data -> data[0], data -> data[1])), null);

	}
	

	public boolean updateUser(String id, String xml) {
		return putApi(URL_USER, Stream.of(new String[][] { { "user_id", id } })
				.collect(Collectors.toMap(data -> data[0], data -> data[1])), xml);

	}

	public int createUser(String xml) {
		int returnCode;

		Response rep = postApi(URL_CREATE_USER, null, xml);

		if (rep.status == false) {
			if (rep.msg.contains("errorCode")) {

				if (rep.msg.contains("401851"))
					returnCode = 401;
				else if (rep.msg.contains("500038"))
					returnCode = 402;
				else
					returnCode = 400;
			} else {
				returnCode = 400;
			}
		} else
			returnCode = 200;

		return returnCode;
	}

	public boolean createUserPurchaseRequest(String user_id, String xml) {

		Response rep = postApi(URL_USER_CREATE_PURCHASE_REQUESTS, Stream.of(new String[][] { { "user_id", user_id } })
				.collect(Collectors.toMap(data -> data[0], data -> data[1])), xml);
		return rep.status;
	}

	public boolean deleteUser(String id) {
		return deleteApi(URL_USER, Stream.of(new String[][] { { "user_id", id } })
				.collect(Collectors.toMap(data -> data[0], data -> data[1])));

	}

	public String getPoLine(String po_line_id) {
		return getApi(URL_POLINES, Stream.of(new String[][] { { "po_line_id", po_line_id } })
				.collect(Collectors.toMap(data -> data[0], data -> data[1])), null);

	}

	public boolean updatePoLine(String po_line_id, String xml) {
		return putApi(URL_POLINES, Stream.of(new String[][] { { "po_line_id", po_line_id } })
				.collect(Collectors.toMap(data -> data[0], data -> data[1])), xml);

	}

	public ArrayList<String> retrievePOLines(String issn, String item_library) {

		ArrayList<String> polines = new ArrayList<String>();

		for (int offset = 0;; offset++) {
			String res = getApi(URL_RETRIEVE_POLINES, null,
					Stream.of(new String[][] { { "q", "issn_isbn~" + issn + " AND " + "item_library~" + item_library },
							{ "limit", "100" }, { "offset", new Integer(offset).toString() } })
					.collect(Collectors.toMap(data -> data[0], data -> data[1])));

			if (res != null && res.contains("</po_line>")) {
				polines.add(res);
			} else
				break;
		}
		return polines;
	}

	public String searchPrimo(String isbn, String view) {

		return getApi(URL_PRIMO_SEARCH, null,
				Stream.of(new String[][] { { "vid", view }, { "tab", "default_tab" }, { "scope", "default_scope" },
						{ "q", "isbn,exact,"+isbn } ,{"qInclude","facet_tlevel,exact,available"}}).collect(Collectors.toMap(data -> data[0], data -> data[1])));

	}

	private String getApi(String urlApi, Map<String, String> urlParams, Map<String, String> queryParams) {

		try {

			boolean OK = false;
			if (urlParams != null) {
				for (Entry<String, String> entry : urlParams.entrySet()) {
					urlApi = urlApi.replace("{" + entry.getKey() + "}", URLEncoder.encode(entry.getValue(), "UTF-8"));

				}
			}

			StringBuilder urlBuilder = new StringBuilder(ALMA_SERVER + urlApi);
			urlBuilder.append("?");

			if (queryParams != null) {
				queryParams.forEach((k, v) -> {
					try {
						urlBuilder.append(URLEncoder.encode(k, "UTF-8") + "=" + URLEncoder.encode(v, "UTF-8") + "&");
					} catch (Throwable e) {
					}
				});
			}
			urlBuilder.append(URLEncoder.encode("apikey", "UTF-8") + "=" + URLEncoder.encode(apiKey, "UTF-8"));

			URL url = new URL(urlBuilder.toString());
			System.out.println("SGA_TEST:"+urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//conn.setConnectTimeout(5000);
			//conn.setReadTimeout(5000);
			conn.setRequestMethod("GET");
			// System.out.println("Response code: " + conn.getResponseCode());
			BufferedReader rd;
			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				OK = true;
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			} else {
				OK = false;
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

			}
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
			conn.disconnect();

			if (OK) {
				// System.out.println(sb.toString());
				return sb.toString();
			} else {

				return null;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return null;

		}

	}

	private boolean putApi(String urlApi, Map<String, String> urlParams, String xml) {
		try {

			boolean OK = false;

			for (Entry<String, String> entry : urlParams.entrySet()) {
				urlApi = urlApi.replace("{" + entry.getKey() + "}", URLEncoder.encode(entry.getValue(), "UTF-8"));

			}

			StringBuilder urlBuilder = new StringBuilder(ALMA_SERVER + urlApi);
			urlBuilder.append("?");

			urlBuilder.append(URLEncoder.encode("apikey", "UTF-8") + "=" + URLEncoder.encode(apiKey, "UTF-8"));

			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");

			conn.setRequestProperty("Content-Type", "application/xml");

			byte[] body = xml.getBytes("UTF-8");
			conn.setFixedLengthStreamingMode(body.length);
			conn.setDoOutput(true);

			OutputStream out = conn.getOutputStream();
			out.write(body);

			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				OK = true;
			} else {
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				System.out.println(sb.toString());
				OK = false;

			}

			conn.disconnect();

			if (OK) {
				return true;
			} else {

				return false;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return false;

		}

	}

	private Response postApi(String urlApi, Map<String, String> urlParams, String xml) {
		try {
			boolean OK = false;

			if (urlParams != null) {

				for (Entry<String, String> entry : urlParams.entrySet()) {
					urlApi = urlApi.replace("{" + entry.getKey() + "}", URLEncoder.encode(entry.getValue(), "UTF-8"));

				}
			}

			StringBuilder urlBuilder = new StringBuilder(ALMA_SERVER + urlApi);
			urlBuilder.append("?");

			urlBuilder.append(URLEncoder.encode("apikey", "UTF-8") + "=" + URLEncoder.encode(apiKey, "UTF-8"));

			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Content-Type", "application/xml");

			byte[] body = xml.getBytes("UTF-8");
			conn.setFixedLengthStreamingMode(body.length);
			conn.setDoOutput(true);

			OutputStream out = conn.getOutputStream();
			out.write(body);

			int code = conn.getResponseCode();

			BufferedReader rd = null;
			if (code >= 200 && code <= 300) {
				OK = true;
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			} else {
				OK = false;
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

			}

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			rd.close();
			conn.disconnect();

			return new Response(OK, sb.toString());
		} catch (Throwable e) {

			return new Response(false, null);

		}

	}

	private boolean deleteApi(String urlApi, Map<String, String> urlParams) {
		try {
			System.out.println("deleteApi");
			boolean OK = false;

			for (Entry<String, String> entry : urlParams.entrySet()) {
				urlApi = urlApi.replace("{" + entry.getKey() + "}", URLEncoder.encode(entry.getValue(), "UTF-8"));

			}

			StringBuilder urlBuilder = new StringBuilder(ALMA_SERVER + urlApi);
			urlBuilder.append("?");

			urlBuilder.append(URLEncoder.encode("apikey", "UTF-8") + "=" + URLEncoder.encode(apiKey, "UTF-8"));

			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			// System.out.println("Response code: " + conn.getResponseCode());
			BufferedReader rd;

			System.out.println("deleteApi:" + conn.getResponseCode());
			if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				OK = true;
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			} else {
				OK = false;
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

			}
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
			conn.disconnect();

			if (OK) {
				System.out.println(sb.toString());
				return true;
			} else {
				System.out.println(sb.toString());
				return false;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return false;

		}

	}

	public static void main(String[] args) throws IOException {
		
	}

}
