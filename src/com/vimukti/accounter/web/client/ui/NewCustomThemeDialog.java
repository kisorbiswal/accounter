package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class NewCustomThemeDialog extends BaseDialog<ClientBrandingTheme> {

	private DynamicForm form;
	private TextItem themeName, overdueBox, creditNoteBox, statementBox,
			quoteBox, cashSaleBox, payPalEmail;
	private ClientBrandingTheme brandingTheme;
	private boolean isEdit;

	public NewCustomThemeDialog(String title,
			ClientBrandingTheme brandingTheme, boolean isNew) {
		super(title, "");
		this.addStyleName("new-custom-theme-dialog");
		this.brandingTheme = brandingTheme;
		this.isEdit = isNew;
		createControls();
		center();
	}

	private void createControls() {
		form = new DynamicForm("formItems");
		themeName = new TextItem("Custom Theme", "themeName");
		themeName.setRequired(true);

		if (isEdit == true) {

			themeName.setValue(brandingTheme.getThemeName().trim());

			overdueBox = new TextItem(messages.overdueInvoiceTitle(),
					"overdueBox");
			overdueBox.setValue(messages.overdueValue());

			creditNoteBox = new TextItem(messages.creditNoteTitle(),
					"creditNoteBox");
			creditNoteBox.setValue(messages.creditNoteValue());

			statementBox = new TextItem(messages.statementTitle(),
					"statementBox");
			statementBox.setValue(messages.statement());

			quoteBox = new TextItem(messages.quoteTitle(), "quoteBox");
			quoteBox.setValue(messages.QuoteOverDueTitle());

			cashSaleBox = new TextItem(messages.cashSaleTitle(), "cashSaleBox");
			cashSaleBox.setValue(messages.cashSaleValue());

			payPalEmail = new TextItem("PayPal Email", "payPalEmail");

			String emailId = brandingTheme.getPayPalEmailID() != null ? brandingTheme
					.getThemeName().trim() : "";
			payPalEmail.setValue(emailId);

			form.add(themeName, overdueBox, creditNoteBox, statementBox,
					quoteBox, cashSaleBox, payPalEmail);
		} else {
			form.add(themeName);
		}

		StyledPanel layout = new StyledPanel("layout");
		layout.add(form);
		setBodyLayout(layout);
	}

	@Override
	protected boolean onOK() {
		if (brandingTheme == null) {
			brandingTheme = new ClientBrandingTheme();
			brandingTheme.setInvoiceTempleteName(messages.classicTemplate());
			brandingTheme.setCreditNoteTempleteName(messages.classicTemplate());
			brandingTheme.setQuoteTemplateName(messages.classicTemplate());
			brandingTheme.setCashSaleTemplateName(messages.classicTemplate());

		}

		if (isEdit == true) {
			brandingTheme.setOverDueInvoiceTitle(this.overdueBox.getValue()
					.toString());
			brandingTheme.setCreditMemoTitle(this.creditNoteBox.getValue()
					.toString());
			brandingTheme.setStatementTitle(this.statementBox.getValue()
					.toString());
			brandingTheme.setQuoteTitle(this.quoteBox.getValue().toString());
			brandingTheme.setCashSaleTitle(this.quoteBox.getValue().toString());
			brandingTheme.setPayPalEmailID(this.payPalEmail.getValue()
					.toString());
		}

		brandingTheme.setThemeName(this.themeName.getValue().toString());
		brandingTheme.setCustomFile(true);
		saveOrUpdate(brandingTheme);

		return true;

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		removeFromParent();
		super.saveSuccess(object);
		ActionFactory.getInvoiceBrandingAction().run(null, true);
	}

	@Override
	protected ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		String name = themeName.getValue().toString();
		if (name.trim().length() == 0)
			result.addError(this, messages.pleaseEnterValidLocationName(""));
		return result;
	}

	public String getLocationGroupName() {
		return this.themeName.getValue().toString();
	}

	@Override
	public void setFocus() {
		themeName.setFocus();

	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
