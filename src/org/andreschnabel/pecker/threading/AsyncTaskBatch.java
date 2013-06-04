package org.andreschnabel.pecker.threading;

import java.util.ArrayList;
import java.util.List;

/**
 * Eine Menge von sequentiell im Hintergrund ausgeführten evtl. blockierenden Aufgaben.
 * @param <T> Typ des Ergebnis.
 */
public class AsyncTaskBatch<T> {

	private final List<AsyncTask<T>> tasks;
	private volatile boolean disposed;
	private volatile boolean done;

	/**
	 * Konstruktor
	 * @param size Anzahl der Aufgaben.
	 */
	public AsyncTaskBatch(int size) {
		tasks = new ArrayList<AsyncTask<T>>(size);
	}

	/**
	 * Ausführung der Aufgaben abbrechen.
	 */
	public synchronized void dipose() {
		disposed = true;
	}

	/**
	 * Ausführung der Aufgaben erfolgreich beendet.
	 * @return true, gdw. Ausführung beendet.
	 */
	public synchronized boolean isDone() {
		return done;
	}

	/**
	 * Füge neue Aufgabe hinzu.
	 * @param task In dieser Menge auszuführende Aufgabe.
	 */
	public void add(AsyncTask<T> task) {
		tasks.add(task);
	}

	/**
	 * Führe sequentiell die hinzugefügten Aufgaben im Hintergrund aus.
	 */
	public void execute() {
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				for(AsyncTask<T> task : tasks) {
					if(disposed) {
						return;
					}
					task.onFinished(task.doInBackground());
				}
				done = true;
			}
		};

		Thread t = new Thread(runner);
		t.start();
	}

}
