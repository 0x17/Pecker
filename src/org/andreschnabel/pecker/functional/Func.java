package org.andreschnabel.pecker.functional;

import java.util.*;

/**
 * Funktionen höherer Ordnung für einen funktionalen Programmierstil.
 */
public final class Func {

	/**
	 * Nur statische Methoden.
	 */
	private Func() {}

	/**
	 * Prüfe, ob eine Collection ein Element enthält, welches ein gegebenes Prädikat erfüllt.
	 *
	 * Entspricht Existenzquantor: wahr, gdw. (es gibt ein X in Sammlung, sodass gilt Prädikat(X)).
	 *
	 * Beispiel: '(1, 2, 3).contains(3) => true, '(1, 2, 3).contains(5) => false.
	 *
	 * @param pred Bedingung für Objekt, nach welcher geprüft wird.
	 * @param coll iterierbare Sammlung von Objekten des Typs T.
	 * @param <T> Typ der Objekte.
	 * @return true, gdw. die Sammlung mindestens ein Objekt enthält, für welches das Prädikat erfüllt ist.
	 */
	public static <T> boolean contains(IPredicate<T> pred, Iterable<T> coll) {
		for(T obj : coll) {
			if(pred.invoke(obj))
				return true;
		}
		return false;
	}

	/**
	 * Prüfe, ob ein Array ein Element enthält, welches ein gegebenes Prädikat erfüllt.
	 *
	 * Entspricht Existenzquantor: wahr, gdw. (es gibt ein X in Sammlung, sodass gilt Prädikat(X)).
	 *
	 * Beispiel: '(1, 2, 3).contains(3) => true, '(1, 2, 3).contains(5) => false.
	 *
	 * @param pred Bedingung für Objekt, nach welcher geprüft wird.
	 * @param arr Array aus Objekten vom Typ T.
	 * @param <T> Typ der Objekte.
	 * @return true, gdw. das Array mindestens ein Objekt enthält, für welches das Prädikat erfüllt ist.
	 */
	public static <T> boolean contains(IPredicate<T> pred, T[] arr) {
		return contains(pred, Arrays.asList(arr));
	}

	/**
	 * Intervall ganzer Zahlen von 0 bis n-1, d.h. (0, 1, ..., n-1).
	 * @param n obere Intervallgrenze. Exklusive.
	 * @return Intervall (0, 1, ..., n-1) als Integer-Liste.
	 */
	public static List<Integer> range(int n) {
		List<Integer> nums = new ArrayList<Integer>(n);
		for(int i=0; i<n; i++) {
			nums.add(i);
		}
		return nums;
	}

	/**
	 * Zähle von 1 bis n.
	 * Intervall ganzer Zahlen von 1 bis n, d.h. (1, 2, ..., n).
	 * @param n obere Intervallgrenze. Inklusive.
	 * @return Intervall (1, 2, ..., n) als Integer-Liste.
	 */
	public static List<Integer> countUpTo(int n) {
		List<Integer> nums = new ArrayList<Integer>(n);
		for(int i=0; i<n; i++) {
			nums.add((i+1));
		}
		return nums;
	}

	/**
	 * Bilde jedes Element aus einer Sammlung mit gegebener Abbildungsvorschrift ab.
	 * Ergebnisse werden in neuer Liste zurückgegeben.
	 * Sammlung bleibt unverändert.
	 *
	 * Beispiel: map(s=>s.size(), '("aa", "a", "bbb")) => '(2, 1, 3).
	 *
	 * @param transform Umwandlungsvorschrift von Objekt von Typ T in Objekt von Typ U.
	 * @param coll Sammlung aus Objekten von Typ T.
	 * @param <T> Definitionsmenge.
	 * @param <U> Zielmenge.
	 * @return Liste abgebildeter Objekte.
	 */
	public static <T,U> List<U> map(ITransform<T,U> transform, Collection<T> coll) {
		List<U> destLst = new ArrayList<U>(coll.size());
		for(T obj : coll) {
			destLst.add(transform.invoke(obj));
		}
		return destLst;
	}

