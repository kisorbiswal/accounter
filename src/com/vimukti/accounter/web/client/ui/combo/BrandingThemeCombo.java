package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.customers.BrandingThemeComboDialog;
import com.vimukti.accounter.web.client.ui.settings.NewBrandThemeAction;

public class BrandingThemeCombo extends CustomCombo<ClientBrandingTheme> {

	private BrandingThemeComboDialog themeDialog;

	public BrandingThemeCombo(String title) {
		super(title);
		initCombo(Accounter.getCompany().getBrandingTheme());
	}

	public BrandingThemeCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1);
		initCombo(Accounter.getCompany().getBrandingTheme());
	}

	@Override
	protected String getDisplayName(ClientBrandingTheme object) {
		if (object != null)
			return object.getThemeName() != null ? object.getThemeName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientBrandingTheme object, int col) {
		switch (col) {
		case 0:
			return object.getThemeName();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.brandingTheme();
	}

	@Override
	public void onAddNew() {
		if (Accounter.hasPermission(Features.BRANDING_THEME)) {

			if (themeDialog != null) {
				themeDialog.hide();
			}

			NewBrandThemeAction action = ActionFactory.getNewBrandThemeAction();
			action.setCallback(new ActionCallback<ClientBrandingTheme>() {

				@Override
				public void actionResult(ClientBrandingTheme result) {
					if (result.getName() != null)
						addItemThenfireEvent(result);
					if (themeDialog != null) {
						themeDialog.show();
					}
				}
			});

			action.run(null, true);
		} else {
			Accounter.showSubscriptionWarning();
		}
	}

	public BrandingThemeComboDialog getThemeDialog() {
		return themeDialog;
	}

	public void setThemeDialog(BrandingThemeComboDialog themeDialog) {
		this.themeDialog = themeDialog;
	}
}
