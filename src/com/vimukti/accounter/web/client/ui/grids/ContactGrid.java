package com.vimukti.accounter.web.client.ui.grids;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.CheckBox;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.CompanyMessages;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;

public class ContactGrid extends ListGrid<ClientContact> {
	CompanyMessages companyConstants;
	boolean isEditMode;

	public ContactGrid() {
		super(false);
	}

	public void initContacts(Set<ClientContact> contactsList) {
		ClientContact rec[] = new ClientContact[contactsList.size()];
		int i = 0;
		@SuppressWarnings("unused")
		ClientContact temp = null;
		for (ClientContact cont : contactsList) {

			rec[i] = new ClientContact();
			rec[i].setPrimary(cont.isPrimary());
			rec[i].setBusinessPhone(cont.getBusinessPhone());
			rec[i].setName(cont.getName());
			rec[i].setEmail(cont.getEmail());
			if (cont.isPrimary()) {
				temp = rec[i];
			}
			rec[i++].setTitle(cont.getTitle());
		}
		this.isEditMode = true;
		this.setRecords(Arrays.asList(rec));
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 50;
		case 5:
			if (UIUtils.isMSIEBrowser())
				return 25;
			else
				return 15;
		default:
			return -1;
		}
	}

	@Override
	protected int getColumnType(int col) {
		if (col == 0) {
			return ListGrid.COLUMN_TYPE_CHECK;
		}
		if (col == 5)
			return ListGrid.COLUMN_TYPE_IMAGE;

		return ListGrid.COLUMN_TYPE_TEXTBOX;
	}

	@Override
	protected Object getColumnValue(ClientContact contact, int col) {
		switch (col) {
		case 0:
			return contact.isPrimary();
		case 1:
			return contact.getName();
		case 2:
			return contact.getTitle();
		case 3:
			return contact.getBusinessPhone();
		case 4:
			return contact.getEmail();
		case 5:
			return Accounter.getFinanceMenuImages().delete();
			// return "/images/delete.png";
		}
		return "";
	}

	@Override
	protected String[] getColumns() {
		companyConstants = GWT.create(CompanyMessages.class);
		return new String[] { companyConstants.primary(),
				companyConstants.contactName(), companyConstants.title(),
				companyConstants.businessPhone(), companyConstants.email(), " " };
	}

	@Override
	protected String[] getSelectValues(ClientContact obj, int col) {
		return null;
	}

	@Override
	protected boolean isEditable(ClientContact obj, int row, int col) {
		return true;
	}

	@Override
	protected void onClick(ClientContact contact, int row, int col) {
		switch (col) {
		case 0:
			boolean isSelected = ((CheckBox) this.getWidget(row, col))
					.getValue();
			List<ClientContact> contacts = getRecords();
			if (isSelected) {
				enableOrDisableCheckBox(false);
				for (ClientContact clientContact : contacts) {
					if (!clientContact.equals(contact)) {
						clientContact.setPrimary(false);
					}
				}

			}
			contact.setPrimary(isSelected);

			updateData(contact);
			break;
		case 5:
			deleteRecord(contact);
			break;
		default:
			break;
		}
	}

	@Override
	public void onDoubleClick(ClientContact obj) {

	}

	@Override
	protected void onValueChange(ClientContact obj, int col, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int sort(ClientContact obj1, ClientContact obj2, int index) {
		switch (index) {

		default:
			break;
		}
		return 0;
	}

	@Override
	public boolean validateGrid() {
		for (ClientContact contact : this.getRecords()) {
			// if (isNullContact(contact)) {
			// Accounter.showError("Empty Conatcts can't be Saved");
			// return false;
			// }
		}
		return true;
	}

	private boolean isNullContact(ClientContact contact) {
		for (int colValue = 1; colValue <= 4; colValue++) {
			Object value = this.getColumnValue(contact, colValue);
			if (value != null && value != "") {
				return false;
			}
		}
		return true;

	}

	@Override
	public void editComplete(ClientContact contact, Object value, int col) {
		switch (col) {
		case 0:
			return;
		case 1:
			contact.setName(value.toString());
			break;
		case 2:
			contact.setTitle(value.toString());
			break;
		case 3:
			contact.setBusinessPhone(getvalidatePhoneValue(value.toString()));
			break;
		case 4:
			String email = value.toString();
			contact.setEmail(getValidMail(email));
			break;
		default:
			break;
		}
		updateRecord(contact, currentRow, col);
		super.editComplete(contact, value, col);
	}

	private String getValidMail(String email) {
		if (!UIUtils.isValidEmail(email)) {
			Accounter.showError(AccounterErrorType.INVALID_EMAIL);
			return "";
		} else
			return email;

	}

	private String getvalidatePhoneValue(String valueString) {
		try {
			Long phone = Long.parseLong(valueString);
			return String.valueOf(phone);
		} catch (Exception e) {
			if (valueString.length() != 0) {
				Accounter.showError("Invalid Business Phone Value");
				return "";
			}
		}
		return "";

	}

	@Override
	public void addData(ClientContact obj) {
		super.addData(obj);
		if (!isEditMode)
			if (this.getRecords().size() == 1) {
				((CheckBox) this.getWidget(0, 0)).setValue(true);
				obj.setPrimary(true);
			}

	}
}
