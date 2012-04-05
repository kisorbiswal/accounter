package com.vimukti.api.crud;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.vimukti.accounter.developer.api.ApiSerializationFactory;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.api.core.APIConstants;
import com.vimukti.api.core.ApiCallback;
import com.vimukti.api.core.ApiRequest;
import com.vimukti.api.process.RequestPeocesser;

public class ApiDAOService {
	private static int REQUEST_CRUD = 1;
	private static int REQUEST_LISTS = 2;
	private static int REQUEST_REPORTS = 2;

	private static int METHOD_GET = 1;
	private static int METHOD_PUT = 2;
	private static int METHOD_DELETE = 3;
	private static int METHOD_POST = 4;

	private ApiSerializationFactory serializer;
	private Map<Integer, String> paths;

	private String apiKey;
	private String secretKey;
	private long companyId;
	private static final String DATE_FORMAT = "yyyy.MM.dd G 'at' HH:mm:ss z";
	protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			DATE_FORMAT);
	private int serializationType;

	public ApiDAOService(int serializationType, String apiKey,
			String secretKey, long companyId) {
		this.serializationType = serializationType;
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.companyId = companyId;
		serializer = new ApiSerializationFactory(
				serializationType == APIConstants.SERIALIZATION_JSON);
		paths = new HashMap<Integer, String>();
		paths.put(REQUEST_CRUD,
				serializationType == APIConstants.SERIALIZATION_JSON ? ""
						: "/company/api/xml/crud");
		paths.put(REQUEST_LISTS,
				serializationType == APIConstants.SERIALIZATION_JSON ? ""
						: "/company/api/xml/lists");
		paths.put(REQUEST_REPORTS,
				serializationType == APIConstants.SERIALIZATION_JSON ? ""
						: "/company/api/xml/reports");

	}

	public <T extends IAccounterCore> void create(T createdObject,
			ApiCallback<T> callback) {
		String xml = "";
		try {
			xml = serializer.serialize(createdObject);
		} catch (Exception e) {
			callback.onFail(e.getMessage());
			return;
		}

		ApiRequest<T> request = new ApiRequest<T>(xml, buildQueryString(null),
				paths.get(REQUEST_CRUD), METHOD_PUT, callback);
		addRequest(request);
	}

	private void addRequest(ApiRequest<?> request) {
		request.setSerializationType(serializationType);
		request.setSecretKey(secretKey);
		RequestPeocesser.add(request);
	}

	public <T extends IAccounterCore> void update(T createdObject,
			ApiCallback<T> callback) {
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
		ApiRequest<T> request = new ApiRequest<T>(xml, buildQueryString(map),
				paths.get(REQUEST_CRUD), METHOD_POST, callback);
		addRequest(request);
	}

	public <T extends IAccounterCore> void delete(T createdObject,
			ApiCallback<T> callback) {
		delete(createdObject.getObjectType().getServerClassSimpleName(),
				createdObject.getID(), callback);
	}

	public <T extends IAccounterCore> void delete(String type, long id,
			ApiCallback<T> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("id", Long.toString(id));
		ApiRequest<T> request = new ApiRequest<T>(buildQueryString(map),
				paths.get(REQUEST_CRUD), METHOD_DELETE, callback);
		addRequest(request);
	}

	public <T extends IAccounterCore> void get(String type, long id,
			ApiCallback<T> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("id", Long.toString(id));
		ApiRequest<T> request = new ApiRequest<T>(buildQueryString(map),
				paths.get(REQUEST_CRUD), METHOD_GET, callback);
		addRequest(request);
	}

	public <T extends IAccounterCore> void get(String type, String name,
			ApiCallback<T> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", type);
		map.put("name", name);
		ApiRequest<T> request = new ApiRequest<T>(buildQueryString(map),
				paths.get(REQUEST_CRUD), METHOD_GET, callback);
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
		map.put("name", type);
		map.put("viewType", viewType);
		map.put("dateType", dateType);
		map.put("start", Integer.toString(startIndex));
		map.put("length", Integer.toString(length));

		ApiRequest<List<T>> request = new ApiRequest<List<T>>(
				buildQueryString(map), paths.get(REQUEST_LISTS), METHOD_GET,
				callback);
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
		map.put("reporttype", reportType);
		map.put("dateRange", viewType);
		map.put("start", Integer.toString(startIndex));
		map.put("length", Integer.toString(length));
		ApiRequest<List<T>> request = new ApiRequest<List<T>>(
				buildQueryString(map), paths.get(REQUEST_LISTS), METHOD_GET,
				callback);
		addRequest(request);
	}

	public <T extends IAccounterCore> void getReports(String reportType,
			Date startDate, Date endDate, int startIndex, int length,
			ApiCallback<List<T>> callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("reporttype", reportType);
		map.put("StartDate", getDateString(startDate));
		map.put("EndDate", getDateString(endDate));
		map.put("start", Integer.toString(startIndex));
		map.put("length", Integer.toString(length));
		ApiRequest<List<T>> request = new ApiRequest<List<T>>(
				buildQueryString(map), paths.get(REQUEST_LISTS), METHOD_GET,
				callback);
		addRequest(request);
	}

	private static String getDateString(Date date) {
		return simpleDateFormat.format(date);
	}

	private String buildQueryString(Map<String, String> map) {
		if (map == null) {
			map = new HashMap<String, String>();
		}
		map.put("ApiKey", apiKey);
		map.put("CompanyId", Long.toString(companyId));
		map.put("Expire", getDateString(new Date()));
		return makeQueryString(map);
	}

	private static String makeQueryString(Map<String, String> map) {
		Set<Entry<String, String>> entrySet = map.entrySet();
		StringBuilder builder = new StringBuilder();
		for (Entry<String, String> entry : entrySet) {
			builder.append(entry.getKey());
			builder.append("=");
			builder.append(getURLEncode(entry.getValue()));
			builder.append("&");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	private static String getURLEncode(String string) {
		try {
			return URLEncoder.encode(string, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}

	public static void authenticate(String apiKey, String secretKey,
			ApiCallback<Map<String, Long>> callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ApiKey", apiKey);
		params.put("Expire", getDateString(new Date()));
		String queryString = makeQueryString(params);
		ApiRequest<Map<String, Long>> request = new ApiRequest<Map<String, Long>>(
				queryString, "/company/api/xml/reports", METHOD_GET, callback);
		request.setSerializationType(APIConstants.SERIALIZATION_JSON);
		request.setSecretKey(secretKey);
		RequestPeocesser.add(request);
	}
}
