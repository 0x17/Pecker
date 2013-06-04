package org.andreschnabel.pecker.threading;

import javax.swing.*;

/**
 * Aufgabe, welche in einem Hintergrundthread ausgeführt wird.
 * Berechnet im Hintergrund ein Ergebnis und liefert dieses dann
 * an den Oberflächenthread zurück, wenn fertig.
 *
 * Benutze zum asynchronen Ausführen von blockierende Anweisungen für Oberflächen-Code.
 *
 * @param <T> Typ des Ergebnisses.
 */
public abstract class AsyncTask<T> {

	/**
	 * Reagiere auf berechnetes Ergebnis.
	 * @param result das Ergebnis.
	 */
	public abstract void onFinished(T result);

	/**
	 * Berechne Ergebnis.
	 * @return das Ergebnis.
	 */
	public abstract T doInBackground();

	/**
	 * Führe asynchronen Task aus.
	 */
	public void execute() {
		SwingWorker<T, Object> worker = new SwingWorker<T, Object>() {
			@Override
			protected T doInBackground() throws Exception {
				return backFunc();
			}

			@Override
			protected void done() {
				super.done();
				try {
					onFinished(get());
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		worker.execute();
	}

	private T backFunc() {
		return doInBackground();
	}
}
