package com.vimukti.accounter.web.client.ui.translation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.translate.ClientLocalMessage;
import com.vimukti.accounter.web.client.translate.ClientMessage;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;

public class MessagePanel extends VerticalPanel {
	private ClientMessage clientMessage;
	private String language;
	private TranslationView view;

	public MessagePanel(TranslationView view, String language,
			ClientMessage clientMessage) {
		this.view = view;
		this.clientMessage = clientMessage;
		this.language = language;
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

		final TextBox addNewMessageBox = new TextBox();
		addNewMessageBox.removeStyleName("gwt-TextBox");
		addNewMessageBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER
						&& addNewMessageBox.getText().trim().length() != 0) {
					Accounter.createTranslateService().addTranslation(
							clientMessage.getId(), language,
							addNewMessageBox.getText(),
							new AsyncCallback<Boolean>() {

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										view.refreshPager();
										view.updateListData();
									} else {
										Accounter
												.showError(Accounter
														.constants()
														.oneUserCanEnterOneValueOnlyForEachMessage());
									}
								}

								@Override
								public void onFailure(Throwable caught) {

								}
							});
				}
			}
		});

		mainPanel.add(valueLabel);
		mainPanel.add(localMessagesPanel);
		mainPanel.add(addNewMessageBox);

		valueLabel.addStyleName("value_lable");
		localMessagesPanel.addStyleName("local_message_panel");
		addNewMessageBox.addStyleName("add_new_message_box");

		mainPanel.addStyleName("message_panel");

		this.add(mainPanel);

		this.addStyleName("message-panel-parent");
	}

	private HorizontalPanel getLocalMessgePanel(
			final ClientLocalMessage clientLocalMessage) {

		HorizontalPanel votesPanel = new HorizontalPanel();
		HorizontalPanel votesWithMsgPanel = new HorizontalPanel();

		VerticalPanel upVotesPanel = new VerticalPanel();
		VerticalPanel downVotesPanel = new VerticalPanel();

		ImageButton upImage = new ImageButton(Accounter.getFinanceImages()
				.upArrow());
		ImageButton downImage = new ImageButton(Accounter.getFinanceImages()
				.downArrow());
		Label messageLabel = new Label(clientLocalMessage.getValue());

		upImage.addStyleName("image_button");
		downImage.addStyleName("image_button");

		upImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Accounter.createTranslateService().vote(
						clientLocalMessage.getId(),
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								Accounter.showError(Accounter.messages()
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
		});

		downImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Accounter.createTranslateService().vote(
						clientLocalMessage.getId(),
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								Accounter.showError(Accounter.messages()
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
		});
		final ImageButton non_approveImageButton = new ImageButton(Accounter
				.getFinanceImages().blackTick());
		final ImageButton approveImageButton = new ImageButton(Accounter
				.getFinanceImages().greenTick());
		final FlowPanel approvePanel = new FlowPanel();

		Accounter.createTranslateService().canApprove(language,
				new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							if (clientLocalMessage.isApproved()) {
								approvePanel.add(approveImageButton);
							} else {
								approvePanel.add(non_approveImageButton);
							}
						}
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});

		non_approveImageButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Accounter.createTranslateService().setApprove(
						clientLocalMessage.getId(), true,
						new AsyncCallback<Boolean>() {

							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									approvePanel.remove(non_approveImageButton);
									approvePanel.add(approveImageButton);
									view.refreshPager();
									view.updateListData();
								}
							}

							@Override
							public void onFailure(Throwable caught) {

							}
						});
			}
		});

		Label upVotesLengthLabel = new Label(String.valueOf(clientLocalMessage
				.getVotes()));
		Label downVotesLenghtLabel = new Label(String.valueOf(1/*
																 * clientLocalMessage
																 * .getDowns()
																 */));

		upVotesPanel.add(upImage);
		upVotesPanel.add(upVotesLengthLabel);
		upVotesPanel.setCellHorizontalAlignment(upVotesLengthLabel,
				HasAlignment.ALIGN_CENTER);

		downVotesPanel.add(downImage);
		downVotesPanel.add(downVotesLenghtLabel);
		downVotesPanel.setCellHorizontalAlignment(downVotesLenghtLabel,
				HasAlignment.ALIGN_CENTER);

		votesPanel.add(upVotesPanel);
		votesPanel.add(downVotesPanel);
		votesWithMsgPanel.add(approvePanel);
		votesWithMsgPanel.add(votesPanel);
		votesWithMsgPanel.add(messageLabel);
		votesPanel.setSpacing(4);
		votesWithMsgPanel.setSpacing(4);
		votesWithMsgPanel.setCellVerticalAlignment(messageLabel,
				HasAlignment.ALIGN_MIDDLE);

		upVotesPanel.addStyleName("up_image_panel");
		downVotesPanel.addStyleName("down_image_panel");
		messageLabel.addStyleName("message_label");
		upVotesLengthLabel.addStyleName("up_label");
		downVotesLenghtLabel.addStyleName("down_label");

		votesWithMsgPanel.setCellWidth(votesPanel, "12%");
		votesWithMsgPanel.addStyleName("votes-message-panel");
		votesPanel.addStyleName("votes_panel");
		approvePanel.getElement().getParentElement()
				.addClassName("approve-img");

		return votesWithMsgPanel;
	}

}
