package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;

public class UserDetailsView extends AbstractBaseView<ClientUser> {
	private VerticalPanel mainPanel;
	private Label userNameLabel, mailIdLabel;
	private HTML changePasswordHtml;
	private ClientUser clientUser;

	@Override
	public void init() {
		createControls();
		super.init();
	}

	private void createControls() {
		mainPanel = new VerticalPanel();
		userNameLabel = new Label("Name:"
				+ FinanceApplication.clientIdentity.getFullname());
		mailIdLabel = new Label("Mail ID:"
				+ FinanceApplication.clientIdentity.getEmailAddress());
		changePasswordHtml = new HTML("<a>change password </a>");
		changePasswordHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CompanyActionFactory.getChangePasswordAction().run(null, false);
			}
		});
		mainPanel.add(userNameLabel);
		mainPanel.add(mailIdLabel);
		mainPanel.add(changePasswordHtml);
		mainPanel.setWidth("100%");
		mainPanel.addStyleName("change_password_view");
		add(mainPanel);
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
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setData(ClientUser user) {
		super.setData(user);
		if (user != null) {
			clientUser = user;
		} else {
			clientUser = new ClientUser();
		}
	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
