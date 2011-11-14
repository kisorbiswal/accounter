package com.vimukti.accounter.web.client.ui.translation;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.vimukti.accounter.web.client.translate.ClientMessage;
import com.vimukti.accounter.web.client.ui.Accounter;

public class TranslationList extends CellTable<ClientMessage> {
	private AsyncDataProvider<ClientMessage> dataProvider;
	private TranslationView view;
	private String lang;
	private int status;

	public TranslationList(TranslationView view, String lang, int status) {
		this.view = view;
		this.lang = lang;
		this.status = status;
		createControls();
	}

	private void createControls() {
		dataProvider = new AsyncDataProvider<ClientMessage>() {
			@Override
			protected void onRangeChanged(HasData<ClientMessage> display) {
				final int start = display.getVisibleRange().getStart();
				int length = display.getVisibleRange().getLength();

				Accounter.createTranslateService().getMessages(lang, status,
						start, length,
						new AsyncCallback<ArrayList<ClientMessage>>() {

							@Override
							public void onSuccess(
									ArrayList<ClientMessage> result) {
								for (int i = 0; i < result.size(); i++) {
									MessagePanel messagePanel = new MessagePanel(
											view, lang, result.get(i));
								}

							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});
			}
		};
	}
}
