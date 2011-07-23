/**
 * 
 */
package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Fernandez
 * 
 */
public enum CustomButtonType {

	SAVE_AND_CLOSE(Accounter.getCustomersMessages().saveAndClose()),

	SAVE_AND_NEW(Accounter.getCustomersMessages().saveAndNew()),

	REGISTER(Accounter.getCustomersMessages().register()),

	ADD(Accounter.getCustomersMessages().add()),

	EDIT(Accounter.getCustomersMessages().edit()),

	DELETE(Accounter.getCustomersMessages().delete()),

	REVIEW_JOURNAL(Accounter.getCustomersMessages().reviewJournal()),

	CANCEL(Accounter.getCompanyMessages().cancel()),

	APPROVE(Accounter.getCompanyMessages().approve()),

	SUBMIT(Accounter.getCompanyMessages().submit());

	private String title;

	private CustomButtonType(String title) {
		this.title = title;
	}

	public String getValue() {

		return this.title;
	}

}
