package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.IAccounterCRUDServiceAsync;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.IAccounterHomeViewServiceAsync;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.externalization.FinanceConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseDialog;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.company.CompanyMessages;
import com.vimukti.accounter.web.client.ui.vat.VATMessages;

/**
 * Base Dialog is abstract class which provides common ground for all small
 * Windows or Dialogs.like header, body, footer with help, ok & cancel buttons
 * 
 * @author kumar kasimala
 * 
 */
public abstract class BaseDialog<T> extends AbstractBaseDialog<T> {

	// private String title;
	protected HorizontalPanel headerLayout;
	private String description;
	protected HorizontalPanel bodyLayout;
	protected HorizontalPanel footerLayout;
	protected CompanyMessages companyMessges;
	protected FinanceConstants constants;
	protected VATMessages vatMessages;

	protected Button cancelBtn;
	protected Button okbtn;
	private InputDialogHandler dialogHandler;
	protected IAccounterGETServiceAsync rpcGetService;
	protected IAccounterCRUDServiceAsync rpcDoSerivce;
	protected IAccounterHomeViewServiceAsync rpcUtilService;
	protected ClientCompany company;
	protected VerticalPanel mainPanel;

	public BaseDialog(String title, String desc) {

		super(title);
		setText(title);
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

			vatMessages = GWT.create(VATMessages.class);
			this.companyMessges = (CompanyMessages) GWT
					.create(CompanyMessages.class);
			this.constants = (FinanceConstants) GWT
					.create(FinanceConstants.class);

		} catch (Exception e) {

			// SC.logWarn(e.getMessage());
			Accounter.showError(FinanceApplication.getFinanceUIConstants()
					.failedToInitializeCompanyConstants());

		}
	}

	protected void initRPCService() {
		this.rpcGetService = FinanceApplication.createGETService();
		this.rpcDoSerivce = FinanceApplication.createCRUDService();
		this.rpcUtilService = FinanceApplication.createHomeService();
	}

	protected void initCompany() {

		this.company = FinanceApplication.getCompany();

	}

	private void createControls() {

		/**
		 * Header Layout
		 */
		headerLayout = new HorizontalPanel();
		headerLayout.setWidth("100%");
		Label label = new Label();
		label.setText(description);
		headerLayout.add(label);

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

		okbtn = new Button(constants.ok());
		okbtn.setWidth("100px");
		this.okbtn.setFocus(true);

		okbtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				okClicked();
			}
		});
		okbtn.setFocus(true);

		cancelBtn = new Button(constants.cancel());
		cancelBtn.setWidth("100px");
		cancelBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				cancelClicked();
			}
		});

		footerLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		footerLayout.add(okbtn);
		footerLayout.add(cancelBtn);
		footerLayout.setCellHorizontalAlignment(okbtn,
				HasHorizontalAlignment.ALIGN_RIGHT);
		footerLayout.setCellHorizontalAlignment(cancelBtn,
				HasHorizontalAlignment.ALIGN_RIGHT);

		/**
		 * adding all Layouts to Window
		 */

		mainPanel = new VerticalPanel();
		mainPanel.setSize("100%", "100%");
		// mainPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
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
	protected void cancelClicked() {

		if (dialogHandler != null) {
			dialogHandler.onCancelClick();
			removeFromParent();
		}
	}

	protected void updateCompany() {

	}

	/**
	 * Called when Ok button clicked
	 */
	protected void okClicked() {
		okbtn.setFocus(true);
		if (dialogHandler != null)
			if (dialogHandler.onOkClick()) {
				removeFromParent();
				// setGlassEnabled(false);
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

	@SuppressWarnings("unchecked")
	public static BaseDialog newInstance() {
		return null;
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONKEYPRESS:
			int keycode = event.getKeyCode();
			if (KeyCodes.KEY_ESCAPE == keycode) {
				this.cancelClicked();
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

}
