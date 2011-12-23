package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientAttachment;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class AttachmentTable extends EditTable<ClientAttachment> {

	public AttachmentTable() {
	}

	private void initTableColumns() {
		AnchorEditColumn<ClientAttachment> attachmentNameColumn = new AnchorEditColumn<ClientAttachment>() {

			@Override
			public String getValue(ClientAttachment object) {
				return object.getName();
			}

			@Override
			protected boolean isEnable(ClientAttachment row) {
				return true;
			}

			@Override
			protected void onClick(ClientAttachment row) {

				UIUtils.downloadTransactionAttachment(row.getAttachmentId(),
						row.getName());
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().attachmentName();
			}
		};

		TextAreaEditColumn<ClientAttachment> attachmentSizeColumn = new TextAreaEditColumn<ClientAttachment>() {

			@Override
			public String getValue(ClientAttachment object) {
				return String.valueOf(object.getSize());
			}

			@Override
			protected void setValue(ClientAttachment row, String value) {

			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().attachmentSize();
			}
		};

		TextAreaEditColumn<ClientAttachment> creatorNameCol = new TextAreaEditColumn<ClientAttachment>() {

			@Override
			public String getValue(ClientAttachment object) {
				ClientUserInfo userById = Accounter.getCompany().getUserById(
						object.getCreatedBy());
				return userById.getName();
			}

			@Override
			protected void setValue(ClientAttachment row, String value) {

			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().creatorName();
			}
		};

		DeleteColumn<ClientAttachment> deleteAttachmentCol = new DeleteColumn<ClientAttachment>();

		this.addColumn(attachmentNameColumn);

		this.addColumn(attachmentSizeColumn);

		this.addColumn(creatorNameCol);

		this.addColumn(deleteAttachmentCol);

	}

	@Override
	protected void initColumns() {
		initTableColumns();
	}

	@Override
	protected boolean isInViewMode() {
		return false;
	}

}
