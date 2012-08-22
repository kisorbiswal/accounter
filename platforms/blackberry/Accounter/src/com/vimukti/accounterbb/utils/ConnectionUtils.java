package com.vimukti.accounterbb.utils;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.SecureConnection;

import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;

public class ConnectionUtils {

	

	public static SecureConnection getConnection(String url) throws IOException {
		SecureConnection connection = getWifiConnection(url);
		if (connection == null) {
			connection = getMDSConnection(url);
		}
		if (connection == null) {
			connection = getDirectTCPConnection(url);
		}
		if (connection == null) {
			connection = getWAP2Connection(url);
		}
		if (connection == null) {
			connection = getBISConnection(url);
		}
		if (connection != null) {
			return connection;
		} else {
			throw new IOException("Unable to connect to server");
		}
	}

	private static SecureConnection getBISConnection(String url) {
		try {
			return (SecureConnection) Connector.open(url
					+ ";deviceside=false;ConnectionType=mds-public",
					Connector.READ_WRITE, true);
		} catch (IOException e) {
			return null;
		}
	}

	private static SecureConnection getDirectTCPConnection(String url) {
		try {
			return (SecureConnection) Connector.open(url + ";deviceside=true",
					Connector.READ_WRITE, true);
		} catch (IOException e) {
			return null;
		}
	}

	private static SecureConnection getMDSConnection(String url) {
		try {
			return (SecureConnection) Connector.open(url + ";deviceside=false",
					Connector.READ_WRITE, true);
		} catch (IOException e) {
			return null;
		}
	}

	private static SecureConnection getWAP2Connection(String url) {
		ServiceBook sb = ServiceBook.getSB();
		int coverageStatus = CoverageInfo.getCoverageStatus();
		ServiceRecord[] records = sb.getRecords();
		for (int i = 0; i < records.length; i++) {
			String cid = records[i].getCid().toLowerCase();
			String uid = records[i].getUid().toLowerCase();
			if (cid.indexOf("wptcp") != -1 && uid.indexOf("wifi") == -1
					&& uid.indexOf("mms") == -1) {
				if (records[i] != null
						&& (coverageStatus & CoverageInfo.COVERAGE_DIRECT) == CoverageInfo.COVERAGE_DIRECT) {
					try {
						return (SecureConnection) Connector.open(url
								+ ";deviceside=true;ConnectionUID=",
								Connector.READ_WRITE, true);
					} catch (IOException e) {
						continue;
					}
				}
			}
		}
		return null;
	}

	private static SecureConnection getWifiConnection(String url) {
		try {
			return (SecureConnection) Connector.open(url + ";interface=wifi",
					Connector.READ_WRITE, true);
		} catch (IOException e) {
			return null;
		}

	}

}
