package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import org.eclipse.jetty.testing.HttpTester;
import org.eclipse.jetty.testing.ServletTester;
import org.junit.Before;
import org.junit.Test;

public class ReportsApiServletTest extends TestCase {
	private ServletTester tester;

	@Before
	public void setUp() throws Exception {
		tester = new ServletTester();
		tester.addServlet(ReportsApiServlet.class, "/api/xmlreports/*");
		tester.addServlet(ReportsApiServlet.class, "/api/jsonreports/*");
		tester.addFilter(ApiFilter.class, "/api/*", 5);
		tester.start();
	}

	@Test
	public void test() {
		HttpTester request = new HttpTester();
		request.setMethod("GET");
		request.setHeader("Host", "tester");
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");
		request.setURI("/service");
		request.setVersion("HTTP/1.0");
		HttpTester response = new HttpTester();

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

	private void sendSalesByCustomerSummaryReq() {
		String apikey = null, secretKey = null, companyId = null;
		long currentTimeMillis = System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyy-MM-ddTHH:mm:ssZ");
		String format = simpleDateFormat.format(currentTimeMillis);
		String string = "/api/xmlreports/salesbycustomersummary?"
				+ "&CompanyId="
				+ companyId
				+ "&Expire="
				+ format
				+ "&StartDate=2011-07-01T12:00:00Z&EndDate=2011-07-30T12:00:00Z";

		HttpTester request = new HttpTester();
		request.setMethod("GET");
		request.setHeader("Host", "tester");
		request.setHeader("Content-Type", "application/x-www-form-urlencoded");

		String signature = getSignature(string, apikey, secretKey);
		request.setURI("/api/xmlreports/salesbycustomersummary?ApiKey="
				+ apikey
				+ "&Signature="
				+ signature
				+ "&CompanyId="
				+ companyId
				+ "&Expire="
				+ format
				+ "&StartDate=2011-07-01T12:00:00Z&EndDate=2011-07-30T12:00:00Z");

	}

	private String getSignature(String string, String apikey, String secretKey) {
		// TODO Auto-generated method stub
		return null;
	}

}
