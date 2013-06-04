package org.andreschnabel.pecker.helpers;

import org.junit.Assert;

import java.util.List;

/**
 * Hilfsfunktionen für Assertions in Tests.
 */
public class AssertHelpers {

	public static <T> void arrayEqualsLstOrderInsensitive(T[] expectedValues, List<T> actualValues) {
		Assert.assertEquals(expectedValues.length, actualValues.size());
		for(T val : expectedValues) {
			if(!actualValues.contains(val)) {
				Assert.fail("Value " + val + " missing!");
			}
		}
	}

	public static <T> void arrayEqualsLstOrderSensitive(T[] expectedValues, List<T> actualValues) {
		Assert.assertEquals(expectedValues.length, actualValues.size());
		for(int i = 0; i < expectedValues.length; i++) {
			if(!expectedValues[i].equals(actualValues.get(i))) {
				Assert.fail(i + "th element not equal, is " + actualValues.get(i) + " should be " + expectedValues[i]);
			}
		}
	}

	public static <T> void lstEqualsLstOrderSensitive(List<T> expectedValues, List<T> actualValues) {
		Assert.assertEquals(expectedValues.size(), actualValues.size());
		for(int i = 0; i < expectedValues.size(); i++) {
			if(!expectedValues.get(i).equals(actualValues.get(i))) {
				Assert.fail(i + "th element not equal, is " + actualValues.get(i) + " should be " + expectedValues.get(i));
			}
		}
	}

	public static <T> void arrayNotEmpty(T[] arr) {
		Assert.assertNotNull(arr);
		Assert.assertTrue(arr.length > 0);
	}

	public static <T> void listNotEmpty(List<T> lst) {
		Assert.assertNotNull(lst);
		Assert.assertTrue(lst.size() > 0);
	}
}
