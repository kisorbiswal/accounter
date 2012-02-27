package com.vimukti.accounter.developer.api.report;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.tools.ant.filters.StringInputStream;

import com.vimukti.accounter.developer.api.core.AbstractTest;
import com.vimukti.accounter.developer.api.core.ApiResult;

public class UpdateTest extends AbstractTest {
	private String path;

	@Override
	public void test() throws Exception {
		path = "/company/api/xml/crud";
		updateCustomer();
	}

	private void updateCustomer() throws Exception {
		String ss = "	<Customer><name>KKK</name></Customer>";
		doRequest(new StringInputStream(ss));
	}

	@SuppressWarnings("deprecation")
	private void doRequest(InputStream body) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "customer");
		map.put("id", "6");
		String queryStr = getQueryString(map);
		PostMethod prepareMethod = preparePostMethod(path, queryStr);
		prepareMethod.setRequestBody(body);
		ApiResult result = executeMethod(prepareMethod);
		eq(result.getStatus(), 200);
		System.out.println(result.getResult());
	}
}
