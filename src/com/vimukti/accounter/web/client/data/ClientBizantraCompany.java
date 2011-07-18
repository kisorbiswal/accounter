package com.vimukti.accounter.web.client.data;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClientBizantraCompany implements IsSerializable, Serializable,
		Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String companyDomainName;
	public String companyDisplayName;
	public Date createdDate;
	public Date expirationDate;
	public int noofusers;
	public int noOfLiteUsers;
	public long totalSize;
	public int bizantraVersion;
	public String address;
	public String city;
	public String country;
	public String zip;
	public String province;
	public long usedDataSize;
	public String holidayStartDate;
	public String dateFormat;
	public int dateStyle;
	public String countryDateCode;
	public boolean isOnlySundaysWeekend;
	public Date deletionDate;
	public String subsType;
	public String subscriberEmail;
}