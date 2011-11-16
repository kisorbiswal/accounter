package com.vimukti.accounter.web.client.ui.translation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.translate.ClientLanguage;
import com.vimukti.accounter.web.client.translate.ClientMessage;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CoreUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.LanguageCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class TranslationView extends AbstractBaseView<ClientMessage> {
	private LanguageCombo languageCombo;
	private SelectCombo optionsCombo;
	private List<String> optionsList;
	private Label selectedLanguageLabel;
	private VerticalPanel dataPanel, mainPanel;
	private CustomTranslationPager pager;
	private boolean haveRecords;
	public boolean hasMoreRecords;

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

		languageCombo = new LanguageCombo(constants.languages());
		ClientLanguage clientLanguage = languageCombo.getComboItems().get(0);
		languageCombo.setComboItem(clientLanguage);
		languageCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientLanguage>() {

					@Override
					public void selectedComboBoxItem(ClientLanguage selectItem) {
						haveRecords = false;
						refreshPager();
						updateListData();
					}
				});

		optionsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						haveRecords = false;
						refreshPager();
						updateListData();
					}
				});

		selectedLanguageLabel = new Label();
		if (languageCombo.getSelectedValue() != null) {
			selectedLanguageLabel.setText(messages
					.selectedTranslated(languageCombo.getSelectedValue()
							.getLanguageTooltip()));
		}
		pager = new CustomTranslationPager(0, 5, this);
		dataPanel = new VerticalPanel();
		refreshPager();
		updateListData();

		DynamicForm combosForm = new DynamicForm();
		combosForm.setNumCols(4);
		combosForm.setFields(languageCombo, optionsCombo);

		mainPanel = new VerticalPanel();
		mainPanel.add(combosForm);
		mainPanel.add(selectedLanguageLabel);
		mainPanel.add(dataPanel);

		selectedLanguageLabel.addStyleName("selected-language");
		mainPanel.setWidth("100%");
		this.add(mainPanel);
		this.add(pager);
		this.setCellHorizontalAlignment(pager, HasAlignment.ALIGN_CENTER);
	}

	protected void refreshPager() {
		pager.setStart(CustomTranslationPager.DEFAULT_START);
		pager.setRange(CustomTranslationPager.DEFAULT_RANGE);
		pager.updateRange();
	}

	public void updateListData() {

		if ((languageCombo.getSelectedValue() != null)
				&& (optionsCombo.getSelectedValue() != null)) {
			selectedLanguageLabel.setText(messages
					.selectedTranslated(languageCombo.getSelectedValue()
							.getLanguageTooltip()));

			Accounter.createTranslateService().getMessages(
					languageCombo.getSelectedValue().getLanguageCode(),
					getStatus(optionsCombo.getSelectedValue()),
					pager.getStart(), pager.getRange() + 1,
					new AsyncCallback<ArrayList<ClientMessage>>() {

						@Override
						public void onSuccess(ArrayList<ClientMessage> result) {
							if (result.size() != 0) {
								result = setCorrectResult(result);
								createNewDataPanel();
								pager.updateData(result);
								for (int i = 0; i < result.size(); i++) {
									addMessageToMessagePanel(result.get(i));
								}
							} else {
								if (!isHaveRecords()) {
									addEmptyMsg();
								}
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}
					});

		}
	}

	protected ArrayList<ClientMessage> setCorrectResult(
			ArrayList<ClientMessage> result) {
		if (result.size() == pager.getRange() + 1) {
			hasMoreRecords = true;
			result.remove(pager.getRange());
		} else {
			hasMoreRecords = false;
		}
		return result;
	}

	protected void addMessageToMessagePanel(ClientMessage result) {
		MessagePanel messagePanel = new MessagePanel(this, languageCombo
				.getSelectedValue().getLanguageCode(), result);
		dataPanel.add(messagePanel);
	}

	protected void addEmptyMsg() {
		createNewDataPanel();
		Label error = new Label(AccounterWarningType.RECORDSEMPTY);
		dataPanel.add(error);
		dataPanel.setCellHorizontalAlignment(error, HasAlignment.ALIGN_CENTER);
		pager.setVisible(false);
	}

	protected void createNewDataPanel() {
		if (dataPanel != null) {
			mainPanel.remove(dataPanel);
			dataPanel = new VerticalPanel();
		}
		mainPanel.add(dataPanel);
		dataPanel.addStyleName("translated-result");
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

	public void setHaveRecords(boolean haveRecords) {
		this.haveRecords = haveRecords;
	}

	public boolean isHaveRecords() {
		return haveRecords;
	}

}
