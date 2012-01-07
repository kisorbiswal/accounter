package com.vimukti.accounter.taxreturn.core;

public class GatewayValidation {
	/**
	 * 1..1
	 */
	private String processed = "Processed";
	/**
	 * 1..1
	 */
	private String result = "result";

	public String getProcessed() {
		return processed;
	}

	public void setProcessed(String processed) {
		this.processed = processed;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
