package org.andreschnabel.pecker.tests.helpers;

import org.andreschnabel.pecker.helpers.AssertHelpers;
import org.andreschnabel.pecker.helpers.RegexHelpers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RegexHelpersTest {

	private ArrayList<String[]> expected;

	@Before
	public void setUp() throws Exception {
		expected = new ArrayList<String[]>();
	}

	@Test
	public void testBatchMatchOneGroup() {
		List<String[]> result = RegexHelpers.batchMatch("([a-z]+);?+", "hund;katze;maus");
		expected.add(new String[] {"hund"});
		expected.add(new String[] {"katze"});
		expected.add(new String[]{"maus"});
		testBatchGroupCommon(result);
	}

	@Test
	public void testBatchMatchTwoGroups() {
		List<String[]> result = RegexHelpers.batchMatch("(\\d+):([a-z]+);?+", "0:null;1:eins;2:zwei");
		expected.add(new String[] {"0", "null"});
		expected.add(new String[] {"1", "eins"});
		expected.add(new String[]{"2", "zwei"});
		testBatchGroupCommon(result);
	}

	@Test
	public void testBatchMatchThreeGroups() {
		List<String[]> result = RegexHelpers.batchMatch("(\\w+):(\\w+):(\\w+);?+", "Hans:Mann:23;Peter:Mann:55;Frauke:Frau:88");
		expected.add(new String[] {"Hans", "Mann", "23"});
		expected.add(new String[] {"Peter", "Mann", "55"});
		expected.add(new String[]{"Frauke", "Frau", "88"});
		testBatchGroupCommon(result);
	}

	private void testBatchGroupCommon(List<String[]> result) {
		Assert.assertEquals(expected.size(), result.size());
		for(int i=0; i<expected.size(); i++) {
			String[] exp = expected.get(i);
			String[] actual = result.get(i);
			Assert.assertArrayEquals(exp, actual);
		}
	}

	@Test
	public void testBatchMatchOneGroupMethod() throws Exception {
		List<String> result = RegexHelpers.batchMatchOneGroup("([a-z]+);?+", "hund;katze;maus");
		AssertHelpers.arrayEqualsLstOrderSensitive(new String[] { "hund", "katze", "maus" }, result);
	}
}
