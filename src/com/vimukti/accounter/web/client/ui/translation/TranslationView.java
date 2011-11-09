package com.vimukti.accounter.web.client.ui.translation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.translate.ClientMessage;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class TranslationView extends AbstractBaseView<ClientMessage> {
	private SelectCombo languageCombo, optionsCombo;
	private List<String> optionsList, languagesList;
	private Label selectedLanguageLabel;
	private VerticalPanel dataPanel;

	public TranslationView() {
		super();
		createControls();
	}

	private void createControls() {
		optionsList = new ArrayList<String>();
		optionsList.add(constants.untranslated());
		optionsList.add(constants.all());
		optionsList.add(constants.myTranslations());
		optionsList.add(constants.unConfirmed());

		optionsCombo = new SelectCombo(constants.show());
		for (int i = 0; i < optionsList.size(); i++) {
			optionsCombo.addItem(optionsList.get(i));
		}
		optionsCombo.setSelected(constants.untranslated());

		languagesList = new ArrayList<String>();
		languagesList.add(constants.userLocal());

		languageCombo = new SelectCombo(constants.languages());
		for (int i = 0; i < languagesList.size(); i++) {
			languageCombo.addItem(languagesList.get(i));
		}
		languageCombo.setSelected(constants.userLocal());

		languageCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						updateData();
					}
				});

		optionsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						updateData();
					}
				});

		selectedLanguageLabel = new Label();
		if (languageCombo.getSelectedValue() != null) {
			selectedLanguageLabel.setText(messages
					.selectedTranslated(languageCombo.getSelectedValue()));
		}
		updateData();
		DynamicForm combosForm = new DynamicForm();
		combosForm.setNumCols(4);
		combosForm.setFields(languageCombo, optionsCombo);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(combosForm);
		mainPanel.add(selectedLanguageLabel);
		if (dataPanel != null) {
			mainPanel.add(dataPanel);
		}
		this.add(mainPanel);

	}

	protected void updateData() {
		if ((languageCombo.getSelectedValue() != null)
				&& (optionsCombo.getSelectedValue() != null)) {
			dataPanel = new VerticalPanel();
			selectedLanguageLabel.setText(messages
					.selectedTranslated(languageCombo.getSelectedValue()));

			Accounter.createTranslateService().getMessages(
					languageCombo.getSelectedValue(),
					getStatus(optionsCombo.getSelectedValue()),
					new AsyncCallback<ArrayList<ClientMessage>>() {

						@Override
						public void onSuccess(ArrayList<ClientMessage> result) {
							for (int i = 0; i < result.size(); i++) {
								MessagePanel messagePanel = new MessagePanel(
										result.get(i));
								dataPanel.add(messagePanel);
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}
					});

		}
	}

	private int getStatus(String statusMessage) {
		if (statusMessage.equals(constants.untranslated())) {
			return ClientMessage.UNTRANSLATED;
		} else if (statusMessage.equals(constants.all())) {
			return ClientMessage.ALL;
		} else if (statusMessage.equals(constants.myTranslations())) {
			return ClientMessage.MYTRANSLATIONS;
		} else if (statusMessage.equals(constants.unConfirmed())) {
			return ClientMessage.UNCONFIRMED;
		}
		return 0;

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

}
