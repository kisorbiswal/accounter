/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.grids.columns.ColumnWithFieldUpdater;
import com.vimukti.accounter.web.client.ui.grids.columns.CustomCellTable;
import com.vimukti.accounter.web.client.ui.grids.columns.ImageActionColumn;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ContactsTable extends CustomCellTable<ClientContact> {

	/**
	 * The key provider that provides the unique ID of a contact.
	 */
	public static final ProvidesKey<ClientContact> KEY_PROVIDER = new ProvidesKey<ClientContact>() {
		public Object getKey(ClientContact item) {
			return item == null ? null : item.getID();
		}
	};

	public void init() {
		// Add a selection model so we can select cells.
		final SelectionModel<ClientContact> selectionModel = new MultiSelectionModel<ClientContact>(
				KEY_PROVIDER);
		this.setSelectionModel(selectionModel, DefaultSelectionEventManager
				.<ClientContact> createCheckboxManager());

		// Initiates the Columns
		initColumns();

	}

	private void initColumns() {

		// Initiating Columns
		Column<ClientContact, Boolean> primaryCheckBox = new ColumnWithFieldUpdater<ClientContact, Boolean>(
				new CheckboxCell(true, false)) {

			@Override
			public Boolean getValue(ClientContact object) {
				return object.isPrimary();
			}

			@Override
			public void update(int index, ClientContact object, Boolean value) {
				object.setPrimary(value);
			}
		};
		this.addColumn(primaryCheckBox, Accounter.constants().primary());

		TextColumn<ClientContact> contactnName = new TextColumn<ClientContact>() {

			@Override
			public String getValue(ClientContact object) {
				return object.getName();
			}
		};
		this.addColumn(contactnName, Accounter.constants().contactName());

		TextColumn<ClientContact> title = new TextColumn<ClientContact>() {

			@Override
			public String getValue(ClientContact object) {
				return object.getTitle();
			}
		};
		this.addColumn(title, Accounter.constants().title());

		TextColumn<ClientContact> businessPhone = new TextColumn<ClientContact>() {

			@Override
			public String getValue(ClientContact object) {
				return object.getBusinessPhone();
			}
		};
		this.addColumn(businessPhone, Accounter.constants().title());

		TextColumn<ClientContact> email = new TextColumn<ClientContact>() {

			@Override
			public String getValue(ClientContact object) {
				return object.getEmail();
			}
		};
		this.addColumn(email, Accounter.constants().email());

		ImageActionColumn<ClientContact> delete = new ImageActionColumn<ClientContact>() {

			@Override
			public ImageResource getValue(ClientContact object) {
				return Accounter.getFinanceMenuImages().delete();
			}

			@Override
			protected void onSelect(int index, ClientContact object) {
				getDataProvider().getList().remove(object);
			}
		};
		this.addColumn(delete, "");
	}

}
