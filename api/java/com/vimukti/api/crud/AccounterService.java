package com.vimukti.api.crud;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.api.core.APIConstants;
import com.vimukti.api.core.ApiCallback;
import com.vimukti.api.core.ApiRequest;

public class AccounterService extends RequestService {

	public AccounterService(int serializationType, String apiKey,
			String secretKey, long companyId) {
		super(serializationType, apiKey, secretKey, companyId);
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
		delete(createdObject.getObjectType().getServerClassSimpleName(),
				createdObject.getID(), callback);
	}

	public void delete(String type, long id, ApiCallback<Boolean> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("id", Long.toString(id));
		ApiRequest<Boolean> request = new ApiRequest<Boolean>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_CRUD),
				RequestUtil.METHOD_DELETE, callback);
		addRequest(request);
	}

	public <T extends IAccounterCore> void get(String type, long id,
			ApiCallback<T> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("id", Long.toString(id));
		ApiRequest<T> request = new ApiRequest<T>(buildQueryString(map),
				getPath(RequestUtil.REQUEST_CRUD), RequestUtil.METHOD_GET,
				callback);
		addRequest(request);
	}

	public <T extends IAccounterCore> void get(String type, String name,
			ApiCallback<T> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("name", name);
		ApiRequest<T> request = new ApiRequest<T>(buildQueryString(map),
				getPath(RequestUtil.REQUEST_CRUD), RequestUtil.METHOD_GET,
				callback);
		addRequest(request);
	}

	public <T extends IAccounterCore> void getList(String type,
			ApiCallback<List<T>> callback) {
		getList(type, APIConstants.VIEW_TYPE_OPEN, APIConstants.VIEW_TYPE_ALL,
				callback);
	}

	public <T extends IAccounterCore> void getList(String type,
			String viewType, String dateType, ApiCallback<List<T>> callback) {
		getList(type, viewType, dateType, 0, 10, callback);
	}

	public <T extends IAccounterCore> void getList(String type, int startIndex,
			int length, ApiCallback<List<T>> callback) {
		getList(type, APIConstants.VIEW_TYPE_OPEN, APIConstants.VIEW_TYPE_ALL,
				startIndex, length, callback);
	}

	public <T extends IAccounterCore> void getList(String type,
			String viewType, String dateType, int startIndex, int length,
			ApiCallback<List<T>> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("viewType", viewType);
		map.put("dateType", dateType);
		map.put("start", Integer.toString(startIndex));
		map.put("length", Integer.toString(length));

		ApiRequest<List<T>> request = new ApiRequest<List<T>>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_LISTS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}

	public <T extends IAccounterCore> void getReports(String reportType,
			ApiCallback<List<T>> callback) {
		getReports(reportType, APIConstants.DATE_RANGE_THISMONTH, callback);
	}

	public <T extends IAccounterCore> void getReports(String reportType,
			String viewType, ApiCallback<List<T>> callback) {
		getReports(reportType, viewType, 0, 10, callback);
	}

	public <T extends IAccounterCore> void getReports(String reportType,
			int startIndex, int length, ApiCallback<List<T>> callback) {
		getReports(reportType, APIConstants.DATE_RANGE_THISMONTH, startIndex,
				length, callback);
	}

	public <T extends IAccounterCore> void getReports(String reportType,
			Date startDate, Date endDate, ApiCallback<List<T>> callback) {
		getReports(reportType, startDate, endDate, 0, 10, callback);
	}

	public <T extends IAccounterCore> void getReports(String reportType,
			String viewType, int startIndex, int length,
			ApiCallback<List<T>> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", reportType);
		map.put("dateRange", viewType);
		map.put("start", Integer.toString(startIndex));
		map.put("length", Integer.toString(length));
		ApiRequest<List<T>> request = new ApiRequest<List<T>>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_LISTS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}

	public <T extends IAccounterCore> void getReports(String reportType,
			Date startDate, Date endDate, int startIndex, int length,
			ApiCallback<List<T>> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", reportType);
		map.put("StartDate", RequestUtil.getDateString(startDate));
		map.put("EndDate", RequestUtil.getDateString(endDate));
		map.put("start", Integer.toString(startIndex));
		map.put("length", Integer.toString(length));
		ApiRequest<List<T>> request = new ApiRequest<List<T>>(
				buildQueryString(map), getPath(RequestUtil.REQUEST_LISTS),
				RequestUtil.METHOD_GET, callback);
		addRequest(request);
	}
}
