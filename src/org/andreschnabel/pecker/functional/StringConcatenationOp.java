package org.andreschnabel.pecker.functional;

/**
 * Konkatenationsoperator.
 */
public class StringConcatenationOp implements IBinaryOperator<String, String> {

	private final String infix;

	public StringConcatenationOp(String infix) {
		this.infix = infix;
	}

	@Override
	public String invoke(String a, String b) {
		return a+infix+b;
	}
}
