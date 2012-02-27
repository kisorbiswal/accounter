package com.vimukti.accounter.developer.api.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.methods.DeleteMethod;

import com.vimukti.accounter.developer.api.core.AbstractTest;
import com.vimukti.accounter.developer.api.core.ApiResult;

public class DeleteTest extends AbstractTest {
	private String path;

	@Override
	public void test() throws Exception {
		path = "/company/api/xml/crud";
		deleteCustomer();
	}

	private void deleteCustomer() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "customer");
		map.put("id", "6");
		String queryStr = getQueryString(map);
		DeleteMethod prepareMethod = prepareDeleteMethod(path, queryStr);

		ApiResult result = executeMethod(prepareMethod);
		eq(result.getStatus(), 200);
		System.out.println(result.getResult());
	}
}
