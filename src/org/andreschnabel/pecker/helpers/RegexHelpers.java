package org.andreschnabel.pecker.helpers;

import org.andreschnabel.pecker.functional.Func;
import org.andreschnabel.pecker.functional.ITransform;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hilfsfunktionen für den Umgang mit regulären Ausdrücken.
 */
public class RegexHelpers {
	public static List<String[]> batchMatch(String regex, String input) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);

		List<String[]> matches = new LinkedList<String[]>();

		while(m.find()) {
			String[] match = new String[m.groupCount()];
			for(int i=0; i<match.length; i++) {
				match[i] = m.group(i+1);
			}
			matches.add(match);
		}

		return matches;
	}

	public static List<String> batchMatchOneGroup(String regex, String input) {
		List<String[]> matches = batchMatch(regex, input);
		ITransform<String[], String> first = new ITransform<String[], String>() {
			@Override
			public String invoke(String[] strs) {
				return strs[0];
			}
		};
		return Func.map(first, matches);
	}
}