	/**
	 * Bilde jedes Element aus einer Sammlung mit gegebener Abbildungsvorschrift ab.
	 * Ergebnisse werden in neuer Liste zurückgegeben.
	 * Sammlung bleibt unverändert.
	 * Umwandlungsvorschrift bekommt Index von Objekt in Ursprungsliste gegeben.
	 *
	 * @param transform Abbildungsvorschrift von Objekt von Typ T in Objekt von Typ U.
	 * @param srcLst Liste von Objekten von Typ T.
	 * @param <T> Definitionsmenge.
	 * @param <U> Zielmenge.
	 * @return Liste abgebildeter Objekte.
	 */
	public static <T,U> List<U> mapi(IIndexedTransform<T,U> transform, List<T> srcLst) {
		List<U> destLst = new ArrayList<U>(srcLst.size());
		for(int i=0; i<srcLst.size(); i++) {
			T obj = srcLst.get(i);
			destLst.add(transform.invoke(i, obj));
		}
		return destLst;
	}

	/**
	 * Erzeuge eine java.util.Map aus einer Liste von Schlüsseln und einer Liste von Werten.
	 * Schlüssel weisen auf Werte, welche gleiche Position (Index) in der Liste haben.
	 *
	 * Beispiel: zipMap('("eins", "zwei", "drei"), '(1, 2, 3)) => {"eins"=>1, "zwei"=>2, "drei"=>3}.
	 *
	 * @param keys Liste von Schlüsseln.
	 * @param vals Liste von Werten.
	 * @param <T> Typ der Schlüssel.
	 * @param <U> Typ der Werte.
	 * @return Map, welche Schlüssel auf Wert mit gleichem Index abbildet.
	 */
	public static <T,U> Map<T, U> zipMap(List<T> keys, List<U> vals) {
		Map<T, U> mapping = new HashMap<T, U>();
		for(int i = 0; i < keys.size(); i++) {
			T key = keys.get(i);
			mapping.put(key, vals.get(i));
		}
		return mapping;
	}

	/**
	 * Erzeuge neue Liste aus Elementen einer Sammlung, welche Prädikat erfüllen.
	 *
	 * Beispiel: filter(x=>(x % 2 == 0), '(1, 2, 3, 4, 5)) => '(2, 4).
	 *
	 * @param pred Bedingung, welche Element erfüllen muss, um in Ausgangsliste zu gelangen.
	 * @param coll Sammlung von Objekten des Typs T.
	 * @param <T> Typ der Elemente der Sammlung.
	 * @return Gefilterte Liste, welche nur Objekte aus coll enthält, die Prädikat pred erfüllen.
	 */
	public static <T> List<T> filter(IPredicate<T> pred, Collection<T> coll) {
		List<T> destLst = new ArrayList<T>(coll.size());
		for(T obj : coll) {
			if(pred.invoke(obj))
				destLst.add(obj);
		}
		return destLst;
	}

	/**
	 * Erzeuge Liste aus Array.
	 * @param array Array mit Elementen von Typ T.
	 * @param <T> Typ der Elemente des Arrays.
	 * @return ArrayList mit Inhalt von Array.
	 */
	public static <T> List<T> fromArray(T[] array) {
		return Arrays.asList(array);
	}

	/**
	 * Zähle die Anzahl der Elemente in einer Sammlung, welche Prädikat erfüllen.
	 *
	 * Beispiel: count(x=>(x%2==0), '(1, 2, 3, 4, 5)) => 2.
	 *
	 * @param pred Prädikat, welches ein Element der Sammlung erfüllen muss, um mitgezählt zu werden.
	 * @param coll Sammlung aus Objekten vom Typ T.
	 * @param <T> Typ der Elemente.
	 * @return Anzahl der Elemente in der Sammlung, welche Prädikat erfüllen.
	 */
	public static <T> int count(IPredicate<T> pred, Iterable<T> coll) {
		int n = 0;
		for(T obj : coll) {
			if(pred.invoke(obj))
				n++;
		}
		return n;
	}

