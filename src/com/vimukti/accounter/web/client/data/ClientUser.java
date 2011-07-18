package com.vimukti.accounter.web.client.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class ClientUser implements Serializable, IsSerializable, Cloneable {

	/**
	 * 
	 */
	public static final String AVAILABLE = "Available";
	public static final String OFFLINE = "Offline";
	private static final long serialVersionUID = 1L;
	public String id;
	public String emailID;
	public String role;
	public String company;
	public String originalCompany;
	public String address1 = "";
	public String address2 = "";
	public String city = "";
	public String province = "";
	public String country = "";
	public String zipCode = "";
	public String workphone = "";
	public String mobile = "";
	public boolean isLocked;
	public boolean isExternal;
	public String status;
	public String fullName;
	public String displayName;
	public String designation = "";
	public String gender = "";
	public String title = "";
	public long lastLogin;

	public Set<String> externalSpaceId = new HashSet<String>();
	public boolean isItDeletedUser = false;
	public boolean isDuplicateMember;
	private Map<String, String> accessInfo = new HashMap<String, String>();
	public String[] categories = new String[] {
			BizantraConstants.HR_CATEGORYNAME,
			BizantraConstants.FINANCE_CATEGORYNAME,
			BizantraConstants.SALES_CATEGORYNAME,
			BizantraConstants.PURCHAGES_CATEGORYNAME,
			BizantraConstants.MARKETING_CATEGORYNAME,
			BizantraConstants.USERS_TAB_NAME };

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getWorkphone() {
		return workphone;
	}

	public void setWorkphone(String workphone) {
		this.workphone = workphone;
	}

	/**
	 * If not required this feild i will delete
	 */
	String identityID;
	public boolean isExternalCompany;

	/***
	 * Key: Category Name and Value=AccessLevel
	 */

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String userName) {
		this.fullName = userName;
	}

	public boolean isItDeletedUser() {
		return isItDeletedUser;
	}

	public void setItDeletedUser(boolean isItDeletedUser) {
		this.isItDeletedUser = isItDeletedUser;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getID() {
		return this.id;
	}

	public Map<String, String> getAccessInfo() {
		return accessInfo;
	}

	public void setAccessInfo(Map<String, String> accessInfo) {
		for (String key : accessInfo.keySet()) {
			this.accessInfo.put(key, accessInfo.get(key));
		}

	}

	public String getEmailID() {
		return emailID;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public boolean isExternal() {
		return isExternal;
	}

	public void setExternal(boolean isExternal) {
		this.isExternal = isExternal;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public ClientUser() {
		// TODO Auto-generated constructor stub
	}

	public String getStatus() {
		if (status != null) {
			return status;
		}
		return OFFLINE;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getNameWithEmailID() {
		String name = "";
		if (displayName != null && displayName.trim().length() != 0) {
			name = displayName;
		} else {
			if (fullName != null && fullName.trim().length() != 0) {
				name = fullName;
			}
		}
		if (name.trim().length() == 0) {
			return emailID;
		}
		return name + " < " + emailID + " >";
	}

	public void setAcessPermissions(String role2) {
		for (int i = 0; i < categories.length; i++) {
			if (role2.equals(BizantraConstants.SUPER_USER_ROLE)) {
				accessInfo.put(categories[i], BizantraConstants.MANAGER_ACESS);
			} else if (role2.equals(BizantraConstants.USER_ROLE)
					|| role2.equals(BizantraConstants.LITE_USER)
					|| role2.equals(BizantraConstants.FULL_USER)
					|| role2.equals(BizantraConstants.FINANCE_LITE_USER)) {

				if (role2.equals(BizantraConstants.FINANCE_LITE_USER)
						&& categories[i]
								.equals(BizantraConstants.FINANCE_CATEGORYNAME)) {
					accessInfo.put(categories[i],
							BizantraConstants.RESTRICTED_ACESS);
					continue;
				}
				accessInfo.put(categories[i], BizantraConstants.NO_ACESS);
			} else {

				if (categories[i].equals(BizantraConstants.USERS_TAB_NAME)) {
					accessInfo.put(categories[i], BizantraConstants.FULL_ACESS);
				} else {
					accessInfo.put(categories[i], BizantraConstants.NO_ACESS);
				}
			}

		}

	}

	public void setExternalSpaceId(String externalSpaceId) {
		this.externalSpaceId.add(externalSpaceId);
	}

	public Set<String> getExternalSpaceId() {
		return externalSpaceId;
	}

	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public ClientFinanceDate getLastLogin() {
		return new ClientFinanceDate(lastLogin);
	}

}
