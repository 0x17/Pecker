package org.andreschnabel.pecker.tests.serialization;

import org.andreschnabel.pecker.functional.ITransform;
import org.andreschnabel.pecker.helpers.AssertHelpers;
import org.andreschnabel.pecker.serialization.CsvData;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CsvDataTest {

	@Test
	public void testToString() {
		List<String[]> rows = new LinkedList<String[]>();
		rows.add(new String[]{"A","B","C"});
		rows.add(new String[]{"1","2","3"});
		CsvData data = new CsvData(rows);
		String str = data.toString();
		Assert.assertEquals("A,B,C\n1,2,3\n", str);

		rows.clear();
		rows.add(new String[] {"name", "tag", "beschreibung"});
		rows.add(new String[] {"Hans", "12.03.2010", "Dieser Text enthält ein Komma! , :)"});
		rows.add(new String[] {"Hans,mit,Kommas", "01.01.2001", "Noch ein Text"});
		data = new CsvData(rows);
		str = data.toString();
		String expectedStr = "name,tag,beschreibung\nHans,12.03.2010,\"Dieser Text enthält ein Komma! , :)\"\n\"Hans,mit,Kommas\",01.01.2001,Noch ein Text\n";
		Assert.assertEquals(expectedStr, str);
	}

	@Test
	public void testFromList() throws Exception {
		Person[] personsArray = new Person[] {
				new Person("Heinrich", 44),
				new Person("Peter", 57),
		};
		ITransform<Person, String[]> personToRow = new ITransform<Person, String[]>() {
			@Override
			public String[] invoke(Person p) {
				return new String[] {p.name, String.valueOf(p.age)};
			}
		};
		CsvData data = CsvData.fromList(new String[]{"name", "age"}, personToRow, Arrays.asList(personsArray));
		Assert.assertArrayEquals(new String[]{"name", "age"}, data.getHeaders());
		Assert.assertArrayEquals(new String[]{"Heinrich", "44"}, data.getRow(0));
		Assert.assertArrayEquals(new String[]{"Peter", "57"}, data.getRow(1));
	}

	@Test
	public void testDataToList() throws Exception {
		List<String[]> rows = new LinkedList<String[]>();
		rows.add(new String[]{"name", "age"});
		rows.add(new String[]{"Heinrich", "44"});
		rows.add(new String[]{"Peter", "57"});
		CsvData data = new CsvData(rows);
		ITransform<String[], Person> rowToPerson = new ITransform<String[], Person>() {
			@Override
			public Person invoke(String[] sa) {
				return new Person(sa[0], Integer.valueOf(sa[1]));
			}
		};
		List<Person> persons = CsvData.toList(rowToPerson, data);
		AssertHelpers.arrayEqualsLstOrderSensitive(new Person[] {
				new Person("Heinrich", 44),
				new Person("Peter", 57),
		}, persons);
	}

	private static class Person {
		public String name;
		public int age;

		private Person(String name, int age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;

			Person person = (Person) o;

			if(age != person.age) return false;
			if(name != null ? !name.equals(person.name) : person.name != null) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = name != null ? name.hashCode() : 0;
			result = 31 * result + age;
			return result;
		}
	}
}
