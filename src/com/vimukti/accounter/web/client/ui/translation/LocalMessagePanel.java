package com.vimukti.accounter.web.client.ui.translation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.translate.ClientLocalMessage;
import com.vimukti.accounter.web.client.translate.TranslateServiceAsync;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class LocalMessagePanel extends HorizontalPanel {

	private ClientLocalMessage clientLocalMessage;
	private TranslationView view;
	private TranslateServiceAsync async = Accounter.createTranslateService();
	private boolean canApprove;
	private ImageButton upImage;
	private VerticalPanel upVotesPanel;
	private HorizontalPanel votesWithMsgPanel, votesPanel;
	private Label messageLabel, upVotesLengthLabel;
	private FlowPanel approvePanel;
	private RadioButton approveButton;

	public LocalMessagePanel(ClientLocalMessage clientLocalMessage,
			TranslationView view, boolean canApprove) {
		this.clientLocalMessage = new ClientLocalMessage();
		this.view = view;
		this.canApprove = canApprove;
		createControls();
	}

	private void createControls() {

		votesWithMsgPanel = new HorizontalPanel();

		createApprovePanel();
		createVotePanel();
		createMessagePanel();

		votesWithMsgPanel.add(votesPanel);

		votesWithMsgPanel.setSpacing(4);
		votesWithMsgPanel.setCellWidth(votesPanel, "6%");
		votesWithMsgPanel.addStyleName("votes-message-panel");

	}

	private void createMessagePanel() {
		messageLabel = new Label(clientLocalMessage.getValue());
		messageLabel.addStyleName("message_label");
		votesWithMsgPanel.add(messageLabel);
		votesWithMsgPanel.setCellVerticalAlignment(messageLabel,
				HasAlignment.ALIGN_MIDDLE);
	}

	private void createVotePanel() {
		votesPanel = new HorizontalPanel();
		upVotesPanel = new VerticalPanel();
		upVotesLengthLabel = new Label(String.valueOf(clientLocalMessage
				.getUps()));
		upImage = new ImageButton(Accounter.getFinanceImages().upArrow());
		upImage.addStyleName("image_button");

		upImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				voteLocalMessage(true);
			}
		});

		upVotesPanel.add(upImage);
		upVotesPanel.add(upVotesLengthLabel);
		upVotesPanel.setCellHorizontalAlignment(upVotesLengthLabel,
				HasAlignment.ALIGN_CENTER);

		votesPanel.add(upVotesPanel);

		upVotesPanel.addStyleName("up_image_panel");
		upVotesLengthLabel.addStyleName("up_label");

		votesPanel.setSpacing(4);
		votesPanel.addStyleName("votes_panel");
	}

	private void createApprovePanel() {
		approveButton = new RadioButton(clientLocalMessage.getMessage()
				.getValue());
		approvePanel = new FlowPanel();
		approveButton.setValue(clientLocalMessage.isApproved());

		approveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				approveMessage();
			}
		});

		if (canApprove) {
			votesWithMsgPanel.add(approvePanel);
			approvePanel.getElement().getParentElement().addClassName(
					"approve-img");
		}
	}

	protected void approveMessage() {
		async.setApprove(clientLocalMessage.getId(), true,
				new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							approveButton.setValue(result);
							view.refreshPager();
							view.updateListData();
						}
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
	}

	protected void voteLocalMessage(boolean isUp) {
		async.vote(clientLocalMessage.getId(), isUp,
				new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Accounter
								.showError(Accounter.messages()
										.unableToVoteThisTranslation(
												caught.toString()));
					}

					@Override
					public void onSuccess(Boolean result) {
						view.refreshPager();
						view.updateListData();
					}
				});
	}

}
