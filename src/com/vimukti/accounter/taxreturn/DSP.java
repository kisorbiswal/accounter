package com.vimukti.accounter.taxreturn;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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

	private XStream xStream;
	private String body;

	private DSP(String body) {
		this.body = body;
		init();
		xStream = XStreamUtil.getXstream();
	}

	public static void main(String[] args) throws Exception {
		new DSP("").submit();
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
				"https://secure.gateway.gov.uk/submission",
				GovTalkMessageGenerator.getRequestMessage());

		if (isContainErrors(response)) {
			// TODO
		}

		wait(Long.parseLong(response.getHeader().getMessageDatails()
				.getResponseEndPoint().getPollInterval()));

		// Poll
		response = send(
				"https://secure.gateway.gov.uk/poll",
				GovTalkMessageGenerator.getPollMessage(response.getHeader()
						.getMessageDatails().getCorrelationID()));
		if (isContainErrors(response)) {
			// TODO
		}

		wait(Long.parseLong(response.getHeader().getMessageDatails()
				.getResponseEndPoint().getPollInterval()));

		// Re-Poll
		response = send(
				"https://secure.gateway.gov.uk/poll",
				GovTalkMessageGenerator.getPollMessage(response.getHeader()
						.getMessageDatails().getCorrelationID()));

		if (isContainErrors(response)) {
			// TODO
		}

		// DELETE
		response = send(
				"https://secure.gateway.gov.uk/submission",
				GovTalkMessageGenerator.getDeleteMessage(response.getHeader()
						.getMessageDatails().getCorrelationID()));
	}

	private boolean isContainErrors(GovTalkMessage response) {
		// TODO Auto-generated method stub
		return false;
	}

	private String getMD5(String password) throws Exception {
		byte[] digest = MessageDigest.getInstance("MD5").digest(
				password.toLowerCase().getBytes("UTF-8"));
		return new Base64Encoder().encode(digest);
	}

	private GovTalkMessage send(String path, GovTalkMessage message) {
		try {
			URL ourURL = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) ourURL
					.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; JVM)");
			conn.setDoOutput(true);

			FileInputStream fileInputStream = new FileInputStream(
					new File(
							"C:/Users/vimukti04/Desktop/accounter/HMRC-VAT/LTS3.10/HMRCTools/TestData/New folder",
							"vatrequest.xml"));
			byte[] data = new byte[fileInputStream.available()];
			fileInputStream.read(data);
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(data);
			outputStream.flush();
			outputStream.close();

			// message.toXML(conn.getOutputStream());
			return (GovTalkMessage) xStream.fromXML(conn.getInputStream());
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
