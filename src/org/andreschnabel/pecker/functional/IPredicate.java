package org.andreschnabel.pecker.functional;

/**
 * Prädikat.
 * @param <T> Typ des geprüften Objekts.
 */
public interface IPredicate<T> {

	public boolean invoke(T obj);

}
