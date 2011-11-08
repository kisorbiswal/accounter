package com.vimukti.accounter.web.client.translate;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StatusPanel extends VerticalPanel {
	public FlexTable table;
	public TranslationWizard translationWizard;
	private ArrayList<Status> list = new ArrayList<Status>();

	public StatusPanel(TranslationWizard translationWizard) {
		this.setWidth("100%");
		this.translationWizard = translationWizard;
		createControls();
	}

	private void createControls() {
		table = new FlexTable();

		TranslationEntryPoint.translateService
				.getStatus(new AsyncCallback<ArrayList<Status>>() {

					@Override
					public void onSuccess(ArrayList<Status> result) {
						list = result;
						initTable();
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
		this.add(table);
	}

	protected void initTable() {
		for (int i = 0; i < list.size(); i++) {
			final Status status = list.get(i);
			Anchor anchor = new Anchor(status.getLang());
			anchor.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					TranslationDetailPanel translationDetailPanel = new TranslationDetailPanel(
							translationWizard, status.getLang());
					translationDetailPanel.setWidth("100%");
					translationWizard.remove(StatusPanel.this);
					translationWizard.add(translationDetailPanel);
				}
			});
			HorizontalPanel translateProgressPanel = new HorizontalPanel();
			Label translatedProgress = new Label("Translated Messages : "
					+ status.getTranslated());
//			translateProgressPanel.setWidth("500px");
			String translateWidth = (500 / status.getTotal())
					* status.getTranslated() + "px";
			translatedProgress.setWidth(translateWidth);

			HorizontalPanel approveProgressPanel = new HorizontalPanel();
			Label approvedProgress = new Label("Approved Messages : "
					+ status.getApproved());
			approveProgressPanel.setWidth("500px");
			String approvedWidth = (500 / status.getTotal())
					* status.getApproved() + "px";
			approvedProgress.setWidth(approvedWidth);

			table.setWidget(i, 0, anchor);
			table.setWidget(i, 1, translateProgressPanel);
			table.setWidget(i, 2, approveProgressPanel);
		}

	}
}
