package com.vimukti.accounter.web.client.ui.translation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.translate.ClientLanguage;
import com.vimukti.accounter.web.client.translate.ClientMessage;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.LanguageCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class TranslationView extends AbstractBaseView<ClientMessage> {
	private LanguageCombo languageCombo;
	private SelectCombo optionsCombo;
	private List<String> optionsList;
	private Label selectedLanguageLabel;
	private VerticalPanel dataPanel, mainPanel;
	private CustomTranslationPager pager;
	private boolean haveRecords;
	public boolean hasMoreRecords;
	private FlowPanel notePanel;
	private boolean canApprove;
	private TextItem searchItem;

	// private TextItem searchItem;

	public TranslationView() {
		super();
		createControls();
	}

	private void createControls() {
		optionsList = new ArrayList<String>();
		optionsList.add(messages.untranslated());
		optionsList.add(messages.all());
		optionsList.add(messages.myTranslations());
		optionsList.add(messages.unConfirmed());

		optionsCombo = new SelectCombo(messages.show());
		for (int i = 0; i < optionsList.size(); i++) {
			optionsCombo.addItem(optionsList.get(i));
		}
		optionsCombo.setSelected(messages.untranslated());

		languageCombo = new LanguageCombo(messages.languages(), this);

		languageCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientLanguage>() {

					@Override
					public void selectedComboBoxItem(ClientLanguage selectItem) {
						languageSelected(selectItem);
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
		searchItem = new TextItem(messages.searchHere());
		searchItem.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				searchItem.getValue();
				updateListData();
			}
		});

		selectedLanguageLabel = new Label();
		if (languageCombo.getSelectedValue() != null) {
			selectedLanguageLabel.setText(messages
					.selectedTranslated(languageCombo.getSelectedValue()
							.getLanguageTooltip()));
		}

		notePanel = new FlowPanel();
		Label noteLabel = new Label(messages.translateNote());
		notePanel.add(noteLabel);
		notePanel.addStyleName("translation_note");
		noteLabel.addStyleName("translation_note_label");

		pager = new CustomTranslationPager(0, 5, this);
		dataPanel = new VerticalPanel();
		refreshPager();
		updateListData();

		DynamicForm combosForm = new DynamicForm();
		combosForm.setNumCols(6);
		combosForm.setFields(languageCombo, optionsCombo, searchItem);

		mainPanel = new VerticalPanel();
		mainPanel.add(combosForm);
		mainPanel.add(notePanel);
		mainPanel.add(selectedLanguageLabel);
		mainPanel.add(dataPanel);

		selectedLanguageLabel.addStyleName("selected-language");
		mainPanel.setWidth("100%");
		this.add(mainPanel);
		this.add(pager);
		this.setCellHorizontalAlignment(pager, HasAlignment.ALIGN_CENTER);
	}

	public void languageSelected(ClientLanguage selectItem) {
		Accounter.createTranslateService().canApprove(
				selectItem.getLanguageCode(), new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						haveRecords = false;
						setCanApprove(result);
						refreshPager();
						updateListData();
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
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

			addMessagesToView();

		}
	}

	private void addMessagesToView() {
		Accounter.createTranslateService().getMessages(
				languageCombo.getSelectedValue().getLanguageCode(),
				getStatus(optionsCombo.getSelectedValue()), pager.getStart(),
				pager.getRange() + 1, searchItem.getValue(),
				new AsyncCallback<ArrayList<ClientMessage>>() {

					@Override
					public void onSuccess(ArrayList<ClientMessage> result) {
						if (result.size() != 0) {
							result = setCorrectResult(result);
							createNewDataPanel();
							pager.updateData(result);
							for (int i = 0; i < result.size(); i++) {
								addMessageToMessagePanel(result.get(i),
										canApprove);
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

	protected void addMessageToMessagePanel(ClientMessage result,
			boolean canApprove) {
		MessagePanel messagePanel = new MessagePanel(this, languageCombo
				.getSelectedValue().getLanguageCode(), result, canApprove);
		dataPanel.add(messagePanel);
	}

	protected void addEmptyMsg() {
		createNewDataPanel();
		Label error = new Label(messages.noRecordsToShow());
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
		if (statusMessage.equals(messages.untranslated())) {
			return ClientMessage.UNTRANSLATED;
		} else if (statusMessage.equals(messages.all())) {
			return ClientMessage.ALL;
		} else if (statusMessage.equals(messages.myTranslations())) {
			return ClientMessage.MYTRANSLATIONS;
		} else if (statusMessage.equals(messages.unConfirmed())) {
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

	private void setCanApprove(boolean canApprove) {
		this.canApprove = canApprove;
	}
}
