/**
 * 
 */
package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ProvidesKey;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.ui.Accounter;
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
		initColumns();
	}

	/**
	 * The key provider that provides the unique ID of a contact.
	 */
	public static final ProvidesKey<ClientContact> KEY_PROVIDER = new ProvidesKey<ClientContact>() {
		public Object getKey(ClientContact item) {
			return item == null ? null : item.getID();
		}
	};

	private void initColumns() {

		this.addColumn(new CheckboxEditColumn<ClientContact>() {

			@Override
			protected void onChangeValue(boolean value, ClientContact obj) {
				onSelectionChanged(obj, value);
			}

			@Override
			protected void onHeaderValueChanged(boolean value) {
				// TODO Auto-generated method stub

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
				row.setBusinessPhone(value);
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
				row.setEmail(value);
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
}
