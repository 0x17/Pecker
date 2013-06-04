package org.andreschnabel.pecker.helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;

import org.andreschnabel.pecker.functional.Func;
import org.andreschnabel.pecker.functional.IPredicate;
import org.andreschnabel.pecker.functional.ITransform;
import org.andreschnabel.pecker.functional.Tautology;

/**
 * Hilfsfunktionen für den Umgang mit Dateien.
 */
public class FileHelpers {

	/**
	 * Liste rekursiv Dateien innerhalb Wurzelverzeichnis auf (nicht Verzeichnisse), welche ein gegebenes Prädikat erfüllen.
	 * @param root Wurzelverzeichnis als Startknoten.
	 * @param pred Prädikat welches Inklusion von Dateien in Ergebnis entscheidet.
	 * @return Liste von Dateien unterhalb von Wurzelverzeichnis, welche Prädikat erfüllen.
	 * @throws Exception
	 */
	public static List<File> filesWithPredicateInTree(File root, IPredicate<File> pred) throws Exception {
		if(!root.isDirectory())
			throw new Exception("Path must point to dir!");

		List<File> files = new LinkedList<File>();

		for(File f : root.listFiles()) {
			if(f.isDirectory()) {
				files.addAll(filesWithPredicateInTree(f, pred));
			} else {
				if(pred.invoke(f)) {
					files.add(f);
				}
			}
		}

		return files;
	}

	/**
	 * Lade eine Klasse mit gegebenem Namen aus einer .class-Datei mithilfe des Classloaders.
	 * @param f .class-Datei.
	 * @param classname Name der zu ladenden Klasse.
	 * @return Class-Objekt der Klasse.
	 * @throws Exception
	 */
	public static Class<?> loadClassFromFile(File f, String classname) throws Exception {
		Class<?> cls = null;
		URLClassLoader cl = null;
		try {
		    URI uri = f.toURI();
		    URL url = uri.toURL();
		    URL[] urls = new URL[]{url};
		    cl = new URLClassLoader(urls);
		    cls = cl.loadClass(classname);
		} finally {
			// close is missing in Java 6 and OS X still ships with that.
			/*if(cl != null) {
				cl.close();
			}*/
		}
		return cls;
	}

	/**
	 * Schreibe eine Zeichenkette in eine Datei.<br />
	 * Analog zu "spit" in Clojure.
	 * @param str Zeichenkette, welche in Datei geschrieben werden soll.
	 * @param outFilename Datei, in welche die Zeichenkette geschrieben wird.
	 * @throws IOException Schreibfehler.
	 */
	public static void writeStrToFile(String str, String outFilename) throws IOException {
		FileWriter fw = new FileWriter(outFilename);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(str);
		bw.close();
		fw.close();
	}

	/**
	 * Rekursives Löschen von Dateien/Verzeichnissen unterhalb und inklusive des gegebenen Wurzelverzeichnis.<br />
	 * Benutzt Java-Datei-API.
	 * @param root Wurzelverzeichnis.
	 * @throws Exception Löschfehler.
	 */
	public static void deleteDir(File root) throws Exception {
		if(!root.isDirectory())
			throw new Exception("Path must point to dir!");

		for(File f : root.listFiles()) {
			if(f.isDirectory()) {
				deleteDir(f);
			} else
				f.delete();
		}
		root.delete();
	}

	/**
	 * Lese gesamten Inhalt einer gegebenen Textdatei in eine Zeichenkette.<br />
	 * Analog zu "slurp" in Clojure.
	 * @param file Textdatei.
	 * @return Zeichenkette mit Inhalt der Textdatei.
	 * @throws Exception Lesefehler.
	 */
	public static String readEntireFile(File file) throws Exception {
		FileReader fr = new FileReader(file);
		StringBuilder builder = new StringBuilder();
		int ch;
		while((ch = fr.read()) != -1) {
			builder.append((char)ch);
		}
		fr.close();
		return builder.toString();
	}

	/**
	 * Lese gesamten Inhalt einer beliebigen Datei in einen Byte-Buffer ein.
	 * @param file Datei.
	 * @return Byte-Buffer mit Inhalt der Datei.
	 * @throws Exception Lesefehler.
	 */
	public static byte[] readBytes(File file) throws Exception {
		byte[] buf = new byte[(int)file.length()];
		InputStream ios = null;
		try {
			ios = new FileInputStream(file);
			ios.read(buf);
		} finally {
			if(ios != null)
				ios.close();
		}
		return buf;
	}

	/**
	 * Zähle rekursiv Dateien mit gegebener Endung.
	 * @param rootDir Wurzelverzeichnis.
	 * @param extension Datei-Endung. Dateien mit dieser Endung werden mitgezählt.
	 * @return Anzahl der Dateien unterhalb des gegebenen Wurzelverzeichnisses, welche gegebene Endung besitzen.
	 */
	public static int recursivelyCountFilesWithExtension(File rootDir, String extension) {
		if(rootDir.isDirectory()) {
			int sum = 0;
			for(File f : rootDir.listFiles()) {
				sum += recursivelyCountFilesWithExtension(f, extension);
			}
			return sum;

		} else {
			return rootDir.getName().endsWith(extension) ? 1 : 0;
		}
	}

