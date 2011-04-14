package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.AbstractBaseDialog;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AddBankDialog extends AbstractBaseDialog<ClientBank> {

	CompanyMessages companyConstants = GWT.create(CompanyMessages.class);
	private TextItem bankNameText;

	public AddBankDialog(AbstractBaseView<ClientBank> parent) {
		super(parent);
		company = FinanceApplication.getCompany();
		createControls();
		center();
	}

	private void createControls() {

		setTitle(companyConstants.addBank());
		setText(companyConstants.addBank());

		bankNameText = new TextItem(companyConstants.bankName());
		bankNameText.setRequired(true);
		final DynamicForm bankForm = new DynamicForm();
		bankForm.setFields(bankNameText);

		Button helpButt = new Button(companyConstants.help());
		Button okButt = new Button(companyConstants.ok());
		Button canButt = new Button(companyConstants.cancel());

		HorizontalPanel helpHLay = new HorizontalPanel();
		helpHLay.setWidth("50%");
		helpHLay.add(helpButt);
		helpButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(helpButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		HorizontalPanel buttHLay = new HorizontalPanel();
		buttHLay.setSpacing(3);
		// buttHLay.add(helpHLay);
		buttHLay.add(okButt);
		buttHLay.add(canButt);
		okButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(okButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		canButt.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(canButt, FinanceApplication.getThemeImages()
				.button_right_blue_image(), "ibutton-right-image");
		okButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!bankForm.validate(true)) {
					// Accounter.showError(FinanceApplication.getCompanyMessages()
					// .youMustEnterBankName());
					return;
				}
				createBank();

			}
		});

		canButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				removeFromParent();
			}
		});

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(bankForm);
		mainVLay.add(buttHLay);
		mainVLay.setCellHorizontalAlignment(buttHLay,
				HasHorizontalAlignment.ALIGN_RIGHT);
		mainVLay.setSpacing(3);
		add(mainVLay);
		setWidth("275");
	}

	protected void createBank() {
		final ClientBank bank = new ClientBank();
		bank.setName(UIUtils.toStr(bankNameText.getValue()));
		if (Utility.isObjectExist(
				FinanceApplication.getCompany().getTaxItems(), bank.getName())) {
			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else {
			ViewManager.getInstance().createObject(bank, this);
		}
	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		if (callBack != null) {
			callBack.onSuccess((ClientBank) object);
		}
		// Accounter.showInformation(FinanceApplication.getCompanyMessages()
		// .bankCreated());
		removeFromParent();
		super.saveSuccess(object);
	}

	@Override
	public void saveFailed(Throwable exception) {
		Accounter.showError(FinanceApplication.getCompanyMessages()
				.failedToCreateBank());
		super.saveFailed(exception);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
