package org.andreschnabel.pecker.tests.functional;

import java.util.Arrays;
import java.util.List;

import org.andreschnabel.pecker.functional.Func;
import org.andreschnabel.pecker.functional.FuncInPlace;
import org.andreschnabel.pecker.functional.IPredicate;
import org.andreschnabel.pecker.helpers.AssertHelpers;
import org.junit.Assert;
import org.junit.Test;

public class FuncInPlaceTest {
	@Test
	public void testRemoveAll() throws Exception {
		List<Integer> nums = Func.countUpTo(10);
		IPredicate<Integer> isOdd = new IPredicate<Integer>() {
			@Override
			public boolean invoke(Integer n) {
				return n % 2 == 1;
			}
		};
		FuncInPlace.removeAll(isOdd, nums);
		Integer[] evens = new Integer[]{2, 4, 6, 8, 10};
		AssertHelpers.lstEqualsLstOrderSensitive(Arrays.asList(evens), nums);
	}

	@Test
	public void testAddNoDups() throws Exception {
		List<Integer> nums = Func.countUpTo(10);
		FuncInPlace.addNoDups(nums, 4);
		Assert.assertEquals(10, nums.size());
		FuncInPlace.addNoDups(nums, 11);
		Assert.assertEquals(11, nums.size());
		Assert.assertTrue(nums.contains(11));
	}
}
