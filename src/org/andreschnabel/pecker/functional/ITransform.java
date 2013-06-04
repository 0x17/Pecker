package org.andreschnabel.pecker.functional;

/**
 * Umwandlungsvorschrift.
 * @param <T> Definitionsmenge.
 * @param <U> Zielmenge.
 */
public interface ITransform<T, U> {

	public U invoke(T obj);

}
