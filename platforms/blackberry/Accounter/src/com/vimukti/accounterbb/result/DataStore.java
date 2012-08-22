package com.vimukti.accounterbb.result;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

public class DataStore {
	private static DataStore dbStore = null;
	private Cookie data;
	private PersistentObject object;
	static final long PERSISTENT_STORE_ID = 0xeb263c815d29213aL;

	private DataStore() {
		object = PersistentStore.getPersistentObject(PERSISTENT_STORE_ID);
		synchronized (object) {
			if (object.getContents() == null) {
				object.setContents(new Cookie());
				object.commit();
			}
		}
		data = (Cookie) object.getContents();
	}

	/**
	 * It is instantiating ProjectStore if not exists.
	 * 
	 * @return
	 */
	public static synchronized DataStore getInstance() {
		if (dbStore == null) {
			return new DataStore();
		}
		return dbStore;
	}

	/**
	 * Save the setting information into ProjectStore.
	 * 
	 * @param defaultData
	 */
	public void saveData(Cookie defaultData) {
		data = defaultData;
		persist();
	}

	/**
	 * Retrieve the setting information from ProjectStore.
	 * 
	 * @param defaultData
	 */
	public Cookie getData() {
		if (data == null) {
			data = new Cookie();
			persist();
		}
		return data;
	}

	public void persist() {
		// We synchronize on our PersistentObject so that no other object can
		// acquire the lock before we finish our commit operation.
		synchronized (object) {
			object.setContents(data);
			PersistentObject.commit(object);
		}
	}
}
