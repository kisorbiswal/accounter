package com.vimukti.accounter.web.client.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientIdentity implements IsSerializable, Cloneable {

	/**
	 * This Map contains Dashboard portlet states Key=Portlet Name Value=This is
	 * s digit value, each digit reprasnetPortalet Visibleity and Position(Row
	 * And Columns Values). First Digit: Values will be 1=Disbled Portlet OR
	 * 2=Enabled Second Digit: Column of Portlet Third DIgit: Row of Potlet
	 * Fouth Digit: MiniMized=3 OR Maxized=4
	 */

	public Map<String, Integer> portletState = new HashMap<String, Integer>();

	private String emailAddress;
	public String displayName;
	public String fullname;
	public String status;
	public int loginCount;
	public boolean isPasswordChanged;
	private String identityID;
	private HashMap<String, String> userAccessInfo = new HashMap<String, String>();

	// For Lite User there will be no holidayCalendarId so equating it to empty
	// string to eliminate NullPointer Exceptions.
	public static final int PORTLET_VISIBILITY = 0;
	public static final int ROW_NUMBER = 1;
	public static final int COLUMN_NUMBER = 2;
	public static final int WINDOW_STATE = 3;
	public ClientBizantraCompany company;

	public String mainServerDomain;

	public int mainServerPort;

	private String bizantraVersion;

	// private IMessageIndicatorListener messageIndicatorListener;

	public ClientIdentity() {
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getDisplayName() {
		if (displayName == null)
			return emailAddress;
		else
			return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public ClientBizantraCompany getCompany() {
		return company;
	}

	public void setCompany(ClientBizantraCompany company) {
		this.company = company;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return identityID;
	}

	public void setId(String id) {
		this.identityID = id;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setPortletState(String portletName, int value) {
		this.portletState.put(portletName, value);
	}

	public boolean isUKVersion() {
		return company.bizantraVersion == BizantraConstants.UK_VERSION ? true
				: false;

	}

	/**
	 * Returns the status for particular constant.
	 * 
	 * @param portletOrdinal
	 * @param portletStatus
	 * @return
	 */
	public int getStatusToken(String portletName, int type) {
		int value = this.portletState.get(portletName);
		String str = String.valueOf(value);

		switch (type) {
		case 1:
			// Enabled OR Diabled
			return Integer.valueOf(str.substring(0, 1));

		case 2:
			// column
			return Integer.valueOf(str.substring(1, 2));
		case 3:
			// row
			return Integer.valueOf(str.substring(2, 3));
		case 4:
			// minimize
			return Integer.valueOf(str.substring(3, 4));

		}

		return 0;
	}

	/**
	 * Returns the status for particular constant.
	 * 
	 * @param portletOrdinal
	 * @param portletStatus
	 * @return
	 */
	public void setPortletStatusToken(String portletName, int type, int value) {
		int portletvalue = this.portletState.get(portletName);
		String str = String.valueOf(portletvalue);
		StringBuffer buf = new StringBuffer(str);

		switch (type) {
		case 1:
			// Enabled OR Diabled
			buf.replace(0, 1, String.valueOf(value));
			break;

		case 2:
			// column
			buf.replace(1, 2, String.valueOf(value));
			break;

		case 3:
			// row
			buf.replace(2, 3, String.valueOf(value));
			break;
		case 4:
			// minimize
			buf.replace(3, 4, String.valueOf(value));
			break;

		}
		this.portletState.put(portletName, Integer.valueOf(buf.toString()));

	}

	public int getPortletState(String portletName) {
		return this.portletState.get(portletName);
	}

	public void setUserAccessInfo(HashMap<String, String> userAccessInfo) {
		this.userAccessInfo = userAccessInfo;
	}

	public HashMap<String, String> getUserAccessInfo() {
		return userAccessInfo;
	}

	public List<String> getUserAccessableCategory() {
		List<String> userCategory = new ArrayList<String>();
		Set<Entry<String, String>> entrySet = this.userAccessInfo.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			if (!next.getValue().equals(BizantraConstants.NO_ACESS)) {
				userCategory.add(next.getKey());
			}
		}
		return userCategory;
	}

	public List<String> getUserViewableCategory() {
		List<String> userCategory = new ArrayList<String>();
		Set<Entry<String, String>> entrySet = this.userAccessInfo.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			if (next.getValue().equals(BizantraConstants.FULL_ACESS)) {
				userCategory.add(next.getKey());
			}
		}
		return userCategory;
	}

	public String getUserCategoryAccess(String key) {
		return this.userAccessInfo.get(key);

	}

	public String getMessageDetails() {
		return emailAddress + ',' + this.getDisplayName() + ','
				+ company.companyDomainName;
	}

	public String getBizantraVersion() {
		return bizantraVersion;
	}

	public void setBizantraVersion(String bizantraVersion) {
		this.bizantraVersion = bizantraVersion;
	}

}
