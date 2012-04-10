package com.vimukti.accounter.core;

/**
 * @author Prasanna Kumar g
 * 
 */
public class ComputaionFormulaFunction {

	public static final int FUNCTION_ADD_PAY_HEAD = 1;

	public static final int FUNCTION_SUBSTRACT_PAY_HEAD = 2;

	public static final int FUNCTION_DIVIDE_ATTENDANCE = 3;

	public static final int FUNCTION_MULTIPLY_ATTENDANCE = 4;

	private int functionType;

	private PayHead payHead;

	/**
	 * @return the functionType
	 */
	public int getFunctionType() {
		return functionType;
	}

	/**
	 * @param functionType
	 *            the functionType to set
	 */
	public void setFunctionType(int functionType) {
		this.functionType = functionType;
	}

	/**
	 * @return the payHead
	 */
	public PayHead getPayHead() {
		return payHead;
	}

	/**
	 * @param payHead
	 *            the payHead to set
	 */
	public void setPayHead(PayHead payHead) {
		this.payHead = payHead;
	}

}
