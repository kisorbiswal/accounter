package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.settings.SettingsActionFactory;

public class BrandingThemeCombo extends CustomCombo<ClientBrandingTheme> {

	public BrandingThemeCombo(String title) {
		super(title);
		initCombo(FinanceApplication.getCompany().getBrandingTheme());
	}

	public BrandingThemeCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1);
		initCombo(FinanceApplication.getCompany().getBrandingTheme());
	}

	@Override
	protected String getDisplayName(ClientBrandingTheme object) {
		if (object != null)
			return object.getThemeName() != null ? object.getThemeName() : "";
		else
			return "";
	}

	@Override
	public SelectItemType getSelectItemType() {
		return SelectItemType.BRANDING_THEME;
	}

	@Override
	protected String getColumnData(ClientBrandingTheme object, int row, int col) {
		switch (col) {
		case 0:
			return object.getThemeName();
		}
		return null;
	}
	@Override
	public void setDefaultValue(String value) {
		super.setDefaultValue(value);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return FinanceApplication.getSettingsMessages().newBrandThemeLabel();
	}

	@Override
	public void onAddNew() {
		Action action = SettingsActionFactory.getNewBrandThemeAction();
		action.setActionSource(this);
		action.run(null, true);
	}

}
