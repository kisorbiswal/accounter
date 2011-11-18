package com.vimukti.accounter.web.client.ui.combo;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.translate.ClientLanguage;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.translation.TranslationView;

public class LanguageCombo extends CustomCombo<ClientLanguage> {

	private TranslationView view;

	public LanguageCombo(String title, final TranslationView view) {
		super(title, false, 1);
		this.view = view;
		initComboData();

	}

	private void initComboData() {
		Accounter.createTranslateService().getLanguages(
				new AsyncCallback<List<ClientLanguage>>() {

					@Override
					public void onSuccess(List<ClientLanguage> result) {
						initCombo(result);
						setLanguage(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("");

					}
				});

	}

	protected void setLanguage(List<ClientLanguage> result) {
		Accounter.createTranslateService().getLocalLanguage(
				new AsyncCallback<ClientLanguage>() {

					@Override
					public void onSuccess(ClientLanguage result) {
						setComboItem(result);
						view.languageSelected(result);
						view.updateListData();
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});

	}

	@Override
	protected String getDisplayName(ClientLanguage object) {
		if (object.getLanguageName() != null) {
			return object.getLanguageName();
		}
		return null;
	}

	@Override
	protected String getColumnData(ClientLanguage object, int col) {
		if (object != null)
			if (object.getLanguageTooltip().equals(object.getLanguageName())) {
				return object.getLanguageName();
			} else {
				return Accounter.messages().languageName(
						object.getLanguageName(), object.getLanguageTooltip());
			}
		return "";
	}

	@Override
	public String getDefaultAddNewCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub

	}

}
