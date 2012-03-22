package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.TransactionMeterEventType;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.util.CoreEvent;
import com.vimukti.accounter.web.client.util.CoreEventHandler;

public class TransactionMeterPanel extends SimplePanel {
	protected static final AccounterMessages messages = Global.get().messages();
	private int transactionNumber = 0;
	private Label countLabel;
	private StyledPanel meterBar, meterbar1;

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
		countLabel = new Label(messages.transactionsUsed(transactionNumber,
				Features.TRANSACTION_PER_MONTH));
		meterBar = new StyledPanel("transactionMeterBar");
		meterbar1 = new StyledPanel("inner_transactionMeterBar");
		mainPanel.add(countLabel);
		meterBar.add(meterbar1);
		addStyleToMeterbar();
		mainPanel.add(meterBar);
		mainPanel.addStyleName("transaction_meter");
		add(mainPanel);
	}

	public void increase() {
		transactionNumber++;
		updateLabel();
	}

	private void updateLabel() {
		addStyleToMeterbar();
		countLabel.setText(messages.transactionsUsed(transactionNumber,
				Features.TRANSACTION_PER_MONTH));

	}

	public void decrease() {
		transactionNumber--;
		updateLabel();
	}

	public void addStyleToMeterbar() {
		int width = transactionNumber * 10;
		meterbar1.setWidth(width + "px");
		if (transactionNumber <= 12) {
			meterbar1.getElement().getStyle().setBackgroundColor("green");
		} else if (transactionNumber > 12 && transactionNumber < 16) {
			meterbar1.getElement().getStyle().setBackgroundColor("yellow");
		} else {
			meterbar1.getElement().getStyle().setBackgroundColor("red");
		}
	}
}
