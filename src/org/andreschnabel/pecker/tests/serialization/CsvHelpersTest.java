package org.andreschnabel.pecker.tests.serialization;

import org.andreschnabel.pecker.serialization.CsvData;
import org.andreschnabel.pecker.serialization.CsvHelpers;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class CsvHelpersTest {

	@Test
	public void testCountColumns() {
		Assert.assertEquals(1, CsvHelpers.countColumns("a"));
		Assert.assertEquals(2, CsvHelpers.countColumns("something,\"some text\""));
		Assert.assertEquals(3, CsvHelpers.countColumns("a,b,c"));		
	}

	@Test
	public void testParseCsvFile() throws Exception {
		CsvData data = CsvHelpers.parseCsv(new File("data/KandidatenProjekteUmfrage1.csv"));
		Assert.assertTrue(data.rowCount() > 0);
		Assert.assertTrue(data.columnCount() > 0);
	}

	@Test
	public void testParseCsvStringSingleLine() throws Exception {
		String csvLine = "Name,\"Age\",date of birth\nAndre,25,11.12.1987";
		String[] expectedHeaders = new String[] {"Name", "\"Age\"", "date of birth"};
		String[] expectedFirstRow = new String[] {"Andre", "25", "11.12.1987"};
		
		CsvData data = CsvHelpers.parseCsv(csvLine);
		Assert.assertEquals(1, data.rowCount());
		
		String[] actualHeaders = data.getHeaders();
		Assert.assertArrayEquals(expectedHeaders, actualHeaders);

		String[] actualFirstRow = data.getRow(0);
		Assert.assertArrayEquals(expectedFirstRow, actualFirstRow);
	}
	
	@Test
	public void testParseCsvStringMultiLines() throws Exception {
		String csvLines = "Name,Age,Comment\nPeter,23,Testcomment\nHans,44,Finish\n";
		
		String[] row1 = new String[] {"Name", "Age", "Comment"};
		String[] row2 = new String[] {"Peter", "23", "Testcomment"};
		String[] row3 = new String[] {"Hans", "44", "Finish"};

		CsvData data = CsvHelpers.parseCsv(csvLines);

		Assert.assertEquals(2, data.rowCount());
		
		Assert.assertArrayEquals(row1, data.getHeaders());
		Assert.assertArrayEquals(row2, data.getRow(0));
		Assert.assertArrayEquals(row3, data.getRow(1));
	}

}
