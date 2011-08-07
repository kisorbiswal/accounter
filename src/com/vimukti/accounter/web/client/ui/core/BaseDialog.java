package com.vimukti.accounter.web.client.ui.core;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.ValidationResult.Error;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.IDeleteCallback;
import com.vimukti.accounter.web.client.ui.ISaveCallback;
import com.vimukti.accounter.web.client.ui.WarningsDialog;
import com.vimukti.accounter.web.client.ui.WidgetWithErrors;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;

/**
 * Base Dialog is abstract class which provides common ground for all small
 * Windows or Dialogs.like header, body, footer with help, ok & cancel buttons
 * 
 * @author kumar kasimala
 * 
 */
public abstract class BaseDialog extends CustomDialog implements
		IAccounterWidget, WidgetWithErrors, ISaveCallback, IDeleteCallback {

	// private String title;
	protected HorizontalPanel headerLayout;
	private String description;
	protected HorizontalPanel bodyLayout;
	protected HorizontalPanel footerLayout;
	protected AccounterConstants constants;

	protected AccounterButton cancelBtn;
	protected AccounterButton okbtn;
	private InputDialogHandler dialogHandler;
	protected IAccounterGETServiceAsync rpcGetService;
	protected IAccounterCRUDServiceAsync rpcDoSerivce;
	protected IAccounterHomeViewServiceAsync rpcUtilService;
	protected ClientCompany company;
	protected VerticalPanel mainPanel, mainVLayPanel;
	public VerticalPanel errorPanel;
	private Map<Object, Widget> errorsMap = new HashMap<Object, Widget>();

	/**
	 * Creates new Instance
	 */
	public BaseDialog() {
	}

	public BaseDialog(String text) {
		setText(text);
	}

	public BaseDialog(String text, String desc) {

		// setText(getViewTitle());
		setText(text);
		setModal(true);
		this.description = desc;
		initCompany();
		initConstants();
		initRPCService();
		createControls();
		okbtn.setFocus(true);

		sinkEvents(Event.ONKEYPRESS);
		sinkEvents(Event.ONMOUSEOVER);
	}

	protected void initConstants() {
		try {
			this.constants = Accounter.constants();

		} catch (Exception e) {

			// SC.logWarn(e.getMessage());
			Accounter.showError(Accounter.constants()
					.failedToInitializeCompanyConstants());

		}
	}

	protected void initRPCService() {
		this.rpcGetService = Accounter.createGETService();
		this.rpcDoSerivce = Accounter.createCRUDService();
		this.rpcUtilService = Accounter.createHomeService();
	}

	protected void initCompany() {
		this.company = Accounter.getCompany();
	}

	private void createControls() {

		/**
		 * Header Layout
		 */
		headerLayout = new HorizontalPanel();
		headerLayout.setWidth("100%");
		if (description != null) {
			Label label = new Label();
			label.setText(description);
			headerLayout.add(label);
		}

		/**
		 * Body LayOut
		 */
		bodyLayout = new HorizontalPanel();
		bodyLayout.setWidth("100%");

		/**
		 * Footer Layout
		 */
		footerLayout = new HorizontalPanel();
		footerLayout.setSpacing(3);
		// footerLayout.addStyleName("dialogfooter");

		this.okbtn = new AccounterButton(constants.ok());
		okbtn.setWidth("80px");
		this.okbtn.setFocus(true);

		okbtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				processOK();
			}
		});
		okbtn.setFocus(true);

		cancelBtn = new AccounterButton(constants.cancel());
		cancelBtn.setWidth("80px");
		cancelBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				processCancel();
			}
		});

		footerLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		footerLayout.add(okbtn);
		footerLayout.add(cancelBtn);

		okbtn.enabledButton("ok-cancel-button");

		cancelBtn.enabledButton("ok-cancel-button");

		footerLayout.setCellHorizontalAlignment(okbtn,
				HasHorizontalAlignment.ALIGN_RIGHT);
		footerLayout.setCellHorizontalAlignment(cancelBtn,
				HasHorizontalAlignment.ALIGN_RIGHT);

		/**
		 * adding all Layouts to Window
		 */

		mainPanel = new VerticalPanel();
		errorPanel = new VerticalPanel();
		errorPanel.setVisible(false);
		errorPanel.addStyleName("dialog_commentPanel");
		mainPanel.setSize("100%", "100%");
		// mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		mainPanel.add(errorPanel);
		mainPanel.add(headerLayout);
		mainPanel.setCellVerticalAlignment(headerLayout,
				HasVerticalAlignment.ALIGN_TOP);

		//
		// mainPanel.setCellVerticalAlignment(bodyLayout,
		// HasVerticalAlignment.ALIGN_TOP);
		mainPanel.add(bodyLayout);
		mainPanel.add(footerLayout);
		mainPanel.setCellHorizontalAlignment(footerLayout,
				HasHorizontalAlignment.ALIGN_RIGHT);

		add(mainPanel);

	}

	/**
	 * add body to this Dialog
	 * 
	 * @param layout
	 */
	public void setBodyLayout(Panel layout) {
		this.bodyLayout.add(layout);
	}

	/**
	 * called when cancelButton clicks
	 */
	protected void processCancel() {
		onCancel();
		if (dialogHandler != null) {
			dialogHandler.onCancel();
		}
		removeFromParent();
	}

	protected void updateCompany() {

	}

	/**
	 * Called when Ok button clicked
	 */
	protected void processOK() {
		clearAllErrors();
		okbtn.setFocus(true);
		ValidationResult validationResult = validate();
		if (validationResult.haveErrors()) {
			for (Error error : validationResult.getErrors()) {
				HTML err = new HTML("<li>" + error.getMessage() + "</li>");
				errorPanel.add(err);
				errorsMap.put(error.getSource(), err);
			}
		} else if (validationResult.haveWarnings()) {

			new WarningsDialog(validationResult.getWarnings(),
					new ErrorDialogHandler() {

						@Override
						public boolean onYesClick() {
							onOK();
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
			onOK();
			if (dialogHandler != null) {
				dialogHandler.onOK();
			}
			this.removeFromParent();
		}

	}

	/**
	 * add InputDialog handler, methods in handler will call when particular
	 * event happen on this Dialog.default implementation does nothing
	 * 
	 * @param handler
	 */
	public void addInputDialogHandler(InputDialogHandler handler) {
		this.dialogHandler = handler;
	}

	public static BaseDialog newInstance() {
		return null;
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONKEYPRESS:
			int keycode = event.getKeyCode();
			if (KeyCodes.KEY_ESCAPE == keycode) {
				this.processCancel();
			}
			break;
		case Event.ONMOUSEOVER:
			// cancelBtn.setFocus(true);

		case Event.ONKEYDOWN:
			// cancelBtn.setFocus(true);
			break;
		default:
			break;
		}
		super.onBrowserEvent(event);
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

	public Object getGridColumnValue(IsSerializable obj, int index) {
		return null;
	}

	protected ClientCompany getCompany() {
		return company;
	}

	public void deleteFailed(Throwable caught) {

	}

	public void deleteSuccess(Boolean result) {

	}

	public void saveFailed(Throwable exception) {

	}

	public void saveSuccess(IAccounterCore object) {
	}

	// }

	protected ValidationResult validate() {
		return new ValidationResult();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	protected <D extends IAccounterCore> void saveOrUpdate(final D core) {
		Accounter.createOrUpdate(this, core);

	}

	protected abstract boolean onOK();

	protected void onCancel() {

	}

}
