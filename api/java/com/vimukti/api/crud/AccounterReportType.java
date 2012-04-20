package com.vimukti.api.crud;

public enum AccounterReportType {
	SALES_BY_CUSTOMER_SUMMARY("salesbycustomersummary");

	private String value;

	AccounterReportType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
