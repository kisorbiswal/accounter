/**
 * 
 */
package com.vimukti.accounter.web.client.ui.vendors;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;

public class Prepare1099MISCView extends AbstractBaseView {
	@Override
	public void init() {
		// TODO Auto-generated method stub
		this.createControl();

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	public void createControl() {

		TreeItem root = new TreeItem("Set up Vendor and Accounts");
		SetUp setUp = new SetUp("");
		root.addItem(setUp);

		TreeItem root1 = new TreeItem("Preview 1099 and 1096 Information");
		root.setState(true);
		Preview otb = new Preview("");
		root1.addItem(otb);

		TreeItem root2 = new TreeItem("Print Alignment and Setup");
		PrintSetUp printSetUp = new PrintSetUp("");
		root2.addItem(printSetUp);

		Tree t = new Tree();
		t.addItem(root);
		t.addItem(root1);
		t.addItem(root2);

		this.add(t);
		EndButtons endButtons = new EndButtons("");
		this.add(endButtons);

	}

	private static class Preview extends Composite implements ClickHandler {

		private TextBox textBox = new TextBox();
		private CheckBox checkBox = new CheckBox();

		/**
		 * Constructs an OptionalTextBox with the given caption on the check.
		 * 
		 * @param caption
		 *            the caption to be displayed with the check box
		 */
		public Preview(String caption) {
			// Place the check above the text box using a vertical panel.

			Grid g = new Grid(5, 5);

			// Put some values in the grid cells.
			for (int row = 0; row < 5; ++row) {
				for (int col = 0; col < 5; ++col)
					g.setText(row, col, "" + row + ", " + col);
			}

			// Just for good measure, let's put a button in the center.
			g.setWidget(2, 2, new Button("Does nothing, but could"));

			// You can use the CellFormatter to affect the layout of the grid's
			// cells.
			g.getCellFormatter().setWidth(0, 2, "256px");

			VerticalPanel panel = new VerticalPanel();
			panel.add(g);
			panel.add(checkBox);
			panel.add(textBox);

			// Set the check box's caption, and check it by default.
			checkBox.setText(caption);
			checkBox.setChecked(true);
			checkBox.addClickHandler(this);

			// All composites must call initWidget() in their constructors.
			initWidget(panel);

		}

		public void onClick(ClickEvent event) {
			Object sender = event.getSource();
			if (sender == checkBox) {
				// When the check box is clicked, update the text box's enabled
				// state.
				textBox.setEnabled(checkBox.isChecked());
			}
		}

		/**
		 * Sets the caption associated with the check box.
		 * 
		 * @param caption
		 *            the check box's caption
		 */
		public void setCaption(String caption) {
			// Note how we use the use composition of the contained widgets to
			// provide
			// only the methods that we want to.
			checkBox.setText(caption);
		}

		/**
		 * Gets the caption associated with the check box.
		 * 
		 * @return the check box's caption
		 */
		public String getCaption() {
			return checkBox.getText();
		}
	}

	private static class SetUp extends Composite implements ClickHandler {

		Button setVendor = new Button("Select Vendor");
		Button addAccount = new Button("Assign Account");
		Label infoLable = new Label(
				"Before paying vendors this year, select vendors and assign accounts. These setup tasks ensure that the forms you file next year will be correct. If you have already made vendor payments this year, you may need to revise them to assign them to the proper accounts.");

		public SetUp(String caption) {

			infoLable.setWordWrap(true);
			infoLable.setHorizontalAlignment(ALIGN_JUSTIFY);

			VerticalPanel panel = new VerticalPanel();
			panel.add(infoLable);
			panel.add(setVendor);
			panel.add(addAccount);

			setVendor.addClickHandler(this);
			addAccount.addClickHandler(this);

			// All composites must call initWidget() in their constructors.
			initWidget(panel);
		}

		public void onClick(ClickEvent event) {
			Object sender = event.getSource();
			if (sender == setVendor) {

			}
			if (sender == addAccount) {

			}
		}

	}

	private static class PrintSetUp extends Composite implements ClickHandler {

		private Button printSample = new Button("Print Sample");

		public PrintSetUp(String caption) {
			// Place the check above the text box using a vertical panel.
			HorizontalPanel panel = new HorizontalPanel();
			panel.add(printSample);
			printSample.addClickHandler(this);

			// All composites must call initWidget() in their constructors.
			initWidget(panel);
		}

		public void onClick(ClickEvent event) {
			Object sender = event.getSource();
			if (sender == printSample) {

			}
		}

	}

	private static class EndButtons extends Composite implements ClickHandler {

		private Button print1099 = new Button("Print on 1099 Form.");
		private Button printInfo = new Button("Print on Information Sheet.");
		private Button cancel = new Button("Cancel");

		public EndButtons(String caption) {
			// Place the check above the text box using a vertical panel.
			HorizontalPanel panel = new HorizontalPanel();
			panel.add(print1099);
			panel.add(printInfo);
			panel.add(cancel);

			print1099.addClickHandler(this);
			printInfo.addClickHandler(this);
			cancel.addClickHandler(this);

			// All composites must call initWidget() in their constructors.
			initWidget(panel);
		}

		public void onClick(ClickEvent event) {
			Object sender = event.getSource();
			if (sender == print1099) {

			}
			if (sender == printInfo) {

			}
			if (sender == cancel) {

			}
		}

	}

	private static class CellTableExample extends Composite implements
			ClickHandler {

		// A simple data type that represents a contact.
		private static class Contact {
			private final String address;
			private final String name;

			public Contact(String name, String address) {
				this.name = name;
				this.address = address;
			}
		}

		static List<Contact> CONTACTS = Arrays
				.asList(new Contact(
						"MyPub 504, KRANTI CLASSIC APT,Prashanti Nagar, ECIL Post, KAPRA HYDERABAD, WY 50012 (998)969-6512",
						"123 Fourth Road"), new Contact("Mary",
						"222 Lancer Lane"), new Contact("Mary",
						"222 Lancer Lane"), new Contact("Mary",
						"222 Lancer Lane"), new Contact("Mary",
						"222 Lancer Lane"), new Contact("Mary",
						"222 Lancer Lane"));

		public CellTableExample(String caption) {

			CellTable<Contact> table = new CellTable<Contact>();

			// Create name column.
			TextColumn<Contact> nameColumn = new TextColumn<Contact>() {
				@Override
				public String getValue(Contact contact) {
					return contact.name;
				}

			};

			// Create address column.
			TextColumn<Contact> addressColumn = new TextColumn<Contact>() {
				@Override
				public String getValue(Contact contact) {
					return contact.address;
				}
			};

			TextArea area = new TextArea();
			area.setText("qweqweqweqw");

			table.setWidth("100%", true);
			table.setColumnWidth(nameColumn, 30.0, Unit.PCT);
			table.setColumnWidth(addressColumn, 30.0, Unit.PCT);
			// Add the columns.
			table.addColumn(nameColumn, "Company Information");
			table.addColumn(addressColumn, "EIN");

			// Set the total row count. This isn't strictly necessary, but it
			// affects
			// paging calculations, so its good habit to keep the row count up
			// to date.
			table.setRowCount(CONTACTS.size(), true);

			// Push the data into the widget.
			table.setRowData(0, CONTACTS);

			initWidget(table);
		}

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

}
