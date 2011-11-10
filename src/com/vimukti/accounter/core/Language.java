package com.vimukti.accounter.core;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class Language implements IAccounterServerCore {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String langugeName;
	private String languageTooltip;
	private String languageCode;

	public Language() {
	}

	public void setLangugeName(String langugeName) {
		this.langugeName = langugeName;
	}

	public String getLangugeName() {
		return langugeName;
	}

	public void setLanguageTooltip(String languageTooltip) {
		this.languageTooltip = languageTooltip;
	}

	public String getLanguageTooltip() {
		return languageTooltip;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}
}
