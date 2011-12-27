package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * this class is used to display all the templetes in a specified directory.
 * 
 * @author vimukti28
 * 
 */
public class TemplateCombo extends CustomCombo {

	private String compareText;

	public TemplateCombo(String title, String compareText) {
		super(title, false, 1);

		this.compareText = compareText;
		initCombo(getTempletes());
	}

	public ArrayList<String> getTempletes() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(Accounter.messages().classicTemplate());
		list.add(Accounter.messages().ModernTemplate());
		list.add(Accounter.messages().PlainTemplate());
		list.add(Accounter.messages().ProfessionalTemplate());

		// File dir = new File("../templetes");
		//
		// String[] children = dir.list();
		// if (children != null) {
		//
		// for (int i = 0; i < children.length; i++) {
		//
		// String filename = children[i];
		//
		// int indexOf = filename.indexOf(compareText);
		// if (indexOf > 0) {
		// filename = filename.substring(0, indexOf) + " Template";
		//
		// list.add(filename);
		// }
		// }
		// }
		return list;
	}

	@Override
	protected String getDisplayName(Object object) {
		if (object != null)
			return object.toString() != null ? object.toString() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(Object object, int col) {
		switch (col) {
		case 0:
			return object.toString();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return Accounter.messages().Template();
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub

	}
}
