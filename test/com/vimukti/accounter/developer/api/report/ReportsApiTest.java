package com.vimukti.accounter.developer.api.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import com.vimukti.accounter.developer.api.core.AbstractTest;

public class ReportsApiTest extends AbstractTest {
	private String path;

	@Override
	public void test() throws Exception {
		path = "/company/api/xml/reports/";
		salesbycustomersummary();
		agedcreditors();
	}

	private void salesbycustomersummary() throws Exception {
		String queryStr = getQueryString();
		GetMethod prepareMethod = prepareGetMethod(path
				+ "salesbycustomersummary", queryStr);
		int statusCode = executeMethod(prepareMethod);
		eq(HttpStatus.SC_OK, statusCode);
	}

	private void agedcreditors() throws Exception {
		String queryStr = getQueryString();
		GetMethod prepareMethod = prepareGetMethod(path + "agedcreditors",
				queryStr);
		int statusCode = executeMethod(prepareMethod);
		eq(HttpStatus.SC_OK, statusCode);
	}

	private String getQueryString() {
		return getQueryString(new HashMap<String, String>());
	}

	@Override
	protected String getQueryString(Map<String, String> params) {
		params.put("StartDate", "2012.02.24 AD at 11:13:56 IST");
		params.put("EndDate", "2012.02.30 AD at 12:13:56 IST");
		return super.getQueryString(params);
	}
}
