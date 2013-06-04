package org.andreschnabel.pecker.tests.helpers;

import org.andreschnabel.pecker.helpers.ProcessHelpers;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class ProcessHelpersTest {
	@Test
	public void testMonitorProcess() throws Exception {
		String str = "\"To be or not to be?\"";
		String out = ProcessHelpers.monitorProcess(new File("."), "echo", str);
		Assert.assertEquals(str, out.trim());
	}

	@Test
	public void testSystemStr() throws Exception {
		ProcessHelpers.system("echo \"Was ist ist, was nicht ist ist möglich.\"");
	}

	@Test
	public void testSystemArray() throws Exception {
		ProcessHelpers.system(new File("."), "echo", "\"Was ist ist, was nicht ist ist möglich.\"");
	}
}
