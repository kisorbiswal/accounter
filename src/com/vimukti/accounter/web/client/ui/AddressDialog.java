package com.vimukti.accounter.web.client.ui;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author venki.p
 * 
 */
@SuppressWarnings("unchecked")
public class AddressDialog extends BaseDialog {

	public AddressDialog(String title, String description,
			final TextAreaItem textAreaItem, final String addressType,
			final LinkedHashMap<Integer, ClientAddress> allAddresses) {
		super(FinanceApplication.getCompanyMessages().address(), "");
		createControls(addressType, textAreaItem, allAddresses);
	}

	protected void createControls(final String addressType,
			final TextAreaItem textAreaItem,
			final LinkedHashMap<Integer, ClientAddress> allAddresses) {

		ClientAddress add = null;
		if (allAddresses != null) {
			add = allAddresses.get(UIUtils.getAddressType(addressType));
		}
		// final TextItem street = new TextItem(FinanceApplication
		// .getFinanceUIConstants().streetName());
		final TextItem address1 = new TextItem(FinanceApplication
				.getFinanceUIConstants().address1());

		final TextItem address2 = new TextItem(FinanceApplication
				.getFinanceUIConstants().address2());

		final TextItem city = new TextItem(FinanceApplication
				.getFinanceUIConstants().city());

		final TextItem state = new TextItem(FinanceApplication
				.getFinanceUIConstants().state());

		final TextItem country = new TextItem(FinanceApplication
				.getFinanceUIConstants().country());

		final TextItem zip = new TextItem(FinanceApplication
				.getFinanceUIConstants().postalCode());

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
		final ClientAddress addNew = add;
		addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {

				if (addNew != null) {
					String toBeSet = "";
					if (addNew.getAddress1() != null
							&& !addNew.getAddress1().isEmpty())
						toBeSet += addNew.getAddress1() + "\n";
					if (addNew.getStreet() != null
							&& !addNew.getStreet().isEmpty())
						toBeSet += addNew.getStreet() + "\n";
					if (addNew.getCity() != null && !addNew.getCity().isEmpty())
						toBeSet += addNew.getCity() + "\n";
					if (addNew.getStateOrProvinence() != null
							&& !addNew.getStateOrProvinence().isEmpty())
						toBeSet += addNew.getStateOrProvinence() + "\n";
					if (addNew.getZipOrPostalCode() != null
							&& !addNew.getZipOrPostalCode().isEmpty())
						toBeSet += addNew.getZipOrPostalCode() + "\n";
					if (addNew.getCountryOrRegion() != null
							&& !addNew.getCountryOrRegion().isEmpty())
						toBeSet += addNew.getCountryOrRegion();
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
				if (address1.getValue().toString() != null
						&& !address1.getValue().toString().isEmpty()) {
					isEmptyCounter++;
					toBeSet = address1.getValue().toString() + "\n";
					value.setAddress1(address1.getValue().toString());
				}
				if (address2.getValue().toString() != null
						&& !address2.getValue().toString().isEmpty()) {
					isEmptyCounter++;
					toBeSet += address2.getValue().toString() + "\n";
					value.setStreet(address2.getValue().toString());
				}

				if (city.getValue() != null
						&& !city.getValue().toString().isEmpty()) {
					isEmptyCounter++;
					toBeSet += city.getValue().toString() + "\n";
					value.setCity(city.getValue().toString());
				}

				if (state.getValue() != null
						&& !state.getValue().toString().isEmpty()) {
					isEmptyCounter++;
					toBeSet += state.getValue().toString() + "\n";
					value.setStateOrProvinence(state.getValue().toString());
				}

				if (zip.getValue() != null
						&& !zip.getValue().toString().isEmpty()) {
					isEmptyCounter++;
					toBeSet += zip.getValue().toString() + "\n";
					value.setZipOrPostalCode(zip.getValue().toString());
				}

				if (country.getValue() != null
						&& !country.getValue().toString().isEmpty()) {
					isEmptyCounter++;
					toBeSet += country.getValue().toString();
					value.setCountryOrRegion(country.getValue().toString());
				}

				if (toBeSet != null && !toBeSet.isEmpty()
						&& isEmptyCounter != 0) {
					textAreaItem.setValue(toBeSet);
					allAddresses
							.put(UIUtils.getAddressType(addressType), value);
					return true;
				} else
					Accounter.showError(AccounterErrorType.SHOULD_NOT_EMPTY);
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

}
