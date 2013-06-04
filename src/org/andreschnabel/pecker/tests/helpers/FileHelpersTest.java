package org.andreschnabel.pecker.tests.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.andreschnabel.pecker.helpers.AssertHelpers;
import org.andreschnabel.pecker.helpers.FileHelpers;
import org.junit.Test;

public class FileHelpersTest {

	@Test
	public void testWriteStrToFile() throws Exception {
		String fname = "test.txt";
		String content = "test!";
		FileHelpers.writeStrToFile(content, fname);
		assertEquals(content, FileHelpers.readEntireFile(new File(fname)));
		File f = new File(fname);
		f.delete();
	}

	@Test
	public void testDeleteDir() throws Exception {
		String dirname = "testdir";
		File dir = new File(dirname);
		assertTrue(dir.mkdir());
		assertTrue(dir.exists());
		FileHelpers.deleteDir(dir);
		assertFalse(dir.exists());
	}

	@Test
	public void testReadEntireFile() throws Exception {
		String content = FileHelpers.readEntireFile(new File("README.md"));
		String expContent = "# JProjectInspector\n" +
				"\n" +
				"Gather data from GitHub projects to determine testing need.\n" +
				"\n" +
				"**License:** BSD\n" +
				"\n" +
				"## Dependencies\n" +
				"**Third party libraries:**\n" +
				"[ASM](http://asm.ow2.org/)\n" +
				"[org.eclipse.egit.github.core](http://www.eclipse.org/egit/)\n" +
				"[google-gson](https://code.google.com/p/google-gson/)\n" +
				"[JCommon](http://www.jfree.org/jcommon/)\n" +
				"[JFreeChart](http://www.jfree.org/jfreechart/)\n" +
				"[iText](http://itextpdf.com/)\n" +
				"\n" +
				"**Third party tools:**\n" +
				"[CLOC](http://cloc.sourceforge.net/)\n" +
				"[git](http://git-scm.com/)\n" +
				"\n" +
				"*Omnia sub specie aeternitatis.*\n";
		assertEquals(expContent, content);
	}


	@Test
	public void testExtension() throws Exception {
		Assert.assertEquals("md", FileHelpers.extension(new File("README.md")));
		Assert.assertEquals("", FileHelpers.extension(new File(".gitignore")));
	}

	@Test
	public void testFoldersInTree() throws Exception {
		List<File> folders = FileHelpers.foldersInTree(new File("data"));
		File[] expectedFolders = new File[] {
				new File("data/benchmark"),
				new File("data/oldxml"),
				new File("data/old"),
				new File("data/oldresults"),
				new File("data/oldresults/java"),
				new File("data/oldresults/java/lists"),
				new File("data/oldresults/java/summaries"),
		};
		AssertHelpers.arrayEqualsLstOrderInsensitive(expectedFolders, folders);
	}
}
