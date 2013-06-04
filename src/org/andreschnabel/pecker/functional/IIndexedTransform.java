package org.andreschnabel.pecker.functional;

/**
 * Indizierte Umwandlungsvorschrift.
 * @param <T> Definitionsmenge.
 * @param <U> Zielmenge.
 */
public interface IIndexedTransform<T, U> {

	public U invoke(int i, T obj);

}
