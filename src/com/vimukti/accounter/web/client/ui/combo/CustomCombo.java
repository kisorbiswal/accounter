package com.vimukti.accounter.web.client.ui.combo;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;

/*
 This Combo can used for All Custom Combo SelectItems, like CustomerCombo,
 * Vendor,,,,etc.,
 * 
 * @author Malcom Fernandez
 */
public abstract class CustomCombo<T> extends DropDownCombo<T> {

	public CustomCombo(String title, boolean isAddNewRequire, int noOfCols) {
		super(title, isAddNewRequire, noOfCols);
		if (title != null) {
			int i = title.length();
			if (i > 5)
				if (title.substring(i - 1).equalsIgnoreCase("s"))
					title = title.replace("s", "");
				else if (title.substring(i - 4, i).equalsIgnoreCase(
						Accounter.constants().name()))
					title = title.replace(Accounter.constants().name(), "")
							.toLowerCase();
			if (isAddNewRequire)
				super.setToolTip(Accounter.messages()
						.selectWhichWeHaveInOurCompanyOrAddNew(title));
			else
				super.setToolTip(Accounter.messages()
						.selectWhichWeHaveInOurCompany(title));
		}
	}

	public CustomCombo(String title) {
		super(title, true, 1);
		if (title != null) {
			int i = title.length();
			if (i > 5)
				if (title.substring(i - 1).equalsIgnoreCase("s"))
					title = title.replace("s", "");
				else if (title.substring(i - 4, i).equalsIgnoreCase(
						Accounter.constants().name()))
					title = title.replace(Accounter.constants().name(), "")
							.toLowerCase();
			super.setToolTip(Accounter.messages()
					.selectWhichWeHaveInOurCompanyOrAddNew(title));
		}
	}

	/*
	 * Override this method to do anything before the other overridden methods
	 * are called from constructor.
	 */
	protected void init() {
		super.init();
	}

	public List<T> getComboItems() {
		return comboItems;
	}

	protected AccounterAsyncCallback<T> createAddNewCallBack() {

		AccounterAsyncCallback<T> callBack = new AccounterAsyncCallback<T>() {

			public void onException(AccounterException caught) {

				if (!GWT.isScript()) {
					caught.printStackTrace();
					Accounter.showError(Accounter.constants()
							.sorryFailedToAdd());
				}

			}

			public void onResultSuccess(T result) {
				boolean usTaxCode = Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US
						&& result instanceof ClientTAXItemGroup;
				if (usTaxCode)
					result = (T) Accounter.getCompany().getTAXCode(
							((ClientTAXItemGroup) result).getID());
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
			this.selectedObject = comboItems.get(index);
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

	protected ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	@Override
	protected int getObjectIndex(T coreObject) {
		try {
			return comboItems.indexOf(Utility.getObject(
					(List<IAccounterCore>) comboItems,
					((IAccounterCore) coreObject).getID()));
		} catch (ClassCastException e) {
			// the combo items are not belongs to IAccounterCore type.
			return comboItems.indexOf(coreObject);
		}
	}

//	@Override
//	public void updateComboItem(T coreObject) {
//		for (T item : comboItems) {
//			if (((IAccounterCore) item).getID() == ((IAccounterCore) coreObject)
//					.getID()) {
//
//				if (this.getSelectedValue() != null ? this.getSelectedValue()
//						.equals(item) : true) {
//					removeComboItem(item);
//					addItemThenfireEvent(coreObject);
//				} else {
//					removeComboItem(item);
//					addComboItem(coreObject);
//				}
//				break;
//			} else if (((IAccounterCore) coreObject).getID() != 0) {
//				addComboItem(coreObject);
//				break;
//			}
//
//			// if((IAccounterCore) item.getSt)
//		}
//
//	}
}
