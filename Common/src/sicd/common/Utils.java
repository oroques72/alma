package sicd.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class Utils {

	public Utils() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static HashMap<String, String> getProperties(String properties) {

		InputStream input;
		try {
			input = new FileInputStream(properties);
			return getProperties(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new HashMap<String, String>();
		}

	}

	public static HashMap<String, String> getProperties(InputStream properties) {

		HashMap<String, String> m = new HashMap<String, String>();

		System.out.println("Loading properties" + properties);
		try {

			Properties prop = new Properties();

			// load a properties file
			prop.load(properties);

			for (Object key : prop.keySet()) {
				System.out.println(key + "=" + prop.get(key));
				m.put((String) key, (String) prop.get(key));
			}

		} catch (Throwable e) {
			e.printStackTrace();

		}
		return m;
	}

	public static String getPropertiesByKey(String key, InputStream input) {

		try {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);
			System.out.println(key + "=" + prop.get(key));
			return (String) prop.get(key);

		} catch (Throwable e) {
			e.printStackTrace();
			return null;

		}

	}
	
	public static Boolean getBooleanPropertiesByKey(String key, InputStream input) {

		try {

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);
			System.out.println(key + "=" + prop.get(key));
			return ((String)prop.get(key)).equals("true");

		} catch (Throwable e) {
			e.printStackTrace();
			return false;

		}

	}
	

	public static String getPropertiesByKey(String key, String properties) {

		System.out.println("Remplissage map " + properties);
		try {

			InputStream input = new FileInputStream(properties);

			Properties prop = new Properties();

			// load a properties file
			prop.load(input);

			return (String) prop.get(key);

		} catch (Throwable e) {
			e.printStackTrace();
			return null;

		}

	}

	@SuppressWarnings("rawtypes")
	public static Object xmlToClass(String xml, Class c) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Object o = unmarshaller.unmarshal(new StringReader(xml));
			return o;
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println(xml);
			return null;
		}

	}

	public static String classToXml(Object o) {
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new
			// Boolean(true));
			StringWriter sw = new StringWriter();
			marshaller.marshal(o, sw);

			return sw.toString();
		} catch (Throwable e) {
			return null;
		}

	}
	
	public static ArrayList<String> file2ArrayString(InputStreamReader reader) throws IOException {
		ArrayList<String> res = new ArrayList<String>();
		Scanner scanner = new Scanner(new BufferedReader(reader));
		while (scanner.hasNextLine()) {
			res.add(scanner.nextLine().trim());
		}

		scanner.close();
		return res;
	}
	
	public static String file2String(InputStreamReader reader) throws IOException {
		String res = "";
		Scanner scanner = new Scanner(new BufferedReader(reader));
		while (scanner.hasNextLine()) {
			res+=scanner.nextLine().trim();
		}

		scanner.close();
		return res;
	}


	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static ArrayList<String> file2ArrayString(String filename) throws IOException {
		ArrayList<String> res = new ArrayList<String>();
		Scanner scanner = new Scanner(new BufferedReader(new FileReader(new File(filename))));
		while (scanner.hasNextLine()) {
			res.add(scanner.nextLine());
		}

		scanner.close();
		return res;
	}

	public static ArrayList<String> file2ArrayStringUTF8(String filename) throws IOException {
		ArrayList<String> res = new ArrayList<String>();
		File f = new File(filename);

		Scanner scanner = new Scanner(new FileInputStream(f), "UTF-8");
		while (scanner.hasNextLine()) {
			res.add(scanner.nextLine());
		}

		scanner.close();
		return res;
	}
	
	public static XMLGregorianCalendar getXMLGregorianDate(String strDate, int day) throws DatatypeConfigurationException {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.FRENCH);
		LocalDate date = LocalDate.parse(strDate, formatter);

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeZone(TimeZone.getTimeZone("UTC"));
		gc.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
		gc.add(GregorianCalendar.DAY_OF_YEAR, day);

		XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		return xmlDate;
	}
	
	public static boolean verifierCaptcha(String secretCaptcha,String token) {
		try {
			 

			StringBuilder urlBuilder = new StringBuilder("https://www.google.com/recaptcha/api/siteverify");

			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			byte[] body = ("secret="+secretCaptcha + "&response=" + token).getBytes();
			
		
			
			conn.setFixedLengthStreamingMode(body.length);
			conn.setDoOutput(true);

			OutputStream out = conn.getOutputStream();
			out.write(body);
			System.out.println("Response code: " + conn.getResponseCode());
		
			boolean response = false;
			BufferedReader rd;
			if (conn.getResponseCode() == 200) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				response = true;
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

			}
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			System.out.println(sb.toString());
			rd.close();
			conn.disconnect();

			if (response == false)
				return false;
			else {
				if (sb.toString().contains("\"success\": true"))
					return true;
				else
					return false;
			}
		} catch (Throwable e) {
			return false;
		}
	}

	public static String EnleveAccent(String s) {

		String ch = Normalizer.normalize(s, Normalizer.Form.NFD);
		// System.out.println(ch);
		ch = ch.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return ch.toUpperCase();
	}

	
	public static String getCurrentDate() {

		SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");

		Date currentTime_1 = new Date();

		String dateString = d.format(currentTime_1);

		return dateString;
	}
	
	public static String getCurrentDateTime() {

		SimpleDateFormat d = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat h = new SimpleDateFormat("HH:mm:ss:SSS");

		Date currentTime_1 = new Date();

		String dateString = d.format(currentTime_1);
		String heureString = h.format(currentTime_1);
		String current_date = (dateString + heureString).replaceAll("[-:]", "");
		return current_date;
	}

	public static String getNormalizeFileName(String name) {
		String fileName = name;
		fileName = Normalizer.normalize(fileName, Normalizer.Form.NFD);
		fileName = fileName.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		// Suppression de tous les caractères non alphanumériques
		fileName = fileName.replaceAll("[^A-Za-z0-9.()]", "");
		fileName = fileName.substring(0, fileName.length() - 4);
		return fileName;
	}
	public static String getHexString(char[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (char b : bytes) {
			sb.append(Integer.toHexString(b) + " ");
		}
		return sb.toString();
	}

	public static String getHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			sb.append(String.format("%x", b) + " ");
		}
		return sb.toString();
	}
	
	
	public static String getTagsValueDesc(String tag, String xml) {

		String debut = "<" + tag + " desc=\"";

		int debutIndex = 0;
		debutIndex = xml.indexOf(debut);
		if (debutIndex < 0) {
			return "";
		}
		String v = xml.substring(debutIndex + debut.length(), xml.indexOf("\"", debutIndex + debut.length()));
		return v;
	}

	public static ArrayList<String> getTagsValue(String tag, String xml) {

		ArrayList<String> a = new ArrayList<String>();
		String debut = "<" + tag + ">";
		String fin = "</" + tag + ">";
		int debutIndex = 0;
		int finIndex = 0;

		while (true) {
			debutIndex = xml.indexOf(debut, finIndex);

			if (debutIndex < 0)
				break;

			finIndex = xml.indexOf(fin, debutIndex);

			String v = xml.substring(debutIndex + tag.length() + 2, finIndex);
			a.add(v);

		}
		return a;
	}
	
}
