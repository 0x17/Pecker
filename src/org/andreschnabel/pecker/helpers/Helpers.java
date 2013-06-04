
package org.andreschnabel.pecker.helpers;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hilfsfunktionen
 */
public class Helpers {

	/**
	 * Prüfe ob Anwendung auf einem Unix-System läuft.
	 * @return true, gdw. Anwendung auf Unix-System läuft.
	 */
	public static boolean runningOnUnix() {
		return !System.getProperty("os.name").toLowerCase().contains("win");
	}

	/**
	 * Lade URL in Zeichenkette.
	 * @param urlStr URL von Datei.
	 * @return Inhalt der Datei als Zeichenkette.
	 * @throws Exception Ladefehler.
	 */
	public static String loadUrlIntoStr(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		StringBuilder builder = new StringBuilder();
		while(br.ready()) {
			builder.append(br.readLine()).append("\n");
		}
		br.close();
		return builder.toString();
	}

	/**
	 * Versuche HTML-Seite von URL in Zeichenkette zu laden.
	 * @param urlStr URL von Seite.
	 * @return Inhalt der HTML-Seite als Zeichenkette.
	 * @throws Exception Ladefehler.
	 */
	public static String loadHTMLUrlIntoStr(String urlStr) throws Exception {
		// Taken from: http://stackoverflow.com/questions/1381617/simplest-way-to-correctly-load-html-from-web-page-into-a-string-in-java
		URL url = new URL(urlStr);
		URLConnection con = url.openConnection();
		Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
		Matcher m = p.matcher(con.getContentType());
		/* If Content-Type doesn't match this pre-conception, choose default and 
		 * hope for the best. */
		String charset = m.matches() ? m.group(1) : "ISO-8859-1";
		Reader r = new InputStreamReader(con.getInputStream(), charset);
		StringBuilder buf = new StringBuilder();
		while(true) {
			int ch = r.read();
			if(ch < 0)
				break;
			buf.append((char) ch);
		}
		return buf.toString();
	}

	/**
	 * Fordere zu Eingabe von Text auf.
	 * @param string Aufforderung.
	 * @return eingegebener Text als Zeichenkette.
	 * @throws Exception
	 */
	public static String prompt(String string) throws Exception {
		System.out.print(string + ": ");
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		return br.readLine();
	}

	/**
	 * Fordere zu Eingabe von Passwort in stdin auf.
	 * @param string Eingabeprompt-Aufforderung für stdout.
	 * @return eingegebenes Passwort.
	 * @throws Exception
	 */
	public static String promptPw(String string) throws Exception {
		final boolean RUNNING_IN_STUPID_ECLIPSE = true;
		if(RUNNING_IN_STUPID_ECLIPSE) {
			return prompt(string);
		} else {
			System.out.print(string + ": ");
			Console c = System.console();
			char[] pw = c.readPassword();
			return new String(pw);
		}
	}

	private static List<IObserver<String>> logObservers = new ArrayList<IObserver<String>>();

	public static void addLogObserver(IObserver<String> observer) {
		logObservers.add(observer);
		Helpers.log("Added observer: " + observer.toString());
	}

	public static void removeLogObserver(IObserver<String> observer) {
		logObservers.remove(observer);
	}

	/**
	 * Schreibe Nachricht an stdout ohne angefügtes \n und stelle sicher, dass Puffer geleert worden ist.
	 * @param msg Nachricht.
	 */
	public static void logNoNewline(String msg) {
		System.out.print(msg);
		System.out.flush();
		for(IObserver<String> observer : logObservers) {
			observer.update(msg);
		}
	}

	/**
	 * Schreibe Nachricht an stdout mit angefügtem \n und stelle sicher, dass Puffer geleert worden ist.
	 * @param msg Nachricht.
	 */
	public static void log(String msg) {
		logNoNewline(msg+"\n");
	}

	public static void log(int c) {
		logNoNewline(""+(char)c);
	}

	/**
	 * Dateiendung von ausführbaren Dateien.
	 * @return leerer String für Unix und ".exe" für Windows.
	 */
	public static String executableExtension() {
		return runningOnUnix() ? "" : ".exe";
	}

	/**
	 * Sucht nach Ort von Befehl mithilfe von Unix "type"-Kommando.
	 * @param cmd Befehl.
	 * @return Pfad zu Befehl.
	 * @throws Exception
	 */
	public static String unixType(String cmd) throws Exception {
		String[] parts = ProcessHelpers.monitorProcess(new File("."), "type", cmd).split(" ");
		return parts[parts.length-1].trim();
	}

	/**
	 * Lade HTML-Seite von URL in eine Zeichenkette.<br />
	 * Bei Problemen bis zu maxRetries-mal wiederholt versuchen.
	 * @param url URL der Seite.
	 * @param maxRetries Anzahl der Wiederholungsversuche.
	 * @return Inhalt der HTML-Seite. Evtl. null bei Fehlern.
	 */
	public static String loadHTMLUrlIntoStrRetry(String url, int maxRetries) {
		String out = null;
		for(int i=0; i<maxRetries; i++) {
			try {
				out = loadHTMLUrlIntoStr(url);
				break;
			} catch(Exception e) {
				Helpers.log("Unable to load " + url + "... retry! (" + (i+1) + "/" + maxRetries + ")");
			}
		}
		return out;
	}
}
