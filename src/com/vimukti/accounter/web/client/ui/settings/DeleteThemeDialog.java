package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class DeleteThemeDialog extends BaseDialog {

	private HTML deleteHtml, undoneHtml;
	// private Button deleteButton, cancelButton;
	private ClientBrandingTheme brandingTheme;

	public DeleteThemeDialog(String title, String desc,
			ClientBrandingTheme theme) {
		super(title, desc);
		this.brandingTheme = theme;
		createControls();
	}

	private void createControls() {
		VerticalPanel deletePanel = new VerticalPanel();

		deleteHtml = new HTML(Accounter.messages().sureToDelete(
				brandingTheme.getThemeName()));
		undoneHtml = new HTML(Accounter.messages().undoneHtml());

		okbtn.setText(Accounter.constants().deleteButton());

		deletePanel.add(deleteHtml);
		deletePanel.add(undoneHtml);

		setBodyLayout(deletePanel);
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		super.deleteSuccess(result);
		ActionFactory.getInvoiceBrandingAction().run(null, true);
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (brandingTheme.isDefault()) {
			result.addError(this, Accounter.messages().wecantDeleteThisTheme(
					brandingTheme.getThemeName()));
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		Accounter.deleteObject(this, brandingTheme);
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
