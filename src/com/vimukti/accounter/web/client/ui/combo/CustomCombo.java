package com.vimukti.accounter.web.client.ui.combo;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Accounter;

/*
 This Combo can used for All Custom Combo SelectItems, like CustomerCombo,
 * Vendor,,,,etc.,
 * 
 * @author Malcom Fernandez
 */
public abstract class CustomCombo<T> extends DropDownCombo<T> {

	public CustomCombo(String title, boolean isAddNewRequire, int noOfCols) {
		super(title, isAddNewRequire, noOfCols);
	}

	public CustomCombo(String title) {
		super(title, true, 1);
	}

	/*
	 * Override this method to do anything before the other overridden methods
	 * are called from constructor.
	 */
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
	}

	public abstract SelectItemType getSelectItemType();

	public List<T> getComboItems() {
		return comboItems;
	}

	protected AsyncCallback<T> createAddNewCallBack() {

		AsyncCallback<T> callBack = new AsyncCallback<T>() {

			public void onFailure(Throwable caught) {

				if (!GWT.isScript()) {
					caught.printStackTrace();
					Accounter.showError(Accounter
							.getAccounterComboConstants().sorryFailedToAdd());
				}

			}

			public void onSuccess(T result) {
				boolean usTaxCode = getCompany()
						.getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US
						&& result instanceof ClientTAXItemGroup;
				if (usTaxCode)
					result = (T) getCompany()
							.getTAXCodeForTAXItemGroup(
									(ClientTAXItemGroup) result);
				if (result != null) {
					if (!usTaxCode)
						setComboItem(result);
					if (handler != null) {
						handler.selectedComboBoxItem(result);
					}
				} else {
					onFailure(null);
				}

			}
		};

		return callBack;
	}

	// public void addChangeHandler() {
	// this.addChangeHandler(new ChangeHandler() {
	// @Override
	// public void onChange(ChangeEvent event) {
	//
	// int index = listbox.getSelectedIndex();
	//
	// IAccounterComboSelectionChangeHandler<T> handler = getHandler();
	//
	// switch (index) {
	//
	// case 1:
	// if (isAddNewRequire)
	// onAddNew();
	// else if (handler != null) {
	// handler.selectedComboBoxItem(comboItems.get(index - 1));
	// }
	//
	// break;
	//
	// case 0:
	//
	// if (handler != null) {
	// handler.selectedComboBoxItem(null);
	// }
	//
	// break;
	//
	// default:
	//
	// if (handler != null) {
	// try {
	// handler.selectedComboBoxItem(comboItems.get(index
	// - (isAddNewRequire ? 2 : 1)));
	// } catch (Exception e) {
	//
	// }
	// }
	//
	// if (grid != null) {
	// grid.remove(listbox);
	// setSelectedItem(0);
	// }
	// break;
	// }
	//
	// }
	// });
	// }
	// public void addChangeHandler() {
	// this.addChangeHandler(new ChangeHandler() {
	// @Override
	// public void onChange(ChangeEvent event) {
	//
	// int index = listbox.getSelectedIndex();
	//
	// IAccounterComboSelectionChangeHandler handler = getHandler();
	//
	// switch (index) {
	//
	// case 1:
	// if (isAddNewRequire)
	// onAddNew();
	// else if (handler != null) {
	// handler.selectedComboBoxItem(comboItems.get(index - 1));
	// }
	//
	// break;
	//
	// case 0:
	//
	// if (handler != null) {
	// handler.selectedComboBoxItem(null);
	// }
	//
	// break;
	//
	// default:
	//
	// if (handler != null) {
	// try {
	// handler.selectedComboBoxItem(comboItems.get(index
	// - (isAddNewRequire ? 2 : 1)));
	// } catch (Exception e) {
	//
	// }
	// }
	//
	// if (grid != null) {
	// grid.remove(listbox);
	// setSelectedItem(0);
	// }
	// break;
	// }
	//
	// }
	// });
	// }

	protected abstract String getDisplayName(T object);

	public interface Filter {
		boolean canAdd(IAccounterCore core);
	}

	public void setSelectedItem(int index) {
		if (index >= 0) {
			setSelectedItem(this.selectedObject, index);
		}
	}

	public void setSelected(String itemName) {
		List<T> combo = comboItems;
		for (T item : combo) {
			if (getDisplayName(item).equals(itemName)) {
				this.selectedObject = item;
				this.setSelectedItem(comboItems.indexOf(item));
			}
		}

	}
}
