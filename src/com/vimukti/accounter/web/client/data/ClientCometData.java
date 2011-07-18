package com.vimukti.accounter.web.client.data;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This is the object used to send data from server to client through comet.
 * 
 * @author Uma
 * 
 */
public class ClientCometData implements IsSerializable, Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This type of objects will be send to client
	 * 
	 */
	public static final int TASKNOTE = 1;
	public static final int CONTACT = 2;
	public static final int IDENTITY = 3;

	public static final int DOCUMENT = 4;
	public static final int MESSAGE = 5;
	public static final int NOTEPAD = 6;
	public static final int DISSCUSSION = 7;
	public static final int WORKSPACE = 8;
	public static final int EVENT = 9;
	public static final int WORKFLOW = 10;
	public static final int PERSONALUSER = 11;
	public static final int USER = 12;
	public static final int PICTURE = 13;
	public static final int DASHBOARD = 14;
	public static final int WORKSPACEMEMBER = 15;
	public static final int HR = 16;
	public static final int CAMPAIGN = 17;
	public static final int MARKETINGLIST = 18;
	public static final int TEMPLATE = 19;
	public static final int EMAILACCOUNT = 20;
	public static final int CALLNOTES = 21;

	public static final int UNREADOBJECT = 22;

	/**
	 * This are the actions can be done by user
	 */
	public static final short CREATE_ACTION = 1;
	public static final short DELETE_ACTION = 2;
	public static final short EDIT_ACTION = 3;
	public static final short REMINDER = 4;

	/**
	 * Which type object is it? it can be Tasknote, document etc.
	 */
	public int objectType;
	/**
	 * Which action has done by user
	 */
	public short actionType;
	/**
	 * if action is done in space then spaceiD will be their else it is null.
	 */
	public String spaceID;
	/**
	 * Actual object should be send to client.
	 */
	public Serializable object;
	/**
	 * objectID
	 */
	public String objectID;

	public ClientCometData() {
	}

	public ClientCometData(int objectType, short actionType, String spaceID,
			Serializable object, String objectID) {
		this.objectType = objectType;
		this.actionType = actionType;
		this.spaceID = spaceID;
		this.object = object;
		this.objectID = objectID;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TYPE: ");
		sb.append(getObjectTypeAsString());
		sb.append(", ACTION: ");
		sb.append(getActionTypeAsString());
		sb.append(", OBJ_ID: ");
		sb.append(objectID);
		sb.append(", OBJ: ");
		sb.append(object);
		return sb.toString();
	}

	private String getObjectTypeAsString() {
		switch (objectType) {
		case TASKNOTE:
			return "TASKNOTE";
		case CONTACT:
			return "CONTACT";
		case DOCUMENT:
			return "DOCUMENT";
		case MESSAGE:
			return "MESSAGE";
		case NOTEPAD:
			return "NOTEPAD";
		case DISSCUSSION:
			return "DISSCUSSION";
		case WORKSPACE:
			return "WORKSPACE";
		case EVENT:
			return "EVENT";
		case WORKFLOW:
			return "WORKFLOW";
		case PERSONALUSER:
			return "PERSONALUSER";
		case USER:
			return "USER";
		case PICTURE:
			return "PICTURE";
		case DASHBOARD:
			return "DASHBOARD";
		case WORKSPACEMEMBER:
			return "WORKSPACEMEMBER";
		case HR:
			return "HR";
		case CAMPAIGN:
			return "CAMPAIGN";
		case MARKETINGLIST:
			return "MARKETINGLIST";
		case TEMPLATE:
			return "TEMPLATE";
		case EMAILACCOUNT:
			return "EMAILACCOUNT";
		default:
			return "";
		}

	}

	private String getActionTypeAsString() {
		switch (actionType) {
		case CREATE_ACTION:
			return "CREATE_ACTION";
		case DELETE_ACTION:
			return "DELETE_ACTION";
		case EDIT_ACTION:
			return "EDIT_ACTION";
		case REMINDER:
			return "REMINDER";
		default:
			return "";
		}
	}

}
