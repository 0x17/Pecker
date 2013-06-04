package org.andreschnabel.pecker.functional;

import java.util.List;

/**
 * Modfizierende Operatoren.
 */
public final class FuncInPlace {

	/**
	 * Nur statische Methoden.
	 */
	private FuncInPlace() {}

	/**
	 * Entferne alle Elemente aus einer Liste, welche ein Prädikat erfüllen.
	 * Arbeitet in-place!
	 * @param pred Prädikat, welches zu entfernende Elemente erfüllen.
	 * @param lst Liste, aus welcher entfernt werden soll.
	 * @param <T> Typ der Elemente.
	 */
	public static <T> void removeAll(IPredicate<T> pred, List<T> lst) {
		List<T> toRem = Func.filter(pred, lst);
		lst.removeAll(toRem);
	}

	/**
	 * Füge ein neues Element zur Liste hinzu, falls es dort noch nicht enthalten ist.
	 * @param lst Liste zu der ein Element hinzugefügt werden soll.
	 * @param o das Element.
	 * @param <T> Typ des Elements.
	 * @return true, gdw. das Element neu hinzugefügt wurde und noch nicht vorher enthalten war.
	 */
	public static <T> boolean addNoDups(List<T> lst, T o) {
		if(!lst.contains(o)) {
			lst.add(o);
			return true;
		} else {
			return false;
		}
	}

}
