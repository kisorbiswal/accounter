package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

@SuppressWarnings("unchecked")
public class AddresssDilaog extends AbstractBaseDialog {

	private TextItem streetText1;
	private TextItem streetText2;
	private TextItem cityText;
	private TextItem stateText;
	private TextItem countryText;
	private int addrType;

	public AddresssDilaog(AbstractBaseView parent, int addrType) {
		super(parent);
		this.addrType = addrType;
		createControls();
	}

	private void createControls() {
		setModal(true);

		streetText1 = new TextItem();
		streetText1.setTitle(Accounter.constants()
				.streetAddress1());
		streetText2 = new TextItem();
		streetText2.setTitle(Accounter.constants()
				.streetAddress2());
		cityText = new TextItem();

		cityText.setTitle(Accounter.constants().city());
		stateText = new TextItem();
		stateText.setTitle(Accounter.constants().state());
		TextItem zipText = new TextItem();
		zipText.setTitle(Accounter.constants()
				.postalCode());
		countryText = new TextItem();
		countryText.setTitle(Accounter.constants()
				.country());

		DynamicForm form = new DynamicForm();
		form.setFields(streetText1, streetText2, cityText, stateText, zipText,
				countryText);

		AccounterButton helpButt = new AccounterButton(Accounter
				.constants().help());
		// helpButt.setLayoutAlign(Alignment.LEFT);
		HorizontalPanel helpHLay = new HorizontalPanel();
		helpHLay.setWidth("50%");
		helpHLay.add(helpButt);
		helpButt.enabledButton();
		AccounterButton okButt = new AccounterButton(Accounter
				.constants().ok());
		// okButt.setLayoutAlign(Alignment.RIGHT);

		AccounterButton canButt = new AccounterButton(Accounter
				.constants().cancel());
		// canButt.setLayoutAlign(Alignment.RIGHT);

		HorizontalPanel buttHLay = new HorizontalPanel();
		// buttHLay.setMargin(10);
		// buttHLay.setMembersMargin(20);
		// buttHLay.setAlign(Alignment.RIGHT);
		buttHLay.add(okButt);
		buttHLay.add(canButt);
		okButt.enabledButton();
		canButt.enabledButton();
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.add(form);
		mainVLay.add(buttHLay);

		setSize("300", "300");
		add(mainVLay);
		// show();
	}

	public ClientAddress getAddress() {
		ClientAddress address = new ClientAddress();

		address.setCity(UIUtils.toStr(cityText.getValue()));
		address.setCountryOrRegion(UIUtils.toStr(countryText.getValue()));
		address.setStateOrProvinence(UIUtils.toStr(stateText.getValue()));
		// address.setStreet(UIUtils.toStr(streetText1.getValue()) + " "
		// + UIUtils.toStr(streetText2.getValue()));
		address.setType(addrType);

		return address;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}
	// FinanceApplication.constants().address();
}
