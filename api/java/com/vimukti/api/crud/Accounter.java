package com.vimukti.api.crud;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.vimukti.api.core.ApiCallback;
import com.vimukti.api.core.ApiRequest;
import com.vimukti.api.process.RequestPeocesser;

public class Accounter {

	private String apiKey;
	private String secretKey;
	private Map<String, Long> companies;
	private int serializationType;

	private Accounter(int serializationType, String apiKey, String secretKey,
			Map<String, Long> obj) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.companies = obj;
		this.serializationType = serializationType;

	}

	public AccounterService openCompany(String companyName) {
		Long companyId = companies.get(companyName);
		if (companyId == null) {
			return null;
		}
		AccounterService service = new AccounterService(serializationType,
				apiKey, secretKey, companyId);
		return service;
	}

	public Set<String> companiesList() {
		return companies.keySet();
	}

	public static void login(final int serializationType, final String apiKey,
			final String secretKey, final ApiCallback<Accounter> callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ApiKey", apiKey);
		params.put("Expire", RequestUtil.getDateString(new Date()));
		params.put("type", "companies");
		String queryString = RequestUtil.makeQueryString(params);
		ApiRequest<Map<String, Long>> request = new ApiRequest<Map<String, Long>>(
				queryString, RequestUtil.getPath(RequestUtil.REQUEST_LISTS,
						serializationType), RequestUtil.METHOD_GET,
				new ApiCallback<Map<String, Long>>() {

					@Override
					public void onSuccess(Map<String, Long> obj) {
						callback.onSuccess(new Accounter(serializationType,
								apiKey, secretKey, obj));
					}

					@Override
					public void onFail(String reason) {
						callback.onFail(reason);
					}
				});
		request.setSerializationType(serializationType);
		request.setSecretKey(secretKey);
		RequestPeocesser.add(request);

	}

	// Encription

	// Subscription
}
