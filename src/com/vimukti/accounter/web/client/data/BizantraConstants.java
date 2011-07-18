package com.vimukti.accounter.web.client.data;

public class BizantraConstants {

	/**
	 * Workflow schema
	 */

	public static final String DEFBIZ_WORKFLOW_SCHEMA = "workflowv1.7.dtd";

	public static final String SYSTEM_USER_ID = "9999999999999999999999999999999999999999";
	public static final String SYSTEM_DISAPLAYNAME = "Bizantra";
	public static final String SYSTEM_EMAILID = "bizantra@bizantra.com";

	public static final String HR_CATEGORYNAME = "HR22222222222222222222222";
	public static final String FINANCE_CATEGORYNAME = "FinanceBizantra2222222222";
	public static final String OPERATIONAL_CATEGORYNAME = "Operations";
	public static final String MARKETING_CATEGORYNAME = "MarketingBizantra22222222";
	public static final String SALES_CATEGORYNAME = "SalesBizantra222222222222";
	public static final String DEFBIZNETWORK_CATEGORYNAME = "Network";
	public static final String USERS_TAB_NAME = "Users";
	public static final String PURCHAGES_CATEGORYNAME = "PurchasingBizantra2222222";
	public static final String WORKFLOWS_TAB = "Taskflows";
	public static final String NETWORK_CATEGORYNAME = "Network";
	public static final String TASKNOTE_CATEGORYNAME = "TaskNotes";
	public static final String CONTACTS_CATEGORYNAME = "Contacts";
	public static final String DOCUMENTS_CACTEGORYNAME = "Documents";
	public static final String DASHBOARD_CATEGORYNAME = "Dashboard";
	public static final String MESSAGES_CATEGORYNAME = "Messages";
	public static final String CALENDER_CATEGORYNAME = "Calendar";
	public static final String WORKSPACE_CATEGORYNAME = "Shared Spaces";
	public static final String SUBSCRIPTION_MANAGMENT = "subscription_management";
	public static final String HELP_DASHBOARD = "?Dashboard.html";
	public static final String HELP_MESSAGES = "?Messages.html";
	public static final String HELP_DOCUMENTS = "?Documents.html";
	public static final String HELP_TASKS = "?Tasks.html";
	public static final String HELP_CONTACTS = "?ContactManagement.html";
	public static final String HELP_NOTES = "?Notes.html";
	public static final String HELP_CALENDER = "?Calendar.html";
	public static final String HELP_MARKETING = "?EmailMarketing.html";
	public static final String HELP_SALES = "?Sales.html";
	public static final String HELP_PURCHAGES = "?Purchasing.html";
	public static final String HELP_FINANCE = "?Finance.html";
	public static final String HELP_HR = "?HR.html";
	public static final String HELP_USERS = "?UserManagement.html";
	public static final String HELP_SHAREDSPACES = "?SharedSpaces.html";
	public static final String HELP_MYACCOUNT = "?MyAccount.html";
	public static final String HELP_SUBSCRIPTION = "?Subscriptions.html";

	/**
	 * Biznatra Acess
	 */
	public static final String FULL_ACESS = "Full Access";
	public static final String RESTRICTED_ACESS = "Restricted Access";
	public static final String NO_ACESS = "No Access";
	public static final String MANAGER_ACESS = "Manager Access";

	/**
	 * Bizantra Roles
	 */
	public static final String ADMIN_ROLE = "Admin";
	public static final String SUPER_USER_ROLE = "Super User";
	public static final String LITE_USER = "Lite User";
	public static final String FINANCE_LITE_USER = "Finance Lite User";
	public static final String FULL_USER = "Full User";
	public static final String USER_ROLE = "User";

	/**
	 * workspaces Roles
	 */
	public static final String MANAGER_ROLE = "Manager";
	public static final String GUEST_ROLE = "Guest";
	public static final String PARTICEPANT_ROLE = "Participant";

	public static final String USER_PREFERRED_DATE_FORMAT = "dd MMM, yyyy";

	/**
	 * This is used in converting date one time zone to another zone
	 */
	public static final String TIMEZONE_DEFAULT_FORMATE = "dd MMM, yyyy hh:mm:sss aaa";

	/**
	 * Dashboard Portlets
	 */

	public static final String MESSAGE_PORTLET = "messageStatus";

	public static final String TASK_PORTLET = "taskStatus";

	public static final String EVENT_PORTLET = "eventsStatus";

	public static final String WORKSPACE_CHANGES_PORTLET = "workspaceChangesStatus";

	public static final String WORKSPACE_TASK_PORTLET = "workspaceTaskStatus";

	public static final String WORKSPACE_EVENTS_PORTLET = "workspaceEventStatus";

	public static final String CUSTOMER_MANAGEMENT_PORTLET = "customerManagmentStatus";

	/**
	 * Task Default Category Names
	 */

	public static final String CONTACTMANAGEMENT = "Contact Management";

	public static final String MYTASKS = "My Tasks";

