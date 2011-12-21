package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;

public class PrintInvoiceCommand extends NewAbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		long transactionId = Long.valueOf(context.getString());
		Session session = context.getHibernateSession();
		Invoice invoice = (Invoice) session.get(Invoice.class, transactionId);
		if (invoice == null) {
			addFirstMessage(context, getMessages().selectInvoiceToPrint());
			return "invoicesList print";
		}
		addFirstMessage(context, getMessages()
				.invoiceHasbeenPrintedAndSentToYourEmail());
		String emailId = context.getEmailId();
		sendPrintAndInvoiceToEmail(invoice, emailId);
		return "cancel";
	}

	private void sendPrintAndInvoiceToEmail(Invoice invoice, String emailId) {
		System.out.println("invoice printed..." + emailId);
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
	}

	@Override
	public String getSuccessMessage() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
	}

}
