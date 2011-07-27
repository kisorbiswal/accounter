package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;

@SuppressWarnings("unchecked")
public class ManageVATView extends AbstractBaseView {

	AccounterButton vatButton;

	public ManageVATView() {

	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void saveAndUpdateView() throws Exception {
		super.saveAndUpdateView();
	}

	@Override
	public boolean validate() throws Exception {
		return super.validate();
	}

	private void createControls() {

		Label stLabel = new Label(Accounter.getVATMessages()
				.thisWndHelpYoyManageVATTasks());
		Label vatLabel = new Label(Accounter.getVATMessages()
				.fileVATReturnNow());

		CaptionPanel capAssignPanel = new CaptionPanel(Accounter
				.getVATMessages().getStarted());
		CaptionPanel capPayVatPanel = new CaptionPanel(Accounter
				.getVATMessages().payVATOwing());
		CaptionPanel capTaskPanel = new CaptionPanel(Accounter.getVATMessages()
				.relatedTask());

		vatButton = new AccounterButton(Accounter.getVATMessages()
				.fileVATReturn());
		AccounterButton closeButton = new AccounterButton(Accounter
				.getVATMessages().close());
		AccounterButton helpButton = new AccounterButton(Accounter
				.getVATMessages().help());

		final Image icon = new Image();
		icon.setUrl("/images/justifyleft.gif");

		Hyperlink assignlink = new Hyperlink(Accounter.getVATMessages()
				.assignVatCodesToItems(), "foo");
		Hyperlink viewLink = new Hyperlink(Accounter.getVATMessages()
				.viewVATItems(), "link1");
		Hyperlink openLink = new Hyperlink(Accounter.getVATMessages()
				.openVATCodeList(), "link2");
		Hyperlink adjLink = new Hyperlink(Accounter.getVATMessages()
				.adjustVATDue(), "link3");

		VerticalPanel vPanel = new VerticalPanel();

		AbsolutePanel assignPanel = new AbsolutePanel();
		AbsolutePanel payVatPanel = new AbsolutePanel();
		AbsolutePanel taskPanel = new AbsolutePanel();

		assignPanel.setSize("600px", "100px");
		payVatPanel.setSize("600px", "100px");
		taskPanel.setSize("600px", "300px");

		assignPanel.add(icon, 30, 10);
		assignPanel.add(assignlink, 80, 10);
		capAssignPanel.add(assignPanel);

		vatLabel.setStyleName("requiredField");
		payVatPanel.add(vatLabel, 30, 10);
		payVatPanel.add(vatButton, 30, 50);
		capPayVatPanel.add(payVatPanel);

		vatButton.enabledButton();

		taskPanel.add(viewLink, 50, 10);
		taskPanel.add(openLink, 50, 30);
		taskPanel.add(adjLink, 50, 50);
		taskPanel.add(closeButton, 80, 100);
		taskPanel.add(helpButton, 180, 100);
		closeButton.enabledButton();
		helpButton.enabledButton();
		capTaskPanel.add(taskPanel);

		vPanel.add(stLabel);
		vPanel.add(capAssignPanel);
		vPanel.add(capPayVatPanel);
		vPanel.add(capTaskPanel);

		add(vPanel);
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vatButton.setFocus(true);
	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected String getViewTitle() {
		return Accounter.getActionsConstants().manageVatCodes();
	}

}
