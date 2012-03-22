package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.TransactionMeterEventType;
import com.vimukti.accounter.web.client.util.CoreEvent;
import com.vimukti.accounter.web.client.util.CoreEventHandler;

public class TransactionMeterPanel extends SimplePanel {
	private int transactionNumber = 0;
	private Label countLabel;

	public TransactionMeterPanel() {
		transactionNumber = Accounter.getCompany().getTransactionsCount();
		initGui();
		Type<CoreEventHandler<TransactionMeterEventType>> type = CoreEvent
				.getType(TransactionMeterEventType.class);
		Accounter.getEventBus().addHandler(type,
				new CoreEventHandler<TransactionMeterEventType>() {

					@Override
					public void onAdd(TransactionMeterEventType obj) {
						increase();
					}

					@Override
					public void onDelete(TransactionMeterEventType obj) {
						decrease();
					}

					@Override
					public void onChange(TransactionMeterEventType obj) {
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
		mainPanel.setCellHorizontalAlignment(countLabel,
				HasHorizontalAlignment.ALIGN_CENTER);
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
