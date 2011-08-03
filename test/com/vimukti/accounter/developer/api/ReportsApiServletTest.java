package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.testing.HttpTester;
import org.mortbay.jetty.testing.ServletTester;
import org.mortbay.util.UrlEncoded;

public class ReportsApiServletTest extends TestCase {
	private static final String SIGNATURE = "Signature";
	private static final String ALGORITHM = "hmacSHA256";
	private static final String DATE_FORMAT = "yyy-MM-dd HH:mm:ssZ";
	private static final String apikey = "rwih8slp";
	private static final String secretKey = "df2q64ik1q3q78lq";
	private static final String LOCAL_PATH = "";// "http://localhost:8890";
	private static final long companyId = 1;
	private static String prefixUrl = "http://localhost:8890/api/xmlreports/";

	private ServletTester tester;
	SimpleDateFormat simpleDateFormat;

	@Before
	public void setUp() throws Exception {
		tester = new ServletTester();
		// tester.setContextPath("/");
		tester.addServlet(ReportsApiServlet.class, "/api/xmlreports/*");
		// tester.addServlet(ReportsApiServlet.class, "/api/jsonreports/*");
		// tester.addFilter(ApiFilter.class, "/api/*", 5);
		tester.start();
		simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
	}

	private HttpTester prepareRequest(String url, String queryStr) {
		HttpClient client = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.setQueryString(queryStr);
		try {
			client.executeMethod(getMethod);
			String string = getMethod.getResponseBodyAsString();
			getMethod.releaseConnection();
			System.out.println(string);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		HttpTester request = new HttpTester();
		request.setMethod("GET");
		request.setHeader("Host", "tester");
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		request.setVersion("HTTP/1.0");
		request.setURI(url);
		return request;
	}

	private String doSigning(String url) {
		byte[] secretKeyBytes = secretKey.getBytes();
		SecretKeySpec keySpec = new SecretKeySpec(secretKeyBytes, ALGORITHM);
		try {
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(keySpec);
			byte[] doFinal = mac.doFinal(url.getBytes());
			String string = new String(doFinal);
			url += "&" + SIGNATURE + "=" + string;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	private void testResponse(HttpTester request) {
		HttpTester response = new HttpTester();
		try {
			response.parse(tester.getResponses(request.generate()));
			String content = response.getContent();
			assertNotNull(content);
			// assertTrue(content.isEmpty());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void testFailResponse(HttpTester request) {
		HttpTester response = new HttpTester();
		try {
			response.parse(tester.getResponses(request.generate()));
			String content = response.getContent();
			assertNull(content);
			assertFalse(content.isEmpty());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSalesByCustomerSummary() throws IOException {
		String exprDate = simpleDateFormat.format(System.currentTimeMillis());
		String queryStr = "ApiKey="
				+ apikey
				+ "&CompanyId="
				+ companyId
				+ "&Expire="
				+ exprDate
				+ "&StartDate=2011-07-01 12:00:00Z&EndDate=2011-08-30 12:00:00Z";

		String encodeString = new UrlEncoded(queryStr).encode();

		String signature = doSigning(encodeString);
		String queryString = encodeString + "&" + SIGNATURE + "=" + signature;
		sendRequest(prefixUrl + "salesbycustomersummary?" + queryString);

		// testResponse(prepareRequest("/api/xmlreports/salesbycustomersummary",
		// doSigning(queryStr)));
	}

	private void sendRequest(String urlStr) throws IOException {

		URL url = new URL(urlStr);
		URLConnection connection = url.openConnection();
		InputStream stream = connection.getInputStream();
		int i = stream.available();
		byte[] data = new byte[i];
		stream.read(data);
		System.out.println(new String(data));
	}

	// @Test
	// public void testEcSalesListDetail() {
	// String name = "";
	// String exprDate = simpleDateFormat.format(System.currentTimeMillis());
	// String string = LOCAL_PATH
	// + "/api/xmlreports/salesbycustomersummary?"
	// + "ApiKey="
	// + apikey
	// + "&CompanyId="
	// + companyId
	// + "&Expire="
	// + exprDate
	// + "&Name="
	// + name
	// + "&StartDate=2011-07-01 12:00:00Z&EndDate=2011-08-30 12:00:00Z";
	// testResponse(prepareRequest(doSigning(string)));
	// }
	//
	// @Test
	// public void testVat100Report() {
	// long taxAgency = 0;
	// String exprDate = simpleDateFormat.format(System.currentTimeMillis());
	// String string = LOCAL_PATH
	// + "/api/xmlreports/salesbycustomersummary?"
	// + "ApiKey="
	// + apikey
	// + "&CompanyId="
	// + companyId
	// + "&Expire="
	// + exprDate
	// + "&TaxAgency="
	// + taxAgency
	// + "&StartDate=2011-07-01T12:00:00Z&EndDate=2011-08-30T12:00:00Z";
	// testResponse(prepareRequest(doSigning(string)));
	// }
	//
	// @Test
	// public void testFailDebitorsList() {
	// String exprDate = simpleDateFormat.format(System.currentTimeMillis());
	// String string = LOCAL_PATH
	// + "/api/xmlreports/debitorslist?"
	// + "ApiKey="
	// + apikey
	// + "&CompanyId=-1"
	// + "&Expire="
	// + exprDate
	// + "&StartDate=2011-07-01T12:00:00Z&EndDate=2011-08-30T12:00:00Z";
	// testFailResponse(prepareRequest(doSigning(string)));
	// }
	//
	// @Test
	// public void testFailPurchaseReportItems() {
	// String exprDate = simpleDateFormat.format(System.currentTimeMillis());
	// String string = LOCAL_PATH
	// + "/api/xmlreports/purchasereportitems?"
	// + "ApiKey="
	// + apikey
	// + "&CompanyId="
	// + companyId
	// + "&Expire="
	// + exprDate
	// + "&StartDate=01-07-2011T12:00:00Z&EndDate=2011-08-30T12:00:00Z";
	// testFailResponse(prepareRequest(doSigning(string)));
	// }
	//
	// @Test
	// public void testFailSalesOpenOrder() {
	// String exprDate = simpleDateFormat.format(System.currentTimeMillis());
	// String string = LOCAL_PATH
	// + "/api/xmlreports/salesopenorder?"
	// + "ApiKey=erwr"
	// + "&CompanyId="
	// + companyId
	// + "&Expire="
	// + exprDate
	// + "&StartDate=2011-07-01T12:00:00Z&EndDate=2011-08-30T12:00:00Z";
	// testFailResponse(prepareRequest(doSigning(string)));
	// }
	//
	// @Test
	// public void testFailCreditors() {
	// String exprDate = LOCAL_PATH + "2011-08-01T01:00:00Z";
	// String string = "/api/xmlreports/creditors?"
	// + "ApiKey=erwr"
	// + "&CompanyId="
	// + companyId
	// + "&Expire="
	// + exprDate
	// + "&StartDate=2011-07-01T12:00:00Z&EndDate=2011-08-30T12:00:00Z";
	// testFailResponse(prepareRequest(doSigning(string)));
	// }
}
