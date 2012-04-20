package com.vimukti.api.core;

import java.util.Map;

public class ClientDetails {

	private Map<String, Long> companies;
	private String autherizedTocken;

	public ClientDetails(String encode, Map<String, Long> companyIds) {
		autherizedTocken = encode;
		companies = companyIds;
	}

	public Map<String, Long> getCompanies() {
		return companies;
	}

	public String getAutherizedTocken() {
		return autherizedTocken;
	}

	public Long getCompanyId(String companyName) {
		return companies.get(companyName);
	}
}
