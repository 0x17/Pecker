package org.andreschnabel.pecker.tests.threading;

import junit.framework.Assert;

import org.andreschnabel.pecker.threading.AsyncTask;
import org.junit.Test;

import javax.swing.*;

public class AsyncTaskTest {

	private volatile int accum = 0;

	@Test
	public void testExecute() throws Exception {
		JFrame frm = new JFrame();
		frm.setVisible(true);
		AsyncTask<Integer> task = new AsyncTask<Integer>() {
			@Override
			public void onFinished(Integer result) {
				accum+=result;
			}

			@Override
			public Integer doInBackground() {
				return 10;
			}
		};
		task.execute();
		accum++;
		Thread.sleep(100);
		Assert.assertEquals(11, accum);
		frm.dispose();
	}
}
