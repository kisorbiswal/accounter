package com.vimukti.accounter.core;

import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.FinanceApplication;

public class CustomButton1 extends Button {
	public CustomButton1(String name) {
		super(name);
	}

	@Override
	protected void onAttach() {
		if (this != null) {
			super.onAttach();
			if (this.isEnabled()) {
				ThemesUtil.addDivToButton(this, FinanceApplication
						.getThemeImages().button_right_blue_image(),
						"add-right-image");
			}
		}
	}
}
