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

	SAVE_AND_CLOSE(Accounter.constants().saveAndClose()),

	SAVE_AND_NEW(Accounter.constants().saveAndNew()),

	REGISTER(Accounter.constants().register()),

	ADD(Accounter.constants().add()),

	EDIT(Accounter.constants().edit()),

	DELETE(Accounter.constants().delete()),

	REVIEW_JOURNAL(Accounter.constants().reviewJournal()),

	CANCEL(Accounter.constants().cancel()),

	APPROVE(Accounter.constants().approve()),

	SUBMIT(Accounter.constants().submit());

	private String title;

	private CustomButtonType(String title) {
		this.title = title;
	}

	public String getValue() {

		return this.title;
	}

}
