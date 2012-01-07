package com.vimukti.accounter.taxreturn;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import com.vimukti.accounter.taxreturn.core.GovTalkMessage;

public class DSP {

	private GovTalkMessage message;

	private XStream xStream;

	private DSP(GovTalkMessage message) {
		this.message = message;
		init();
		xStream = XStreamUtil.getXstream();
	}

	private void init() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}
	}

	public void submit() throws Exception {
		// Request
		GovTalkMessage response = send(
				"https://secure.gateway.gov.uk/submission", true);

		// Poll
		response = send("https://secure.gateway.gov.uk/poll", true);

		// Re-Poll
		response = send("https://secure.gateway.gov.uk/poll", true);

		// DELETE
		response = send("https://secure.gateway.gov.uk/submission", true);
	}

	private String getMD5(String password) throws Exception {
		byte[] digest = MessageDigest.getInstance("MD5").digest(
				password.toLowerCase().getBytes("UTF-8"));
		return new Base64Encoder().encode(digest);
	}

	private GovTalkMessage send(String path, boolean hasBody) {
		try {
			URL ourURL = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) ourURL
					.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; JVM)");
			conn.setDoOutput(true);
			message.toXML(conn.getOutputStream(), hasBody);
			return (GovTalkMessage) xStream.fromXML(conn.getInputStream());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
