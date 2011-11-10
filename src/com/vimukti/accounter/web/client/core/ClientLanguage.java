package com.vimukti.accounter.web.client.core;

public class ClientLanguage implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String languageName;
	private String languageTooltip;
	private String languageCode;

	public ClientLanguage(String languageTooltip, String langugeName,
			String languageCode) {
		this.setLangugeName(langugeName);
		this.setLanguageTooltip(languageTooltip);
		this.setLanguageCode(languageCode);
	}

	@Override
	public String getClientClassSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

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

	public void setLangugeName(String langugeName) {
		this.languageName = langugeName;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageTooltip(String languageTooltip) {
		this.languageTooltip = languageTooltip;
	}

	public String getLanguageTooltip() {
		return languageTooltip;
	}

	public ClientLanguage clone() {
		ClientLanguage language = (ClientLanguage) this.clone();
		return language;

	}
}
