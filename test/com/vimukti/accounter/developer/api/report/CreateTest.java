package com.vimukti.accounter.developer.api.report;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.tools.ant.filters.StringInputStream;

import com.vimukti.accounter.developer.api.core.AbstractTest;

public class CreateTest extends AbstractTest {
	private String path;

	@Override
	public void test() throws Exception {
		path = "/company/api/xml/crud/";
		createCustomer();
	}

	private void createCustomer() throws Exception {
		String ss = "	<Customer></Customer>";
		doRequest(new StringInputStream(ss));
	}

	private void doRequest(InputStream body) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String queryStr = getQueryString(map);
		PutMethod prepareMethod = preparePutMethod(path, queryStr);
		prepareMethod.setRequestBody(body);
		int statusCode = executeMethod(prepareMethod);
		System.out.println(prepareMethod.getResponseBodyAsString());
		eq(HttpStatus.SC_OK, statusCode);
	}

}