	/**
	 * Liste rekursiv Java-Quelldateien in einem Verzeichnis.
	 * @param dir Verzeichnis.
	 * @return Liste von Java-Quelldateien.
	 * @throws Exception
	 */
	public static List<File> recursiveCollectJavaSrcFiles(File dir) throws Exception {
		IPredicate<File> isJavaSrc = new IPredicate<File>() {
			@Override
			public boolean invoke(File f) {
				return f.getName().endsWith(".java");
			}
		};
		return filesWithPredicateInTree(dir, isJavaSrc);
	}

	/**
	 * Lösche Datei.
	 * @param path Pfad zur Datei.
	 * @throws Exception
	 */
	public static void deleteFile(String path) throws Exception {
		File f = new File(path);
		if(!f.delete()) throw new Exception("Failed to delete file!");
	}

	/**
	 * Stelle sicher, dass Pfad auf Verzeichnis zeigt.
	 * @param path Pfad.
	 * @return Pfad.
	 * @throws Exception
	 */
	public static File enforceDir(String path) throws Exception {
		File root = new File(path);
		if(!root.isDirectory()) {
			throw new Exception("Path must point to directory!");
		}
		return root;
	}

	/**
	 * Liste Java-Dateien in Pfad auf, welche kein Suffix "Test" besitzen.
	 * @param path Pfad.
	 * @return Java-Dateien ohne Suffix "Test".
	 * @throws Exception
	 */
	public static List<String> listProductFiles(String path) throws Exception {
		File root = enforceDir(path);
		List<String> productfiles = new LinkedList<String>();
		recursivelyCollectProductFiles(root, productfiles);
		return productfiles;
	}

	public static void recursivelyCollectProductFiles(File root, List<String> productfiles) {
		for(File f : root.listFiles()) {
			if(f.isDirectory()) {
				recursivelyCollectProductFiles(f, productfiles);
			} else {
				String fname = f.getName();
				if(!fname.endsWith("Test.java") && fname.endsWith(".java"))
					productfiles.add(f.getPath());
			}
		}
	}

	/**
	 * Prüfe, ob Datei mit Pfad existiert.
	 * @param path Pfad auf eine Datei.
	 * @return true, gdw. Datei mit Pfad existiert.
	 */
	public static boolean exists(String path) {
		return new File(path).exists();
	}

	/**
	 * Liste Java-Quelldateien in Verzeichnis auf.
	 * @param dir Verzeichnis.
	 * @return Java-Quelldateien in dir.
	 * @throws Exception
	 */
	public static List<String> listJavaSourceFiles(File dir) throws Exception {
		ITransform<File, String> fileToName = new ITransform<File, String>() {
			@Override
			public String invoke(File f) {
				return f.getName();
			}
		};
		return Func.map(fileToName, FileHelpers.recursiveCollectJavaSrcFiles(dir));
	}

	/**
	 * Liste rekursiv alle Dateien in Verzeichnis auf.
	 * @param root Wurzelverzeichnis.
	 * @return Liste aller Dateien in Verzeichnis.
	 * @throws Exception
	 */
	public static List<File> filesInTree(File root) throws Exception {
		return filesWithPredicateInTree(root, new Tautology<File>());
	}

	/**
	 * Gebe die Dateiendung einer gegebenen Datei zurück.<br />
	 * Bspw.: extension(new File("test.txt")) => "txt".
	 * @param f Datei.
	 * @return Endung der Datei (ohne .)
	 */
	public static String extension(File f) {
		String filename = f.getName();
		if(filename.contains(".") && filename.charAt(0) != '.') {
			String[] parts = filename.split("\\.");
			return parts[parts.length-1];
		} else return "";
	}

	/**
	 * Datei ohne Erweiterung.<br />
	 * Beispiel: trimExtension(new File("test.txt")) => "test".
	 * @param f Datei.
	 * @return Dateiname ohne Erweiterung.
	 */
	public static String trimExtension(File f) {
		return f.getName().substring(0, f.getName().lastIndexOf("."));
	}

	/**
	 * Schreibe Zeichenkette in Datei.
	 * @param str Zeichenkette.
	 * @param outFile Datei zum Schreiben.
	 * @throws Exception
	 */
	public static void writeStrToFile(String str, File outFile) throws Exception {
		FileWriter fw = new FileWriter(outFile);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(str);
		bw.close();
		fw.close();
	}

	/**
	 * Liste Verzeichnisse unterhalb eines Wurzelverzeichnis auf. Wurzelverzeichnis ist nicht enthalten (exklusive).
	 * @param root Wurzelverzeichnis.
	 * @return Liste der Verzeichnisse unterhalb von root. Ohne root.
	 * @throws Exception
	 */
	public static List<File> foldersInTree(File root) throws Exception {
		if(!root.isDirectory())
			throw new Exception("Path must point to dir!");

		List<File> files = new LinkedList<File>();

		for(File f : root.listFiles()) {
			if(f.isDirectory()) {
				files.add(f);
				files.addAll(foldersInTree(f));
			}
		}

		return files;
	}
}
