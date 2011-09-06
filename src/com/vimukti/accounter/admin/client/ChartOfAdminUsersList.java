package com.vimukti.accounter.admin.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;

public class ChartOfAdminUsersList extends CellTable<ClientAdminUser> {
	List<ClientAdminUser> userlistList;

	ChartOfAdminUsersList() {
		createControls();
	}

	private void createControls() {
		userlistList = getUsersList();
		ListDataProvider<ClientAdminUser> dataProvider = new ListDataProvider<ClientAdminUser>(
				userlistList);
		dataProvider.addDataDisplay(this);
		initTableColumns();

	}

	private List<ClientAdminUser> getUsersList() {
		List<ClientAdminUser> userNames = new ArrayList<ClientAdminUser>();
		for (int i = 0; i <= 10; i++) {
			ClientAdminUser cAdminUser = new ClientAdminUser();
			cAdminUser.setId(1 + i);
			cAdminUser.setEmailId(i + "dummay@email.com");
			cAdminUser.setStatus(i + "activated");
			cAdminUser.setTypeOfUser(1);
			userNames.add(cAdminUser);
		}
		return userNames;
	}

	private void initTableColumns() {

		TextColumn<ClientAdminUser> serialNo = new TextColumn<ClientAdminUser>() {

			@Override
			public String getValue(ClientAdminUser object) {
				return String.valueOf(object.getId());
			}
		};

		TextColumn<ClientAdminUser> email = new TextColumn<ClientAdminUser>() {

			@Override
			public String getValue(ClientAdminUser object) {
				return String.valueOf(object.getEmailId());
			}
		};
		TextColumn<ClientAdminUser> status = new TextColumn<ClientAdminUser>() {

			@Override
			public String getValue(ClientAdminUser object) {
				return String.valueOf(object.getStatus());
			}
		};

		TextColumn<ClientAdminUser> type = new TextColumn<ClientAdminUser>() {

			@Override
			public String getValue(ClientAdminUser object) {
				return String.valueOf(object.getTypeOfUser());
			}
		};

		this.addColumn(serialNo, "S.no");
		this.addColumn(email, "Email");
		this.addColumn(status, "Status");
		this.addColumn(type, "Type");

	}

}
