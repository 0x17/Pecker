package org.andreschnabel.pecker.helpers;

import java.util.LinkedList;
import java.util.List;

import org.andreschnabel.pecker.functional.Func;
import org.andreschnabel.pecker.functional.IBinaryOperator;
import org.andreschnabel.pecker.functional.IPredicate;

/**
 * Hilfsfunktionen für den Umgang mit Zeichenketten.
 */
public final class StringHelpers {

	/**
	 * Zähle wie oft ein Teilstring word in der Zeichenkette str vorkommt.
	 * @param str Zeichenkette, weilche Teilstring enthält.
	 * @param word Teilstring dessen Häufigkeit in Zeichenkette gezählt werden soll.
	 * @return Anzahl der Vorkommen von word als Teilstring in str.
	 */
	public static int countOccurencesOfWord(String str, String word) {
		int ctr = 0;
		int j = 0;
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i) == word.charAt(j)) {
				if(j == word.length() - 1) {
					ctr++;
					j = 0;
				} else
					j++;
			} else {
				j = 0;
			}
		}
		return ctr;
	}

	public static List<Integer> indicesOf(String str, String substr) {
		List<Integer> indices = new LinkedList<Integer>();
		int j = 0;
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i) == substr.charAt(j)) {
				if(j == substr.length() - 1) {
					indices.add(i-j);
					j = 0;
				} else
					j++;
			} else {
				j = 0;
			}
		}
		return indices;
	}

	public static int countOccurencesOfWords(final String str, String[] words) {
		return Func.reduce(new IBinaryOperator<String, Integer>() {
			@Override
			public Integer invoke(Integer accum, String word) {
				return accum + countOccurencesOfWord(str, word);
			}
		}, 0, words);
	}

	public static boolean strEndsWithOneOf(final String str, String... suffixes) {
		return Func.contains(new IPredicate<String>() {
			@Override
			public boolean invoke(String suffix) {
				return str.endsWith(suffix);
			}
		}, suffixes);
	}

	public static boolean containsOneOf(final String str, String... candidates) {
		return Func.contains(new IPredicate<String>() {
			@Override
			public boolean invoke(String candidate) {
				return str.contains(candidate);
			}
		}, candidates);
	}

	public static boolean equalsOneOf(final String str, final String... candidates) {
		return Func.contains(new IPredicate<String>() {
			@Override
			public boolean invoke(String candidate) {
				return str.equals(candidate);
			}
		}, candidates);
	}

	public static String removeAllWhitespace(String s) {
		return s.replaceAll("\\s", "");
	}

	public static String removeWhitespace(String s) {
		return s.replaceAll("[\n\t]*", "");
	}

	public static String firstLine(String str) {
		return str.substring(0, str.indexOf('\n'));
	}

	public static String lastLine(String str) {
		return str.substring(str.lastIndexOf('\n')+1, str.length());
	}

	public static String capitalizeFirstLetter(String s) {
		if(s == null) return null;
		else if(s.isEmpty()) return s;
		else {
			char firstLetter = Character.toUpperCase(s.charAt(0));
			return firstLetter + s.substring(1);
		}
	}

	public static String removeQuotes(String s) {
		return s.replaceAll("\"", "");
	}

	public static String replaceCorresponding(String str, String[] oldStrs, String[] newStrs) {
		StringBuilder out = new StringBuilder();

		@SuppressWarnings("rawtypes")
		List[] startPositionsLists = new LinkedList[oldStrs.length];

		for(int i=0; i<oldStrs.length; i++) {
			startPositionsLists[i] = StringHelpers.indicesOf(str, oldStrs[i]);
		}

		for(int i=0; i<str.length(); i++) {
			for(int j=0; j<startPositionsLists.length; j++) {
				for(int k=0; k<startPositionsLists[j].size(); k++) {
					int startPos = (Integer)startPositionsLists[j].get(k);
					int endPos = startPos+oldStrs[j].length();
					if(i >= startPos && i < endPos) {
						i = endPos;
						out.append(newStrs[j]);
						continue;
					}
				}
			}

			if(i < str.length()) {
				out.append(str.charAt(i));
			}
		}

		return out.toString();
	}
}
