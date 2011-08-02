package com.vimukti.accounter.developer.api;

import java.io.IOException;

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
		tester.addServlet(ReportsApiServlet.class, "/service");
		tester.start();
	}

	@Test
	public void test() {
		HttpTester request = new HttpTester();
		request.setMethod("POST");
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

}
