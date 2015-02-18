package com.vimukti.accounter.migration;

import org.json.JSONObject;

public interface IMigrator<T> {

	JSONObject migrate(T obj, MigratorContext context);

}
