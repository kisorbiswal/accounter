package com.vimukti.accounter.web.client.ui.translation;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.translate.ClientLocalMessage;
import com.vimukti.accounter.web.client.translate.ClientMessage;

public class MessagePanel extends VerticalPanel {
	private ClientMessage clientMessage;

	public MessagePanel(ClientMessage clientMessage) {
		this.clientMessage = clientMessage;
		createControls();
	}

	private void createControls() {
		VerticalPanel mainPanel = new VerticalPanel();
		VerticalPanel localMessagesPanel = new VerticalPanel();

		Label valueLabel = new Label(clientMessage.getValue());

		for (int i = 0; i < clientMessage.getLocalMessages().size(); i++) {
			localMessagesPanel.add(getLocalMessgePanel(clientMessage
					.getLocalMessages().get(i)));
		}

		TextBox addNewMessageBox = new TextBox();

		mainPanel.add(valueLabel);
		mainPanel.add(localMessagesPanel);
		mainPanel.add(addNewMessageBox);

		this.add(mainPanel);
	}

	private HorizontalPanel getLocalMessgePanel(
			ClientLocalMessage clientLocalMessage) {

		HorizontalPanel votesPanel = new HorizontalPanel();

		VerticalPanel upVotesPanel = new VerticalPanel();
		VerticalPanel downVotesPanel = new VerticalPanel();

		Image upImage = new Image("/images/arrow_up.gif");
		Image downImage = new Image("/images/arrow_down.gif");
		Label messageLabel = new Label(clientLocalMessage.getValue());

		Label upVotesLengthLabel = new Label(String.valueOf(clientLocalMessage
				.getUps()));
		Label downVotesLenghtLabel = new Label(String
				.valueOf(clientLocalMessage.getDowns()));

		upVotesPanel.add(upImage);
		upVotesPanel.add(upVotesLengthLabel);

		downVotesPanel.add(downImage);
		downVotesPanel.add(downVotesLenghtLabel);

		votesPanel.add(upVotesPanel);
		votesPanel.add(downVotesPanel);
		votesPanel.add(messageLabel);

		return votesPanel;
	}

}
