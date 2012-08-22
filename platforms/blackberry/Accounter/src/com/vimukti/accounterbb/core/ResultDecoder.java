package com.vimukti.accounterbb.core;

import java.util.Vector;


import com.vimukti.accounterbb.result.Cell;
import com.vimukti.accounterbb.result.Command;
import com.vimukti.accounterbb.result.CommandList;
import com.vimukti.accounterbb.result.InputType;
import com.vimukti.accounterbb.result.Record;
import com.vimukti.accounterbb.result.Result;
import com.vimukti.accounterbb.result.ResultList;
import com.vimukti.json.me.JSONArrayVimukti;
import com.vimukti.json.me.JSONExceptionVimukti;
import com.vimukti.json.me.JSONObjectVimukti;

/**
 * this class is used to Decode the data received from the server
 * 
 */
public class ResultDecoder {

	private JSONObjectVimukti jsonObject;

	ResultDecoder(JSONObjectVimukti jsonObject) {
		this.jsonObject = jsonObject;
	}

	public Result decode() throws JSONExceptionVimukti {

		Result result = new Result();
		try {
			Object cookieObj = jsonObject.get("cookie");
			if (cookieObj != null) {
				result.setCookie(cookieObj.toString());
			}

		} catch (Exception e) {
		}

		try {
			Object titleObj = jsonObject.get("title");
			if (titleObj != null) {
				result.setTitle(titleObj.toString());
			}
		} catch (Exception e) {
		}
		try {

			boolean isShowBack = jsonObject.getBoolean("showBack");
			result.setShowBack(isShowBack);

			boolean ishideCancel = jsonObject.getBoolean("hideCancel");
			result.setHideCancel(ishideCancel);

		} catch (Exception e) {
		}

		JSONArrayVimukti jresultParts = (JSONArrayVimukti) jsonObject.get("resultParts");
		Vector resultParts = result.getResultParts();
		for (int i = 0; i < jresultParts.length(); i++) {
			Object object = jresultParts.get(i);
			JSONObjectVimukti jsonObject = (JSONObjectVimukti) object;
			Object typeObj = jsonObject.get("type");
			int type = Integer.parseInt(typeObj.toString());
			switch (type) {
			case 0:
				String asString = jsonObject.getString("message");
				resultParts.addElement(asString);
				break;

			case 1:

				// ResultList
				ResultList list = new ResultList();
				list.setMultiSelection(jsonObject
						.getBoolean("isMultiSelection"));
				// list.setName(jsonElement.getString("name"));

				// Object titleObj = jsonObject.get("title");
				// if (titleObj != null) {
				// list.setTitle(titleObj.toString());
				// }

				JSONArrayVimukti asJsonArray = (JSONArrayVimukti) jsonObject.get("records");
				Vector records = list.getRecords();
				for (int j = 0; j < asJsonArray.length(); j++) {
					JSONObjectVimukti asJsonObject2 = (JSONObjectVimukti) asJsonArray.get(j);
					Record record = new Record();
					String codeStr = asJsonObject2.getString("code");
					record.setCode(codeStr);
					JSONArrayVimukti asJsonArray2 = (JSONArrayVimukti) asJsonObject2
							.get("cells");
					Vector cells = record.getCells();
					for (int k = 0; k < asJsonArray2.length(); k++) {
						JSONObjectVimukti asJsonObject3 = (JSONObjectVimukti) asJsonArray2
								.get(k);
						Cell cell = new Cell();
						cell.setTitle(asJsonObject3.getString("title"));
						cell.setValue(asJsonObject3.getString("value"));
						cells.addElement(cell);
					}
					records.addElement(record);
				}
				resultParts.addElement(list);
				break;

			case 2:

				Object object2 = jsonObject.get("commandNames");
				if (object2 != null) {
					// CommandList
					CommandList commandList = new CommandList();
					Vector commandNames = commandList.getCommandNames();
					JSONArrayVimukti jsonArray = (JSONArrayVimukti) object2;
					for (int j = 0; j < jsonArray.length(); j++) {
						JSONObjectVimukti asJsonObject2 = (JSONObjectVimukti) jsonArray
								.get(j);
						Command command = new Command();
						command.setName(asJsonObject2.getString("name"));
						command.setCode(asJsonObject2.getString("code"));
						commandNames.addElement(command);
					}
					resultParts.addElement(commandList);
				}

				break;

			case 3:

				int inputType = jsonObject.getInt("inputType");
				Object nameObj = jsonObject.get("name");
				Object valueObj;
				try {
					valueObj = jsonObject.get("value");
				} catch (Exception e) {
					valueObj = null;
				}

				String name = "";
				String value = "";
				if (nameObj == null) {
					name = "";
				}
				if (valueObj == null) {
					value = "";
				}
				InputType inputTypeObj = new InputType(inputType, name, value);
				resultParts.addElement(inputTypeObj);
				break;

			default:
				break;
			}

		}
		return result;
	}
}
