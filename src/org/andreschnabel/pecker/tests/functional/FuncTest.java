package org.andreschnabel.pecker.tests.functional;

import java.util.Arrays;
import java.util.List;

import org.andreschnabel.pecker.functional.Func;
import org.andreschnabel.pecker.functional.IBinaryOperator;
import org.andreschnabel.pecker.functional.IIndexedTransform;
import org.andreschnabel.pecker.functional.IPredicate;
import org.andreschnabel.pecker.functional.ITransform;
import org.andreschnabel.pecker.functional.IVarIndexedAction;
import org.andreschnabel.pecker.helpers.AssertHelpers;
import org.junit.Assert;
import org.junit.Test;

public class FuncTest {
	@Test
	public void testContains() throws Exception {
		List<Integer> nums = Func.countUpTo(10);

		IPredicate<Integer> pred10 = new IPredicate<Integer>() {
			@Override
			public boolean invoke(Integer num) {
				return num == 10;
			}
		};
		Assert.assertTrue(Func.contains(pred10, nums));

		IPredicate<Integer> pred20 = new IPredicate<Integer>() {
			@Override
			public boolean invoke(Integer num) {
				return num == 20;
			}
		};
		Assert.assertFalse(Func.contains(pred20, nums));
	}

	@Test
	public void testRange() throws Exception {
		Integer[] zeroToNine = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		AssertHelpers.arrayEqualsLstOrderSensitive(zeroToNine, Func.range(10));
	}

	@Test
	public void testCountUpTo() throws Exception {
		Integer[] oneToTen = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		AssertHelpers.arrayEqualsLstOrderSensitive(oneToTen, Func.countUpTo(10));
	}

	@Test
	public void testMap() throws Exception {
		Integer[] oneToTen = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		Integer[] oneToTenSquared = new Integer[] { 1, 4, 9, 16, 25, 36, 49, 64, 81, 100 };
		ITransform<Integer, Integer> square = new ITransform<Integer, Integer>() {
			@Override
			public Integer invoke(Integer obj) {
				return obj * obj;
			}
		};
		AssertHelpers.arrayEqualsLstOrderSensitive(oneToTenSquared, Func.map(square, Func.fromArray(oneToTen)));
	}

	@Test
	public void testMapi() throws Exception {
		String[] names = new String[] { "Hans", "Peter", "Uwe", "Harald", "Anton", "Heinrich", "Hermann", "Siegfried", "Joachim", "Heinz" };
		IIndexedTransform<String, Integer> itrans = new IIndexedTransform<String, Integer>() {
			@Override
			public Integer invoke(int i, String obj) {
				return i;
			}
		};
		AssertHelpers.lstEqualsLstOrderSensitive(Func.range(10), Func.mapi(itrans, Func.fromArray(names)));
	}

	@Test
	public void testFromArray() throws Exception {
		Integer[] oneToTen = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		AssertHelpers.arrayEqualsLstOrderSensitive(oneToTen, Func.fromArray(oneToTen));
	}

	@Test
	public void testFilter() throws Exception {
		Integer[] evens = new Integer[] { 2, 4, 6, 8, 10 };
		IPredicate<Integer> isEven = new IPredicate<Integer>() {
			@Override
			public boolean invoke(Integer num) {
				return num % 2 == 0;
			}
		};
		AssertHelpers.arrayEqualsLstOrderSensitive(evens, Func.filter(isEven, Func.countUpTo(10)));
	}

	@Test
	public void testCount() throws Exception {
		IPredicate<Integer> isEven = new IPredicate<Integer>() {
			@Override
			public boolean invoke(Integer num) {
				return num % 2 == 0;
			}
		};
		Assert.assertEquals(5, Func.count(isEven, Func.countUpTo(10)));
	}

	@Test
	public void testFind() throws Exception {
		List<Integer> nums = Func.countUpTo(10);

		IPredicate<Integer> pred10 = new IPredicate<Integer>() {
			@Override
			public boolean invoke(Integer num) {
				return num == 10;
			}
		};
		Assert.assertEquals(10, (int) Func.find(pred10, nums));

		IPredicate<Integer> pred20 = new IPredicate<Integer>() {
			@Override
			public boolean invoke(Integer num) {
				return num == 20;
			}
		};
		Assert.assertEquals(null, Func.find(pred20, nums));
	}

	@Test
	public void testReduce() throws Exception {
		List<Integer> nums = Func.countUpTo(10);
		int sumOfIntsTo10 = 10 * 11 / 2;
		IBinaryOperator<Integer, Integer> add = new IBinaryOperator<Integer, Integer>() {
			@Override
			public Integer invoke(Integer a, Integer b) {
				return a + b;
			}
		};
		Assert.assertEquals(sumOfIntsTo10, (int) Func.reduce(add, 0, nums));
	}

	@Test
	public void testRemDups() throws Exception {
		List<Integer> noDups = Func.remDups(Arrays.asList(1, 2, 2, 3, 4, 5, 5, 5, 6, 5, 3, 1, 0));
		Assert.assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 0), noDups);
	}

	@Test
	public void testNestedForDepth1() throws Exception {
		final StringBuilder sb = new StringBuilder();
		Func.nestedFor(1, 0, 10, new IVarIndexedAction() {
			@Override
			public void invoke(int[] indices) {
				sb.append(indices[0] + ",");
			}
		});
		Assert.assertEquals("0,1,2,3,4,5,6,7,8,9,", sb.toString());
	}

	@Test
	public void testNestedForDepth2() throws Exception {
		final StringBuilder sb = new StringBuilder();
		IVarIndexedAction action = new IVarIndexedAction() {
			@Override
			public void invoke(int[] indices) {
				sb.append(indices[0]+","+indices[1]+"\n");
			}
		};
		Func.nestedFor(2, 0, 2, action);
		Assert.assertEquals("0,0\n0,1\n1,0\n1,1\n", sb.toString());
	}
}
