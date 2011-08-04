/**
 * 
 */
package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.DialogBox;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.vimukti.accounter.web.client.IAccounterReportServiceAsync;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectItemType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CustomButton;
import com.vimukti.accounter.web.client.ui.core.IAccounterWidget;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

/**
 * This Class serves as the Base Root Class for all the views, in Accounter GUI,
 * providing common Functionality (This includes CustomerView (Customer Creation
 * and Editing), VendorView , Account View, ListViews, etc., and all Transaction
 * Related View Classes
 * 
 * @author Fernandez
 */
@SuppressWarnings("serial")
public abstract class AbstractBaseView<T extends IAccounterCore> extends
		ParentCanvas<T> implements IAccounterWidget {

	protected boolean errorOccured = false;
	protected boolean warnOccured = false;

	public AbstractBaseView() {
		sinkEvents(Event.ONCHANGE | Event.KEYEVENTS);

		initRPCService();
		this.addStyleName("abstract_base_view");
	}

	protected abstract String getViewTitle();

	private Map<SelectItemType, List<CustomCombo>> comboMap = new HashMap<SelectItemType, List<CustomCombo>>();

	/**
	 * To Maintain a list of all Accounts, for Every View, (Optional)
	 */
	// protected List<Account> accounts;
	/**
	 * Optional to Deleted in Future
	 */
	// XXX if required
	// protected FinanceGrid<T> view;
	/**
	 * Base reference for all RPC GET Services
	 */
	protected IAccounterGETServiceAsync rpcGetService;

	@Override
	public void init() {
		// OverRidden in Sub-Classes

	}

	public void setPrevoiusOutput(Object preObject) {

		Action action = getAction();

		if (action != null && action.getActionSource() != null) {
			FormItem formItm = action.getActionSource();
			if (formItm instanceof CustomCombo) {
				CustomCombo combo = (CustomCombo) action.getActionSource();
				combo.addItemThenfireEvent(preObject);
			}

		}

	}

	/**
	 * Base reference for all RPC DO Services
	 */
	protected IAccounterCRUDServiceAsync rpcDoSerivce;

	/**
	 * Base reference for all RPC Util Service
	 */
	protected IAccounterHomeViewServiceAsync rpcUtilService;

	/**
	 * Member Flag Variable to determine, whether to Save And Close or Save and
	 * New
	 */
	protected boolean saveAndClose;

	protected List<DynamicForm> forms = new ArrayList<DynamicForm>();

	protected List<FormItem> formItems = new ArrayList<FormItem>() {

		@Override
		public boolean add(FormItem e) {
			if (super.add(e)) {
				if (e instanceof CustomCombo) {
					addComboItemToMap((CustomCombo) e);
				}
				return true;
			} else
				return false;
		};
	};

	public boolean yesClicked;

	/**
	 * CallBack Functionality to Support the Add New Feature of Custom Combo
	 * Boxes Like say, Post, creating a Customer, the Customer object should be
	 * added back to the Combo list.
	 */
	public AccounterAsyncCallback<Object> callback;

	protected IAccounterReportServiceAsync rpcReportService;

	public boolean isRegister;

	private DialogBox dialog;

	private boolean isViewModfied;
	protected boolean isEdit;

	/**
	 * Convenience Method to Set CallBack
	 * 
	 * @param callBack
	 */
	public final void setCallBack(AccounterAsyncCallback<Object> callBack) {

		this.callback = callBack;

	}

	protected void initRPCService() {
		this.rpcGetService = Accounter.createGETService();
		this.rpcDoSerivce = Accounter.createCRUDService();
		this.rpcUtilService = Accounter.createHomeService();
		this.rpcReportService = Accounter.createReportService();

	}

	/**
	 * Called when any Saving any Transactional / Non-Transactional View has
	 * Failed to save.
	 */
	public void saveFailed(Throwable exception) {

		if (dialog != null) {
			dialog.removeFromParent();
		}

		// Accounter.showError(exception.getMessage());

		// SC.logWarn(exception.getMessage());
		/*
		 * if (saveAndCloseButton != null) {
		 * saveAndCloseButton.setDisabled(false); }
		 * 
		 * if (saveAndNewButton != null) { saveAndNewButton.setDisabled(false);
		 * }
		 */

		// saveAndCloseButton.getParentElement().enable();
	}

	/**
	 * Called when Any Non-Transaction View is Success
	 */
	@Override
	public void saveSuccess(IAccounterCore object) {
		try {
			if (this.callback != null) {
				this.callback.onSuccess(object);
			}
			if (saveAndClose) {

				MainFinanceWindow.getViewManager().closeView(this.getAction(),
						object);
			} else {
				if (!History.getToken().equals(getAction().getHistoryToken())) {

				}
				getAction().run(null, true);

			}
		} catch (Exception e) {
			Accounter.showInformation(((JavaScriptException) e)
					.getDescription());
		}

	}

	public boolean validate() throws Exception {
		// TO BE OVERRIDEN
		return true;
	}

	public void saveAndUpdateView() throws Exception {
		// TO BE OVERRIDDEN
	}

	protected void updateCompany(IAccounterCore obj) {

		getCompany().processCommand(obj);

	}

	protected void refreshData() {
		// TODDO Refresh the View Data
	}

	public void initData() {

	}

	public void setData(T data) {
		super.setData(data);

		this.isEdit = (data != null && data.getID() != 0);

	}

	public void disableSaveButtons() {
		//
		// if (this.saveAndCloseButton != null) {
		//
		// this.saveAndCloseButton.setDisabled(true);
		// }
		//
		// if (this.saveAndNewButton != null) {
		//
		// this.saveAndNewButton.setDisabled(true);
		// }

	}

	public void enableSaveButtons() {

		// if (this.saveAndCloseButton != null) {
		//
		// this.saveAndCloseButton.setDisabled(false);
		// }
		//
		// if (this.saveAndNewButton != null) {
		//
		// this.saveAndNewButton.setDisabled(false);
		// }

	}


	public List<CustomCombo> getComboItems() {

		ArrayList<CustomCombo> items = new ArrayList<CustomCombo>();

		for (FormItem formItem : formItems) {

			if (formItem instanceof CustomCombo) {
				items.add((CustomCombo) formItem);
			}
		}

		return items;

	}

	private void addComboItemToMap(CustomCombo comboItem) {
		// FIXED --replaced comboItem.getComboType() with
		// comboItem.getSelectItemType()
		if (comboItem == null)
			return;

		List<CustomCombo> comboList = comboMap.get(comboItem
				.getSelectItemType());

		if (comboList == null) {
			comboList = new ArrayList<CustomCombo>();
		}

		comboList.add(comboItem);

		comboMap.put(comboItem.getSelectItemType(), comboList);

	}

	public List<CustomCombo> getComboList(SelectItemType type) {

		return comboMap.get(type);

	}

	protected <P extends IAccounterCore> void createObject(final P core) {
		ViewManager.getInstance().createObject(core, this);
	}

	protected <P extends IAccounterCore> void alterObject(final P core) {
		ViewManager.getInstance().alterObject(core, this);
	}

	@Override
	public void setFocus() {

	}

	//
	// @Override
	// public void saveSuccess(IAccounterCore object) {
	// saveSuccess((T) object);
	// }
	@Override
	public String toString() {
		return Accounter.constants().actionClassNameis()
				+ this.getAction().getText();
	}

	@Override
	public void onBrowserEvent(Event event) {
		Element element = DOM.eventGetTarget(event);

		switch (DOM.eventGetType(event)) {
		case Event.ONKEYPRESS:
			if (Arrays.asList("INPUT", "TEXTAREA").contains(
					element.getTagName()))
				isViewModfied = true;

			break;
		case Event.ONCHANGE:
			if (Arrays.asList("SELECT", "RADIO", "CHECKBOX", "INPUT",
					"TEXTAREA").contains(element.getTagName())) {
				isViewModfied = true;
			}
			if (Arrays.asList("SELECT", "RADIO", "CHECKBOX").contains(
					element.getTagName()))
				isViewModfied = true;

			break;
		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	public boolean isViewModfied() {
		return isViewModfied;
	}

	public void setViewModfied(boolean isViewModified) {
		this.isViewModfied = isViewModified;
	}

	public ClientCompany getCompany() {
		return Accounter.getCompany();

	}

}
