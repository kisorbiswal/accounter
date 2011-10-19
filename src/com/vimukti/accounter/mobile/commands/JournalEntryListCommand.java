package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.server.FinanceTool;

public class JournalEntryListCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

	}

	@Override
	public Result run(Context context) {
		Result result = optionalRequirement(context);
		return result;
	}

	private Result optionalRequirement(Context context) {

		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				markDone();
				return null;
			default:
				break;
			}
		}
		selection = context.getSelection("values");
		Result result = getJournalEntryList(context);
		if (result != null) {
			return result;
		}
		return null;
	}

	private Result getJournalEntryList(Context context) {
		Result result = context.makeResult();
		ResultList userList = new ResultList("journalEntryList");
		List<ClientJournalEntry> entryList = getJournalEntries(context);
		for (ClientJournalEntry entry : entryList) {
			userList.add(createJournalRecord(entry));

		}

		result.add(userList);

		CommandList command = new CommandList();
		command.add("Add a New Journal Entry");

		result.add(command);

		return result;
	}

	private Record createJournalRecord(ClientJournalEntry entry) {
		Record record = new Record(entry);
		record.add("Voucher No", entry.getNumber());
		record.add("Date Created", entry.getDate());
		record.add("Amount", entry.getTotal());
		record.add("Memo", entry.getMemo());

		// record.add("Voided", entry.isVoid());
		return record;
	}

	public ArrayList<ClientJournalEntry> getJournalEntries(Context context) {
		List<ClientJournalEntry> clientJournalEntries = new ArrayList<ClientJournalEntry>();
		List<JournalEntry> serverJournalEntries = null;
		try {

			serverJournalEntries = new FinanceTool().getJournalEntries(context
					.getCompany().getID());
			for (JournalEntry journalEntry : serverJournalEntries) {
				clientJournalEntries
						.add(new ClientConvertUtil().toClientObject(
								journalEntry, ClientJournalEntry.class));
			}
			// journalEntry = (List<ClientJournalEntry>)
			// manager.merge(journalEntry);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientJournalEntry>(clientJournalEntries);
	}

}
