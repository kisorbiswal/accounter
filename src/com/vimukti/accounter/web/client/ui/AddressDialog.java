package com.vimukti.accounter.web.client.ui;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author venki.p
 * 
 */
@SuppressWarnings("unchecked")
public class AddressDialog extends BaseDialog {

	ClientAddress addNew;
	TextAreaItem textAreaItem;
	
	public AddressDialog(String title, String description,
			final TextAreaItem textAreaItem, final String addressType,
			final LinkedHashMap<Integer, ClientAddress> allAddresses) {
		super(FinanceApplication.getCompanyMessages().address(), "");
		createControls(addressType, textAreaItem, allAddresses);
	}

	protected void createControls(final String addressType,
			final TextAreaItem textAreaItem,
			final LinkedHashMap<Integer, ClientAddress> allAddresses) {

		this.textAreaItem = textAreaItem;
		ClientAddress add = null;
		if (allAddresses != null) {
			add = allAddresses.get(UIUtils.getAddressType(addressType));
		}
		// final TextItem street = new TextItem(FinanceApplication
		// .getFinanceUIConstants().streetName());
		final TextItem address1 = new TextItem(FinanceApplication
				.getFinanceUIConstants().address1());
		address1.setHelpInformation(true);

		final TextItem address2 = new TextItem(FinanceApplication
				.getFinanceUIConstants().address2());
		address2.setHelpInformation(true);

		final TextItem city = new TextItem(FinanceApplication
				.getFinanceUIConstants().city());
		city.setHelpInformation(true);

		final TextItem state = new TextItem(FinanceApplication
				.getFinanceUIConstants().state());
		state.setHelpInformation(true);

		final TextItem country = new TextItem(FinanceApplication
				.getFinanceUIConstants().country());
		country.setHelpInformation(true);

		final TextItem zip = new TextItem(FinanceApplication
				.getFinanceUIConstants().postalCode());
		zip.setHelpInformation(true);
		// country.setWidth(100);

		// country.setValueMap(FinanceApplication.getFinanceUIConstants().uk(),
		// FinanceApplication.getFinanceUIConstants().india(),
		// FinanceApplication.getFinanceUIConstants().us());
		// Iterator itr=countryList.iterator();
		// while(itr.hasNext())
		// // country.setValue(itr.next());
		// String[] countryList = (String[]) getCountryList().toArray();
		// country.setCountryList();

		// country.setValueMap(getCountryList());
		// country.setDefaultValue(FinanceApplication.getCompanyMessages().UK());

		if (add != null) {
			if (add.getAddress1() != null)
				address1.setValue(add.getAddress1());
			if (add.getStreet() != null)
				address2.setValue(add.getStreet());
			if (add.getCity() != null)
				city.setValue(add.getCity());
			if (add.getStateOrProvinence() != null)
				state.setValue(add.getStateOrProvinence());
			if (add.getZipOrPostalCode() != null)
				zip.setValue(add.getZipOrPostalCode());
			if (add.getCountryOrRegion() != null)
				country.setValue(add.getCountryOrRegion());
		}

		DynamicForm AddressForm = new DynamicForm();
		AddressForm.setCellSpacing(10);
		AddressForm.setWidth("100%");
		AddressForm.setFields(address1, address2, city, state, zip, country);
		addNew = add;
		addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {

				if (addNew != null) {
					String toBeSet = "";
					if (addNew.getAddress1().trim() != null
							&& !addNew.getAddress1().trim().isEmpty())
						toBeSet += addNew.getAddress1().trim() + "\n";
					if (addNew.getStreet().trim() != null
							&& !addNew.getStreet().trim().isEmpty())
						toBeSet += addNew.getStreet().trim() + "\n";
					if (addNew.getCity().trim() != null
							&& !addNew.getCity().trim().isEmpty())
						toBeSet += addNew.getCity().trim() + "\n";
					if (addNew.getStateOrProvinence().trim() != null
							&& !addNew.getStateOrProvinence().trim().isEmpty())
						toBeSet += addNew.getStateOrProvinence().trim() + "\n";
					if (addNew.getZipOrPostalCode().trim() != null
							&& !addNew.getZipOrPostalCode().trim().isEmpty())
						toBeSet += addNew.getZipOrPostalCode().trim() + "\n";
					if (addNew.getCountryOrRegion().trim() != null
							&& !addNew.getCountryOrRegion().trim().isEmpty())
						toBeSet += addNew.getCountryOrRegion().trim();
					textAreaItem.setValue(toBeSet);
				} else
					textAreaItem.setValue("");
				removeFromParent();

			}

			public boolean onOkClick() {
				String toBeSet = "";
				int isEmptyCounter = 0;

				ClientAddress value = new ClientAddress();
				value.setType(UIUtils.getAddressType(addressType));
				if (address1.getValue().toString().trim() != null
						&& !address1.getValue().toString().trim().isEmpty()) {
					isEmptyCounter++;
					toBeSet = address1.getValue().toString().trim() + "\n";
					value.setAddress1(address1.getValue().toString().trim());
				}
				if (address2.getValue().toString().trim() != null
						&& !address2.getValue().toString().trim().isEmpty()) {
					isEmptyCounter++;
					toBeSet += address2.getValue().toString().trim() + "\n";
					value.setStreet(address2.getValue().toString().trim());
				}

				if (city.getValue() != null
						&& !city.getValue().toString().trim().isEmpty()) {
					isEmptyCounter++;
					toBeSet += city.getValue().toString().trim() + "\n";
					value.setCity(city.getValue().toString().trim());
				}

				if (state.getValue() != null
						&& !state.getValue().toString().trim().isEmpty()) {
					isEmptyCounter++;
					toBeSet += state.getValue().toString().trim() + "\n";
					value.setStateOrProvinence(state.getValue().toString()
							.trim());
				}

				if (zip.getValue() != null
						&& !zip.getValue().toString().isEmpty()) {
					isEmptyCounter++;
					toBeSet += zip.getValue().toString().trim() + "\n";
					value.setZipOrPostalCode(zip.getValue().toString().trim());
				}

				if (country.getValue() != null
						&& !country.getValue().toString().trim().isEmpty()) {
					isEmptyCounter++;
					toBeSet += country.getValue().toString().trim();
					value.setCountryOrRegion(country.getValue().toString()
							.trim());
				}

				if (toBeSet != null && !toBeSet.isEmpty()
						&& isEmptyCounter != 0) {
					textAreaItem.setValue(toBeSet);
					allAddresses
							.put(UIUtils.getAddressType(addressType), value);
					return true;
				} else {
					BaseDialog.errordata.setHTML("<li> "
							+ AccounterErrorType.SHOULD_NOT_EMPTY + ".");
					BaseDialog.commentPanel.setVisible(true);
					// Accounter.showError(AccounterErrorType.SHOULD_NOT_EMPTY);
				}
				return false;
			}

		});

		VerticalPanel v1 = new VerticalPanel();

		v1.add(AddressForm);
		v1.setHeight("100%");
		v1.setWidth("100%");

		setBodyLayout(v1);

		center();

	}

	public void close() {
		removeFromParent();
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		return null;
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	public String[] getCountryList() {
		// List list = new ArrayList();
		String[] list = { FinanceApplication.getCompanyMessages().australia(),
				FinanceApplication.getCompanyMessages().belgium(),
				FinanceApplication.getCompanyMessages().canada(),
				FinanceApplication.getCompanyMessages().cyprus(),
				FinanceApplication.getCompanyMessages().france(),
				FinanceApplication.getCompanyMessages().germany(),
				FinanceApplication.getCompanyMessages().greece(),
				FinanceApplication.getCompanyMessages().india(),
				FinanceApplication.getCompanyMessages().ireland(),
				FinanceApplication.getCompanyMessages().italy(),
				FinanceApplication.getCompanyMessages().kenya(),
				FinanceApplication.getCompanyMessages().malta(),
				FinanceApplication.getCompanyMessages().mauritius(),
				FinanceApplication.getCompanyMessages().mozabique(),
				FinanceApplication.getCompanyMessages().netherlands(),
				FinanceApplication.getCompanyMessages().newZeland(),
				FinanceApplication.getCompanyMessages().nigeria(),
				FinanceApplication.getCompanyMessages().pakistan(),
				FinanceApplication.getCompanyMessages().portugal(),
				FinanceApplication.getCompanyMessages().southAfrica(),
				FinanceApplication.getCompanyMessages().spain(),
				FinanceApplication.getCompanyMessages().switzerland(),
				FinanceApplication.getCompanyMessages().thailand(),
				FinanceApplication.getCompanyMessages().UK(),
				FinanceApplication.getCompanyMessages().USA(),
				FinanceApplication.getCompanyMessages().others() };

		return list;
	}

	@Override
	protected String getViewTitle() {
		return FinanceApplication.getCompanyMessages().address();
	}
	
	@Override
	protected boolean onCloseClick() {
		if (addNew != null) {
			String toBeSet = "";
			if (addNew.getAddress1().trim() != null
					&& !addNew.getAddress1().trim().isEmpty())
				toBeSet += addNew.getAddress1().trim() + "\n";
			if (addNew.getStreet().trim() != null
					&& !addNew.getStreet().trim().isEmpty())
				toBeSet += addNew.getStreet().trim() + "\n";
			if (addNew.getCity().trim() != null
					&& !addNew.getCity().trim().isEmpty())
				toBeSet += addNew.getCity().trim() + "\n";
			if (addNew.getStateOrProvinence().trim() != null
					&& !addNew.getStateOrProvinence().trim().isEmpty())
				toBeSet += addNew.getStateOrProvinence().trim() + "\n";
			if (addNew.getZipOrPostalCode().trim() != null
					&& !addNew.getZipOrPostalCode().trim().isEmpty())
				toBeSet += addNew.getZipOrPostalCode().trim() + "\n";
			if (addNew.getCountryOrRegion().trim() != null
					&& !addNew.getCountryOrRegion().trim().isEmpty())
				toBeSet += addNew.getCountryOrRegion().trim();
			textAreaItem.setValue(toBeSet);
		} else
			textAreaItem.setValue("");
		return super.onCloseClick();
	}

}
