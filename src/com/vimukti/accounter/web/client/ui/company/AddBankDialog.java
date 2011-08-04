package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientBank;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class AddBankDialog extends BaseDialog {

	private TextItem bankNameText;
	private AccounterAsyncCallback<ClientBank> callBack;

	public AddBankDialog(AbstractBaseView<ClientBank> parent) {
		super(Accounter.constants().addBank(), null);
		createControls();
		center();
	}

	private void createControls() {

		setText(Accounter.constants().addBank());

		bankNameText = new TextItem(Accounter.constants().bankName());
		bankNameText.setRequired(true);
		final DynamicForm bankForm = new DynamicForm();
		bankForm.setFields(bankNameText);

		AccounterButton helpButt = new AccounterButton(Accounter.constants()
				.help());
		AccounterButton okButt = new AccounterButton(Accounter.constants().ok());
		AccounterButton canButt = new AccounterButton(Accounter.constants()
				.cancel());

		HorizontalPanel helpHLay = new HorizontalPanel();
		helpHLay.setWidth("50%");
		helpHLay.add(helpButt);
		helpButt.enabledButton();
		HorizontalPanel buttHLay = new HorizontalPanel();
		buttHLay.setSpacing(3);
		// buttHLay.add(helpHLay);
		buttHLay.add(okButt);
		buttHLay.add(canButt);
		okButt.enabledButton();
		canButt.enabledButton();
		okButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!bankForm.validate(true)) {
					// Accounter.showError(FinanceApplication.constants()
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
		if (Utility.isObjectExist(company.getTaxItems(), bank.getName())) {
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
		// Accounter.showInformation(FinanceApplication.constants()
		// .bankCreated());
		removeFromParent();
		super.saveSuccess(object);
	}

	@Override
	public void saveFailed(Throwable exception) {
		Accounter.showError(Accounter.constants().failedToCreateBank());
		super.saveFailed(exception);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// its not using any where

	}

	public void addCallBack(AccounterAsyncCallback<ClientBank> callback) {
		this.callBack = callback;
	}

}
