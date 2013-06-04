package org.andreschnabel.pecker.tests.helpers;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.andreschnabel.pecker.helpers.AssertHelpers;
import org.andreschnabel.pecker.helpers.StringHelpers;
import org.junit.Assert;
import org.junit.Test;

public class StringHelpersTest {

	@Test
	public void testFirstLine() throws Exception {
		String fline = StringHelpers.firstLine("This is the first line.\nThis is the second line.\nCorrect guess: This is the third.");
		Assert.assertEquals("This is the first line.", fline);
	}

	@Test
	public void testLastLine() throws Exception {
		String lline = StringHelpers.lastLine("This is the first line.\nThis is the second line.\nThis is the last line.");
		Assert.assertEquals("This is the last line.", lline);
	}

	@Test
	public void testStrContainsOneOf() {
		Assert.assertTrue(StringHelpers.containsOneOf("dies ist ein test", "ein"));
		Assert.assertFalse(StringHelpers.containsOneOf("dies ist ein test", ":D"));
	}

	@Test
	public void testCapitalize() {
		Assert.assertEquals("Bernd", StringHelpers.capitalizeFirstLetter("bernd"));
		assertEquals("Bernd", StringHelpers.capitalizeFirstLetter("Bernd"));
	}

	@Test
	public void testCountOccurencesOfWord() {
		assertEquals(4, StringHelpers.countOccurencesOfWord("dewiofjwoiefjewofjblablablaeiofjiowejfbla", "bla"));
	}

	@Test
	public void testCountOccurencesOfWords() {
		assertEquals(4, StringHelpers.countOccurencesOfWords("asiodjiosjdoaisdjiaosdjasiodjblabluppblasjidojsdiobla", new String[]{"bla", "blupp"}));
	}

	@Test
	public void testIndicesOf() throws Exception {
		List<Integer> indices = StringHelpers.indicesOf("Bla something blupp something somethingsomething", "something");
		AssertHelpers.arrayEqualsLstOrderSensitive(new Integer[]{4, 20, 30, 39}, indices);
	}

	@Test
	public void testReplaceCorresponding() throws Exception {
		String result = StringHelpers.replaceCorresponding("Fight Club Is Closed", new String[] {"Fight", "Closed"}, new String[] {"Dance", "Open"});
		Assert.assertEquals("Dance Club Is Open", result);
	}

	@Test
	public void testReplaceCorrespondingSingleCharStrs() throws Exception {
		String result = StringHelpers.replaceCorresponding("a*b/c", new String[]{"a", "b", "c"}, new String[] {"Velocity", "Mass", "Force"});
		Assert.assertEquals("Velocity*Mass/Force", result);
	}

	@Test
	public void testReplaceCorrespondingMultipleOccurences() throws Exception {
		String result = StringHelpers.replaceCorresponding("Come on feed the da da da da da", new String[]{"da"}, new String[]{"DA"});
		Assert.assertEquals("Come on feed the DA DA DA DA DA", result);
	}
}
