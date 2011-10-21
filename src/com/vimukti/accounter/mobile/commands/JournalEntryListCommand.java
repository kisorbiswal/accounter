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
				return closeCommand();
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
		ResultList actions = new ResultList("actions");

		ResultList userList = new ResultList("journalEntryList");
		List<ClientJournalEntry> entryList = getJournalEntries(context);
		for (ClientJournalEntry entry : entryList) {
			userList.add(createJournalRecord(entry));

		}

		result.add(userList);

		CommandList command = new CommandList();
		command.add(getConstants().addNewJournalEntry());

		result.add(command);
		Record finishRecord = new Record(ActionNames.FINISH);
		finishRecord.add("", getConstants().close());
		actions.add(finishRecord);

		result.add(actions);
		return result;
	}

	private Record createJournalRecord(ClientJournalEntry entry) {
		Record record = new Record(entry);
		record.add("", getConstants().voucherNo());
		record.add("", entry.getNumber());
		record.add("", getConstants().date());
		record.add("", entry.getDate());
		record.add("", getConstants().amount());
		record.add("", entry.getTotal());
		record.add("", getConstants().memo());
		record.add("", entry.getMemo());
		record.add("", getConstants().voided());
		record.add("", entry.isVoid() == true ? getConstants().Voided()
				: getConstants().nonVoided());

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
