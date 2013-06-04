package org.andreschnabel.pecker.serialization;

import org.andreschnabel.pecker.functional.Func;
import org.andreschnabel.pecker.functional.ITransform;
import org.andreschnabel.pecker.helpers.FileHelpers;
import org.andreschnabel.pecker.helpers.StringHelpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Kapselt CSV-Daten.
 */
public class CsvData {

	/**
	 * Liste aus CSV-Zeilen.
	 * Eine CSV-Zeile besteht aus einem Array von Strings.
	 * Die erste CSV-Zeile enthält die Spaltenbezeichnungen.
	 * Der Index im String-Array ist die Spaltennummer.
	 * Der Index in der Liste ist die Zeilennummer+1 (für Header-Zeile).
	 */
	protected List<String[]> rowList;

	/**
	 * Titel der CSV-Daten. Für Darstellung in GUI nützlich.
	 */
	public String title;

	/**
	 * Konstruktor
	 *
	 * Unbetitelte CSV-Daten.
	 *
	 * @param rowList Liste aus CSV-Zeilen.
	 *
	 * Eine CSV-Zeile besteht aus einem Array von Strings.
	 * Die erste CSV-Zeile enthält die Spaltenbezeichnungen.
	 * Der Index im String-Array ist die Spaltennummer.
	 * Der Index in der Liste ist die Zeilennummer+1 (für Header-Zeile).
	 */
	public CsvData(List<String[]> rowList) {
		this("untitled", rowList);
	}

	/**
	 * Konstruktor
	 *
	 * CSV-Daten mit Titel.
	 *
	 * @param title Titel für die Daten.
	 * @param rowList Liste aus CSV-Zeilen.
	 *
	 * Eine CSV-Zeile besteht aus einem Array von Strings.
	 * Die erste CSV-Zeile enthält die Spaltenbezeichnungen.
	 * Der Index im String-Array ist die Spaltennummer.
	 * Der Index in der Liste ist die Zeilennummer+1 (für Header-Zeile).
	 */
	public CsvData(String title, List<String[]> rowList) {
		this.title = title;
		this.rowList = rowList;
	}

