package org.andreschnabel.pecker.functional;

/**
 * Indizierte Aktion.
 */
public interface IVarIndexedAction {

	/**
	 * Führe Aktion aus.
	 * @param indices Indizes.
	 */
	public void invoke(int[] indices);

}
