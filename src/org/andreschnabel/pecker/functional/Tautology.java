package org.andreschnabel.pecker.functional;

/**
 * Tautologie. Ist immer wahr.
 * @param <T> Typ des zu prüfenden Objekts.
 */
public final class Tautology<T> implements IPredicate<T> {
	@Override
	public boolean invoke(T obj) {
		return true;
	}
}
