package org.andreschnabel.pecker.tests.functional;

import junit.framework.Assert;

import org.andreschnabel.pecker.functional.Tautology;
import org.junit.Test;

public class TautologyTest {
	@Test
	public void testInvoke() throws Exception {
		Assert.assertTrue(new Tautology<String>().invoke("anything"));
		Assert.assertTrue(new Tautology<Object>().invoke(new Object()));
		Assert.assertTrue(new Tautology<Object>().invoke(null));
	}
}
