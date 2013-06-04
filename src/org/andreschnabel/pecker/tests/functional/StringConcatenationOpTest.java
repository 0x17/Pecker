package org.andreschnabel.pecker.tests.functional;

import org.andreschnabel.pecker.functional.StringConcatenationOp;
import org.junit.Assert;
import org.junit.Test;

public class StringConcatenationOpTest {
	@Test
	public void testInvoke() throws Exception {
		String result = new StringConcatenationOp(", ").invoke("This might be a test", "right?");
		Assert.assertEquals("This might be a test, right?", result);

		result = new StringConcatenationOp("").invoke("Concatenate", "This!");
		Assert.assertEquals("ConcatenateThis!", result);
	}
}
