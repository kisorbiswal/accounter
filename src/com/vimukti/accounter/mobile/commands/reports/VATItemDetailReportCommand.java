package com.vimukti.accounter.mobile.commands.reports;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ReportResultRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.server.FinanceTool;

public class VATItemDetailReportCommand extends
		NewAbstractReportCommand<VATItemDetail> {
	TAXItem taxItem;

	@Override
	protected void addRequirements(List<Requirement> list) {
		addDateRangeFromToDateRequirements(list);
		list.add(new ReportResultRequirement<VATItemDetail>() {

			@Override
			protected String onSelection(VATItemDetail selection, String name) {
				return addCommandOnRecordClick(selection);
			}

			@Override
			protected void fillResult(Context context, Result makeResult) {
				List<VATItemDetail> records = getRecords();
				if (records.isEmpty()) {
					makeResult.add("No Records to show");
					return;
				}

				Map<String, List<VATItemDetail>> recordGroups = new HashMap<String, List<VATItemDetail>>();
				for (VATItemDetail transactionDetailByAccount : records) {
					String taxItemName = transactionDetailByAccount.getName();
					List<VATItemDetail> group = recordGroups.get(taxItemName);
					if (group == null) {
						group = new ArrayList<VATItemDetail>();
						recordGroups.put(taxItemName, group);
					}
					group.add(transactionDetailByAccount);
				}

				Set<String> keySet = recordGroups.keySet();
				List<String> taxItems = new ArrayList<String>(keySet);
				Collections.sort(taxItems);
				for (String accountName : taxItems) {
					List<VATItemDetail> group = recordGroups.get(accountName);
					double totalAmount = 0.0;
					addSelection(accountName);
					ResultList resultList = new ResultList(accountName);
					resultList.setTitle(accountName);
					for (VATItemDetail rec : group) {
						totalAmount += rec.getAmount();
						resultList.add(createReportRecord(rec));
					}
					makeResult.add(resultList);
					makeResult.add("Total: "
							+ getAmountWithCurrency(totalAmount));
				}
			}
		});
	}

	protected Record createReportRecord(VATItemDetail record) {
		Record ecRecord = new Record(record);
		ecRecord.add(getMessages().type(),
				Utility.getTransactionName(record.getTransactionType()));
		ecRecord.add(getMessages().date(),
				getDateByCompanyType(record.getDate(), getPreferences()));
		ecRecord.add(getMessages().number(), record.getTransactionNumber());
		ecRecord.add(getMessages().name(), record.getName());
		ecRecord.add(getMessages().memo(), record.getMemo());
		ecRecord.add(getMessages().amount(),
				getAmountWithCurrency(record.getAmount()));
		ecRecord.add(getMessages().salesPrice(),
				getAmountWithCurrency(record.getSalesPrice()));
		return ecRecord;
	}

	protected List<VATItemDetail> getRecords() {

		try {
			if (taxItem == null) {
				return new FinanceTool().getReportManager()
						.getVATItemDetailReport(getStartDate(), getEndDate(),
								getCompanyId());
			} else {
				return new FinanceTool().getReportManager()
						.getVATItemDetailReport(taxItem.getName(),
								getStartDate(), getEndDate(), getCompanyId());
			}
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return new ArrayList<VATItemDetail>();
	}

	protected String addCommandOnRecordClick(VATItemDetail selection) {
		return "updateTransaction " + selection.getTransactionId();
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String itemName = null;
		String string = context.getString();
		if (string != null) {
			String[] split = string.split(",");
			if (split.length > 1) {
				context.setString(split[0]);
				itemName = split[1];
			}
		}
		if (itemName != null) {
			Set<TAXItem> taxItems = context.getCompany().getTaxItems();
			for (TAXItem taxItem : taxItems) {
				if (taxItem.getName().equalsIgnoreCase(itemName)) {
					this.taxItem = taxItem;
				}
			}
		}
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "VAT Item detail report command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return "VAT Item report details";
	}

	@Override
	public String getSuccessMessage() {
		return "VAT Item detail report command closed successfully";
	}

}