	/**
	 * Finde Element in Sammlung, welches Prädikat erfüllt. Gebe ersten Treffer zurück.
	 *
	 * Beispiel: find(x=>(x>4), '(1, 2, 3, 4, 5)) => 5.
	 *
	 * @param pred Prädikat, welches ein zu findenes Element erfüllen muss.
	 * @param coll Sammlung von Elementen des Typs T.
	 * @param <T> Typ der Elemente.
	 * @return Das erste Element, welches gegebenes Prädikat erfüllt.
	 */
	public static <T> T find(IPredicate<T> pred, Iterable<T> coll) {
		for(T obj : coll) {
			if(pred.invoke(obj))
				return obj;
		}
		return null;
	}

	/**
	 * Falte Elemente einer Sammlung mit einem Binäroperator von links zusammen. Initialisiere Akkumulator mit gegebenem Wert.
	 *
	 * Linksassoziativ!
	 *
	 * @param op Binäroperator.
	 * @param accum Akkumulator für Zwischenergebnisse.
	 * @param coll Sammlung mit Elementen des Typs T.
	 * @param <T> Typ der Elemente der Sammlung.
	 * @param <U> Typ der Zwischenergebnisse und des Endergebnis.
	 * @return Endergebnis vom Typ U.
	 */
	public static <T,U> U reduce(IBinaryOperator<T,U> op, U accum, Iterable<T> coll) {
		for(T obj : coll) {
			accum = op.invoke(accum, obj);
		}
		return accum;
	}

	/**
	 * Falte Elemente eines Arrays mit einem Binäroperator von links zusammen. Initialisiere Akkumulator mit gegebenem Wert.
	 *
	 * Linksassoziativ!
	 *
	 * @param op Binäroperator.
	 * @param accum Akkumulator für Zwischenergebnisse.
	 * @param arr Array mit Elementen des Typs T.
	 * @param <T> Typ der Elemente des Arrays.
	 * @param <U> Typ der Zwischenergebnisse und des Endergebnis.
	 * @return Endergebnis vom Typ U.
	 */
	public static <T,U> U reduce(IBinaryOperator<T,U> op, U accum, T[] arr) {
		return reduce(op, accum, Arrays.asList(arr));
	}

	/**
	 * Erzeuge neue duplikatfreie Liste aus gegebener Liste.
	 * @param lst Liste mit eventuell Duplikaten.
	 * @param <T> Typ der Elemente.
	 * @return Duplikatfreie Liste mit Elementen aus lst.
	 */
	public static <T> List<T> remDups(List<T> lst) {
		List<T> copy = new ArrayList<T>();
		for(T obj : lst) {
			FuncInPlace.addNoDups(copy, obj);
		}
		return copy;
	}

	/**
	 * Beliebig tief verschachtelte for-Schleife.
	 *
	 * Beispiele:
	 * nestedFor(1, 0, n, [i]=>System.out.println(""+i)) entspricht "for(int i=0; i &leq; n; i++) {System.out.println(""+i)}"
	 * nestedFor(2, 0, n, [i,j]=>System.out.println(""+i+j)) entspricht "for(int i=0; i &leq; n; i++) {for(int j=0; j&leq; n; j++) {System.out.println(""+i+j); }}"
	 * u.s.w.
	 *
	 * @param depth Schachtelungstiefe.
	 * @param fromInclusive Inklusive Untergrenze der Iteration jeder Ebene.
	 * @param toExclusive Exklusive Obergrenze der Iteration jeder Ebene.
	 * @param action Indizierte Aktion, welche im Zentrum der Schleife ausgeführt werden soll. Enthält Indizes als Integer-Array.
	 */
	public static void nestedFor(int depth, int fromInclusive, int toExclusive, IVarIndexedAction action) {
		nestedFor(depth, fromInclusive, toExclusive, action, new int[0]);
	}

	public static void nestedFor(int depth, int fromInclusive, int toExclusive, IVarIndexedAction action, int[] indices) {
		if(depth > 0) {
			indices = Arrays.copyOf(indices, indices.length+1);
			for(int i=fromInclusive; i<toExclusive; i++) {
				indices[indices.length-1] = i;
				nestedFor(depth - 1, fromInclusive, toExclusive, action, indices);
			}
		} else {
			action.invoke(indices);
		}
	}
}
