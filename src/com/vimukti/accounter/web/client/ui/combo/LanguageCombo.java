package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientLanguage;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;

public class LanguageCombo extends CustomCombo<ClientLanguage> {

	public LanguageCombo(String title) {
		super(title, false, 1);
		initCombo(CoreUtils.getLanguages());
	}

	@Override
	protected String getDisplayName(ClientLanguage object) {
		if (object.getLanguageName() != null) {
			return object.getLanguageName();
		}
		return null;
	}

	@Override
	protected String getColumnData(ClientLanguage object, int col) {
		if (object != null)
			if (object.getLanguageTooltip().equals(object.getLanguageName())) {
				return object.getLanguageName();
			} else {
				return Accounter.messages().languageName(
						object.getLanguageName(), object.getLanguageTooltip());
			}
		return "";
	}

	@Override
	public String getDefaultAddNewCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub

	}

}