	// public static final String DEFAULT_GROUP_NAME = "Bizantra Shared Spaces";

	public static final int FILE_TO0L_EDITOR = 1;

	public static final int PICTURE_TO0L_EDITOR = 2;

	/**
	 * Email Marketing Filter conditions constants
	 */

	public static final int MATCH_ALL = 1;

	public static final int MATTCH_ANY_ONE = 2;

	protected static final int ALL_CONTACT = 1;

	protected static final int INTERNAL_CONTACT = 2;

	protected static final int EXTERNAL_CONTACT = 3;

	public static final String WORKSPACE_CATEGORY = "Shared Space Tasks";

	public static final String ROOT_FOLDER_NAME = "";

	public static final String ROOT_PICTURE_FOLDER_NAME = "";

	public static final String ENCRYPTEDFOLDER = "Encrypted";

	public static final String NEW_MEMBER = "New Member Added";

	public static String VALID_EXTERNAL_ACCOUNT = "Valid Account";

	public static String INVALID_EXTERNAL_ACCOUNT = "Invalid Account";

	public static final String PENDING_ACCOUNT_STATUS = "Pending";

	public static final String TEMPATE_IMAGE_ATTACHMENT = ",Image-Attachment";

	// HR Constants

	public static String ABSENCE_DETAIL = "absenceDetails";
	public static String PERSONAL_DETAIL = "PersonalDetail";
	public static String SALARY_DETAIL = "salaryDetail";
	public static String EMPLOYEE_LEAVE_REACORD = "leaveRecords";
	public static String PERFORMANCE_REVIEW = "performanceReviews";
	public static String INCIDENT_HISTORY = "incidentDetails";
	public static String GENERAL_NOTES = "generalNotes";
	public static String LEAVE_STATUS_APPROVED = "Approved";
	public static String LEAVE_STATUS_NOT_APPROVED = "Not Approved";
	public static final int ADD_LEAVE_RECORD = 1;
	public static final int EDIT_LEAVE_RECORD = 2;
	public static final int DELETE_LEAVE_RECORD = 3;

	public static final int RED = 1;
	public static final int AMBER = 2;
	public static final int GREEN = 3;
	public static final int ALL = 0;

	// HR Reports Constants

	public static String ABSENCE_SUMMARY_REPORT = "Absence Summary Report";
	public static String ABSENCE_DETAIL_REPORT = "Absence Details Report";
	public static String PERFORMANCE_REVIEW_SUMMARY_REPORT = "Performance Review Summary Report";
	public static String PERFORMANCE_REVIEW_DETAIL_REPORT = "Performance Review Details Report";
	public static String INCIDENT_DETAIL_REPORT = "Incident Details Report";
	public static String GENERAL_NOTES_DETAIL_REPORT = "General Notes Details Report";
	public static String LEAVE_SUMMARY_REPORT = "Leave Summary Report";
	public static String LEAVE_DETAIL_REPORT = "Leave Details Report";
	public static String SALARY_DETAIL_REPORT = "Salary Details Report";
	public static String SALARY_SUMMARY_REPORT = "Salary Summary Report";
	public static String ALL_EMPLOYEE_DETAIL_REPORT = "All Employee Details Report";
	public static String EMPLOYEE_DIVERSITY_REPORT = "Employee Diversity Report";
	public static String EMPLOYEE_SUMMARY_REPORT = "Employee Summary Report";
	public static String EMPLOYEE_DETAIL_REPORT = "Employee Details Report";
	public static String RIGHT_TO_WORK_REPORT = "Work Documentation Report";

	// Change Password Constant
	public static final String CHANGE_PASSWORD_SUCCESS_LOCAL_MESSAGE = "succeed locally";
	public static final String CHANGE_PASSWORD_FAILURE_MESSAGE = "failed totally";

	public static final int US_VERSION = 0;

	public static final int UK_VERSION = 1;

	public static final int ERROR_VALUE = -777;

	public static final String WORKFLOWS_DEFAULT_CATEGORYNAME = "Operations";

	public static final String AFTERNOON_ONLY = "P.M";

	public static final String MORNING_ONLY = "A.M";

	public static final String FULL_DAY = "Full day";

	public static final String SERVER_IP = "173.203.206.73";

	public static final String SERVER_IP_AND_PORT = "173.203.206.73:8892";

	public static final String USERS_CATEGORY = null;

	public static final String NOTE_CATEGORYNAME = null;

	public static final String HR_CATEGORY = null;

	public static final String EMAIL_CATEGORY = null;

	public static final String SUBSCRIPTION_UPGRADE_URL = "https://subs1.bizantra.com/authenticateuser.jsp";

	public static final String ANNUAL_SUBSCRIPTION = "annual";
	public static final String MONTHLY_SUBSCRIPTION = "monthly";
	public static final String TRIAL_SUBSCRIPTION = "trial";
	public static final String FREE_USER = "free user";

	public static final int SUBS_CONNECTION_ERROR = 103;

	public static final int TRIAL_COMPANY = 101;

}
