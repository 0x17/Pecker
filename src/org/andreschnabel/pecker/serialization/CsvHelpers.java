package org.andreschnabel.pecker.serialization;

import org.andreschnabel.pecker.helpers.FileHelpers;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Parse GoogleDocs CSV results.
 * List of rows.
 * '([col11 col12 ... col1n] ... [colm1 colm2 ... colmn])
 */
public class CsvHelpers {

	/**
	 * Zähle Anzahl der Spalten in CSV-Textzeile.
	 * @param line CSV-Textzeile.
	 * @return Spaltenzahl.
	 */
	public static int countColumns(String line) {
		int numColums = 1;
		boolean escaped = false;
		for(int i=0; i<line.length(); i++) {
			char c = line.charAt(i);
			switch(c) {
			case ',':
				if(!escaped) numColums++;
				break;
			case '"':
				escaped = !escaped;
				break;
			}
		}
		return numColums;
	}

	/**
	 * Extrahiere Kopfzeile (erste Zeile) aus CSV-Text.
	 * @param content CSV-Text.
	 * @return Kopfzeile.
	 */
	public static String headerLine(String content) {
		return content.contains("\n") ? content.substring(0, content.indexOf("\n")) : content;
	}

	/**
	 * Parse CSV-Daten aus CSV-Text.
	 * @param content CSV-Text.
	 * @return CSV-Daten.
	 * @throws Exception
	 */
	public static CsvData parseCsv(String content) throws Exception {
		if(!content.endsWith("\n"))
			content += "\n";
		
		List<String[]> rows = new LinkedList<String[]>();	
		int numColumns = countColumns(headerLine(content));
		
		String[] curRow = new String[numColumns];
		int curColumn = 0;
		
		StringBuffer buf = new StringBuffer();
		
		boolean escaped = false;

		int curLineNum = 0;
		
		for(int i=0; i<content.length(); i++) {			
			char c = content.charAt(i);
			
			if(c == '"') {
				escaped = !escaped;
			}
			
			switch(c) {
			case '\n':
				curLineNum++;
				if(curColumn == numColumns-1) {					
					curRow[curColumn++] = buf.toString();
					buf = new StringBuffer();
					rows.add(curRow);
					curRow = new String[numColumns];
					curColumn = 0;
				}
				break;
				
			case ',':
				if(!escaped) {
					if(curColumn == numColumns) {
						throw new Exception("Line malformed with too many columns: " + content.split("\n")[curLineNum]);
					}
					curRow[curColumn++] = buf.toString();
					buf = new StringBuffer();
				}
				break;
				
			default:
				buf.append(c);
				break;
			}
		}
		
		return new CsvData(rows);
	}

	/**
	 * Parse CSV-Daten aus CSV-Datei.
	 * @param file CSV-Datei.
	 * @return CSV-Daten.
	 * @throws Exception
	 */
	public static CsvData parseCsv(File file) throws Exception {
		return parseCsv(FileHelpers.readEntireFile(file));
	}

	/**
	 * Escape Zelle mit Anführungsstrichen, falls sie ein Komma enthält.
	 * @param str Zelle.
	 * @return Zelle oder Zelle in Anführungsstrichen, falls sie Komma enthält.
	 */
	public static String escapeIfComma(String str) {
		return str.contains(",") ? "\"" + str + "\"" : str;
	}
}
