package com.vimukti.accounter.result;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ResultJsonDeserializer implements JsonDeserializer<Result> {

	@Override
	public Result deserialize(JsonElement json, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		Result result = new Result();
		JsonObject jsonObject = json.getAsJsonObject();
		JsonElement jsonElement3 = jsonObject.get("cookie");
		if (jsonElement3 != null) {
			result.setCookie(jsonElement3.getAsString());
		}
		JsonElement title = jsonObject.get("title");
		if (title != null) {
			result.setTitle(title.getAsString());
		}

		result.setHideCancel(jsonObject.get("hideCancel").getAsBoolean());
		result.setShowBack(jsonObject.get("showBack").getAsBoolean());

		JsonArray jresultParts = jsonObject.get("resultParts").getAsJsonArray();
		List<Object> resultParts = result.getResultParts();
		for (int i = 0; i < jresultParts.size(); i++) {
			JsonObject asJsonObject = jresultParts.get(i).getAsJsonObject();
			int type = asJsonObject.get("type").getAsInt();
			switch (type) {
			case 0:
				String asString = asJsonObject.get("message").getAsString();
				resultParts.add(asString);
				break;
			case 1:
				ResultList list = new ResultList();
				list.setMultiSelection(asJsonObject.get("isMultiSelection")
						.getAsBoolean());
				JsonElement resultTitle = asJsonObject.get("title");
				if (resultTitle != null) {
					list.setTitle(resultTitle.getAsString());
				}
				JsonArray asJsonArray = asJsonObject.get("records")
						.getAsJsonArray();
				List<Record> records = list.getRecords();
				for (int j = 0; j < asJsonArray.size(); j++) {
					JsonObject asJsonObject2 = asJsonArray.get(j)
							.getAsJsonObject();
					Record record = new Record();
					String code = asJsonObject2.get("code").getAsString();
					record.setCode(code);
					JsonArray asJsonArray2 = asJsonObject2.get("cells")
							.getAsJsonArray();
					List<Cell> cells = record.getCells();
					for (int k = 0; k < asJsonArray2.size(); k++) {
						JsonObject asJsonObject3 = asJsonArray2.get(k)
								.getAsJsonObject();
						Cell cell = new Cell();
						cell.setTitle(asJsonObject3.get("title").getAsString());
						cell.setValue(asJsonObject3.get("value").getAsString());
						cells.add(cell);
					}
					records.add(record);
				}
				resultParts.add(list);
				break;
			case 2:
				JsonElement jsonElement2 = asJsonObject.get("commandNames");
				CommandList commandList = new CommandList();
				List<Command> commandNames = commandList.getCommandNames();
				JsonArray asJsonArray2 = jsonElement2.getAsJsonArray();
				for (int j = 0; j < asJsonArray2.size(); j++) {
					JsonObject asJsonObject2 = asJsonArray2.get(j)
							.getAsJsonObject();
					Command command = new Command();
					command.setName(asJsonObject2.get("name").getAsString());
					command.setCode(asJsonObject2.get("code").getAsString());
					commandNames.add(command);
				}
				resultParts.add(commandList);
				break;
			case 3:
				int input = asJsonObject.get("inputType").getAsInt();
				String name = asJsonObject.get("name").getAsString();
				InputType inputType = new InputType(input, name);
				inputType.setValue(asJsonObject.get("value").getAsString());
				resultParts.add(inputType);
				break;
			default:
				break;
			}
		}

		return result;
	}
}
