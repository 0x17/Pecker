package org.andreschnabel.pecker.tests.helpers;

import junit.framework.Assert;

import org.andreschnabel.pecker.helpers.AssertHelpers;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AssertHelpersTest {

	private String[] expectedValues;
	private List<String> actualValues;

	@Before
	public void setUp() {
		expectedValues = new String[]{"some", "strings"};
		actualValues = new ArrayList<String>();
		actualValues.add("strings");
		actualValues.add("some");
	}

	@Test
	public void testArrayEqualsLstOrderInsensitiveCorrect() {
		try {
			AssertHelpers.arrayEqualsLstOrderInsensitive(expectedValues, actualValues);
		} catch(Exception e) {
			Assert.fail();
		}
	}

	@Test(expected = AssertionError.class)
	public void testArrayEqualsLstOrderInsensitiveLengthFail() {
		actualValues.add("doesntmatter");
		AssertHelpers.arrayEqualsLstOrderInsensitive(expectedValues, actualValues);
	}

	@Test(expected = AssertionError.class)
	public void testArrayEqualsLstOrderInsensitiveContentFail() {
		actualValues.clear();
		actualValues.add("some");
		actualValues.add("integers");
		AssertHelpers.arrayEqualsLstOrderInsensitive(expectedValues, actualValues);
	}

	@Test
	public void testListNotEmptyOk() throws Exception {
		AssertHelpers.listNotEmpty(actualValues);
	}

	@Test
	public void testArrayNotEmptyOk() throws Exception {
		AssertHelpers.arrayNotEmpty(expectedValues);
	}

	@Test(expected = AssertionError.class)
	public void testListNotEmptyFail() throws Exception {
		List<Object> emptyLst = new LinkedList<Object>();
		AssertHelpers.listNotEmpty(emptyLst);
	}

	@Test(expected = AssertionError.class)
	public void testArrayNotEmptyFail() throws Exception {
		Object[] emptyArray = new Object[0];
		AssertHelpers.arrayNotEmpty(emptyArray);
	}
}
