package com.vimukti.api.crud;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.api.core.ApiCallback;
import com.vimukti.api.core.ApiRequest;

public class AccounterService extends RequestService {

	public AccounterService(int serializationType, String apiKey,
			String secretKey, String autherizedTocken, long companyId) {
		super(serializationType, apiKey, secretKey, autherizedTocken, companyId);
	}

	public void createUserSecret(String companyPassword,
			ApiCallback<Boolean> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "usersecret");
		map.put("password", companyPassword);
		ApiRequest<Boolean> request = new ApiRequest<Boolean>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_OPERATIONS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}

	public void encryptCompany(String newCompanyPassword, String passwordHint,
			ApiCallback<Boolean> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "encryptcompany");
		map.put("password", newCompanyPassword);
		map.put("hint", passwordHint == null ? "" : passwordHint);
		ApiRequest<Boolean> request = new ApiRequest<Boolean>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_OPERATIONS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}

	public void create(IAccounterCore createdObject, ApiCallback<Long> callback) {
		String xml = "";
		try {
			xml = serializer.serialize(createdObject);
		} catch (Exception e) {
			callback.onFail(e.getMessage());
			return;
		}

		ApiRequest<Long> request = new ApiRequest<Long>(xml,
				buildQueryString(null), getPath(RequestUtil.REQUEST_CRUD),
				RequestUtil.METHOD_PUT, callback);
		addRequest(request);
	}

	public void update(IAccounterCore createdObject, ApiCallback<Long> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "customer");
		map.put("id", Long.toString(createdObject.getID()));
		String xml = "";
		try {
			xml = serializer.serialize(createdObject);
		} catch (Exception e) {
			callback.onFail(e.getMessage());
			return;
		}
		ApiRequest<Long> request = new ApiRequest<Long>(xml,
				buildQueryString(map), getPath(RequestUtil.REQUEST_CRUD),
				RequestUtil.METHOD_POST, callback);
		addRequest(request);
	}

	public void delete(IAccounterCore createdObject,
			ApiCallback<Boolean> callback) {
		delete(createdObject.getObjectType(), createdObject.getID(), callback);
	}

	public void delete(AccounterCoreType type, long id,
			ApiCallback<Boolean> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type.getServerClassSimpleName());
		map.put("id", Long.toString(id));
		ApiRequest<Boolean> request = new ApiRequest<Boolean>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_CRUD),
				RequestUtil.METHOD_DELETE, callback);
		addRequest(request);
	}

	public <T> void get(AccounterCoreType type, long id, ApiCallback<T> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type.getServerClassSimpleName());
		map.put("id", Long.toString(id));
		ApiRequest<T> request = new ApiRequest<T>(buildQueryString(map),
				getPath(RequestUtil.REQUEST_CRUD), RequestUtil.METHOD_GET,
				callback);
		addRequest(request);
	}

	public <T> void get(AccounterCoreType type, String name,
			ApiCallback<T> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type.getServerClassSimpleName());
		map.put("name", name);
		ApiRequest<T> request = new ApiRequest<T>(buildQueryString(map),
				getPath(RequestUtil.REQUEST_CRUD), RequestUtil.METHOD_GET,
				callback);
		addRequest(request);
	}

	public <T> void getList(AccounterCoreType type,
			ApiCallback<List<T>> callback) {
		getList(type, AccounterViewType.OPEN, AccounterDateRangeType.ALL,
				callback);
	}

	public <T> void getList(AccounterCoreType type, AccounterViewType viewType,
			AccounterDateRangeType dateType, ApiCallback<List<T>> callback) {
		getList(type, viewType, dateType, 0, 10, callback);
	}

	public <T> void getList(AccounterCoreType type, int startIndex, int length,
			ApiCallback<List<T>> callback) {
		getList(type, AccounterViewType.OPEN, AccounterDateRangeType.ALL,
				startIndex, length, callback);
	}

	public <T> void getList(AccounterCoreType type, AccounterViewType viewType,
			AccounterDateRangeType dateType, int startIndex, int length,
			ApiCallback<List<T>> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type.getServerClassSimpleName());
		map.put("viewType", viewType.getValue());
		map.put("dateType", dateType.getValue());
		map.put("start", Integer.toString(startIndex));
		map.put("length", Integer.toString(length));

		ApiRequest<List<T>> request = new ApiRequest<List<T>>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_LISTS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}

	public <T> void getReports(AccounterReportType reportType,
			ApiCallback<List<T>> callback) {
		getReports(reportType, AccounterDateRangeType.THIS_MONTH, callback);
	}

	public <T> void getReports(AccounterReportType reportType,
			AccounterDateRangeType dateType, ApiCallback<List<T>> callback) {
		getReports(reportType, dateType, 0, 10, callback);
	}

	public <T> void getReports(AccounterReportType reportType, int startIndex,
			int length, ApiCallback<List<T>> callback) {
		getReports(reportType, AccounterDateRangeType.THIS_MONTH, startIndex,
				length, callback);
	}

	public <T> void getReports(AccounterReportType reportType, Date startDate,
			Date endDate, ApiCallback<List<T>> callback) {
		getReports(reportType, startDate, endDate, 0, 10, callback);
	}

	public <T> void getReports(AccounterReportType reportType,
			AccounterDateRangeType dateType, int startIndex, int length,
			ApiCallback<List<T>> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", reportType.getValue());
		map.put("dateRange", dateType.getValue());
		map.put("start", Integer.toString(startIndex));
		map.put("length", Integer.toString(length));
		ApiRequest<List<T>> request = new ApiRequest<List<T>>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_LISTS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}

	public <T> void getReports(AccounterReportType reportType, Date startDate,
			Date endDate, int startIndex, int length,
			ApiCallback<List<T>> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", reportType.getValue());
		map.put("StartDate", RequestUtil.getDateString(startDate));
		map.put("EndDate", RequestUtil.getDateString(endDate));
		map.put("start", Integer.toString(startIndex));
		map.put("length", Integer.toString(length));
		ApiRequest<List<T>> request = new ApiRequest<List<T>>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_LISTS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}

	public void getAvailableReportsList(
			ApiCallback<Map<String, List<String>>> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "reportslist");
		ApiRequest<Map<String, List<String>>> request = new ApiRequest<Map<String, List<String>>>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_OPERATIONS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}
}