	/**
	 * Kopierkonstruktor.
	 * @param data CSV-Daten.
	 */
	public CsvData(CsvData data) {
		this.rowList = data.rowList;
		this.title = data.title;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < rowList.size(); i++) {
			String[] row = rowList.get(i);
			for(int j = 0; j < row.length; j++) {
				String cell = row[j];
				sb.append(CsvHelpers.escapeIfComma(StringHelpers.removeQuotes(cell)));
				if(j+1<row.length)
					sb.append(',');
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	/**
	 * Bezeichner der Spalten.
	 * @return Array aus Spaltenbezeichnern.
	 */
	public String[] getHeaders() {
		return rowList.get(0);
	}

	/**
	 * Index der Spalte mit Bezeichner.
	 * @param header Bezeichner.
	 * @return Spaltenindex.
	 */
	public int columnWithHeader(String header) {
		return Func.fromArray(getHeaders()).indexOf(header);
	}

	/**
	 * Prüfe ob Spalte mit Bezeichner existiert.
	 * @param header Spaltenbezeichner.
	 * @return true, gdw. Spalte mit Bezeichner existiert.
	 */
	public boolean hasColumnWithHeader(String header) {
		return Func.fromArray(getHeaders()).contains(header);
	}

	/**
	 * Anzahl der tatsächlichen Zeilen ohne Kopfzeile.
	 * @return Zeilenzahl.
	 */
	public int rowCount() {
		return rowList.size()-1;
	}

	/**
	 * Anzahl der Spalten.
	 * @return Spaltenzahl.
	 */
	public int columnCount() {
		return getHeaders().length;
	}

	/**
	 * Inhaltszelle mit Zeilenindex row und Spaltenindex col.
	 * @param row Zeilenindex.
	 * @param col Spaltenindex.
	 * @return Zelleninhalt.
	 */
	public String getCellAt(int row, int col) {
		return rowList.get(row+1)[col];
	}

	/**
	 * Zelleninhalt von Zelle mit Zeilenindex row und Spaltenbezeichner header.
	 * @param row Zeilenindex.
	 * @param header Spaltenbezeichner.
	 * @return Zelleninhalt.
	 */
	public String getCellAt(int row, String header) {
		return rowList.get(row+1)[columnWithHeader(header)];
	}

	/**
	 * Setze Zelleninhalt für Zelle mit Zeilenindex row und Spaltenbezeichner header zu content.
	 * In-place Modifikation der Daten!
	 * @param row Zeilenindex.
	 * @param header Spaltenbezeichner.
	 * @param content Neuer Zelleninhalt.
	 */
	public void setCellAt(int row, String header, String content) {
		rowList.get(row+1)[columnWithHeader(header)] = content;
	}

	/**
	 * Setze Zelleninahlt für Zelle mit Zeilenindex row und Spaltenbezeichner col zu content.
	 * In-place Modifikation der Daten!
	 * @param row Zeilenindex.
	 * @param col Spaltenbezeichner.
	 * @param content Neuer Zelleninhalt.
	 */
	public void setCellAt(int row, int col, String content) {
		rowList.get(row+1)[col] = content;
	}

	/**
	 * Füge neue Spalte mit gegebenem Header rechts zu CSV-Daten als neue letzte Spalte hinzu.
	 * In-place Modifikation der Daten!
 	 * @param header Spaltenbezeichner der neuen Spalte.
	 */
	public void addColumn(String header) {
		ITransform<String[], String[]> addColumnToRow = new ITransform<String[], String[]>() {
			@Override
			public String[] invoke(String[] row) {
				return Arrays.copyOf(row, row.length+1);
			}
		};
		rowList = Func.map(addColumnToRow, rowList);
		rowList.get(0)[columnCount()-1] = header;
	}

	/**
	 * Konvertiere Liste mit gegebener Transformation zu CSV-Daten mit gegebenen Spaltenbezeichnungen.
	 * @param headers Spaltenbezeichnungen.
	 * @param elemToRow Transformationsvorschrift von Listenelement zu CSV-Zeile.
	 * @param lst Liste beliebigen Typs T.
	 * @param <T> Typ der Listenelemente.
	 * @return CSV-Daten-Darstellung der Liste.
	 * @throws Exception
	 */
	public static <T> CsvData fromList(String[] headers, ITransform<T, String[]> elemToRow, List<T> lst) throws Exception {
		List<String[]> rows = new ArrayList<String[]>(lst.size()+1);
		rows.add(headers);
		for(T elem : lst) {
			String[] row = elemToRow.invoke(elem);

			if(row.length != headers.length) {
				throw new Exception("Row length is " + row.length + " but header count is " + headers.length);
			}

			rows.add(row);
		}
		return new CsvData(rows);
	}

	/**
	 * Speicher CSV-Daten in Textdatei.
	 * @param file Datei in die gespeichert wird.
	 * @throws Exception
	 */
	public void save(File file) throws Exception {
		FileHelpers.writeStrToFile(toString(), file);
	}

	/**
	 * Liefer Spalte mit gegebener Bezeichnung.
	 * @param header Spaltenbezeichner.
	 * @return Liste der Zellen dieser Spalte.
	 */
	public List<String> getColumn(String header) {
		final int columnIndex = columnWithHeader(header);
		ITransform<String[], String> cellForColumn = new ITransform<String[], String>() {
			@Override
			public String invoke(String[] row) {
				return row[columnIndex];
			}
		};
		return Func.map(cellForColumn, rowList);
	}

	/**
	 * Wandel CSV-Datei in Liste.
	 * @param rowToElem Umwandlung von CSV-Zeile in Element.
	 * @param f CSV-Datei.
	 * @param <T> Typ der Listenelemente.
	 * @return Liste, welche von CSV-Datei repräsentiert worden ist.
	 * @throws Exception
	 */
	public static <T> List<T> toList(ITransform<String[], T> rowToElem, File f) throws Exception {
		CsvData data = CsvHelpers.parseCsv(f);
		return toList(rowToElem, data);
	}

	/**
	 * Wandel CSV-Daten in Liste um.
	 * @param rowToElem Umwandlungsvorschrift von CSV-Zeile in Element.
	 * @param data CSV-Daten.
	 * @param <T> Typ der Listenelemente.
	 * @return Liste, welche von den CSV-Daten repräsentiert worden ist.
	 * @throws Exception
	 */
	public static <T> List<T> toList(ITransform<String[], T> rowToElem, CsvData data) throws Exception {
		List<T> lst = new ArrayList<T>(data.rowCount()-1);
		for(int row = 0; row<data.rowCount(); row++) {
			lst.add(rowToElem.invoke(data.getRow(row)));
		}
		return lst;
	}

	/**
	 * Inhalts-Zeile mit Index row.
	 * @param row Zeilenindex.
	 * @return Zeile des Inhalts mit Index row. Keine Kofpzeile. Array aus Zelleninahlten.
	 */
	public String[] getRow(int row) {
		return rowList.get(row+1);
	}

	/**
	 * Spaltenbezeichnung für Spalte mit Index column.
	 * @param column Spaltenindex.
	 * @return Spaltenbezeichnung.
	 */
	public String columnName(int column) {
		return getHeaders()[column];
	}

	/**
	 * Entferne Zeile mit gegebenen Zeilenindex. Ohne Kopfzeile.
	 * @param row Zeilenindex.
	 */
	public void removeRow(int row) {
		rowList.remove(row + 1);
	}

	/**
	 * Füge neue Spalte über gegebenem Index hinzu.
	 * @param row Zeilenindex.
	 */
	public void addRow(int row) {
		String[] nrow = new String[columnCount()];
		rowList.add(row, nrow);
	}
}
