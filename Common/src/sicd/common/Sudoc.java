package sicd.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Sudoc {

	public static boolean RCRexiste(String ppn, String rcr) {
		HashSet<String> s = new HashSet<String>();
		s.add(ppn);
		

		String res = sudocWebServicesMultiwhere(s, false);
System.out.println(res);
		if (res != null && res.contains("<rcr>" + rcr + "</rcr>"))
			return true;
		else
			return false;

	}

	public static String sudocWebServicesMultiwhere(Set<String> ppnList, boolean jsonFormat) {

		String urlStr = "https://www.sudoc.fr/services/multiwhere/";
		try {
			Iterator<String> it = ppnList.iterator();
			while (it.hasNext()) {
				urlStr += it.next();

				if (it.hasNext())
					urlStr += ",";
			}

			if (jsonFormat)
				urlStr += "&format=text/json";

			System.out.println(urlStr);
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			if (conn.getResponseCode() != 200) {
				conn.disconnect();
				return null;
			} else {

				// Buffer the result into a string
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();

				conn.disconnect();
				return sb.toString();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(RCRexiste("000140953", "315552319"));

	}

}
