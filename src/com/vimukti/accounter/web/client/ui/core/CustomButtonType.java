/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.ui.FinanceApplication;

/**
 * @author Fernandez
 * 
 */
public enum CustomButtonType {

	SAVE_AND_CLOSE(FinanceApplication.getCustomersMessages().saveAndClose()),

	SAVE_AND_NEW(FinanceApplication.getCustomersMessages().saveAndNew()),

	REGISTER(FinanceApplication.getCustomersMessages().register()),

	ADD(FinanceApplication.getCustomersMessages().add()),

	EDIT(FinanceApplication.getCustomersMessages().edit()),

	DELETE(FinanceApplication.getCustomersMessages().delete()),

	REVIEW_JOURNAL(FinanceApplication.getCustomersMessages().reviewJournal()),

	CANCEL(FinanceApplication.getCompanyMessages().cancel());

	private String title;

	private CustomButtonType(String title) {
		this.title = title;
	}

	public String getValue() {

		return this.title;
	}

}
