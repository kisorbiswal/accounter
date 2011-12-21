package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.FinanceTool;

public class InvoiceListCommand extends AbstractTransactionListCommand {

	private boolean isPrinting;

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		isPrinting = context.getString().contains("print");
		return null;
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
		super.setDefaultValues(context);
		get(VIEW_BY).setDefaultValue(getMessages().open());
	}

	@Override
	public String getSuccessMessage() {
		return "Success";
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		list.add(new ShowListRequirement<InvoicesList>(
				getMessages().invoices(), getMessages().pleaseSelect(
						getMessages().invoice()), 20) {
			@Override
			protected String onSelection(InvoicesList value) {
				if (isPrinting) {
					return "printAndSendEmail " + value.getTransactionId();
				}
				return "editTransaction " + value.getTransactionId();
			}

			@Override
			protected String getShowMessage() {
				return getMessages().invoices();
			}

			@Override
			protected String getEmptyString() {
				return getMessages()
						.youDontHaveAny(getMessages().invoiceList());
			}

			@Override
			protected Record createRecord(InvoicesList value) {
				Record record = new Record(value);
				record.add(getMessages().transactionName(),
						Utility.getTransactionName(value.getType()));
				record.add(getMessages().name(), value.getCustomerName());
				record.add(getMessages().date(), value.getDate());
				record.add(getMessages().total(),
						getCurrency(value.getCurrency()).getSymbol() + " "
								+ value.getTotalPrice());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createInvoice");
			}

			@Override
			protected boolean filter(InvoicesList e, String name) {
				return e.getCustomerName().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			@Override
			protected List<InvoicesList> getLists(Context context) {
				return getInvoices(context);
			}
		});
	}

	@Override
	protected List<String> getViewByList() {
		List<String> list = new ArrayList<String>();
		list.add(getMessages().open());
		list.add(getMessages().overDue());
		list.add(getMessages().voided());
		list.add(getMessages().all());
		return list;
	}

	private List<InvoicesList> getInvoices(Context context) {
		String viewType = get(VIEW_BY).getValue();
		try {
			List<InvoicesList> invoices = new FinanceTool()
					.getInventoryManager()
					.getInvoiceList(context.getCompany().getId(),
							getStartDate().getDate(), getEndDate().getDate(), 0);
			List<InvoicesList> list = new ArrayList<InvoicesList>(
					invoices.size());
			for (InvoicesList invoice : invoices) {
				if (viewType.equals(getMessages().open())) {
					if (invoice.getBalance() != null
							&& DecimalUtil.isGreaterThan(invoice.getBalance(),
									0)
							&& invoice.getDueDate() != null
							&& (invoice.getStatus() != ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
							&& !invoice.isVoided()) {
						list.add(invoice);
					}

				} else if (viewType.equals(getMessages().overDue())) {
					if (invoice.getBalance() != null
							&& DecimalUtil.isGreaterThan(invoice.getBalance(),
									0)
							&& invoice.getDueDate() != null
							&& (invoice.getDueDate().compareTo(
									new ClientFinanceDate()) < 0)
							&& !invoice.isVoided()) {
						list.add(invoice);
					}
				} else if (viewType.equals(getMessages().voided())) {
					if (invoice.isVoided()) {
						list.add(invoice);
					}
				} else if (viewType.equals(getMessages().all())) {
					list.add(invoice);
				}
			}

			return list;
		} catch (DAOException e) {
		}
		return null;
	}
}