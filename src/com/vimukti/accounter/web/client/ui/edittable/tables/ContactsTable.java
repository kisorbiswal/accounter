/**
 * 
 */
package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ProvidesKey;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ContactsTable extends EditTable<ClientContact> {

	public ContactsTable() {

	}

	/**
	 * The key provider that provides the unique ID of a contact.
	 */
	public static final ProvidesKey<ClientContact> KEY_PROVIDER = new ProvidesKey<ClientContact>() {
		public Object getKey(ClientContact item) {
			return item == null ? null : item.getID();
		}
	};

	protected void initColumns() {

		this.addColumn(new CheckboxEditColumn<ClientContact>() {

			@Override
			protected void onChangeValue(boolean value, ClientContact obj) {
				onSelectionChanged(obj, value);
			}

			@Override
			public IsWidget getHeader() {
				Label columnHeader = new Label(Accounter.constants().primary());
				return columnHeader;
			}
		});

		this.addColumn(new TextEditColumn<ClientContact>() {

			@Override
			protected void setValue(ClientContact row, String value) {
				row.setName(value);
			}

			@Override
			protected String getValue(ClientContact row) {
				return row.getName();
			}

			@Override
			public int getWidth() {
				return 160;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().contactName();
			}
		});

		this.addColumn(new TextEditColumn<ClientContact>() {

			@Override
			protected void setValue(ClientContact row, String value) {
				row.setTitle(value);
			}

			@Override
			protected String getValue(ClientContact row) {
				return row.getTitle();
			}

			@Override
			public int getWidth() {
				return 160;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().title();
			}
		});

		this.addColumn(new TextEditColumn<ClientContact>() {

			@Override
			protected void setValue(ClientContact row, String value) {
				if (UIUtils.isValidPhone(value))
					row.setBusinessPhone(value);
				else {
					Accounter.showError(Accounter.constants()
							.invalidBusinessPhoneVal());
					row.setBusinessPhone("");
				}
			}

			@Override
			protected String getValue(ClientContact row) {
				return row.getBusinessPhone();
			}

			@Override
			public int getWidth() {
				return 160;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().businessPhone();
			}
		});

		this.addColumn(new TextEditColumn<ClientContact>() {

			@Override
			protected void setValue(ClientContact row, String value) {
				if (UIUtils.isValidEmail(value))
					row.setEmail(value);
				else {
					Accounter.showError(Accounter.constants().invalidEmail());
					row.setEmail("");
				}
			}

			@Override
			protected String getValue(ClientContact row) {
				return row.getEmail();
			}

			@Override
			public int getWidth() {
				return 160;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().email();
			}
		});

		this.addColumn(new DeleteColumn<ClientContact>());
	}

	private void onSelectionChanged(ClientContact obj, boolean isChecked) {

		List<ClientContact> records = getSelectedRecords(0);
		for (ClientContact contact : records) {
			int index = indexOf(contact);
			checkColumn(index, 0, false);
			contact.setPrimary(false);
		}

		int row = indexOf(obj);
		if (isChecked) {
			update(obj);
			obj.setPrimary(true);
		}
		super.checkColumn(row, 0, isChecked);
	}

	public int indexOf(ClientContact selectedObject) {
		return getAllRows().indexOf(selectedObject);
	}

	public List<ClientContact> getRecords() {
		return getAllRows();
	}

	public void validate(ValidationResult result) {
		for (int i = 0; i < getAllRows().size(); i++) {
			for (int j = 0; j < getAllRows().size(); j++) {
				if (i != j) {
					if (getAllRows().get(i).getTitle().equals(
							getAllRows().get(i).getTitle())
							&& getAllRows().get(i).getEmail().equals(
									getAllRows().get(i).getEmail())
							&& getAllRows().get(i).getDisplayName().equals(
									getAllRows().get(i).getDisplayName())
							&& getAllRows().get(i).getBusinessPhone().equals(
									getAllRows().get(i).getBusinessPhone()))
						result.addError(this, Accounter.constants()
								.youHaveEnteredduplicateContacts());
				}
			}
		}
	}
}
