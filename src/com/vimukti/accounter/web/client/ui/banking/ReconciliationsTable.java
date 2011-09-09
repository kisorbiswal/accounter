/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import java.util.List;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconciliationsTable extends CellTable<ClientReconciliation> {

	private ListDataProvider<ClientReconciliation> dataprovider = new ListDataProvider<ClientReconciliation>();

	/**
	 * Creates new Instance
	 */
	public ReconciliationsTable() {
		initColumns();
		dataprovider.addDataDisplay(this);
	}

	/**
	 * 
	 */
	private void initColumns() {
		TextColumn<ClientReconciliation> reconciliationDate = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {
				return object.getReconcilationDate().toString();
			}
		};

		TextColumn<ClientReconciliation> reconciliationPeriod = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {
				return Accounter.messages().to(
						object.getStartDate().toString(),
						object.getEndDate().toString());
			}
		};

		Column<ClientReconciliation, SafeHtml> reconciliationDetails = new Column<ClientReconciliation, SafeHtml>(
				new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(final ClientReconciliation object) {
				SafeHtml html = new SafeHtml() {

					@Override
					public String asString() {
						Anchor show = new Anchor("Show");
						show.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								openReconciliation(object);
							}

						});
						Anchor undo = new Anchor("Undo");
						undo.addClickHandler(new ClickHandler() {

							@Override
							public void onClick(ClickEvent event) {
								deleteReconciliation(object);
							}

						});

						return "<div>"
								+ Accounter.messages().to(
										object.getStartDate().toString(),
										object.getEndDate().toString())
								+ "<br>" + show + "  " + undo + "</div>";
					}
				};
				return html;
			}
		};

		TextColumn<ClientReconciliation> openingBalance = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {
				return String.valueOf(object.getOpeningBalance());
			}
		};

		TextColumn<ClientReconciliation> closingBalance = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {
				return String.valueOf(object.getClosingBalance());
			}
		};

		this.addColumn(reconciliationDate, Accounter.constants()
				.ReconciliationDate());
		this.addColumn(reconciliationDetails, Accounter.constants()
				.ReconciliationPeriod());
		this.addColumn(openingBalance, Accounter.constants().openBalance());
		this.addColumn(closingBalance, Accounter.constants().ClosingBalance());
	}

	private void openReconciliation(ClientReconciliation object) {
		// TODO Auto-generated method stub

	}

	private void deleteReconciliation(ClientReconciliation object) {
		// TODO Auto-generated method stub

	}

	public void setData(List<ClientReconciliation> data) {
		this.dataprovider.setList(data);
	}
}
