package org.andreschnabel.pecker.threading;

/**
 * Aufgabe welche ununterbrochen im Hintergrund abläuft, bis explizit das Beenden mit "dispose" gefordert wurde.
 * @param <T> Typ des Ergebnis.
 */
public abstract class ContinuousTask<T> {

	private volatile boolean disposed;

	/**
	 * Breche Ausführung ab!
	 */
	public synchronized void dipose() {
		disposed = true;
	}

	/**
	 * Ergebnis von ununterbrochenem Task.
	 * @param result Ergebnis.
	 */
	public abstract void onSuccess(T result);

	/**
	 * Ein Durchlauf des ununterbrochenen Tasks für ein Ergebnis.
	 * @return das Ergebnis.
	 */
	public abstract T iterateInBackground();

	/**
	 * Führe ununterbrochenen Hintergrundtask aus.
	 */
	public void execute() {
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				while(!disposed) {
					T result = iterateInBackground();
					if(result != null) {
						onSuccess(result);
					}
				}
			}
		};

		Thread t = new Thread(runner);
		t.start();
	}

}
