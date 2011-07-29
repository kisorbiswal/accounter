package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

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
		userNameLabel = new Label("Name : " + Accounter.getUser().getFullName());
		userNameLabel.setStyleName("user-name");
		mailIdLabel = new Label("Mail ID : " + Accounter.getUser().getEmail());
		mailIdLabel.setStyleName("user-name");
		changePasswordHtml = new HTML("<a> Change Password </a>");
		changePasswordHtml.setStyleName("change-password");
		changePasswordHtml.setWidth("12%");
		changePasswordHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getChangePasswordAction().run(null, false);
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
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void deleteFailed(Throwable caught) {

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

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().userDetails();
	}

}
