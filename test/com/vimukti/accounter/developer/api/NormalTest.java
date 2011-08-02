package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.mortbay.util.UrlEncoded;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class NormalTest {
	private static final String SIGNATURE = "Signature";
	private static final String ALGORITHM = "hmacSHA256";
	private static String prefixUrl = "http://localhost:8890/api/xmlreports/";
	private static final String DATE_FORMAT = "yyyy.MM.dd G 'at' HH:mm:ss z";

	private String apikey = "y14c6lde";
	private String secretKey = "c7hinn7j6uba998x";
	private long companyId = 1;
	private SimpleDateFormat simpleDateFormat;

	public NormalTest() {
		simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
	}

	public static void main(String[] args) {
		NormalTest test = new NormalTest();
		try {
			test.testSalesByCustomerSummary();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String doSigning(String url) {
		byte[] secretKeyBytes = secretKey.getBytes();
		SecretKeySpec keySpec = new SecretKeySpec(secretKeyBytes, ALGORITHM);
		try {
			Mac mac = Mac.getInstance(ALGORITHM);
			mac.init(keySpec);
			byte[] doFinal = mac.doFinal(url.getBytes());
			String encode = Base64.encode(doFinal);
			return encode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void testSalesByCustomerSummary() throws IOException {
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
}
