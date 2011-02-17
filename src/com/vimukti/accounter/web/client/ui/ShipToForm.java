package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.SelectItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;

/**
 * 
 * @author Venki.p
 * 
 */

public class ShipToForm extends DynamicForm {
	LinkedHashMap<Integer, ClientAddress> allAddresses;
	ClientAddress toBeShown = null;
	public SelectItem businessSelect;
	public TextAreaItem addrArea;

	public ShipToForm(Set<ClientAddress> addresses) {

		@SuppressWarnings("unused")
		Label l1 = new Label(FinanceApplication.getFinanceUIConstants()
				.enterAddress());
		allAddresses = new LinkedHashMap<Integer, ClientAddress>();

		setAddresses(addresses);

		businessSelect = new SelectItem(FinanceApplication
				.getFinanceUIConstants().shipTo());
		businessSelect.setWidth(85);
		businessSelect.getMainWidget().removeStyleName(
				FinanceApplication.getFinanceUIConstants().gwtListBox());
		LinkedHashMap<String, String> addressTypes = new LinkedHashMap<String, String>();

		addressTypes.putAll(new ClientAddress().getAddressTypes());
		addressTypes.remove(ClientAddress.TYPE_BILL_TO + "");
		businessSelect.setValueMap(addressTypes);

		addrArea = new TextAreaItem();
		addrArea.setWidth(100);
		addrArea.setShowTitle(false);
		addrArea.setDisabled(true);

		if (toBeShown != null) {
			businessSelect.setValue(toBeShown.getAddressTypes().get(
					toBeShown.getType() + ""));

			String toToSet = new String();
			if (toBeShown.getAddress1() != null) {
				toToSet = toBeShown.getAddress1().toString() + "\n";
			}

			if (toBeShown.getStreet() != null) {
				toToSet = toBeShown.getStreet().toString() + "\n";
			}

			if (toBeShown.getCity() != null) {
				toToSet += toBeShown.getCity().toString() + "\n";
			}

			if (toBeShown.getStateOrProvinence() != null) {
				toToSet += toBeShown.getStateOrProvinence() + "\n";
			}
			if (toBeShown.getZipOrPostalCode() != null) {
				toToSet += toBeShown.getZipOrPostalCode() + "\n";
			}
			if (toBeShown.getCountryOrRegion() != null) {
				toToSet += toBeShown.getCountryOrRegion();
			}
			addrArea.setValue(toToSet);
		} else
			businessSelect.setDefaultToFirstOption(Boolean.TRUE);
		setGroupTitle(FinanceApplication.getFinanceUIConstants().addresses());
		setNumCols(3);
		setFields(businessSelect, addrArea);
	}

	@SuppressWarnings("unchecked")
	private void setAddresses(Set<ClientAddress> addresses) {
		if (addresses != null) {
			Iterator it = addresses.iterator();
			while (it.hasNext()) {
				ClientAddress add = (ClientAddress) it.next();
				if (add.getIsSelected()) {
					toBeShown = add;
				}
				allAddresses.put(add.getType(), add);
				// System.out.println("Existing Address  Type " + add.getType()
				// + " Street is " + add.getStreet() + " Is Selected"
				// + add.getIsSelected());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Set<ClientAddress> getAddresss() {
		ClientAddress selectedAddress = allAddresses.get(UIUtils
				.getAddressType(businessSelect.getValue().toString()));
		if (selectedAddress != null) {
			selectedAddress.setIsSelected(true);
			allAddresses.put(UIUtils.getAddressType((String) businessSelect
					.getValue()), selectedAddress);
		}
		Collection add = allAddresses.values();
		Set<ClientAddress> toBeSet = new HashSet<ClientAddress>();
		Iterator it = add.iterator();
		while (it.hasNext()) {
			ClientAddress a = (ClientAddress) it.next();
			toBeSet.add(a);
			// System.out.println("Sending Address  Type " + a.getType()
			// + " Street is " + a.getStreet() + " Is Selected"
			// + a.getIsSelected());
		}
		return toBeSet;
	}

	@SuppressWarnings("unchecked")
	public List<ClientAddress> getAddresssList() {
		ClientAddress selectedAddress = allAddresses.get(businessSelect
				.getValue());
		if (selectedAddress != null) {
			selectedAddress.setIsSelected(true);
			allAddresses.put(UIUtils.getAddressType((String) businessSelect
					.getValue()), selectedAddress);
		}
		Collection add = allAddresses.values();
		List<ClientAddress> toBeSet = new ArrayList<ClientAddress>();
		Iterator it = add.iterator();
		while (it.hasNext()) {
			ClientAddress a = (ClientAddress) it.next();
			toBeSet.add(a);
			// System.out.println("Sending Address  Type " + a.getType()
			// + " Street is " + a.getStreet() + " Is Selected"
			// + a.getIsSelected());
		}
		return toBeSet;
	}

	public void setSelectValueMap(LinkedHashMap<String, String> linkedHashMap) {
		this.businessSelect.setValueMap(linkedHashMap);
	}

	public void setAddress(List<ClientAddress> addresses) {
		if (!addresses.isEmpty()) {
			for (ClientAddress address : addresses) {
				this.allAddresses.put(address.getType(), address);
				if (address.getType() == ClientAddress.TYPE_SHIP_TO) {
					
					addrArea.setValue(getValidAddress(address));

					businessSelect.setValue(address.getAddressTypes().get(
							address.getType() + ""));
				}

			}
		} else {
			addrArea.setValue("");
			this.allAddresses.clear();
		}
	}

	public ClientAddress getAddress() {

		ClientAddress selectedAddress = allAddresses.get(UIUtils
				.getAddressType(businessSelect.getValue().toString()));
		return selectedAddress;
	}

	public void setAddres(ClientAddress address) {

		addrArea.setValue(getValidAddress(address));
	}

	public void setListOfCustomerAdress(List<ClientAddress> addresses) {
		if (!addresses.isEmpty()) {
			for (ClientAddress address : addresses) {
				this.allAddresses.put(address.getType(), address);
			}
		}
	}

	public String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		return toToSet;
	}
}
