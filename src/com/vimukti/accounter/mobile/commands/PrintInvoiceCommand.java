package com.vimukti.accounter.mobile.commands;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.core.AccounterThreadLocal;
import com.vimukti.accounter.core.BrandingTheme;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.CompanyPreferenceThreadLocal;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.IGlobal;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.server.FinanceTool;

public class PrintInvoiceCommand extends AbstractCommand {

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
		try {
			sendPrintAndInvoiceToEmail(invoice, emailId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "cancel";// To close this command
	}

	private void sendPrintAndInvoiceToEmail(Invoice invoice,
			final String emailId) {
		Company company = getCompany();
		Set<BrandingTheme> brandingTheme = company.getBrandingTheme();
		Iterator<BrandingTheme> iterator = brandingTheme.iterator();
		if (!iterator.hasNext()) {
			return;
		}
		// Sending to mail in a separate thread.
		BrandingTheme next = iterator.next();
		final long brandingThemeId = next.getID();
		final long companyId = company.getID();
		final long invoiceId = invoice.getID();
		final String tradingName = company.getTradingName();
		final String customerName = invoice.getCustomer().getName();
		final Locale locale = ServerLocal.get();
		final ClientCompanyPreferences clientCompanyPreferences = CompanyPreferenceThreadLocal
				.get();
		final User user = AccounterThreadLocal.get();
		final IGlobal iGlobal = Global.get();
		new Thread(new Runnable() {

			@Override
			public void run() {
				Session openSession = HibernateUtil.openSession();
				try {
					ServerLocal.set(locale);
					Global.set(iGlobal);
					CompanyPreferenceThreadLocal.set(clientCompanyPreferences);
					AccounterThreadLocal.set(user);
					FinanceTool tool = new FinanceTool();
					String createPdfFile = tool.createPdfFile(invoiceId,
							Transaction.TYPE_INVOICE, brandingThemeId,
							companyId);

					UsersMailSendar.sendPdfMail(new File(createPdfFile),
							tradingName, getMessages().invoiceFor() + " "
									+ customerName, getMessages()
									.pleaseFindAttachedInvoice(), null,
							emailId, "");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (openSession.isOpen()) {
						openSession.close();
					}
				}
			}
		}).start();

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
