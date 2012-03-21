package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TransactionMeterPanel extends SimplePanel {
	private int transactionNumber = 0;
	private Label countLabel;

	public TransactionMeterPanel() {
		Accounter.createGETService().getInitialTransactionCount(
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						transactionNumber = result;
						initGui();
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});

	}

	public TransactionMeterPanel(int transactionNo) {
		transactionNumber = transactionNo;
		initGui();
	}

	private void initGui() {
		VerticalPanel mainPanel = new VerticalPanel();
		Label transactionCountLabel = new Label(Accounter.getMessages()
				.transactionsCount());
		countLabel = new Label(transactionNumber + "");
		mainPanel.add(transactionCountLabel);
		mainPanel.add(countLabel);
		transactionCountLabel.addStyleName("transaction_count");
		countLabel.addStyleName("count_label");
		mainPanel.addStyleName("transaction_meter");
		mainPanel.setCellHorizontalAlignment(countLabel, HasHorizontalAlignment.ALIGN_CENTER);
		add(mainPanel);
	}

	public void increase() {
		transactionNumber++;
		updateLabel();
	}

	private void updateLabel() {
		countLabel.setText(transactionNumber + "");
	}

	public void decrease() {
		transactionNumber--;
		updateLabel();
	}
}
