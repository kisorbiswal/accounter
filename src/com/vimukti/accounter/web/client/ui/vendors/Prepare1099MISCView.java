/**
 * 
 */
package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.Client1099Form;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

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

	@SuppressWarnings("unchecked")
	public void createControl() {

		// DecoratedStackPanel root = new DecoratedStackPanel();
		// root.setWidth("1000px");
		// root.setHeight("300px");
		SetUp setUp = new SetUp("");
		// root.add(setUp, "Add Vendor and Accounts", true);

		Preview1099 preview1099 = new Preview1099("");
		// root.add(preview1099, "Preview 1099 and 1096 Information", true);
		// root1.addItem(get1099InformationGrid());

		PrintSetUp printSetUp = new PrintSetUp("");
		// root.add(printSetUp, "Print Alignment and Setup", true);

		DisclosurePanel advancedDisclosure = new DisclosurePanel(
				"Print Alignment and Setup");
		advancedDisclosure.setAnimationEnabled(true);
		advancedDisclosure.setContent(printSetUp);

		// this.add(root);
		this.add(setUp);
		this.add(preview1099);
		this.add(advancedDisclosure);
		EndButtons endButtons = new EndButtons("");
		this.add(endButtons);

	}

	public static CellTable<Client1099Form> get1099InformationGrid() {
		CellTable<Client1099Form> cellTable = new CellTable<Client1099Form>();

		CheckboxCell checkboxCell = new CheckboxCell();
		Column<Client1099Form, Boolean> checkBoxColumn = new Column<Client1099Form, Boolean>(
				checkboxCell) {

			@Override
			public Boolean getValue(Client1099Form object) {
				return object.isSelected();
			}
		};

		ClickableTextCell informationLink = new ClickableTextCell();

		Column<Client1099Form, String> informationColumn = new Column<Client1099Form, String>(
				informationLink) {

			@Override
			public String getValue(Client1099Form object) {
				return "Information";
			}
		};
		informationColumn.setFieldUpdater(new FieldUpdater() {

			@Override
			public void update(int index, Object object, Object value) {
				ActionFactory.getNewVendorAction().run(null, false);
			}
		});

		Column<Client1099Form, String> total1099PaymentsCell = new Column<Client1099Form, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(Client1099Form object) {
				return "" + object.getTotal1099Payments();
			}
		};

		Column<Client1099Form, String> totalAllPaymentsCell = new Column<Client1099Form, String>(
				new ClickableTextCell()) {

			@Override
			public String getValue(Client1099Form object) {
				return "" + object.getTotalAllPayments();
			}
		};

		ArrayList<Client1099Form> arrayList = new ArrayList<Client1099Form>();
		arrayList.add(new Client1099Form());
		cellTable.addColumn(checkBoxColumn, "Select");
		cellTable.addColumn(informationColumn, Accounter.messages()
				.vendorInformation(Global.get().Vendor()));
		cellTable.addColumn(total1099PaymentsCell, Accounter.constants()
				.total1099Payments());
		cellTable.addColumn(totalAllPaymentsCell, Accounter.constants()
				.totalAllPayments());

		cellTable.setRowCount(arrayList.size());

		cellTable.setRowData(0, arrayList);

		return cellTable;
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
			panel.setSize("1000px", "200px");
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
				SelectVendorsTo1099Dialog selectVendorsTo1099Dialog = new SelectVendorsTo1099Dialog(
						Accounter.messages()
								.selectVendor(Global.get().Vendor()), Accounter
								.messages().SelectVendorsToTrack1099(
										Global.get().Vendor()));
				selectVendorsTo1099Dialog.show();
			}
			if (sender == addAccount) {
				String assignAccounts = Accounter.messages().assignAccounts(
						Global.get().Account());
				AssignAccountsTo1099Dialog assignAccountsTo1099Dialog = new AssignAccountsTo1099Dialog(
						assignAccounts, assignAccounts);
				assignAccountsTo1099Dialog.show();
			}
		}
	}

	private static class Preview1099 extends Composite implements ClickHandler {

		Label clientInfo = new Label("Company Information");
		Label einInfo = new Label("EIN");

		TextArea clientInfoText = new TextArea();
		TextArea einInfoText = new TextArea();

		public Preview1099(String caption) {

			VerticalPanel panelR = new VerticalPanel();
			VerticalPanel panelL = new VerticalPanel();
			panelL.add(clientInfo);
			panelL.add(clientInfoText);

			panelR.add(einInfo);
			panelR.add(einInfoText);

			HorizontalPanel panel = new HorizontalPanel();
			panel.setSize("1000px", "100px");
			panel.add(panelL);
			panel.add(panelR);

			VerticalPanel panel1 = new VerticalPanel();
			panel1.add(panel);
			panel1.add(get1099InformationGrid());

			// All composites must call initWidget() in their constructors.
			initWidget(panel1);
		}

		public void onClick(ClickEvent event) {
			Object sender = event.getSource();
		}

	}

	private static class PrintSetUp extends Composite implements ClickHandler {

		Button printSample = new Button("Print Sample");
		Label blankLabel = new Label("Load empty paper");
		Label adjustLabel = new Label(
				"	Enter adjustments to move text 1/100th of an inch.");

		Label verLabel = new Label("Vertical");
		Label horLabel = new Label("Horizantal");

		Label infoLabel = new Label(
				"	Alignment adjustment values are saved when you click Print Sample (above), or Print (below).If you print forms on more than one printer, write down the alignment values for each.");

		public PrintSetUp(String caption) {
			// Place the check above the text box using a vertical panel.

			// HorizontalPanel panel1 = new HorizontalPanel();
			// panel1.add(verLabel);
			// panel1.add(getListBox(true));
			//
			// HorizontalPanel panel2 = new HorizontalPanel();
			// panel2.add(horLabel);
			// panel2.add(getListBox(true));
			//
			// VerticalPanel panel = new VerticalPanel();
			// panel.setHeight("400px");
			// panel.add(blankLabel);
			// panel.add(printSample);
			// panel.add(adjustLabel);
			// panel.add(panel1);
			// panel.add(panel2);
			// panel.add(infoLabel);

			Grid advancedOptions = new Grid(6, 2);
			advancedOptions.setCellSpacing(6);
			advancedOptions.setWidget(0, 0, blankLabel);
			advancedOptions.setHTML(0, 1, "");
			advancedOptions.setWidget(1, 0, printSample);
			advancedOptions.setHTML(1, 1, "");
			advancedOptions.setWidget(2, 0, adjustLabel);
			advancedOptions.setHTML(2, 1, "");
			advancedOptions.setWidget(3, 0, verLabel);
			advancedOptions.setWidget(3, 1, getListBox(true));
			advancedOptions.setWidget(4, 0, horLabel);
			advancedOptions.setWidget(4, 1, getListBox(true));
			advancedOptions.setWidget(5, 0, infoLabel);
			advancedOptions.setHTML(5, 1, "");

			printSample.addClickHandler(this);

			// All composites must call initWidget() in their constructors.
			initWidget(advancedOptions);
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

	static ListBox getListBox(boolean dropdown) {
		ListBox widget = new ListBox();
		widget.addStyleName("demo-ListBox");
		widget.addItem("One");
		widget.addItem("Two");
		widget.addItem("Three");
		widget.addItem("Four");
		widget.addItem("Five");
		if (!dropdown)
			widget.setVisibleItemCount(3);
		return widget;
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
