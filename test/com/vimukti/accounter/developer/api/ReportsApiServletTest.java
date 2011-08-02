package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import junit.framework.TestCase;

import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.testing.ServletTester;
import org.junit.Before;
import org.junit.Test;

public class ReportsApiServletTest extends TestCase {
	private static final String SIGNATURE = "signature";
	private static final String ALGORITHM = "hmacSHA256";
	private static final String DATE_FORMAT = "yyy-MM-ddTHH:mm:ssZ";
	private static final String apikey = null;
	private static final String secretKey = null;
	private static final String companyId = null;

	private ServletTester tester;
	SimpleDateFormat simpleDateFormat;

	@Before
	public void setUp() throws Exception {
		tester = new ServletTester();
		tester.addServlet(ReportsApiServlet.class, "/api/xmlreports/*");
		tester.addServlet(ReportsApiServlet.class, "/api/jsonreports/*");
		tester.addFilter(ApiFilter.class, "/api/*", 5);
		tester.start();
		simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
	}

	@Test
	public void test() {
		HttpTester request = new HttpTester();
		request.setMethod("GET");
		request.setHeader("Host", "tester");
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		request.setVersion("HTTP/1.0");
		HttpTester response = new HttpTester();

		request.setURI(getSalesByCustomerSummaryUrl());
		testResponse(request, response);
	}

	private void testResponse(HttpTester request, HttpTester response) {
		try {
			response.parse(tester.getResponses(request.generate()));
			String content = response.getContent();
			assertNotNull(content);
			assertTrue(content.isEmpty());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getSalesByCustomerSummaryUrl() {
		String exprDate = simpleDateFormat.format(System.currentTimeMillis());
		String string = "/api/xmlreports/salesbycustomersummary?"
				+ "&ApiKey="
				+ apikey
				+ "&CompanyId="
				+ companyId
				+ "&Expire="
				+ exprDate
				+ "&StartDate=2011-07-01T12:00:00Z&EndDate=2011-08-30T12:00:00Z";

		return doSigning(string);
	}

	private String getEcSalesListDetailUrl(String name) {
		String exprDate = simpleDateFormat.format(System.currentTimeMillis());
		String string = "/api/xmlreports/salesbycustomersummary?"
				+ "&ApiKey="
				+ apikey
				+ "&CompanyId="
				+ companyId
				+ "&Expire="
				+ exprDate
				+ "&Name="
				+ name
				+ "&StartDate=2011-07-01T12:00:00Z&EndDate=2011-08-30T12:00:00Z";
		return doSigning(string);
	}

	private String getVat100ReportUrl(long taxAgency) {
		String exprDate = simpleDateFormat.format(System.currentTimeMillis());
		String string = "/api/xmlreports/salesbycustomersummary?"
				+ "&ApiKey="
				+ apikey
				+ "&CompanyId="
				+ companyId
				+ "&Expire="
				+ exprDate
				+ "&TaxAgency="
				+ taxAgency
				+ "&StartDate=2011-07-01T12:00:00Z&EndDate=2011-08-30T12:00:00Z";
		return doSigning(string);
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
}
