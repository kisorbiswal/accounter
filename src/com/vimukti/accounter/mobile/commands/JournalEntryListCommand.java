package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;

public class JournalEntryListCommand extends AbstractTransactionCommand {

	private static final int NO_OF_JOURNAL_ENTRY_TO_SHOW = 5;

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
		int num = 0;
		List<JournalEntry> entryList = getJournalEntryList();
		for (JournalEntry entry : entryList) {
			userList.add(createJournalRecord(entry));
			num++;
			if (num == NO_OF_JOURNAL_ENTRY_TO_SHOW) {
				break;
			}
		}

		result.add(userList);

		CommandList command = new CommandList();
		command.add("Create");

		result.add(command);

		return result;
	}

	private Record createJournalRecord(JournalEntry entry) {
		Record record = new Record(entry);
		record.add("Voucher No", entry.getNumber());
		record.add("Date Created", entry.getDate());
		return record;
	}

	private List<JournalEntry> getJournalEntryList() {
		// TODO need to get JournalEntryList
		return null;
	}

}
