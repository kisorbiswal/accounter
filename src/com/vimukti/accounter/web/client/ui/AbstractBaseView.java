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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.vimukti.accounter.web.client.IAccounterReportServiceAsync;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.ValidationResult.Error;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.core.IAccounterWidget;
import com.vimukti.accounter.web.client.ui.core.IEditableView;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

/**
 * This Class serves as the Base Root Class for all the views, in Accounter GUI,
 * providing common Functionality (This includes CustomerView (Customer Creation
 * and Editing), VendorView , Account View, ListViews, etc., and all Transaction
 * Related View Classes
 * 
 */
/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
@SuppressWarnings("serial")
public abstract class AbstractBaseView<T> extends ParentCanvas<T> implements
		IAccounterWidget, WidgetWithErrors,IEditableView {

	@Override
	public boolean canEdit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}

	public AbstractBaseView() {

		sinkEvents(Event.ONCHANGE | Event.KEYEVENTS);

		initRPCService();
		this.addStyleName("abstract_base_view");
		this.errorPanel = new VerticalPanel();
		this.errorPanel.addStyleName("errors");
	}

	protected abstract String getViewTitle();

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

	protected List<FormItem> formItems = new ArrayList<FormItem>();

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
	private VerticalPanel errorPanel;
	private Map<Object, Widget> errorsMap = new HashMap<Object, Widget>();
	private boolean isDirty;

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
				getManager().closeCurrentView();
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

	public ValidationResult validate() {
		// TO BE OVERRIDEN
		return new ValidationResult();
	}

	public void saveAndUpdateView() {
		// TO BE OVERRIDDEN
	}

	protected void refreshData() {
		// TODDO Refresh the View Data
	}

	public void setData(T data) {
		super.setData(data);
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

	protected <P extends IAccounterCore> void saveOrUpdate(final P core) {

		final AccounterAsyncCallback<Long> transactionCallBack = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {
				saveFailed(caught);
				caught.printStackTrace();
				// TODO handle other kind of errors
			}

			public void onSuccess(Long result) {
				core.setID(result);
				Accounter.getCompany().processUpdateOrCreateObject(core);
				saveSuccess(core);
			}

		};
		if (core.getID() == 0) {
			Accounter.createCRUDService().create((IAccounterCore) core,
					transactionCallBack);
		} else {
			Accounter.createCRUDService().update((IAccounterCore) core,
					transactionCallBack);
		}

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

	/**
	 * Adds Error
	 * 
	 * @param item
	 * @param erroMsg
	 */
	public void addError(Object item, String erroMsg) {
		HTML error = new HTML("<li>" + erroMsg + "</li>");
		this.errorPanel.add(error);
		this.errorPanel.setVisible(true);
		this.errorsMap.put(item, error);
	}

	/**
	 * Clears All Errors
	 */
	public void clearAllErrors() {
		this.errorsMap.clear();
		this.errorPanel.clear();
		this.errorPanel.setVisible(false);
	}

	/**
	 * Clears the given Error
	 * 
	 * @param obj
	 */
	public void clearError(Object obj) {
		Widget remove = this.errorsMap.remove(obj);
		if (remove != null) {
			this.errorPanel.remove(remove);
			if (this.errorsMap.isEmpty()) {
				errorPanel.setVisible(false);
			}
		}
	}

	public void setCloseOnSave(boolean closeOnSave) {
		this.saveAndClose = true;
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	/**
	 * @param b
	 */
	public void onSave(boolean reopen) {
		clearAllErrors();
		ValidationResult validationResult = this.validate();
		if (validationResult.haveErrors()) {
			for (Error error : validationResult.getErrors()) {
				HTML err = new HTML("<li>" + error.getMessage() + "</li>");
				errorPanel.add(err);
			}
		} else if (validationResult.haveWarnings()) {

			new WarningsDialog(validationResult.getWarnings(),
					new ErrorDialogHandler() {

						@Override
						public boolean onYesClick() {
							saveAndUpdateView();
							return true;
						}

						@Override
						public boolean onNoClick() {
							return true;
						}

						@Override
						public boolean onCancelClick() {
							return true;
						}
					});
		} else {
			this.saveAndClose = !reopen;
			saveAndUpdateView();
		}
	}

	/**
	 * Closes the View
	 */
	public void onClose() {
		if (isDirty) {
			Accounter.showWarning(AccounterWarningType.saveOrClose,
					AccounterType.WARNINGWITHCANCEL, new ErrorDialogHandler() {

						@Override
						public boolean onCancelClick() {
							return true;
						}

						@Override
						public boolean onNoClick() {
							close();
							return true;
						}

						@Override
						public boolean onYesClick() {
							onSave(false);
							return true;
						}
					});
		} else {
			close();
		}
	}

}
