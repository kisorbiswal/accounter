package com.vimukti.accounter.web.client.core;

import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.MakeDepositView;
import com.vimukti.accounter.web.client.ui.company.JournalEntryView;

public class AddButton extends ImageButton {

	private AbstractBaseView<?> view;

	public AddButton(AbstractBaseView<?> baseView) {
		super(Accounter.constants().add(), Accounter.getFinanceImages()
				.addIcon());
		this.view = baseView;
		if (this.view instanceof MakeDepositView)
			this.setTitle(Accounter.messages().clickToAddContact(
					baseView.getAction().getViewName()).replace(
					Accounter.constants().contact(),
					Accounter.constants().transfer()));
		else if (this.view instanceof JournalEntryView)
			this.setTitle(Accounter.messages().clickToAddContact(
					baseView.getAction().getViewName()).replace(
					Accounter.constants().contact(),
					Accounter.constants().transaction()));
		else
			this.setTitle(Accounter.messages().clickToAddContact(
					baseView.getAction().getViewName()));
	}
}
