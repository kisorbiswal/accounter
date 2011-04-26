package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
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
		setTitle(FinanceApplication.getFinanceUIConstants().address());
		setModal(true);

		streetText1 = new TextItem();
		streetText1.setTitle(FinanceApplication.getFinanceUIConstants()
				.streetAddress1());
		streetText2 = new TextItem();
		streetText2.setTitle(FinanceApplication.getFinanceUIConstants()
				.streetAddress2());
		cityText = new TextItem();

		cityText.setTitle(FinanceApplication.getFinanceUIConstants().city());
		stateText = new TextItem();
		stateText.setTitle(FinanceApplication.getFinanceUIConstants().state());
		TextItem zipText = new TextItem();
		zipText.setTitle(FinanceApplication.getFinanceUIConstants()
				.postalCode());
		countryText = new TextItem();
		countryText.setTitle(FinanceApplication.getFinanceUIConstants()
				.country());

		DynamicForm form = new DynamicForm();
		form.setFields(streetText1, streetText2, cityText, stateText, zipText,
				countryText);

		Button helpButt = new Button(FinanceApplication.getFinanceUIConstants()
				.help());
		// helpButt.setLayoutAlign(Alignment.LEFT);
		HorizontalPanel helpHLay = new HorizontalPanel();
		helpHLay.setWidth("50%");
		helpHLay.add(helpButt);
		if (helpButt.isEnabled()) {
			helpButt.getElement().getParentElement().setClassName("ibutton");
			ThemesUtil.addDivToButton(helpButt, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
		Button okButt = new Button(FinanceApplication.getFinanceUIConstants()
				.ok());
		// okButt.setLayoutAlign(Alignment.RIGHT);

		Button canButt = new Button(FinanceApplication.getFinanceUIConstants()
				.cancel());
		// canButt.setLayoutAlign(Alignment.RIGHT);

		HorizontalPanel buttHLay = new HorizontalPanel();
		// buttHLay.setMargin(10);
		// buttHLay.setMembersMargin(20);
		// buttHLay.setAlign(Alignment.RIGHT);
		buttHLay.add(okButt);
		buttHLay.add(canButt);
		if (okButt.isEnabled()) {
			okButt.getElement().getParentElement().setClassName("ibutton");
			ThemesUtil.addDivToButton(okButt, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
		if (canButt.isEnabled()) {
			canButt.getElement().getParentElement().setClassName("ibutton");
			ThemesUtil.addDivToButton(canButt, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
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
}
