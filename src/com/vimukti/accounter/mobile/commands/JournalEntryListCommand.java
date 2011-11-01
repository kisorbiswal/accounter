package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.ShowListRequirement;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.server.FinanceTool;

public class JournalEntryListCommand extends NewAbstractCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
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

		list.add(new ShowListRequirement<ClientJournalEntry>(getConstants()
				.journalEntryList(), "", 10) {

			@Override
			protected String onSelection(ClientJournalEntry value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return getConstants().journalEntryList();
			}

			@Override
			protected String getEmptyString() {
				return "There are no Journal entries";
			}

			@Override
			protected Record createRecord(ClientJournalEntry entry) {
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
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("Create JournalEntry");
			}

			@Override
			protected boolean filter(ClientJournalEntry e, String name) {
				return e.getName().toLowerCase().startsWith(name)
						|| e.getNumber().startsWith(
								"" + getNumberFromString(name));
			}

			;

			@Override
			protected List<ClientJournalEntry> getLists(Context context) {
				List<ClientJournalEntry> clientJournalEntries = new ArrayList<ClientJournalEntry>();
				List<JournalEntry> serverJournalEntries = null;
				try {
					serverJournalEntries = new FinanceTool()
							.getJournalEntries(context.getCompany().getID());
					for (JournalEntry journalEntry : serverJournalEntries) {
						clientJournalEntries.add(new ClientConvertUtil()
								.toClientObject(journalEntry,
										ClientJournalEntry.class));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return clientJournalEntries;
			}
		});

	}

}
