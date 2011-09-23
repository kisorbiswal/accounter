package com.vimukti.accounter.web.server;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.HelpLink;
import com.vimukti.accounter.web.client.core.HrEmployee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.KeyFinancialIndicators;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Fernandez
 * 
 */

public class AccounterGETServiceImpl extends AccounterRPCBaseServiceImpl
		implements IAccounterGETService {
	private static List<String> countriesList, statesList, timezoneslist /*
																		 * ,
																		 * citiesList
																		 */;
	private String[] countries, currencyCodes, currencyNames, timezones;
	private String[][] states;
	private List<ClientCurrency> currencies;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccounterGETServiceImpl() {
		super();

	}

	@Override
	public <T extends IAccounterCore> T getObjectById(AccounterCoreType type,
			long id) throws AccounterException {

		FinanceTool tool = getFinanceTool();
		Company company = tool.getCompany(getCompanyId());
		try {
			return tool.getObjectById(type, id, company.getAccountingType());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public <T extends IAccounterCore> T getObjectByName(AccounterCoreType type,
			String name) throws AccounterException {

		FinanceTool tool = getFinanceTool();

		try {
			return tool.getObjectByName(type, name);
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public <T extends IAccounterCore> ArrayList<T> getObjects(
			AccounterCoreType type) {

		try {

			return getFinanceTool().getObjects(type);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ClientCompany getCompany() throws AccounterException {
		FinanceTool tool = getFinanceTool();
		return tool.getClientCompany(getUserEmail(), getCompanyId());
	}

	@Override
	public KeyFinancialIndicators getKeyFinancialIndicators() {
		KeyFinancialIndicators keyFinancialIndicators = new KeyFinancialIndicators();
		try {
			keyFinancialIndicators = getFinanceTool()
					.getKeyFinancialIndicators();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return keyFinancialIndicators;
	}

	@Override
	public ArrayList<HrEmployee> getHREmployees() throws AccounterException {
		FinanceTool tool = (FinanceTool) getFinanceTool();
		return tool.getHREmployees();
	}

	public ArrayList<HelpLink> getHelpLinks(int type) {

		HelpLink link = new HelpLink(
				"How to EnterBill transaction?",
				"Click on the Enter Bill action or in the Supplier drop down select Enter Bill.");
		HelpLink link1 = new HelpLink(
				"How to Invoice transaction?",
				"Click on the New Invoice action or in the Customer drop down select New and then New Invoice");
		HelpLink link2 = new HelpLink(
				"How to Quote transaction?",
				"Click on the Quote action or in the Customer drop down select New and then New Quote");
		HelpLink link3 = new HelpLink("How to Paybill transaction?",
				"Click on the PayBill action or in the Supplier drop down select PayBill");
		HelpLink link4 = new HelpLink("How to CustomerPayment transaction?",
				"In the Customer drop down select Customer payment");
		HelpLink link5 = new HelpLink("How to Makedeposit transaction?",
				"In the Banking drop down select Deposit/Transfer Funds");

		List<HelpLink> helpLinks = new ArrayList<HelpLink>();

		helpLinks.add(link);
		helpLinks.add(link1);
		helpLinks.add(link2);
		helpLinks.add(link3);
		helpLinks.add(link4);
		helpLinks.add(link5);

		return new ArrayList<HelpLink>(helpLinks);

	}

	public List<ClientCurrency> getCurrencies() {
		currencies = new ArrayList<ClientCurrency>();
		currencyCodes = new String[] { "USD ", "AFN ", "ALL ", "DZD ", "AOA ",
				"ARS ", "AMD ", "AWG ", "AUD ", "AZN ", "BSD ", "BHD ", "BDT ",
				"BBD ", "BYR ", "BZD ", "BMD ", "BTN ", "BOB ", "BAM ", "BWP ",
				"BRL ", "GBP ", "BND ", "BGN ", "BIF ", "KHR ", "CAD ", "CVE ",
				"KYD ", "CLP ", "CNY ", "COP ", "KMF ", "CDF ", "CDF ", "CRC ",
				"HRK ", "CUC ", "CZK ", "DJF ", "DOP ", "DOP ", "EGP ", "ERN ",
				"EEK ", "ETB ", "FKP ", "FJD ", "GMD ", "GEL ", "GHS ", "GIP ",
				"GTQ ", "GNF ", "GYD ", "HTG ", "HNL ", "HKD ", "HUF ", "ISK ",
				"INR", "IDR ", "IRR ", "IQD ", "ILS ", "JMD ", "JPY ", "JOD ",
				"KZT ", "KES ", "KPW ", "KRW ", "KWD ", "KGS ", "LAK ", "LVL ",
				"LBP", "LSL ", "LRD ", "LYD", "LTL ", "MKD", "MWK", "MYR ",
				"MVR ", "MRO ", "MUR ", "MXN ", "MDL ", "MNT ", "MAD ", "MZN ",
				"NAD ", "NPR ", "ANG ", "NZD ", "NIO ", "NGN ", "NOK ", "OMR",
				"PKR ", "PAB ", "PGK", "PYG ", "PEN ", "PHP", "QAR ", "RON ",
				"RUB ", "RWF", "SHP", "WST ", "STD ", "SAR", "RSD ", "SCR ",
				"SLL ", "SGD ", "SKK ", "SBD ", "SOS ", "ZAR ", "LKR ", "SRD",
				"SZL ", "CHF ", "SYP ", "TWD", "TJS ", "TZS ", "THB ", "TOP ",
				"TTD ", "TND ", "TMT ", "TRY ", "UGX", "UAH ", "AED ", "UYU ",
				"UZS ", "VUV", "VEF ", "VND", "YER", "ZMK ", "ZWD" };

		currencyNames = new String[] { "United States 	Dollar",
				"Afghan 	Afghani", "Albanian 	Lek", "Algerian 	Dinar",
				"Angolan 	Kwanza", "Argentine 	Peso", "Armenian 	Dram",
				"Aruban 	Guilder", "Australian 	Dollar", "Azerbaijani 	Manat",
				"Bahamian 	Dollar", "Bahraini 	Dinar", "Bangladeshi 	Taka",
				"Barbadian 	Dollar", "Belarusian 	Ruble", "Belize  	Dollar",
				"Bermudian 	Dollar", "Bhutanese 	Ngultrum",
				"Bolivian 	Boliviano",
				"Bosnia and Herzegovina Convertible 	Mark", "Botswana 	Pula",
				"Brazilian 	Real", "British 	Pound", "Brunei 	Dollar",
				"Bulgarian 	Lev", "Burundian 	Franc", "Cambodian 	Riel",
				"Canadian 	Dollar", "Cape Verdean 	Escudo",
				"Cayman Islands 	Dollar", "Chilean 	Peso", "Chinese 	Yuan",
				"Colombian 	Peso", "Comorian 	Franc", "Congolese 	Franc",
				"Congolese 	Franc", "Costa Rican 	Colón", "Croatian 	Kuna",
				"Cuban Convertible 	Peso", "Czech 	Koruna",
				"Djiboutian 	Franc", "Dominican 	Peso", "Dominican 	Peso",
				"Egyptian 	Pound", "Eritrean 	Nakfa", "Estonian 	Kroon",
				"Ethiopian 	Birr", "Falkland Island	Pound", "Fijian 	Dollar",
				"Gambian 	alasi", "Georgian	 Lari", "Ghanaian 	Cedi",
				"Gibraltar 	Pound", "Guatemalan 	Quetzal", "Guinean 	Franc",
				"Guyanese 	Dollar", "Haitian 	Gourde", "Honduran 	Lempira",
				"Hong Kong 	Dollar", "Hungarian 	Forint", "Icelandic 	Króna",
				"Indian	Rupee", "Indonesian 	Rupiah", "Iranian 	Rial",
				"Iraqi 	Dinar", "Israeli 	New Sheqel", "Jamaican 	Dollar",
				"Japanese 	Yen", "Jordanian 	Dinar", "Kazakhstani 	Tenge",
				"Kenyan 	Shilling", "North Korean 	Won", "South Korean 	Won",
				"Kuwaiti 	Dinar", "Kyrgyzstani 	Som", "Lao 	Pound",
				"Latvian	Lats", "Lebanese 	Pound", "Lesotho	 Loti",
				"Liberian 	Dollar", "Libyan 	Dinar", "Lithuanian 	Litas",
				"Macedonian 	Denar", "Malawian 	Kwacha", "Malaysian 	Ringgit",
				"Maldivian 	Rufiyaa", "Mauritanian 	Ouguiya",
				"Mauritian 	Rupee", "Mexican 	Peso", "Moldovan 	Leu",
				"Mongolian	 Tugrik", "Moroccan 	Dirham", "Mozambican	Metical",
				"Namibian 	Dollar", "Nepalese 	Rupee",
				"Netherlands Antillean 	Guilder", "New Zealand 	Dollar",
				"Nicaraguan 	Córdoba", "Nigerian	 Naira", "Norwegian 	Krone",
				"Omani 	Rial", "Pakistani 	Rupee", "Panamanian 	Balboa",
				"Papua New Guinean 	Kina", "Paraguayan 	Guaraní",
				"Peruvian Nuevo 	Sol", "Philippine 	Peso", "Qatari 	Riyal",
				"Romanian 	Leu", "Russian 	Ruble", "Rwandan 	Franc",
				"Saint Helenian 	Pound", "Samoan 	Tala",
				"São Tomé and Príncipe 	Dobra", "Saudi 	Riyal",
				"Serbian 	Dinar", "Seychellois 	Rupee",
				"Sierra Leonean 	Leone", "Singapore 	Dollar", "Slovak 	Koruna",
				"Solomon Islands 	Dollar", "Somali  	Shilling",
				"South African 	Rand", "Sri Lankan 	Rupee",
				"Surinamese 	Dollar", "Swazi 	Lilangeni", "Swiss 	Franc",
				"Syrian 	Pound", "Taiwanese New 	Dollar",
				"Tajikistani 	Somoni", "Tanzanian 	Shilling", "Thai 	Baht",
				"Tongan 	Pa?anga", "Trinidad and Tobago 	Dollar",
				"Tunisian 	Dinar", "Turkmenistani 	Manat", "Turkish 	Lira",
				"Ugandan 	Shilling", "Ukrainian 	Hryvnia",
				"United Arab Emirates 	Dirham", "Uruguayan 	Peso",
				"Uzbekistani 	Som", "Vanuatu 	Vatu",
				"Venezuelan Bolívar 	Fuertes", "Vietnamese 	Dong",
				"Yemeni 	Rial", "Zambian 	Kwacha", "Zimbabwean 	Dollar" };
		for (int i = 0; i < currencyCodes.length; i++) {
			ClientCurrency clientCurrency = new ClientCurrency();
			clientCurrency.setFormalName(currencyCodes[i]);
			clientCurrency.setName(currencyNames[i]);
			currencies.add(clientCurrency);
		}
		return currencies;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.IAccounterGETService#getUser(java.lang
	 * .String, java.lang.String, boolean, int)
	 */
	@Override
	public ClientUser getUser(String userName, String password,
			boolean isremeber, int offset) {
		return login(userName, password, isremeber, offset);
	}

	public List<String> getCountries() {
		countries = new String[] { "United Kingdom", "United States",
				"Afghanistan", "Albania", "Algeria", "American Samoa",
				"Andorra", "Angola", "Anguilla", "Antarctica",
				"Antigua and Barbuda", "Argentina", "Armenia", "Aruba",
				"Austria", "Australia", "Azerbaijan", "Bahamas, The",
				"Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
				"Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
				"Bosnia and Herzegovina", "Botswana", "Bouvet Island",
				"Brazil", "British Indian Ocean Territory", "Brunei",
				"Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon",
				"Canada", "Cape Verde", "Cayman Islands",
				"Central African Republic", "Chad", "Chile", "China",
				"Christmas Island", "Cocos (Keeling) Islands", "Colombia",
				"Comoros", "Congo, Democratic Republic of the",
				"Congo, Republic of the", "Cook Islands", "Costa Rica",
				"Cote d'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic",
				"Denmark", "Djibouti", "Dominica", "Dominican Republic",
				"Ecuador", "Egypt", "El Salvador", "Equatorial Guinea",
				"Eritrea", "Estonia", "Ethiopia",
				"Falkland Islands (Islas Malvinas)", "Faroe Islands", "Fiji",
				"Finland", "France", "French Guiana", "French Polynesia",
				"French Southern and Antarctic Lands", "Gabon", "Gambia, The",
				"Georgia", "Germany", "Ghana", "Gibraltar", "Greece",
				"Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala",
				"Guinea", "Guinea-Bissau", "Guyana", "Haiti",
				"Heard Island and McDonald Islands", "Holy See (Vatican City)",
				"Honduras", "Hong Kong", "Hungary", "Iceland", "India",
				"Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man",
				"Israel", "Italy", "Jamaica", "Jan Mayen", "Japan", "Jordan",
				"Kazakhstan", "Kenya", "Kiribati", "Korea, North",
				"Korea, South", "Kuwait", "Kyrgyzstan", "Laos", "Latvia",
				"Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein",
				"Lithuania", "Luxembourg", "Macao", "Macedonia", "Madagascar",
				"Malawi", "Malaysia", "Maldives", "Mali", "Malta",
				"Marshall Islands", "Martinique", "Mauritania", "Mauritius",
				"Mayotte", "Mexico", "Micronesia, Federated States of",
				"Moldova", "Monaco", "Mongolia", "Montserrat", "Morocco",
				"Mozambique", "Namibia", "Nauru", "Nepal", "Netherlands",
				"Netherlands Antilles", "New Caledonia", "New Zealand",
				"Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island",
				"Northern Mariana Islands", "Norway", "Oman", "Pakistan",
				"Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru",
				"Philippines", "Pitcairn Islands", "Poland", "Portugal",
				"Puerto Rico", "Qatar", "Reunion", "Romania", "Russia",
				"Rwanda", "Saint Helena", "Saint Kitts and Nevis",
				"Saint Lucia", "Saint Pierre and Miquelon",
				"Saint Vincent and the Grenadines", "Samoa", "San Marino",
				"Sao Tome and Principe", "Saudi Arabia", "Senegal",
				"Serbia and Montenegro", "Seychelles", "Sierra Leone",
				"Singapore", "Slovakia", "Slovenia", "Solomon Islands",
				"Somalia", "South Africa",
				"South Georgia and the South Sandwich Islands", "Spain",
				"Sri Lanka", "Sudan", "Suriname", "Svalbard", "Swaziland",
				"Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan",
				"Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau",
				"Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",
				"Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda",
				"Ukraine", "United Arab Emirates", "Uruguay", "Uzbekistan",
				"Vanuatu", "Venezuela", "Vietnam", "Virgin Islands",
				"Wallis and Futuna", "Western Sahara", "Yemen", "Zambia",
				"Zimbabwe" };
		countriesList = new ArrayList<String>();
		for (int i = 0; i < countries.length; i++) {
			countriesList.add(countries[i]);
		}
		return countriesList;
	}

	public List<String> getTimezones() {
		timezones = new String[] { "UTC-12:00 Etc/GMT+12",
				"UTC-11:00 Etc/GMT+11", "UTC-11:00 MIT",
				"UTC-11:00 Pacific/Apia", "UTC-11:00 Pacific/Midway",
				"UTC-11:00 Pacific/Niue", "UTC-11:00 Pacific/Pago_Pago",
				"UTC-11:00 Pacific/Samoa", "UTC-11:00 US/Samoa",
				"UTC-10:00 America/Adak", "UTC-10:00 America/Atka",
				"UTC-10:00 Etc/GMT+10", "UTC-10:00 HST",
				"UTC-10:00 Pacific/Fakaofo", "UTC-10:00 Pacific/Honolulu",
				"UTC-10:00 Pacific/Johnston", "UTC-10:00 Pacific/Rarotonga",
				"UTC-10:00 Pacific/Tahiti", "UTC-10:00 SystemV/HST10",
				"UTC-10:00 US/Aleutian", "UTC-10:00 US/Hawaii",
				"UTC-09:30 Pacific/Marquesas", "UTC-09:00 AST",
				"UTC-09:00 America/Anchorage", "UTC-09:00 America/Juneau",
				"UTC-09:00 America/Nome", "UTC-09:00 America/Sitka",
				"UTC-09:00 America/Yakutat", "UTC-09:00 Etc/GMT+9",
				"UTC-09:00 Pacific/Gambier", "UTC-09:00 SystemV/YST9",
				"UTC-09:00 SystemV/YST9YDT", "UTC-09:00 US/Alaska",
				"UTC-08:00 America/Dawson", "UTC-08:00 America/Ensenada",
				"UTC-08:00 America/Los_Angeles",
				"UTC-08:00 America/Metlakatla",
				"UTC-08:00 America/Santa_Isabel", "UTC-08:00 America/Tijuana",
				"UTC-08:00 America/Vancouver", "UTC-08:00 America/Whitehorse",
				"UTC-08:00 Canada/Pacific", "UTC-08:00 Canada/Yukon",
				"UTC-08:00 Etc/GMT+8", "UTC-08:00 Mexico/BajaNorte",
				"UTC-08:00 PST", "UTC-08:00 PST8PDT",
				"UTC-08:00 Pacific/Pitcairn", "UTC-08:00 SystemV/PST8",
				"UTC-08:00 SystemV/PST8PDT", "UTC-08:00 US/Pacific",
				"UTC-08:00 US/Pacific-New", "UTC-07:00 America/Boise",
				"UTC-07:00 America/Cambridge_Bay",
				"UTC-07:00 America/Chihuahua",
				"UTC-07:00 America/Dawson_Creek", "UTC-07:00 America/Denver",
				"UTC-07:00 America/Edmonton", "UTC-07:00 America/Hermosillo",
				"UTC-07:00 America/Inuvik", "UTC-07:00 America/Mazatlan",
				"UTC-07:00 America/Ojinaga", "UTC-07:00 America/Phoenix",
				"UTC-07:00 America/Shiprock", "UTC-07:00 America/Yellowknife",
				"UTC-07:00 Canada/Mountain", "UTC-07:00 Etc/GMT+7",
				"UTC-07:00 MST", "UTC-07:00 MST7MDT",
				"UTC-07:00 Mexico/BajaSur", "UTC-07:00 Navajo",
				"UTC-07:00 PNT", "UTC-07:00 SystemV/MST7",
				"UTC-07:00 SystemV/MST7MDT", "UTC-07:00 US/Arizona",
				"UTC-07:00 US/Mountain", "UTC-06:00 America/Bahia_Banderas",
				"UTC-06:00 America/Belize", "UTC-06:00 America/Cancun",
				"UTC-06:00 America/Chicago", "UTC-06:00 America/Costa_Rica",
				"UTC-06:00 America/El_Salvador", "UTC-06:00 America/Guatemala",
				"UTC-06:00 America/Indiana/Knox",
				"UTC-06:00 America/Indiana/Tell_City",
				"UTC-06:00 America/Knox_IN", "UTC-06:00 America/Managua",
				"UTC-06:00 America/Matamoros", "UTC-06:00 America/Menominee",
				"UTC-06:00 America/Merida", "UTC-06:00 America/Mexico_City",
				"UTC-06:00 America/Monterrey",
				"UTC-06:00 America/North_Dakota/Beulah",
				"UTC-06:00 America/North_Dakota/Center",
				"UTC-06:00 America/North_Dakota/New_Salem",
				"UTC-06:00 America/Rainy_River",
				"UTC-06:00 America/Rankin_Inlet", "UTC-06:00 America/Regina",
				"UTC-06:00 America/Swift_Current",
				"UTC-06:00 America/Tegucigalpa", "UTC-06:00 America/Winnipeg",
				"UTC-06:00 CST", "UTC-06:00 CST6CDT",
				"UTC-06:00 Canada/Central",
				"UTC-06:00 Canada/East-Saskatchewan",
				"UTC-06:00 Canada/Saskatchewan",
				"UTC-06:00 Chile/EasterIsland", "UTC-06:00 Etc/GMT+6",
				"UTC-06:00 Mexico/General", "UTC-06:00 Pacific/Easter",
				"UTC-06:00 Pacific/Galapagos", "UTC-06:00 SystemV/CST6",
				"UTC-06:00 SystemV/CST6CDT", "UTC-06:00 US/Central",
				"UTC-06:00 US/Indiana-Starke", "UTC-05:00 America/Atikokan",
				"UTC-05:00 America/Bogota", "UTC-05:00 America/Cayman",
				"UTC-05:00 America/Coral_Harbour", "UTC-05:00 America/Detroit",
				"UTC-05:00 America/Fort_Wayne", "UTC-05:00 America/Grand_Turk",
				"UTC-05:00 America/Guayaquil", "UTC-05:00 America/Havana",
				"UTC-05:00 America/Indiana/Indianapolis",
				"UTC-05:00 America/Indiana/Marengo",
				"UTC-05:00 America/Indiana/Petersburg",
				"UTC-05:00 America/Indiana/Vevay",
				"UTC-05:00 America/Indiana/Vincennes",
				"UTC-05:00 America/Indiana/Winamac",
				"UTC-05:00 America/Indianapolis", "UTC-05:00 America/Iqaluit",
				"UTC-05:00 America/Jamaica",
				"UTC-05:00 America/Kentucky/Louisville",
				"UTC-05:00 America/Kentucky/Monticello",
				"UTC-05:00 America/Lima", "UTC-05:00 America/Louisville",
				"UTC-05:00 America/Montreal", "UTC-05:00 America/Nassau",
				"UTC-05:00 America/New_York", "UTC-05:00 America/Nipigon",
				"UTC-05:00 America/Panama", "UTC-05:00 America/Pangnirtung",
				"UTC-05:00 America/Port-au-Prince",
				"UTC-05:00 America/Resolute", "UTC-05:00 America/Thunder_Bay",
				"UTC-05:00 America/Toronto", "UTC-05:00 Canada/Eastern",
				"UTC-05:00 Cuba", "UTC-05:00 EST", "UTC-05:00 EST5EDT",
				"UTC-05:00 Etc/GMT+5", "UTC-05:00 IET", "UTC-05:00 Jamaica",
				"UTC-05:00 SystemV/EST5", "UTC-05:00 SystemV/EST5EDT",
				"UTC-05:00 US/East-Indiana", "UTC-05:00 US/Eastern",
				"UTC-05:00 US/Michigan", "UTC-04:30 America/Caracas",
				"UTC-04:00 America/Anguilla", "UTC-04:00 America/Antigua",
				"UTC-04:00 America/Argentina/San_Luis",
				"UTC-04:00 America/Aruba", "UTC-04:00 America/Asuncion",
				"UTC-04:00 America/Barbados", "UTC-04:00 America/Blanc-Sablon",
				"UTC-04:00 America/Boa_Vista",
				"UTC-04:00 America/Campo_Grande", "UTC-04:00 America/Cuiaba",
				"UTC-04:00 America/Curacao", "UTC-04:00 America/Dominica",
				"UTC-04:00 America/Eirunepe", "UTC-04:00 America/Glace_Bay",
				"UTC-04:00 America/Goose_Bay", "UTC-04:00 America/Grenada",
				"UTC-04:00 America/Guadeloupe", "UTC-04:00 America/Guyana",
				"UTC-04:00 America/Halifax", "UTC-04:00 America/La_Paz",
				"UTC-04:00 America/Manaus", "UTC-04:00 America/Marigot",
				"UTC-04:00 America/Martinique", "UTC-04:00 America/Moncton",
				"UTC-04:00 America/Montserrat",
				"UTC-04:00 America/Port_of_Spain",
				"UTC-04:00 America/Porto_Acre",
				"UTC-04:00 America/Porto_Velho",
				"UTC-04:00 America/Puerto_Rico",
				"UTC-04:00 America/Rio_Branco", "UTC-04:00 America/Santiago",
				"UTC-04:00 America/Santo_Domingo",
				"UTC-04:00 America/St_Barthelemy",
				"UTC-04:00 America/St_Kitts", "UTC-04:00 America/St_Lucia",
				"UTC-04:00 America/St_Thomas", "UTC-04:00 America/St_Vincent",
				"UTC-04:00 America/Thule", "UTC-04:00 America/Tortola",
				"UTC-04:00 America/Virgin", "UTC-04:00 Antarctica/Palmer",
				"UTC-04:00 Atlantic/Bermuda", "UTC-04:00 Atlantic/Stanley",
				"UTC-04:00 Brazil/Acre", "UTC-04:00 Brazil/West",
				"UTC-04:00 Canada/Atlantic", "UTC-04:00 Chile/Continental",
				"UTC-04:00 Etc/GMT+4", "UTC-04:00 PRT",
				"UTC-04:00 SystemV/AST4", "UTC-04:00 SystemV/AST4ADT",
				"UTC-03:30 America/St_Johns", "UTC-03:30 CNT",
				"UTC-03:30 Canada/Newfoundland", "UTC-03:00 AGT",
				"UTC-03:00 America/Araguaina",
				"UTC-03:00 America/Argentina/Buenos_Aires",
				"UTC-03:00 America/Argentina/Catamarca",
				"UTC-03:00 America/Argentina/ComodRivadavia",
				"UTC-03:00 America/Argentina/Cordoba",
				"UTC-03:00 America/Argentina/Jujuy",
				"UTC-03:00 America/Argentina/La_Rioja",
				"UTC-03:00 America/Argentina/Mendoza",
				"UTC-03:00 America/Argentina/Rio_Gallegos",
				"UTC-03:00 America/Argentina/Salta",
				"UTC-03:00 America/Argentina/San_Juan",
				"UTC-03:00 America/Argentina/Tucuman",
				"UTC-03:00 America/Argentina/Ushuaia",
				"UTC-03:00 America/Bahia", "UTC-03:00 America/Belem",
				"UTC-03:00 America/Buenos_Aires",
				"UTC-03:00 America/Catamarca", "UTC-03:00 America/Cayenne",
				"UTC-03:00 America/Cordoba", "UTC-03:00 America/Fortaleza",
				"UTC-03:00 America/Godthab", "UTC-03:00 America/Jujuy",
				"UTC-03:00 America/Maceio", "UTC-03:00 America/Mendoza",
				"UTC-03:00 America/Miquelon", "UTC-03:00 America/Montevideo",
				"UTC-03:00 America/Paramaribo", "UTC-03:00 America/Recife",
				"UTC-03:00 America/Rosario", "UTC-03:00 America/Santarem",
				"UTC-03:00 America/Sao_Paulo", "UTC-03:00 Antarctica/Rothera",
				"UTC-03:00 BET", "UTC-03:00 Brazil/East",
				"UTC-03:00 Etc/GMT+3", "UTC-02:00 America/Noronha",
				"UTC-02:00 Atlantic/South_Georgia",
				"UTC-02:00 Brazil/DeNoronha", "UTC-02:00 Etc/GMT+2",
				"UTC-01:00 America/Scoresbysund", "UTC-01:00 Atlantic/Azores",
				"UTC-01:00 Atlantic/Cape_Verde", "UTC-01:00 Etc/GMT+1",
				"UTC+00:00 Africa/Abidjan", "UTC+00:00 Africa/Accra",
				"UTC+00:00 Africa/Bamako", "UTC+00:00 Africa/Banjul",
				"UTC+00:00 Africa/Bissau", "UTC+00:00 Africa/Casablanca",
				"UTC+00:00 Africa/Conakry", "UTC+00:00 Africa/Dakar",
				"UTC+00:00 Africa/El_Aaiun", "UTC+00:00 Africa/Freetown",
				"UTC+00:00 Africa/Lome", "UTC+00:00 Africa/Monrovia",
				"UTC+00:00 Africa/Nouakchott", "UTC+00:00 Africa/Ouagadougou",
				"UTC+00:00 Africa/Sao_Tome", "UTC+00:00 Africa/Timbuktu",
				"UTC+00:00 America/Danmarkshavn", "UTC+00:00 Atlantic/Canary",
				"UTC+00:00 Atlantic/Faeroe", "UTC+00:00 Atlantic/Faroe",
				"UTC+00:00 Atlantic/Madeira", "UTC+00:00 Atlantic/Reykjavik",
				"UTC+00:00 Atlantic/St_Helena", "UTC+00:00 Eire",
				"UTC+00:00 Etc/GMT", "UTC+00:00 Etc/GMT+0",
				"UTC+00:00 Etc/GMT-0", "UTC+00:00 Etc/GMT0",
				"UTC+00:00 Etc/Greenwich", "UTC+00:00 Etc/UCT",
				"UTC+00:00 Etc/UTC", "UTC+00:00 Etc/Universal",
				"UTC+00:00 Etc/Zulu", "UTC+00:00 Europe/Belfast",
				"UTC+00:00 Europe/Dublin", "UTC+00:00 Europe/Guernsey",
				"UTC+00:00 Europe/Isle_of_Man", "UTC+00:00 Europe/Jersey",
				"UTC+00:00 Europe/Lisbon", "UTC+00:00 Europe/London",
				"UTC+00:00 GB", "UTC+00:00 GB-Eire", "UTC+00:00 GMT",
				"UTC+00:00 GMT0", "UTC+00:00 Greenwich", "UTC+00:00 Iceland",
				"UTC+00:00 Portugal", "UTC+00:00 UCT", "UTC+00:00 UTC",
				"UTC+00:00 Universal", "UTC+00:00 WET", "UTC+00:00 Zulu",
				"UTC+01:00 Africa/Algiers", "UTC+01:00 Africa/Bangui",
				"UTC+01:00 Africa/Brazzaville", "UTC+01:00 Africa/Ceuta",
				"UTC+01:00 Africa/Douala", "UTC+01:00 Africa/Kinshasa",
				"UTC+01:00 Africa/Lagos", "UTC+01:00 Africa/Libreville",
				"UTC+01:00 Africa/Luanda", "UTC+01:00 Africa/Malabo",
				"UTC+01:00 Africa/Ndjamena", "UTC+01:00 Africa/Niamey",
				"UTC+01:00 Africa/Porto-Novo", "UTC+01:00 Africa/Tunis",
				"UTC+01:00 Africa/Windhoek", "UTC+01:00 Arctic/Longyearbyen",
				"UTC+01:00 Atlantic/Jan_Mayen", "UTC+01:00 CET",
				"UTC+01:00 ECT", "UTC+01:00 Etc/GMT-1",
				"UTC+01:00 Europe/Amsterdam", "UTC+01:00 Europe/Andorra",
				"UTC+01:00 Europe/Belgrade", "UTC+01:00 Europe/Berlin",
				"UTC+01:00 Europe/Bratislava", "UTC+01:00 Europe/Brussels",
				"UTC+01:00 Europe/Budapest", "UTC+01:00 Europe/Copenhagen",
				"UTC+01:00 Europe/Gibraltar", "UTC+01:00 Europe/Ljubljana",
				"UTC+01:00 Europe/Luxembourg", "UTC+01:00 Europe/Madrid",
				"UTC+01:00 Europe/Malta", "UTC+01:00 Europe/Monaco",
				"UTC+01:00 Europe/Oslo", "UTC+01:00 Europe/Paris",
				"UTC+01:00 Europe/Podgorica", "UTC+01:00 Europe/Prague",
				"UTC+01:00 Europe/Rome", "UTC+01:00 Europe/San_Marino",
				"UTC+01:00 Europe/Sarajevo", "UTC+01:00 Europe/Skopje",
				"UTC+01:00 Europe/Stockholm", "UTC+01:00 Europe/Tirane",
				"UTC+01:00 Europe/Vaduz", "UTC+01:00 Europe/Vatican",
				"UTC+01:00 Europe/Vienna", "UTC+01:00 Europe/Warsaw",
				"UTC+01:00 Europe/Zagreb", "UTC+01:00 Europe/Zurich",
				"UTC+01:00 MET", "UTC+01:00 Poland", "UTC+02:00 ART",
				"UTC+02:00 Africa/Blantyre", "UTC+02:00 Africa/Bujumbura",
				"UTC+02:00 Africa/Cairo", "UTC+02:00 Africa/Gaborone",
				"UTC+02:00 Africa/Harare", "UTC+02:00 Africa/Johannesburg",
				"UTC+02:00 Africa/Kigali", "UTC+02:00 Africa/Lubumbashi",
				"UTC+02:00 Africa/Lusaka", "UTC+02:00 Africa/Maputo",
				"UTC+02:00 Africa/Maseru", "UTC+02:00 Africa/Mbabane",
				"UTC+02:00 Africa/Tripoli", "UTC+02:00 Asia/Amman",
				"UTC+02:00 Asia/Beirut", "UTC+02:00 Asia/Damascus",
				"UTC+02:00 Asia/Gaza", "UTC+02:00 Asia/Istanbul",
				"UTC+02:00 Asia/Jerusalem", "UTC+02:00 Asia/Nicosia",
				"UTC+02:00 Asia/Tel_Aviv", "UTC+02:00 CAT", "UTC+02:00 EET",
				"UTC+02:00 Egypt", "UTC+02:00 Etc/GMT-2",
				"UTC+02:00 Europe/Athens", "UTC+02:00 Europe/Bucharest",
				"UTC+02:00 Europe/Chisinau", "UTC+02:00 Europe/Helsinki",
				"UTC+02:00 Europe/Istanbul", "UTC+02:00 Europe/Kaliningrad",
				"UTC+02:00 Europe/Kiev", "UTC+02:00 Europe/Mariehamn",
				"UTC+02:00 Europe/Minsk", "UTC+02:00 Europe/Nicosia",
				"UTC+02:00 Europe/Riga", "UTC+02:00 Europe/Simferopol",
				"UTC+02:00 Europe/Sofia", "UTC+02:00 Europe/Tallinn",
				"UTC+02:00 Europe/Tiraspol", "UTC+02:00 Europe/Uzhgorod",
				"UTC+02:00 Europe/Vilnius", "UTC+02:00 Europe/Zaporozhye",
				"UTC+02:00 Israel", "UTC+02:00 Libya", "UTC+02:00 Turkey",
				"UTC+03:00 Africa/Addis_Ababa", "UTC+03:00 Africa/Asmara",
				"UTC+03:00 Africa/Asmera", "UTC+03:00 Africa/Dar_es_Salaam",
				"UTC+03:00 Africa/Djibouti", "UTC+03:00 Africa/Kampala",
				"UTC+03:00 Africa/Khartoum", "UTC+03:00 Africa/Mogadishu",
				"UTC+03:00 Africa/Nairobi", "UTC+03:00 Antarctica/Syowa",
				"UTC+03:00 Asia/Aden", "UTC+03:00 Asia/Baghdad",
				"UTC+03:00 Asia/Bahrain", "UTC+03:00 Asia/Kuwait",
				"UTC+03:00 Asia/Qatar", "UTC+03:00 Asia/Riyadh",
				"UTC+03:00 EAT", "UTC+03:00 Etc/GMT-3",
				"UTC+03:00 Europe/Moscow", "UTC+03:00 Europe/Samara",
				"UTC+03:00 Europe/Volgograd", "UTC+03:00 Indian/Antananarivo",
				"UTC+03:00 Indian/Comoro", "UTC+03:00 Indian/Mayotte",
				"UTC+03:00 W-SU", "UTC+03:07 Asia/Riyadh87",
				"UTC+03:07 Asia/Riyadh88", "UTC+03:07 Asia/Riyadh89",
				"UTC+03:07 Mideast/Riyadh87", "UTC+03:07 Mideast/Riyadh88",
				"UTC+03:07 Mideast/Riyadh89", "UTC+03:30 Asia/Tehran",
				"UTC+03:30 Iran", "UTC+04:00 Asia/Baku",
				"UTC+04:00 Asia/Dubai", "UTC+04:00 Asia/Muscat",
				"UTC+04:00 Asia/Tbilisi", "UTC+04:00 Asia/Yerevan",
				"UTC+04:00 Etc/GMT-4", "UTC+04:00 Indian/Mahe",
				"UTC+04:00 Indian/Mauritius", "UTC+04:00 Indian/Reunion",
				"UTC+04:00 NET", "UTC+04:30 Asia/Kabul",
				"UTC+05:00 Antarctica/Mawson", "UTC+05:00 Asia/Aqtau",
				"UTC+05:00 Asia/Aqtobe", "UTC+05:00 Asia/Ashgabat",
				"UTC+05:00 Asia/Ashkhabad", "UTC+05:00 Asia/Dushanbe",
				"UTC+05:00 Asia/Karachi", "UTC+05:00 Asia/Oral",
				"UTC+05:00 Asia/Samarkand", "UTC+05:00 Asia/Tashkent",
				"UTC+05:00 Asia/Yekaterinburg", "UTC+05:00 Etc/GMT-5",
				"UTC+05:00 Indian/Kerguelen", "UTC+05:00 Indian/Maldives",
				"UTC+05:00 PLT", "UTC+05:30 Asia/Calcutta",
				"UTC+05:30 Asia/Colombo", "UTC+05:30 Asia/Kolkata",
				"UTC+05:30 IST", "UTC+05:45 Asia/Kathmandu",
				"UTC+05:45 Asia/Katmandu", "UTC+06:00 Antarctica/Vostok",
				"UTC+06:00 Asia/Almaty", "UTC+06:00 Asia/Bishkek",
				"UTC+06:00 Asia/Dacca", "UTC+06:00 Asia/Dhaka",
				"UTC+06:00 Asia/Novokuznetsk", "UTC+06:00 Asia/Novosibirsk",
				"UTC+06:00 Asia/Omsk", "UTC+06:00 Asia/Qyzylorda",
				"UTC+06:00 Asia/Thimbu", "UTC+06:00 Asia/Thimphu",
				"UTC+06:00 BST", "UTC+06:00 Etc/GMT-6",
				"UTC+06:00 Indian/Chagos", "UTC+06:30 Asia/Rangoon",
				"UTC+06:30 Indian/Cocos", "UTC+07:00 Antarctica/Davis",
				"UTC+07:00 Asia/Bangkok", "UTC+07:00 Asia/Ho_Chi_Minh",
				"UTC+07:00 Asia/Hovd", "UTC+07:00 Asia/Jakarta",
				"UTC+07:00 Asia/Krasnoyarsk", "UTC+07:00 Asia/Phnom_Penh",
				"UTC+07:00 Asia/Pontianak", "UTC+07:00 Asia/Saigon",
				"UTC+07:00 Asia/Vientiane", "UTC+07:00 Etc/GMT-7",
				"UTC+07:00 Indian/Christmas", "UTC+07:00 VST",
				"UTC+08:00 Antarctica/Casey", "UTC+08:00 Asia/Brunei",
				"UTC+08:00 Asia/Choibalsan", "UTC+08:00 Asia/Chongqing",
				"UTC+08:00 Asia/Chungking", "UTC+08:00 Asia/Harbin",
				"UTC+08:00 Asia/Hong_Kong", "UTC+08:00 Asia/Irkutsk",
				"UTC+08:00 Asia/Kashgar", "UTC+08:00 Asia/Kuala_Lumpur",
				"UTC+08:00 Asia/Kuching", "UTC+08:00 Asia/Macao",
				"UTC+08:00 Asia/Macau", "UTC+08:00 Asia/Makassar",
				"UTC+08:00 Asia/Manila", "UTC+08:00 Asia/Shanghai",
				"UTC+08:00 Asia/Singapore", "UTC+08:00 Asia/Taipei",
				"UTC+08:00 Asia/Ujung_Pandang", "UTC+08:00 Asia/Ulaanbaatar",
				"UTC+08:00 Asia/Ulan_Bator", "UTC+08:00 Asia/Urumqi",
				"UTC+08:00 Australia/Perth", "UTC+08:00 Australia/West",
				"UTC+08:00 CTT", "UTC+08:00 Etc/GMT-8", "UTC+08:00 Hongkong",
				"UTC+08:00 PRC", "UTC+08:00 Singapore",
				"UTC+08:45 Australia/Eucla", "UTC+09:00 Asia/Dili",
				"UTC+09:00 Asia/Jayapura", "UTC+09:00 Asia/Pyongyang",
				"UTC+09:00 Asia/Seoul", "UTC+09:00 Asia/Tokyo",
				"UTC+09:00 Asia/Yakutsk", "UTC+09:00 Etc/GMT-9",
				"UTC+09:00 JST", "UTC+09:00 Japan", "UTC+09:00 Pacific/Palau",
				"UTC+09:00 ROK", "UTC+09:30 ACT",
				"UTC+09:30 Australia/Adelaide",
				"UTC+09:30 Australia/Broken_Hill",
				"UTC+09:30 Australia/Darwin", "UTC+09:30 Australia/North",
				"UTC+09:30 Australia/South", "UTC+09:30 Australia/Yancowinna",
				"UTC+10:00 AET", "UTC+10:00 Antarctica/DumontDUrville",
				"UTC+10:00 Asia/Sakhalin", "UTC+10:00 Asia/Vladivostok",
				"UTC+10:00 Australia/ACT", "UTC+10:00 Australia/Brisbane",
				"UTC+10:00 Australia/Canberra", "UTC+10:00 Australia/Currie",
				"UTC+10:00 Australia/Hobart", "UTC+10:00 Australia/Lindeman",
				"UTC+10:00 Australia/Melbourne", "UTC+10:00 Australia/NSW",
				"UTC+10:00 Australia/Queensland", "UTC+10:00 Australia/Sydney",
				"UTC+10:00 Australia/Tasmania", "UTC+10:00 Australia/Victoria",
				"UTC+10:00 Etc/GMT-10", "UTC+10:00 Pacific/Chuuk",
				"UTC+10:00 Pacific/Guam", "UTC+10:00 Pacific/Port_Moresby",
				"UTC+10:00 Pacific/Saipan", "UTC+10:00 Pacific/Truk",
				"UTC+10:00 Pacific/Yap", "UTC+10:30 Australia/LHI",
				"UTC+10:30 Australia/Lord_Howe",
				"UTC+11:00 Antarctica/Macquarie", "UTC+11:00 Asia/Anadyr",
				"UTC+11:00 Asia/Kamchatka", "UTC+11:00 Asia/Magadan",
				"UTC+11:00 Etc/GMT-11", "UTC+11:00 Pacific/Efate",
				"UTC+11:00 Pacific/Guadalcanal", "UTC+11:00 Pacific/Kosrae",
				"UTC+11:00 Pacific/Noumea", "UTC+11:00 Pacific/Pohnpei",
				"UTC+11:00 Pacific/Ponape", "UTC+11:00 SST",
				"UTC+11:30 Pacific/Norfolk", "UTC+12:00 Antarctica/McMurdo",
				"UTC+12:00 Antarctica/South_Pole", "UTC+12:00 Etc/GMT-12",
				"UTC+12:00 Kwajalein", "UTC+12:00 NST", "UTC+12:00 NZ",
				"UTC+12:00 Pacific/Auckland", "UTC+12:00 Pacific/Fiji",
				"UTC+12:00 Pacific/Funafuti", "UTC+12:00 Pacific/Kwajalein",
				"UTC+12:00 Pacific/Majuro", "UTC+12:00 Pacific/Nauru",
				"UTC+12:00 Pacific/Tarawa", "UTC+12:00 Pacific/Wake",
				"UTC+12:00 Pacific/Wallis", "UTC+12:45 NZ-CHAT",
				"UTC+12:45 Pacific/Chatham", "UTC+13:00 Etc/GMT-13",
				"UTC+13:00 Pacific/Enderbury", "UTC+13:00 Pacific/Tongatapu",
				"UTC+14:00 Etc/GMT-14", "UTC+14:00 Pacific/Kiritimati" };
		timezoneslist = new ArrayList<String>();
		for (int i = 0; i < timezones.length; i++) {
			timezoneslist.add(timezones[i]);
		}
		return timezoneslist;
	}

	public List<String> getStates(String country) {
		states = new String[][] {
				// UNITED KINGDOM
				{ "Buckinghamshire", "Hampshire", "Cambridgeshire", "Hackney",
						"East Riding of Yorkshire", "Worcestershire",
						"Barking and Dagenham", "Moray", "Staffordshire",
						"Devon", "Harrow", "Falkirk", "Rhondda Cynon Taff",
						"East Ayrshire", "Sutton", "Larne", "Bedfordshire",
						"Tameside", "Cleveland", "Lothian Region", "Coventry",
						"Nottinghamshire", "Leicestershire", "Knowsley",
						"Torbay", "Wrexham", "Derbyshire", "Darlington",
						"Renfrewshire", "Hammersmith and Fulham", "Highland",
						"Orkney Islands", "West Yorkshire", "Merton",
						"Hertfordshire", "Merseyside", "Bristol", "Bexley",
						"East Renfrewshire", "Edinburgh", "Thurrock",
						"North Lanarkshire", "Barnet", "Conwy",
						"Bracknell Forest", "North Yorkshire", "Milton Keynes",
						"Rotherham", "London", "Angus", "Scottish Borders",
						"Dumfries and Galloway", "Greater London",
						"Tyne and Wear", "Surrey", "Suffolk", "Leeds",
						"Fermanagh", "Oxfordshire", "Moyle", "West Midlands",
						"East Lothian", "Aberdeenshire", "Calderdale",
						"Newport", "Ceredigion", "Limavady", "Clwyd",
						"Dungannon", "Rutland", "Sheffield", "Wokingham",
						"Slough", "Dyfed", "Cookstown", "Richmond upon Thames",
						"Blackpool", "Doncaster", "North Lincolnshire",
						"Denbighshire", "Bolton", "Caerphilly", "Lewisham",
						"Sunderland", "Armagh", "Stockton-on-Tees",
						"North Tyneside", "Greenwich", "Lincolnshire",
						"Solihull", "Vale of Glamorgan", "Grampian Region",
						"Islington", "Oldham", "Magherafelt", "Waltham Forest",
						"Bury", "South Yorkshire", "Northumberland",
						"Middlesbrough", "Southampton", "Durham", "Enfield",
						"Greater Manchester", "Redcar and Cleveland", "Sefton",
						"StHelens", "West Sussex", "Trafford", "Newham",
						"West Glamorgan", "Kensington and Chelsea", "Dorset",
						"Argyll and Bute", "Hillingdon", "East Dunbartonshire",
						"South Gloucestershire", "Neath Port Talbot", "Fife",
						"Down", "Merthyr Tydfil", "Castlereagh",
						"Wolverhampton", "Liverpool", "Plymouth", "Croydon",
						"Ealing", "Brent", "Nottingham", "Norfolk",
						"Lancashire", "Strabane", "Halton", "Camden",
						"Ballymoney", "Windsor and maidenhead",
						"Isles of Scilly", "Coleraine", "Reading", "Wirral",
						"Derry", "Cumbria", "Carrickfergus", "Southwark",
						"Gateshead", "Manchester", "Wiltshire",
						"South Glamorgan", "Gloucestershire",
						"South Lanarkshire", "Essex", "North Down",
						"Warrington", "East Sussex", "Belfast",
						"Herefordshire", "North Somerset", "Antrim",
						"Banbridge", "Carmarthenshire", "Northamptonshire",
						"Walsall", "Clackmannanshire", "South Ayrshire",
						"North Ayrshire", "Bradford", "Ballymena",
						"Dundee City", "Tayside Region", "Glasgow City",
						"Omagh", "Medway", "Warwickshire", "Haringey",
						"South Tyneside", "Kirklees", "Mid Glamorgan",
						"Flintshire", "Ards", "Lambeth", "Shropshire",
						"Telford and Wrekin", "Hounslow", "Craigavon",
						"Birmingham", "Monmouthshire", "Isle of Wight",
						"Portsmouth", "Somerset", "Shetland Islands", "Poole",
						"Sandwell", "West Dunbartonshire", "Borders Region",
						"Aberdeen City", "Berkshire", "Stirling",
						"Southend-on-Sea", "Bath and North East Somerset",
						"Western Isles", "Inverclyde", "Midlothian",
						"Perth and Kinross", "Leicester", "Swansea",
						"Kingston upon Hull", "Wandsworth", "West Berkshire",
						"Tower Hamlets", "Lisburn", "Bournemouth", "Cardiff",
						"Eilean Siar", "Dudley", "North East Lincolnshire",
						"Wigan", "Gwent", "Strathclyde Region", "West Lothian",
						"Redbridge", "Cheshire", "Newtownabbey",
						"Brighton and Hove", "Wakefield",
						"Newcastle upon Tyne", "Bromley", "Stockport",
						"Torfaen", "Newry and Mourne", "Hartlepool",
						"Pembrokeshire", "Hereford and Worcester",
						"Peterborough", "Stoke-on-Trent", "Powys", "Barnsley",
						"Kent", "Westminster", "Bridgend", "Derby", "Cornwall",
						"Gwynedd", "Blackburn with Darwen", "Humberside",
						"York", "Swindon", "Isle of Anglesey" },
				// US
				{ "Alabama", "Alaska", "Arizona", "Arkansas", "California",
						"Colorado", "Connecticut", "Delaware",
						"District of Columbia", "Florida", "Georgia", "Hawaii",
						"Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
						"Kentucky", "Louisiana", "Maine", "Maryland",
						"Massachusetts", "Michigan", "Minnesota",
						"Mississippi", "Missouri", "Montana", "Nebraska",
						"Nevada", "New Hampshire", "New Jersey", "New Mexico",
						"New York", "North Carolina", "North Dakota", "Ohio",
						"Oklahoma", "Oregon", "Pennsylvania", "Rhode Island",
						"South Carolina", "South Dakota", "Tennessee", "Texas",
						"Utah", "Vermont", "Virginia", "Washington",
						"West Virginia", "Wisconsin", "Wyoming"

				},
				// AFGHANISTAN
				{ "Badah_šan", "Badgis", "Baglan", "Balh_", "Bamiyan", "Farah",
						"Faryab", "Gawr", "Gazni", "H_awst", "Herat",
						"Hilmand", "Jawzjan", "Kabul", "Kandahar", "Kapisa",
						"Kunarha", "Kunduz", "Lagman", "Lawgar",
						"Maydan-Wardak", "Nangarhar", "Nimruz", "Nuristan",
						"Paktika", "Paktiya", "Parwan", "Samangan",
						"Sar-e Pul", "Tah_ar", "Uruzgan", "Zabul" },// ALBANIA
				{ "Berat", "Bulqizë", "Delvinë", "Devoll", "Dibrë", "Durrës",
						"Elbasan", "Fier", "Gjirokastër", "Gramsh", "Has",
						"Kavajë", "Kolonjë", "Korçë", "Krujë", "Kuçovë",
						"Kukës", "Kurbin", "Lezhë", "Librazhd", "Lushnjë",
						"Mallakastër", "Malsi e Madhe", "Mat", "Mirditë",
						"Peqin", "Përmet", "Pogradec", "Pukë", "Sarandë",
						"Shkodër", "Skrapar", "Tepelenë", "Tirana", "Tropojë",
						"Vlorë" },
				// ALGERIA
				{ "Adrar", "al-Agwat", "al-Bayad", "al-Buirah", "Algier",
						"al-Jilfah", "al-Masilah", "al-Midyah", "al-Wad",
						"an-Na'amah", "Annabah", "aš-Šalif", "at-Tarif",
						"'Ayn ad-Dafla", "'Ayn Timušanat", "Baššar", "Batnah",
						"Bijayah", "Biskrah", "Blidah", "Bumardas",
						"Burj Bu Arririj", "Galizan", "Gardayah", "H_anšalah",
						"Ilizi", "Jijili", "Milah", "Mu'askar", "Mustaganam",
						"Qalmah", "Qusantinah", "Sa'idah", "Sakikdah", "Satif",
						"Sidi bal'abbas", "Suq Ahras", "Tamanrasat", "Tibazah",
						"Tibissah", "Tilimsan", "Tinduf", "Tisamsilt",
						"Tiyarat", "Tizi Wazu", "Umm-al-Bawagi", "Wahran",
						"Warqla" },
				// AMERICAN SAMOA
				{ "Eastern", "Manu'a", "Swains Island", "Western" },
				// ANDORRA
				{ "Andorra la Vella", "Canillo", "Encamp",
						"Escaldes-Engordany", "La Massana", "Ordino",
						"Sant Julià de Lòria" }
				// ANGOLA
				,
				{ "Bengo", "Benguela", "Bié", "Cabinda", "Cuando-Cubango",
						"Cuanza-Norte", "Cuanza-Sul", "Cunene", "Huambo",
						"Huíla", "Luanda", "Lunda Norte", "Lunda Sul",
						"Malanje", "Moxico", "Namibe", "Uíge", "Zaire" }
				// ANGUILLA
				,
				{ "" }
				// ANTARCTICA
				,
				{}
				// ANTIGUA AND BARBUDA
				,
				{ "Barbuda", "Saint George", "Saint John", "Saint Mary",
						"Saint Paul", "Saint Peter", "Saint Philip" }
				// ARGENTINA
				,
				{ "Buenos Aires", "Catamarca", "Chaco", "Chubut", "Córdoba",
						"Corrientes", "Distrito Federal", "Entre Ríos",
						"Formosa", "Jujuy", "La Pampa", "La Rioja", "Mendoza",
						"Misiones", "Neuquén", "Río Negro", "Salta",
						"San Juan", "San Luis", "Santa Cruz", "Santa Fé",
						"Santiago del Estero", "Tierra del Fuego", "Tucumán" }
				// ARMENIA
				,
				{ "Aragatsotn", "Ararat", "Armavir", "Gegharkunik", "Kotayk",
						"Lori", "Shirak", "Syunik", "Tavush", "Vayots Dzor",
						"Yerevan" }
				// ARUBA
				,
				{ "" }
				// AUSTRIA
				,
				{ "Burgenland", "Kärnten", "Niederösterreich",
						"Oberösterreich", "Salzburg", "Steiermark", "Tirol",
						"Vorarlberg", "Wien" }
				// AUSTRALIA
				,
				{ "Australian Capital Territory", "New South Wales",
						"Northern Territory", "Queensland", "South Australia",
						"Tasmania", "Victoria", "Western Australia" }
				// AZERBAIJAN
				,
				{ "Abseron", "Aran", "Baki", "Dagliq Sirvan", "G?nc?-Qazax",
						"K?lb?c?r-Laçin", "L?nk?ran", "Naxçivan",
						"Quba-Xaçmaz", "S?ki-Zaqatala", "Yuxari Qarabag" }
				// BAHAMAS, THE
				,
				{ "Abaco", "Acklins Island", "Andros", "Berry Islands",
						"Biminis", "Cat Island", "Crooked Island", "Eleuthera",
						"Exuma and Cays", "Grand Bahama", "Inagua Islands",
						"Long Island", "Mayaguana", "New Providence",
						"Ragged Island", "Rum Cay", "San Salvador" }
				// BAHRAIN
				,
				{ "al-Garbiyah", "al-H?idd", "al-Muh?arraq", "al-Wusta",
						"ar-Rifa'a", "aš-Šamaliyah", "'Isa", "Jidh?afs",
						"Madinat H?amad", "Manama", "Sitrah" }
				// BANGLADESH
				,
				{ "Bagar Hat", "Bandarban", "Barguna", "Barisal", "Bhola",
						"Bogora", "Brahman Bariya", "Chandpur", "Chattagam",
						"Chuadanga", "Dhaka", "Dinajpur", "Faridpur", "Feni",
						"Gaybanda", "Gazipur", "Gopalganj", "Habiganj",
						"Jaipur Hat", "Jamalpur", "Jessor", "Jhalakati",
						"Jhanaydah", "Khagrachhari", "Khulna", "Kishorganj",
						"Koks Bazar", "Komilla", "Kurigram", "Kushtiya",
						"Lakshmipur", "Lalmanir Hat", "Madaripur", "Magura",
						"Maimansingh", "Manikganj", "Maulvi Bazar", "Meherpur",
						"Munshiganj", "Naral", "Narayanganj", "Narsingdi",
						"Nator", "Naugaon", "Nawabganj", "Netrakona",
						"Nilphamari", "Noakhali", "Pabna", "Panchagarh",
						"Patuakhali", "Pirojpur", "Rajbari", "Rajshahi",
						"Rangamati", "Rangpur", "Satkhira", "Shariatpur",
						"Sherpur", "Silhat", "Sirajganj", "Sunamganj",
						"Tangayal", "Thakurgaon" }
				// BARBADOS
				,
				{ "Christ Church", "Saint Andrew", "Saint George",
						"Saint James", "Saint John", "Saint Joseph",
						"Saint Lucy", "Saint Michael", "Saint Peter",
						"Saint Philip", "Saint Thomas" }
				// BELARUS
				,
				{ "Christ Church", "Saint Andrew", "Saint George",
						"Saint James", "Saint John", "Saint Joseph",
						"Saint Lucy", "Saint Michael", "Saint Peter",
						"Saint Philip", "Saint Thomas" }
				// BELGIUM
				,
				{ "Antwerpen", "Brabant Wallon", "Brüssel", "Flämisch Brabant",
						"Hennegau", "Limburg", "Lüttich", "Luxemburg", "Namur",
						"Ost-Flandern", "West-Flandern" }
				// BELIZE
				,
				{ "Belize", "Cayo", "Corozal", "Orange Walk", "Stann Creek",
						"Toledo" }
				// BENIN
				,
				{ "Alibori", "Atacora", "Atlantique", "Borgou", "Collines",
						"Couffo", "Donga", "Littoral", "Mono", "Ouémé",
						"Plateau", "Zou" }
				// BERMUDA
				,
				{ "Hamilton", "Saint George" }
				// BHUTAN
				,
				{ "Bumthang", "Chhukha", "Dagana", "Gasa", "Haa", "Lhuntse",
						"Mongar", "Paro", "Pemagatshel", "Punakha",
						"Samdrup Jongkhar", "Samtse", "Sarpang", "Thimphu",
						"Trashigang", "Trashiyangtse", "Trongsa", "Tsirang",
						"Wangdue Phodrang", "Zhemgang" }
				// BOLIVIA
				,
				{ "Tarija" }
				// BOSNIA AND HERZEGOVINA
				,
				{ "Distrikt Brcko", "Federacija Bosna i Hercegovina",
						"Republika Srpska" }
				// BOTSWANA
				,
				{ "Bobonong", "Boteti", "Chobe", "Francistown", "Gaborone",
						"Ghanzi", "Jwaneng", "Kgalagadi North",
						"Kgalagadi South", "Kgatleng", "Kweneng", "Lobatse",
						"Mahalapye", "Ngamiland", "Ngwaketse", "North East",
						"Okavango", "Orapa", "Selibe Phikwe", "Serowe-Palapye",
						"South East", "Sowa", "Tutume" }
				// BOUVET ISLAND
				,
				{}
				// BRAZIL
				,
				{ "Acre", "Alagoas", "Amapá", "Amazonas", "Bahia", "Ceará",
						"Distrito Federal", "Espírito Santo", "Goiás",
						"Maranhão", "Mato Grosso", "Mato Grosso do Sul",
						"Minas Gerais", "Pará", "Paraíba", "Paraná",
						"Pernambuco", "Piauí", "Rio de Janeiro",
						"Rio Grande do Norte", "Rio Grande do Sul", "Rondônia",
						"Roraima", "Santa Catarina", "São Paulo", "Sergipe",
						"Tocantins" }
				// BRITISH INDIAN OCEAN TERRITORY
				,
				{ "Tortola" }
				// BRUNEI
				,
				{ "Belait", "Brunei-Muara", "Temburong", "Tutong" }
				// BULGARIA
				,
				{ "Blagoevgrad", "Burgas", "Dobric", "Gabrovo", "Haskovo",
						"Jambol", "Kardžali", "Kjustendil", "Lovec", "Montana",
						"Pazardžik", "Pernik", "Pleven", "Plovdiv", "Razgrad",
						"Ruse", "Silistra", "Sliven", "Smoljan", "Sofija grad",
						"Sofijska oblast", "Stara Zagora", "Šumen",
						"Targovište", "Varna", "Veliko Tarnovo", "Vidin",
						"Vraca" }
				// BURKINA FASO
				,
				{ "Balé", "Bam", "Banwa", "Bazéga", "Bougouriba", "Boulgou",
						"Boulkiemdé", "Comoé", "Ganzourgou", "Gnagna",
						"Gourma", "Houet", "Ioba", "Kadiogo", "Kénédougou",
						"Komandjoari", "Kompienga", "Kossi", "Koulpélogo",
						"Kouritenga", "Kourwéogo", "Léraba", "Loroum",
						"Mouhoun", "Nahouri", "Namentenga", "Nayala",
						"Noumbiel", "Oubritenga", "Oudalan", "Passoré", "Poni",
						"Sanguié", "Sanmatenga", "Séno", "Sissili", "Soum",
						"Sourou", "Tapoa", "Tuy", "Yagha", "Yatenga", "Ziro",
						"Zondoma", "Zoundwéogo" }
				// BURUNDI
				,
				{ "Bubanza", "Bujumbura", "Bururi", "Cankuzo", "Cibitoke",
						"Gitega", "Karuzi", "Kayanza", "Kirundo", "Makamba",
						"Muramvya", "Muyinga", "Ngozi", "Rutana", "Ruyigi" }
				// CAMBODIA
				,
				{ "Banteay Mean Chey", "Bat Dâmbâng", "Kâmpóng Cham",
						"Kâmpóng Chhnang", "Kâmpóng Spoeu", "Kâmpóng Thum",
						"Kâmpôt", "Kândal", "Kaôh Kông", "Krâchéh",
						"Krong Kaeb", "Krong Pailin", "Krong Preah Sihanouk",
						"Môndôl Kiri", "Otdar Mean Chey", "Phnum Pénh",
						"Pousat", "Preah Vihéar", "Prey Veaeng",
						"Rôtanak Kiri", "Siem Reab", "Stueng Traeng",
						"Svay Rieng", "Takaev" }
				// CAMEROON
				,
				{ "Adamaoua", "Centre", "Est", "Littoral", "Nord",
						"Nord Extrème", "Nordouest", "Ouest", "Sud", "Sudouest" }
				// CANADA
				,
				{ "Alberta", "British Columbia", "Manitoba", "New Brunswick",
						"Newfoundland and Labrador", "Northwest Territories",
						"Nova Scotia", "Nunavut", "Ontario",
						"Prince Edward Island", "Québec", "Saskatchewan",
						"Yukon" }
				// CAPE VERDE
				,
				{ "Boavista", "Brava", "Fogo", "Maio", "Sal", "Santo Antão",
						"São Nicolau", "São Tiago", "São Vicente" }
				// CAYMAN ISLANDS
				,
				{ "Grand Cayman" }
				// CENTRAL AFRICAN REPUBLIC
				,
				{ "Bamingui-Bangoran", "Bangui", "Basse-Kotto", "Haute-Kotto",
						"Haut-Mbomou", "Kémo", "Lobaye", "Mambéré-Kadéï",
						"Mbomou", "Nana-Gribizi", "Nana-Mambéré",
						"Ombella Mpoko", "Ouaka", "Ouham", "Ouham-Pendé",
						"Sangha-Mbaéré", "Vakaga" }
				// CHAD
				,
				{ "Batha", "Biltine", "Bourkou-Ennedi-Tibesti",
						"Chari-Baguirmi", "Guéra", "Kanem", "Lac",
						"Logone Occidental", "Logone Oriental", "Mayo-Kébbi",
						"Moyen-Chari", "Ouaddaï", "Salamat", "Tandjilé" }
				// CHILE
				,
				{ "Aisén", "Antofagasta", "Araucanía", "Atacama", "Bío Bío",
						"Coquimbo", "Libertador General Bernardo O'Higgins",
						"Los Lagos", "Magellanes", "Maule", "Metropolitana",
						"Tarapacá", "Valparaíso" }
				// CHINA
				,
				{ "Anhui", "Aomen", "Beijing", "Chongqing", "Fujian", "Gansu",
						"Guangdong", "Guangxi", "Guizhou", "Hainan", "Hebei",
						"Heilongjiang", "Henan", "Hongkong", "Hubei", "Hunan",
						"Jiangsu", "Jiangxi", "Jilin", "Liaoning", "Neimenggu",
						"Ningxia", "Qinghai", "Shaanxi", "Shandong",
						"Shanghai", "Shanxi", "Sichuan", "Tianjin", "Xinjiang",
						"Xizang", "Yunnan", "Zhejiang" }
				// CHRISTMAS ISLAND
				,
				{}
				// COCOS (KEELING) ISLANDS
				,
				{ "Home Island", "West Island" }
				// COLOMBIA
				,
				{ "Amazonas", "Antioquia", "Arauca", "Atlántico", "Bogotá",
						"Bolívar", "Boyacá", "Caldas", "Caquetá", "Casanare",
						"Cauca", "César", "Chocó", "Córdoba", "Cundinamarca",
						"Guainía", "Guaviare", "Huila", "La Guajira",
						"Magdalena", "Meta", "Nariño", "Norte de Santander",
						"Putumayo", "Quindió", "Risaralda",
						"San Andrés y Providencia", "Santander", "Sucre",
						"Tolima", "Valle del Cauca", "Vaupés", "Vichada" }
				// COMOROS
				,
				{ "Mwali", "Ndzouani", "Ngazidja" }
				// CONGO, DEMOCRATIC REPUBLIC OF THE
				,
				{ "Bandundu", "Bas-Congo", "Équateur", "Haut-Congo",
						"Kasai-Occidental", "Kasai-Oriental", "Katanga",
						"Kinshasa", "Maniema", "Nord-Kivu", "Sud-Kivu" }
				// CONGO, REPUBLIC OF THE
				,
				{ "Bouenza", "Brazzaville", "Cuvette", "Cuvette-Ouest",
						"Kouilou", "Lékoumou", "Likouala", "Niari", "Plateaux",
						"Pool", "Sangha" }
				// COOK ISLANDS
				,
				{ "Aitutaki", "Atiu", "Mangaia", "Manihiki", "Mauke",
						"Mitiaro", "Nassau", "Pukapuka", "Rakahanga",
						"Rarotonga", "Tongareva" }
				// COSTA RICA
				,
				{ "Alajuela", "Cartago", "Guanacaste", "Heredia", "Limón",
						"Puntarenas", "San José" }
				// COTE D'IVOIRE
				,
				{ "Abidjan", "Bouaké", "Daloa", "Korhogo", "Yamoussoukro" }
				// CROATIA
				,
				{ "Bjelovar-Bilogora", "Dubrovnik-Neretva", "Grad Zagreb",
						"Istra", "Karlovac", "Koprivnica-Križevci",
						"Krapina-Zagorje", "Lika-Senj", "Medimurje",
						"Osijek-Baranja", "Požega-Slavonija",
						"Primorje-Gorski Kotar", "Šibenik-Knin",
						"Sisak-Moslavina", "Slavonski Brod-Posavina",
						"Split-Dalmacija", "Varaždin", "Virovitica-Podravina",
						"Vukovar-Srijem", "Zadar", "Zagreb" }
				// CUBA
				,
				{ "Camagüey", "Ciego de Ávila", "Cienfuegos",
						"Ciudad de la Habana", "Granma", "Guantánamo",
						"Holguín", "Isla de la Juventud", "La Habana",
						"Las Tunas", "Matanzas", "Pinar del Río",
						"Sancti Spíritus", "Santiago de Cuba", "Villa Clara" }
				// CYPRUS
				,
				{ "Government controlled area", "Turkish controlled area" }
				// CZECH REPUBLIC
				,
				{ "Jihoceský", "Jihomoravský", "Karlovarský",
						"Královéhradecký", "Liberecký", "Moravskoslezský",
						"Olomoucký", "Pardubický", "Plzenský", "Prag",
						"Stredoceský", "Ústecký", "Vysocina", "Zlínský" }
				// DENMARK
				,
				{ "Hovedstaden", "Midtjylland", "Nordjylland", "Sjælland",
						"Syddanmark" }
				// DJIBOUTI
				,
				{ "Ali Sabieh", "Dikhil", "Djibouti", "Obock", "Tadjoura" }
				// DOMINICA
				,
				{ "Saint Andrew", "Saint David", "Saint George", "Saint John",
						"Saint Joseph", "Saint Luke", "Saint Mark",
						"Saint Patrick", "Saint Paul", "Saint Peter" }
				// DOMINICAN REPUBLIC
				,
				{ "Azua", "Baoruco", "Barahona", "Dajabón", "Duarte",
						"Elías Piña", "El Seibo", "Espaillat", "Hato Mayor",
						"Independencia", "La Altagracia", "La Romana",
						"La Vega", "María Trinidad Sánchez", "Monseñor Nouel",
						"Monte Cristi", "Monte Plata", "Pedernales", "Peravia",
						"Puerto Plata", "Salcedo", "Samaná", "Sánchez Ramírez",
						"San Cristóbal", "San José de Ocoa", "San Juan",
						"San Pedro de Macorís", "Santiago",
						"Santiago Rodríguez", "Santo Domingo", "Valverde" }
				// ECUADOR
				,
				{ "Azuay", "Bolívar", "Cañar", "Carchi", "Chimborazo",
						"Cotopaxi", "El Oro", "Esmeraldas", "Galápagos",
						"Guayas", "Imbabura", "Loja", "Los Ríos", "Manabí",
						"Morona Santiago", "Napo", "Orellana", "Pastaza",
						"Pichincha", "Sucumbíos", "Tungurahua",
						"Zamora Chinchipe" }
				// EGYPT
				,
				{ "ad-Daqahliyah", "al-Bah?r-al-Ah?mar", "al-Buh?ayrah",
						"Alexandria", "al-Fayyum", "al-Garbiyah",
						"al-Ismailiyah", "al-Minufiyah", "al-Minya",
						"al-Qalyubiyah", "al-Wadi al-Jadid", "aš-Šarqiyah",
						"Assiut", "Assuan", "as-Suways", "Bani Suwayf",
						"Bur Sa'id", "Dumyat", "Giseh", "Kafr-aš-Šayh_",
						"Kairo", "Luxor", "Matruh", "Qina", "Šamal Sina",
						"Sawhaj", "South Sinai" }
				// EL SALVADOR
				,
				{ "Ahuachapán", "Cabañas", "Chalatenango", "Cuscatlán",
						"La Libertad", "La Paz", "La Unión", "Morazán",
						"San Miguel", "San Salvador", "Santa Ana",
						"San Vicente", "Sonsonate", "Usulután" }
				// EQUATORIAL GUINEA
				,
				{ "Annobón", "Bioko Norte", "Bioko Sur", "Centro Sur",
						"Kié-Ntem", "Litoral", "Wele-Nzas" }
				// ERITREA
				,
				{ "Anseba", "Debub", "Debub-Keih-Bahri", "Gash-Barka",
						"Maekel", "Semien-Keih-Bahri" }
				// ESTONIA
				,
				{ "Harju", "Hiiu", "Ida-Viru", "Järva", "Jogeva", "Lääne",
						"Lääne-Viru", "Pärnu", "Polva", "Rapla", "Saare",
						"Tartu", "Valga", "Viljandi", "Voru" }
				// ETHIOPIA
				,
				{ "Addis Abeba", "Afar", "Amhara", "Benishangul", "Diredawa",
						"Gambella", "Harar", "Oromia", "Somali", "Southern",
						"Tigray" }
				// FALKLAND ISLANDS (ISLAS MALVINAS)
				,
				{ "Falkland Islands", "South Georgia" }
				// FAROE ISLANDS
				,
				{ "Klaksvík", "Norðara Eysturoy", "Norðoy", "Sandoy",
						"Streymoy", "Suðuroy", "Syðra Eysturoy", "Tórshavn",
						"Vága" }
				// FIJI
				,
				{ "Central", "Eastern", "Northern", "Western" }
				// FINLAND
				,
				{ "Ahvenanmaa", "Etelä-Karjala", "Etelä-Pohjanmaa",
						"Etelä-Savo", "Itä-Uusimaa", "Kainuu", "Kanta-Häme",
						"Keski-Pohjanmaa", "Keski-Suomi", "Kymenlaakso",
						"Lappland", "Päijät-Häme", "Pirkanmaa", "Pohjanmaa",
						"Pohjois-Karjala", "Pohjois-Pohjanmaa", "Pohjois-Savo",
						"Satakunta", "Uusimaa", "Varsinais-Suomi" }
				// FRANCE
				,
				{ "Alsace", "Aquitaine", "Auvergne", "Basse-Normandie",
						"Bourgogne", "Bretagne", "Centre", "Champagne-Ardenne",
						"Corse", "Franche-Comté", "Haute-Normandie",
						"Île-de-France", "Languedoc-Roussillon", "Limousin",
						"Lorraine", "Midi-Pyrénées", "Nord-Pas-de-Calais",
						"Pays-de-la-Loire", "Picardie", "Poitou-Charentes",
						"Provence-Alpes-Côte-d'Azur", "Rhône-Alpes" }
				// FRENCH GUIANA
				,
				{ "Cayenne", "Saint-Laurent-du-Maroni" }
				// FRENCH POLYNESIA
				,
				{ "Îles du Vent", "Îles sous le Vent", "Marquises",
						"Tuamotu-Gambier", "Tubuai" }
				// FRENCH SOUTHERN AND ANTARCTIC LANDS
				,
				{ "Amsterdam", "Crozet Islands", "Kerguelen" }
				// GABON
				,
				{ "Estuaire", "Haut-Ogooué", "Moyen-Ogooué", "Ngounié",
						"Nyanga", "Ogooué-Ivindo", "Ogooué-Lolo",
						"Ogooué-Maritime", "Woleu-Ntem" }
				// GAMBIA, THE
				,
				{ "Banjul", "Basse", "Brikama", "Janjanbureh", "Kanifing",
						"Kerewan", "Kuntaur", "Mansakonko" }
				// GEORGIA
				,
				{ "Abkhasia", "Ajaria", "Guria", "Imereti", "Kakheti",
						"Kvemo Kartli", "Mtskheta-Mtianeti",
						"Raga-Lechkumi and Kverno Svaneti",
						"Samagrelo and Zemo Svaneti", "Samtskhe-Javakheti",
						"Shida Kartli", "Tiflis" }
				// GERMANY
				,
				{ "Baden-Württemberg", "Bayern", "Berlin", "Brandenburg",
						"Bremen", "Hamburg", "Hessen",
						"Mecklenburg-Vorpommern", "Niedersachsen",
						"Nordrhein-Westfalen", "Rheinland-Pfalz", "Saarland",
						"Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein",
						"Thüringen" }
				// GHANA
				,
				{ "Ashanti", "Brong-Ahafo", "Central", "Eastern",
						"Greater Accra", "Northern", "Upper East",
						"Upper West", "Volta", "Western" }
				// GIBRALTAR
				,
				{ "" }
				// GREECE
				,
				{ "Attikí", "Kentriki Ellada", "Nisia Aigaiou Kriti",
						"Voria Ellada" }
				// GREENLAND
				,
				{ "Aasiaat", "Ammassalik", "Illoqqortoormiut", "Ilulissat",
						"Ivittuut", "Kangaatsiaq", "Maniitsoq", "Nanortalik",
						"Narsaq", "Nuuk", "Paamiut", "Qaanaaq", "Qaqortoq",
						"Qasigiannguit", "Qeqertarsuaq", "Sisimiut",
						"Udenfor kommunal inddeling", "Upernavik", "Uummannaq"

				}
				// GRENADA
				,
				{ "Carriacou-Petite Martinique", "Saint Andrew",
						"Saint Davids", "Saint George", "Saint John",
						"Saint Mark", "Saint Patrick"

				}
				// GUADELOUPE
				,
				{ "Basse-Terre", "Grande-Terre", "Îles des Saintes",
						"La Désirade", "Marie-Galante", "Saint Barthélemy",
						"Saint Martin" }
				// GUAM
				,
				{ "Agana Heights", "Agat", "Barrigada", "Chalan-Pago-Ordot",
						"Dededo", "Hagatña", "Inarajan", "Mangilao", "Merizo",
						"Mongmong-Toto-Maite", "Santa Rita", "Sinajana",
						"Talofofo", "Tamuning", "Yigo", "Yona" }
				// GUATEMALA
				,
				{ "Alta Verapaz", "Baja Verapaz", "Chimaltenango",
						"Chiquimula", "El Progreso", "Escuintla", "Guatemala",
						"Huehuetenango", "Izabal", "Jalapa", "Jutiapa",
						"Petén", "Quezaltenango", "Quiché", "Retalhuleu",
						"Sacatepéquez", "San Marcos", "Santa Rosa", "Sololá",
						"Suchitepéquez", "Totonicapán", "Zacapa" }
				// GUINEA
				,
				{ "Basse Guinée", "Conakry", "Guinée Forestière",
						"Haute Guinée", "Moyenne Guinée" }
				// GUINEA-BISSAU
				,
				{ "Bafatá", "Biombo", "Bissau", "Bolama", "Cacheu", "Gabú",
						"Oio", "Quinara", "Tombali" }
				// GUYANA
				,
				{ "Barima-Waini", "Cuyuni-Mazaruni", "Demerara-Mahaica",
						"East Berbice-Corentyne",
						"Essequibo Islands-West Demerara", "Mahaica-Berbice",
						"Pomeroon-Supenaam", "Upper Demerara-Berbice",
						"Upper Takutu-Upper Essequibo" }
				// HAITI
				,
				{ "Artibonite", "Centre", "Grand'Anse", "Nord", "Nord-Est",
						"Nord-Ouest", "Ouest", "Sud", "Sud-Est" }
				// HEARD ISLAND AND MCDONALD ISLANDS
				,
				{}
				// HOLY SEE (VATICAN CITY)
				,
				{}
				// HONDURAS
				,
				{ "Atlántida", "Choluteca", "Colón", "Comayagua", "Copán",
						"Cortés", "Distrito Central", "El Paraíso",
						"Francisco Morazán", "Gracias a Dios", "Intibucá",
						"Islas de la Bahía", "La Paz", "Lempira", "Ocotepeque",
						"Olancho", "Santa Bárbara", "Valle", "Yoro" }
				// HONG KONG
				,
				{ "Hongkong", "Kowloon and New Kowl" }
				// HUNGARY
				,
				{ "Bács-Kiskun", "Baranya", "Békés", "Borsod-Abaúj-Zemplén",
						"Budapest", "Csongrád", "Fejér", "Gyor-Moson-Sopron",
						"Hajdú-Bihar", "Heves", "Jász-Nagykun-Szolnok",
						"Komárom-Esztergom", "Nógrád", "Pest", "Somogy",
						"Szabolcs-Szatmár-Bereg", "Tolna", "Vas", "Veszprém",
						"Zala" }
				// ICELAND
				,
				{ "Austurland", "Höfuðborgarsvæði", "Norðurland eystra",
						"Norðurland vestra", "Suðurland", "Suðurnes",
						"Vestfirðir", "Vesturland" }
				// INDIA
				,
				{ "Andaman and Nicobar Islands", "Andhra Pradesh",
						"Arunachal Pradesh", "Assam", "Bangla", "Bihar",
						"Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli",
						"Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana",
						"Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
						"Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh",
						"Maharashtra", "Manipur", "Meghalaya", "Mizoram",
						"Nagaland", "Orissa", "Pondicherry", "Punjab",
						"Rajasthan", "Sikkim", "Tamil Nadu", "Tripura",
						"Uttaranchal", "Uttar Pradesh" }
				// INDONESIA
				,
				{ "Aceh", "Bali", "Bangka-Belitung", "Banten", "Bengkulu",
						"Gorontalo", "Jakarta", "Jambi", "Jawa Barat",
						"Jawa Tengah", "Jawa Timur", "Kalimantan Barat",
						"Kalimantan Selatan", "Kalimantan Tengah",
						"Kalimantan Timur", "Lampung", "Maluku",
						"Maluku Utara", "Nusa Tenggara Barat",
						"Nusa Tenggara Timur", "Papua", "Riau",
						"Riau Kepulauan", "Sulawesi Selatan",
						"Sulawesi Tengah", "Sulawesi Tenggara",
						"Sulawesi Utara", "Sumatera Barat", "Sumatera Selatan",
						"Sumatera Utara", "Yogyakarta" }
				// IRAN
				,
				{ "Ardabil", "Azarbayejan-e Gharbi", "Azarbayejan-e Sharqi",
						"Bushehr", "Chahar Mahal-e Bakhtiari", "Esfahan",
						"Fars", "Gilan", "Golestan", "Hamadan", "Hormozgan",
						"Ilam", "Kerman", "Kermanshah", "Khorasan-e Razavi",
						"Khorasan Janubi", "Khorasan Shamali", "Khuzestan",
						"Kohgiluyeh-e Boyerahmad", "Kordestan", "Lorestan",
						"Markazi", "Mazandaran", "Qazvin", "Qom", "Semnan",
						"Sistan-e Baluchestan", "Teheran", "Yazd", "Zanjan" }
				// IRAQ
				,
				{ "al-Anbar", "al-Basrah", "al-Mut_anna", "al-Qadisiyah",
						"an-Najaf", "as-Sulaymaniyah", "at-Ta'mim", "Babil",
						"Bagdad", "Dahuk", "Ði Qar", "Diyala", "Irbil",
						"Karbala", "Maysan", "Ninawa", "Salah?-ad-Din", "Wasit" }
				// IRELAND
				,
				{ "Carlow", "Cavan", "Clare", "Cork", "Donegal", "Dublin",
						"Galway", "Kerry", "Kildare", "Kilkenny", "Laois",
						"Leitrim", "Limerick", "Longford", "Louth", "Mayo",
						"Meath", "Monaghan", "Offaly", "Roscommon", "Sligo",
						"Tipperary North Riding", "Tipperary South Riding",
						"Waterford", "Westmeath", "Wexford", "Wicklow" }
				// ISLE OF MAN
				,
				{ "Castletown", "Douglas", "Laxey", "Onchan", "Peel",
						"Port Erin", "Port Saint Mary", "Ramsey" }
				// ISRAEL
				,
				{ "Hadarom", "Haifa", "Hamerkaz", "Haz_afon", "Jerusalem",
						"Judea and Samaria", "Tel Aviv" }
				// ITALY
				,
				{ "Abruzzen", "Apulien", "Basilicata", "Calabria", "Campania",
						"Emilia-Romagna", "Friuli-Venezia Giulia", "Lazio",
						"Ligurien", "Lombardei", "Marken", "Molise",
						"Piemonte", "Sardinien", "Sizilien", "Toscana",
						"Trentino-Alto Adige", "Umbria", "Valle d'Aosta",
						"Veneto" }
				// JAMAICA
				,
				{ "Clarendon", "Hanover", "Kingston", "Manchester", "Portland",
						"Saint Andrew", "Saint Ann", "Saint Catherine",
						"Saint Elizabeth", "Saint James", "Saint Mary",
						"Saint Thomas", "Trelawney", "Westmoreland" }
				// JAN MAYEN
				,
				{ "Länsimaa " }
				// JAPAN
				,
				{ "Aichi", "Akita", "Aomori", "Chiba", "Ehime", "Fukui",
						"Fukuoka", "Fukushima", "Gifu", "Gumma", "Hiroshima",
						"Hokkaido", "Hyogo", "Ibaraki", "Ishikawa", "Iwate",
						"Kagawa", "Kagoshima", "Kanagawa", "Kochi", "Kumamoto",
						"Kyoto", "Mie", "Miyagi", "Miyazaki", "Nagano",
						"Nagasaki", "Nara", "Niigata", "Oita", "Okayama",
						"Okinawa", "Osaka", "Saga", "Saitama", "Shiga",
						"Shimane", "Shizuoka", "Tochigi", "Tokio", "Tokushima",
						"Tottori", "Toyama", "Wakayama", "Yamagata",
						"Yamaguchi", "Yamanashi" }
				// JORDAN
				,
				{ "'Ajlun", "al-'Aqabah", "al-Balqa'", "al-Karak", "al-Mafraq",
						"'Amman", "at-Tafilah", "az-Zarqa'", "Irbid", "Jaraš",
						"Ma'an", "Madaba" }
				// KAZAKHSTAN
				,
				{ "Akmechet", "Almaty", "Aqmola", "Aqtöbe", "Atyrau",
						"Batis Kazakstan", "Mankistau", "Ontüstik Kazakstan",
						"Pavlodar", "Qaragandy", "Qostanay", "Sigis Kazakstan",
						"Soltüstik Kazakstan", "Taraz" }
				// KENYA
				,
				{ "Central", "Coast", "Eastern", "Nairobi", "North Eastern",
						"Nyanza", "Rift Valley", "Western" }
				// KIRIBATI
				,
				{ "Abaiang", "Abemana", "Aranuka", "Arorae", "Banaba", "Beru",
						"Butaritari", "Kiritimati", "Kuria", "Maiana", "Makin",
						"Marakei", "Nikunau", "Nonouti", "Onotoa",
						"Phoenix Islands", "Tabiteuea North",
						"Tabiteuea South", "Tabuaeran", "Tamana",
						"Tarawa North", "Tarawa South", "Teraina" }
				// KOREA, NORTH
				,
				{ "Chagangdo", "Hamgyongbukto", "Hamgyongnamdo",
						"Hwanghaebukto", "Hwanghaenamdo", "Kangwon",
						"Pyonganbukto", "Pyongannamdo", "Pyongyang", "Rason",
						"Yanggang" }
				// KOREA, SOUTH
				,
				{ "Cheju", "Chollabuk", "Chollanam", "Chungchongbuk",
						"Chungchongnam", "Inchon", "Kangwon", "Kwangju",
						"Kyonggi", "Kyongsangbuk", "Kyongsangnam", "Pusan",
						"Soul", "Taegu", "Taejon", "Ulsan" }

				// KUWAIT
				,
				{ "al-Ah?madi", "al-'Asamah", "al-Farwaniyah", "al-Jahra'",
						"Hawalli", "Mubarak al-Kabir" }
				// KYRGYZSTAN
				,
				{ "Batken", "Biskek", "Celalabad", "Çuy", "Issik-Göl", "Narin",
						"Os", "Talas" }
				// LAOS
				,
				{ "Attopu", "Bokeo", "Bolikhamsay", "Champasak", "Houaphanh",
						"Khammouane", "Luang Nam Tha", "Luang Prabang",
						"Oudomxay", "Phongsaly", "Saravan", "Savannakhet",
						"Sekong", "Viangchan Prefecture", "Viangchan Province",
						"Xaignabury", "Xiang Khuang" }
				// LATVIA
				,
				{ "Aizkraukles", "Aluksnes", "Balvu", "Bauskas", "Cesu",
						"Daugavpils", "Daugavpils pilseta", "Dobeles",
						"Gulbenes", "Jekabspils", "Jelgava", "Jelgavas",
						"Jurmala pilseta", "Kraslavas", "Kuldigas", "Liepaja",
						"Liepajas", "Limbažu", "Ludzas", "Madonas", "Ogres",
						"Preilu", "Rezekne", "Rezeknes", "Riga", "Rigas",
						"Saldus", "Talsu", "Tukuma", "Valkas", "Valmieras",
						"Ventspils", "Ventspils pilseta" }
				// LEBANON
				,
				{ "al-Biqa'a", "al-Janub", "an-Nabatiyah", "aš-Šamal",
						"Jabal Lubnan" }
				// LESOTHO
				,
				{ "Berea", "Butha-Buthe", "Leribe", "Mafeteng", "Maseru",
						"Mohale's Hoek", "Mokhotlong", "Qacha's Nek",
						"Quthing", "Thaba-Tseka" }
				// LIBERIA
				,
				{ "Bomi", "Bong", "Grand Bassa", "Grand Cape Mount",
						"Grand Gedeh", "Maryland and Grand Kru", "Montserrado",
						"Nimba" }
				// LIBYA
				,
				{ "Ajdabiya", "al-Butnan", "al-Hizam al-Ah_d?ar",
						"al-Jabal al-Ahd?ar", "al-Jifarah", "al-Jufrah",
						"al-Kufrah", "al-Marj", "al-Marqab", "al-Qubbah",
						"al-Wah?at", "an-Nuqat al-Hums", "az-Zawiyah",
						"Bangazi", "Bani Walid", "Darnah", "Gadamis", "Garyan",
						"Gat", "Marzuq", "Misratah", "Mizdah", "Nalut",
						"Sabha", "Sabratah wa Surman", "Surt", "Tarabulus",
						"Tarhunah wa Masallatah", "Wadi al-H?ayat",
						"Wadi aš-Šati", "Yafran wa Jadu" }
				// LIECHTENSTEIN
				,
				{ "Balzers", "Eschen", "Gamprin", "Mauren", "Planken",
						"Ruggell", "Schaan", "Schellenberg", "Triesen",
						"Triesenberg", "Vaduz", }
				// LITHUANIA
				,
				{ "Kaunas", "Klaipeda", "Panevezys", "Šiauliai", "Vilna" }
				// LUXEMBOURG
				,
				{ "Capellen", "Clervaux", "Diekirch", "Echternach",
						"Esch-sur-Alzette", "Grevenmacher", "Luxemburg",
						"Mersch", "Redange", "Remich", "Vianden", "Wiltz" }
				// MACAU
				,
				{ "Macau" }
				// MACEDONIA
				,
				{ "Berovo", "Bitola", "Brod", "Debar", "Delcevo",
						"Demir Hisar", "Gevgelija", "Gostivar", "Kavadarci",
						"Kicevo", "Kocani", "Kratovo", "Kriva Palanka",
						"Kruševo", "Kumanovo", "Negotino", "Ohrid", "Prilep",
						"Probištip", "Radoviš", "Resen", "Skopje", "Štip",
						"Struga", "Strumica", "Sveti Nikole", "Tetovo",
						"Valandovo", "Veles", "Vinica" }
				// MADAGASCAR
				,
				{ "Antananarivo", "Antsiranana", "Fianarantsoa", "Mahajanga",
						"Toamasina", "Toliary" }
				// MALAWI
				,
				{ "Balaka", "Blantyre City", "Chikwawa", "Chiradzulu",
						"Chitipa", "Dedza", "Dowa", "Karonga", "Kasungu",
						"Lilongwe City", "Machinga", "Mangochi", "Mchinji",
						"Mulanje", "Mwanza", "Mzimba", "Mzuzu City",
						"Nkhata Bay", "Nkhotakota", "Nsanje", "Ntcheu",
						"Ntchisi", "Phalombe", "Rumphi", "Salima", "Thyolo",
						"Zomba Municipality" }
				// MALAYSIA
				,
				{ "Johor", "Kedah", "Kelantan", "Kuala Lumpur", "Labuan",
						"Melaka", "Negeri Sembilan", "Pahang", "Perak",
						"Perlis", "Pulau Pinang", "Sabah", "Sarawak",
						"Selangor", "Terengganu" }
				// MALDIVES
				,
				{ "Alif Alif", "Alif Dhaal", "Baa", "Dhaal", "Faaf",
						"Gaaf Alif", "Gaaf Dhaal", "Ghaviyani", "Haa Alif",
						"Haa Dhaal", "Kaaf", "Laam", "Lhaviyani", "Malé",
						"Miim", "Nuun", "Raa", "Shaviyani", "Siin", "Thaa",
						"Vaav" }
				// MALI
				,
				{ "Bamako", "Gao", "Kayes", "Kidal", "Koulikoro", "Mopti",
						"Ségou", "Sikasso", "Tombouctou" }
				// MALTA
				,
				{ "Gozo and Comino", "Northern", "Northern Harbour",
						"South Eastern", "Southern Harbour", "Western" }
				// MARSHALL ISLANDS
				,
				{ "Ailinlaplap", "Ailuk", "Arno", "Aur", "Bikini", "Ebon",
						"Enewetak", "Jabat", "Jaluit", "Kili", "Kwajalein",
						"Lae", "Lib", "Likiep", "Majuro", "Maloelap", "Mejit",
						"Mili", "Namorik", "Namu", "Rongelap", "Ujae", "Utrik",
						"Wotho", "Wotje" }
				// MARTINIQUE
				,
				{ "Fort-de-France", "La Trinité", "Le Marin", "Saint-Pierre" }
				// MAURITANIA
				,
				{ "Adrar", "Assaba", "Brakna", "Ðah_lat Nawadibu",
						"Guidimagha", "Gurgul", "Hud-al-Garbi", "Hud-aš-Šarqi",
						"Inširi", "Nawakšut", "Takant", "Tiris Zammur",
						"Trarza" }
				// MAURITIUS
				,
				{ "Black River", "Flacq", "Grand Port", "Moka",
						"Pamplemousses", "Plaines Wilhelm",
						"Riviere du Rempart", "Rodrigues", "Savanne" }
				// MAYOTTE
				,
				{ "Mayotte", "Pamanzi" }
				// MEXICO
				,
				{ "Aguascalientes", "Baja California", "Baja California Sur",
						"Campeche", "Chiapas", "Chihuahua", "Coahuila",
						"Colima", "Distrito Federal", "Durango", "Guanajuato",
						"Guerrero", "Hidalgo", "Jalisco", "México",
						"Michoacán", "Morelos", "Nayarit", "Nuevo León",
						"Oaxaca", "Puebla", "Querétaro", "Quintana Roo",
						"San Luis Potosí", "Sinaloa", "Sonora", "Tabasco",
						"Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán",
						"Zacatecas" }
				// MICRONESIA, FEDERATED STATES OF
				,
				{ "Chuuk", "Kusaie", "Pohnpei", "Yap" }
				// MOLDOVA
				,
				{ "Anenii Noi", "Balti", "Basarabeasca", "Briceni", "Cahul",
						"Calarasi", "Camenca", "Cantemir", "Causeni",
						"Chisinau", "Cimislia", "Criuleni", "Donduseni",
						"Drochia", "Dubasari municipiu", "Edinet", "Falesti",
						"Floresti", "Gagauzia", "Glodeni", "Grigoriopol",
						"Hîncesti", "Ialoveni", "Leova", "Nisporeni", "Ocnita",
						"Orhei", "Rezina", "Rîbnita", "Rîscani", "Sîngerei",
						"Slobozia", "Soldanesti", "Soroca", "Stefan Voda",
						"Straseni", "Taraclia", "Telenesti", "Tighina",
						"Tiraspol", "Ungheni" }
				// MONACO
				,
				{ "Fontvieille", "La Condamine", "Monaco-Ville", "Monte Carlo" }
				// MONGOLIA
				,
				{ "Arhangaj", "Bajanhongor", "Bajan-Ölgij", "Bulgan",
						"Darhan-Uul", "Dornod", "Dornogovi", "Dundgovi",
						"Govi-Altaj", "Govisumber", "Hèntij", "Hovd",
						"Hövsgöl", "Ömnögovi", "Orhon", "Övörhangaj",
						"Sèlèngè", "Sühbaatar", "Töv", "Ulaanbaatar", "Uvs",
						"Zavhan" }
				// MONTSERRAT
				,
				{ "Plymouth " }
				// MOROCCO
				,
				{ "Casablanca", "Chaouia-Ouardigha", "Doukkala-Abda",
						"Fès-Boulemane", "Gharb-Chrarda-Béni Hssen", "Guelmim",
						"Marrakech-Tensift-Al Haouz", "Meknes-Tafilalet",
						"Oriental", "Rabat-Salé-Zammour-Zaer",
						"Souss Massa-Draâ", "Tadla-Azilal", "Tangier-Tétouan",
						"Taza-Al Hoceima-Taounate" }
				// MOZAMBIQUE
				,
				{ "Cabo Delgado", "Gaza", "Inhambane", "Manica", "Maputo",
						"Maputo Provincia", "Nampula", "Niassa", "Sofala",
						"Tete", "Zambezia" }
				// NAMIBIA
				,
				{ "Caprivi", "Erongo", "Hardap", "Karas", "Kavango", "Khomas",
						"Kunene", "Ohangwena", "Omaheke", "Omusati", "Oshana",
						"Oshikoto", "Otjozondjupa" }
				// NAURU
				,
				{ "Aiwo" }
				// NEPAL
				,
				{ "Baglung", "Banke", "Bara", "Bardiya", "Bhaktapur",
						"Chitwan", "Dadeldhura", "Dailekh", "Dang Deokhuri",
						"Darchula", "Dhankuta", "Dhanusa", "Dolakha", "Doti",
						"Gorkha", "Ilam", "Jhapa", "Jumla", "Kailali",
						"Kanchanpur", "Kapilvastu", "Kaski", "Kathmandu",
						"Kavrepalanchok", "Lalitpur", "Mahottari", "Makwanpur",
						"Morang", "Nawalparasi", "Nuwakot", "Palpa", "Parsa",
						"Rautahat", "Rupandehi", "Sankhuwasabha", "Saptari",
						"Sarlahi", "Sindhuli", "Siraha", "Sunsari", "Surkhet",
						"Syangja", "Tanahu", "Udayapur" }
				// NETHERLANDS
				,
				{ "Drenthe", "Flevoland", "Friesland", "Gelderland",
						"Groningen", "Limburg", "Noord-Brabant",
						"Noord-Holland", "Overijssel", "Utrecht", "Zeeland",
						"Zuid-Holland" }
				// NETHERLANDS ANTILLES
				,
				{ "Bonaire", "Curaçao", "Saba", "Sint Eustatius",
						"Sint Maarten" }
				// NEW CALEDONIA
				,
				{ "Îles", "Nord", "Sud" }
				// NEW ZEALAND
				,
				{ "Auckland", "Bay of Plenty", "Canterbury", "Gisborne",
						"Hawke's Bay", "Manawatu-Wanganui", "Marlborough",
						"Nelson", "Northland", "Otago", "Southland",
						"Taranaki", "Tasman", "Waikato", "Wellington",
						"West Coast" },
				// NICARAGUA
				{ "Atlántico Norte", "Atlántico Sur", "Boaco", "Carazo",
						"Chinandega", "Chontales", "Estelí", "Granada",
						"Jinotega", "León", "Madriz", "Managua", "Masaya",
						"Matagalpa", "Nueva Segovia", "Río San Juan", "Rivas" },
				// NIGER
				{ "Agadez", "Diffa", "Dosso", "Maradi", "Niamey", "Tahoua",
						"Tillabéry", "Zinder" },
				// NIGERIA
				{ "Abia", "Abuja Federal Capital Territory", "Adamawa",
						"Akwa Ibom", "Anambra", "Bauchi", "Bayelsa", "Benue",
						"Borno", "Cross River", "Delta", "Ebonyi", "Edo",
						"Ekiti", "Enugu", "Gombe", "Imo", "Jigawa", "Kaduna",
						"Kano", "Katsina", "Kebbi", "Kogi", "Kwara", "Lagos",
						"Nassarawa", "Niger", "Ogun", "Ondo", "Osun", "Oyo",
						"Plateau", "Rivers", "Sokoto", "Taraba", "Yobe",
						"Zamfara" },
				// NIUE
				{ "" },
				// NORFOLK ISLAND
				{ "" },
				// NORTHERN MARIANA ISLANDS
				{ "Northern Islands", "Rota", "Saipan", "Tinian" },
				// NORWAY
				{ "Akershus", "Aust-Agder", "Buskerud", "Finnmark", "Hedmark",
						"Hordaland", "Møre og Romsdal", "Nordland",
						"Nord-Trøndelag", "Oppland", "Oslo", "Østfold",
						"Rogaland", "Sogn og Fjordane", "Sør-Trøndelag",
						"Telemark", "Troms", "Vest-Agder", "Vestfold" },
				// OMAN
				{ "ad-Dah_iliyah", "al-Batinah", "aš-Šarqiyah", "az?-Z?ahirah",
						"Maskat", "Musandam", "Z?ufar" },
				// PAKISTAN
				{ "Azad Kashmir", "Baluchistan", "Federal Capital Area",
						"Federally administered Tribal Areas",
						"Northern Areas", "North-West Frontier", "Punjab",
						"Sind" },
				// PALAU
				{ "Aimeliik", "Airai", "Angaur", "Hatobohei", "Kayangel",
						"Koror", "Melekeok", "Ngaraard", "Ngardmau",
						"Ngaremlengui", "Ngatpang", "Ngchesar", "Ngerchelong",
						"Ngiwal", "Peleliu", "Sonsorol" },
				// PANAMA
				{ "Bocas del Toro", "Chiriquí", "Coclé", "Colón", "Darién",
						"Emberá", "Herrera", "Kuna de Madungandí",
						"Kuna de Wargandí", "Kuna Yala", "Los Santos",
						"Ngöbe Buglé", "Panamá", "Veraguas" }
				// PAPUA NEW GUINEA
				,
				{ "Eastern Highlands", "East New Britain", "East Sepik",
						"Enga", "Fly River", "Gulf", "Madang", "Manus",
						"Milne Bay", "Morobe", "National Capital District",
						"New Ireland", "North Solomons", "Oro", "Sandaun",
						"Simbu", "Southern Highlands", "Western Highlands",
						"West New Britain" },
				// PARAGUAY

				{ "Alto Paraguay", "Alto Paraná", "Amambay", "Asunción",
						"Boquerón", "Caaguazú", "Caazapá", "Canendiyú",
						"Central", "Concepción", "Cordillera", "Guairá",
						"Itapúa", "Misiones", "Ñeembucú", "Paraguarí",
						"Presidente Hayes", "San Pedro" },
				// PERU

				{ "Amazonas", "Ancash", "Apurímac", "Arequipa", "Ayacucho",
						"Cajamarca", "Callao", "Cusco", "Huancavelica",
						"Huánuco", "Ica", "Junín", "La Libertad", "Lambayeque",
						"Lima Provincias", "Loreto", "Madre de Dios",
						"Moquegua", "Pasco", "Piura", "Puno", "San Martín",
						"Tacna", "Tumbes", "Ucayali" },
				// PHILIPPINES

				{ "Bicol", "Cagayan Valley", "Caraga", "Central Luzon",
						"Central Mindanao", "Central Visayas", "Cordillera",
						"Eastern Visayas", "Ilocos", "Muslim Mindanao",
						"National Capital Region", "Northern Mindanao",
						"Southern Mindanao", "Southern Tagalog",
						"Western Mindanao", "Western Visayas" },

				// POLAND
				{ "Dolnoslaskie", "Kujawsko-Pomorskie", "Lódzkie", "Lubelskie",
						"Lubuskie", "Malopolskie", "Mazowieckie", "Opolskie",
						"Podkarpackie", "Podlaskie", "Pomorskie", "Slaskie",
						"Swietokrzyskie", "Warminsko-Mazurskie",
						"Wielkopolskie", "Zachodnio-Pomorskie" },
				// PORTUGAL

				{ "Açores", "Alentejo", "Algarve", "Centro",
						"Lisboa e Vale do Tejo", "Madeira", "Norte" },
				// PUERTO RICO

				{ "Arecibo", "Bayamón", "Carolina", "Guayama", "Humacao",
						"Mayagüez-Aguadilla", "Ponce", "San Juan" },
				// QATAR

				{ "al-Guwayriyah", "al-H_awr", "al-Jumayliyah", "al-Wakrah",
						"ar-Rayyan", "aš-Šamal", "Doha", "Jariyan al-Batnah",
						"Musay'id", "Umm Salal" },
				// REUNION
				{ "Saint-Benoît", "Saint-Denis", "Saint-Paul", "Saint-Pierre" },
				// ROMANIA
				{ "Alba", "Arad", "Arges", "Bacau", "Bihor", "Bistrita-Nasaud",
						"Botosani", "Braila", "Brasov", "Bukarest", "Buzau",
						"Calarasi", "Caras-Severin", "Cluj", "Constanta",
						"Covasna", "Dâmbovita", "Dolj", "Galati", "Giurgiu",
						"Gorj", "Harghita", "Hunedoara", "Ialomita", "Iasi",
						"Ilfov", "Maramures", "Mehedinti", "Mures", "Neamt",
						"Olt", "Prahova", "Salaj", "Satu Mare", "Sibiu",
						"Suceava", "Teleorman", "Timis", "Tulcea", "Vâlcea",
						"Vaslui", "Vrancea" },
				// RUSSIA
				{ "Adygeja", "Aga", "Alanija", "Altaj", "Amur", "Arhangelsk",
						"Astrahan", "Baškortostan", "Belgorod", "Brjansk",
						"Burjatija", "Cecenija", "Celjabinsk", "Cita",
						"Cukotka", "Cuvašija", "Dagestan", "Evenkija",
						"Gorno-Altaj", "Habarovsk", "Hakasija",
						"Hanty-Mansija", "Ingušetija", "Irkutsk", "Ivanovo",
						"Jamalo-Nenets", "Jaroslavl", "Jevrej",
						"Kabardino-Balkarija", "Kaliningrad", "Kalmykija",
						"Kaluga", "Kamcatka", "Karacaj-Cerkessija", "Karelija",
						"Kemerovo", "Kirov", "Komi", "Komi-Permjak",
						"Korjakija", "Kostroma", "Krasnodar", "Krasnojarsk",
						"Kurgan", "Kursk", "Leningrad", "Lipeck", "Magadan",
						"Marij El", "Mordovija", "Moskau",
						"Moskovskaja Oblast", "Murmansk", "Nenets",
						"Nižnij Novgorod", "Novgorod", "Novosibirsk", "Omsk",
						"Orenburg", "Orjol", "Penza", "Perm", "Primorje",
						"Pskov", "Rjazan", "Rostov", "Saha", "Sahalin",
						"Samara", "Sankt Petersburg", "Saratov", "Smolensk",
						"Stavropol", "Sverdlovsk", "Tajmyr", "Tambov",
						"Tatarstan", "Tjumen", "Tomsk", "Tula", "Tver", "Tyva",
						"Udmurtija", "Uljanovsk", "Ust-Orda", "Vladimir",
						"Volgograd", "Vologda", "Voronež" },

				// RWANDA
				{ "Butare", "Byumba", "Cyangugu", "Gikongoro", "Gisenyi",
						"Gitarama", "Kibungo", "Kibuye", "Ruhengeri",
						"Ville de Kigali" },
				// SAINT HELENA
				{ "Ascension", "Saint Helena", "Tristan da Cunha" },
				// SAINT KITTS AND NEVIS
				{ "Christ Church Nichola Town", "Saint Anne Sandy Point",
						"Saint George Basseterre", "Saint George Gingerland",
						"Saint James Windward", "Saint John Capesterre",
						"Saint John Figtree", "Saint Mary Cayon",
						"Saint Paul Capesterre", "Saint Paul Charlestown",
						"Saint Peter Basseterre", "Saint Thomas Lowland",
						"Saint Thomas Middle Island", "Trinity Palmetto Point" },
				// SAINT LUCIA
				{ "Anse-la-Raye", "Canaries", "Castries", "Choiseul",
						"Dennery", "Gros Inlet", "Laborie", "Micoud",
						"Soufrière", "Vieux Fort" },
				// SAINT PIERRE AND MIQUELON
				{ "Miquelon-Langlade", "Saint-Pierre" },
				// SAINT VINCENT AND THE GRENADINES
				{ "Charlotte", "Grenadines", "Saint David", "Saint George" },
				// SAMOA
				{ "Apia Urban Area", "North West Upolu", "Rest of Upolu",
						"Savaii" },
				// SAN MARINO
				{ "Acquaviva", "Borgo Maggiore", "Chiesanuova", "Domagnano",
						"Faetano", "Fiorentino", "Montegiardino", "San Marino",
						"Serravalle" },
				// SAO TOME AND PRINCIPE
				{ "Água Grande", "Cantagalo", "Caué", "Lemba", "Lobata",
						"Mé-Zochi", "Pagué" },
				// SAUDI ARABIA
				{ "al-Bah?ah", "al-H?udud-aš-Šamaliyah", "al-Jawf",
						"al-Madinah", "al-Qasim", "'Asir", "aš-Šarqiyah",
						"H?a'il", "Jizan", "Makkah", "Najran", "Riad", "Tabuk" },
				// SENEGAL
				{ "Dakar", "Diourbel", "Fatick", "Kaolack", "Kolda", "Louga",
						"Matam", "Saint-Louis", "Tambacounda", "Thiès",
						"Ziguinchor" },
				// SERBIA AND MONTENEGRO
				{ "Central Serbia", "Vojvodina" },
				// SEYCHELLES
				{ "Mahé" },
				// SIERRA LEONE
				{ "Eastern", "Northern", "Southern", "Western" },
				// SINGAPORE
				{ " " },
				// SLOVAKIA
				{ "Banskobystrický", "Bratislavský", "Košický", "Nitrianský",
						"Prešovský", "Trencianský", "Trnavský", "Žilinský" },
				// SLOVENIA
				{ "Gorenjska", "Goriška", "Jugovžodna Slovenija", "Koroška",
						"Notranjsko-kraška", "Obalno-kraška",
						"Osrednjeslovenska", "Podravska", "Pomurska",
						"Savinjska", "Spodnjeposavska", "Zasavska" },
				// SOLOMON ISLANDS
				{ "Central", "Choiseul", "Guadalcanal", "Isabel",
						"Makira and Ulawa", "Malaita", "Rennell and Bellona",
						"Temotu", "Western" },
				// SOMALIA
				{ "Awdaal", "Baakool", "Baarii", "Baay", "Banaadir",
						"Gaalguuduud", "Gedoo", "Hiiraan", "Jubbada Dhexe",
						"Jubbada Hoose", "Mudug", "Nuugaal", "Sanaag",
						"Shabeellaha Dhexe", "Shabeellaha Hoose", "Sool",
						"Togdeer", "Woqooyi Galbeed" },
				// SOUTH AFRICA
				{ "Eastern Cape", "Free State", "Gauteng", "KwaZulu Natal",
						"Limpopo", "Mpumalanga", "Northern Cape", "North West",
						"Western Cape" },
				// SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS
				{},
				// SPAIN
				{ "Andalusien", "Aragonien", "Asturien", "Balearen",
						"Baskenland", "Ceuta", "Extremadura", "Galizien",
						"Kanaren", "Kantabrien", "Kastilien-La Mancha",
						"Kastilien-León", "Katalonien", "La Rioja", "Madrid",
						"Melilla", "Murcia", "Navarra", "Valencia" },
				// SRI LANKA

				{ "Amparai", "Anuradhapuraya", "Badulla", "Colombo", "Galla",
						"Gampaha", "Hambantota", "Kalatura", "Kegalla",
						"Kilinochchi", "Kurunegala", "Mad?akalpuwa",
						"Maha Nuwara", "Mannarama", "Matale", "Matara",
						"Monaragala", "Nuwara Eliya", "Puttalama",
						"Ratnapuraya", "Tirikunamalaya", "Vavuniyawa",
						"Yapanaya" },
				// SUDAN

				{ "A'ali-an-Nil", "al-Bah?r-al-Ah?mar", "al-Buh?ayrat",
						"al-Jazirah", "al-Qad?arif", "al-Wah?dah",
						"an-Nil-al-Abyad?", "an-Nil-al-Azraq", "aš-Šamaliyah",
						"Bah?r-al-Jabal", "Garb-al-Istiwa'iyah",
						"Garb Bah?r-al-Gazal", "Garb Darfur", "Garb Kurdufan",
						"Janub Darfur", "Janub Kurdufan", "Junqali", "Kassala",
						"Khartum", "Nahr-an-Nil", "Šamal Bah?r-al-Gazal",
						"Šamal Darfur", "Šamal Kurdufan",
						"Šarq-al-Istiwa'iyah", "Sinnar", "Warab" },
				// SURINAME
				{ "Brokopondo", "Commewijne", "Coronie", "Marowijne",
						"Nickerie", "Para", "Paramaribo", "Saramacca", "Wanica" },
				// SVALBARD
				{ "Jan Mayen", "Svalbard" },
				// SWAZILAND
				{ "Hhohho", "Lubombo", "Manzini", "Shiselweni" },
				// SWEDEN
				{ "Blekinge", "Dalarna", "Gävleborg", "Gotland", "Halland",
						"Jämtland", "Jönköping", "Kalmar", "Kronoberg",
						"Norrbotten", "Örebro", "Östergötland", "Skåne",
						"Södermanland", "Stockholm", "Uppsala", "Värmland",
						"Västerbotten", "Västernorrland", "Västmanland",
						"Västra Götaland" },
				// SWITZERLAND
				{ "Aargau", "Appenzell-Ausser Rhoden",
						"Appenzell Inner-Rhoden", "Basel-Landschaft",
						"Basel-Stadt", "Bern", "Freiburg", "Genf", "Glarus",
						"Graubünden", "Jura", "Luzern", "Neuenburg",
						"Nidwalden", "Obwalden", "Sankt Gallen",
						"Schaffhausen", "Schwyz", "Solothurn", "Tessin",
						"Thurgau", "Uri", "Waadt", "Wallis", "Zug", "Zürich" },

				// SYRIA
				{ "al-H?asakah", "al-Ladiqiyah", "al-Qunaytirah", "ar-Raqqah",
						"as-Suwayda", "Damaskus", "Dar'a", "Dayr-az-Zawr",
						"H?alab", "H?amah", "H?ims", "Idlib", "Tartus" },

				// TAIWAN
				{ "Changhwa", "Chiayi Hsien", "Chiayi Shih", "Hsinchu Hsien",
						"Hsinchu Shih", "Hualien", "Ilan", "Kaohsiung Hsien",
						"Kaohsiung Shih", "Keelung Shih", "Kinmen", "Miaoli",
						"Nantou", "Penghu", "Pingtung", "Taichung Hsien",
						"Taichung Shih", "Tainan Hsien", "Tainan Shih",
						"Taipei Hsien", "Taitung", "Taoyuan", "Yunlin" }
				// TAJIKISTAN
				,
				{ "Dushanbe", "Gorno-Badakhshan", "Karotegin", "Khatlon",
						"Sughd" }
				// TANZANIA
				,
				{ "Arusha", "Dar es Salaam", "Dodoma", "Iringa", "Kagera",
						"Kigoma", "Kilimanjaro", "Lindi", "Manyara", "Mara",
						"Mbeya", "Morogoro", "Mtwara", "Mwanza", "Pwani",
						"Rukwa", "Ruvuma", "Shinyanga", "Singida", "Tabora",
						"Tanga", "Zanzibar and Pemba" }
				// THAILAND
				,
				{ "Central", "Krung Thep", "Northeastern", "Northern",
						"Southern" }
				// TIMOR-LESTE
				,
				{}
				// TOGO
				,
				{ "Centre", "Kara", "Maritime", "Plateaux", "Savanes" }
				// TOKELAU
				,
				{ "Atafu", "Fakaofo", "Nukunonu" }
				// TONGA
				,
				{ "Eua", "Ha'apai", "Niuas", "Tongatapu", "Vava'u" }
				// TRINIDAD AND TOBAGO
				,
				{ "Arima", "Chaguanas", "Couva-Tabaquite-Talparo",
						"Diego Martín", "Mayaro-Río Claro", "Peñal Débé",
						"Point Fortín", "Port of Spain", "Princes Town",
						"San Fernando", "Sangre Grande",
						"San Juan-Laventville", "Siparia", "Tobago",
						"Tunapuna-Piarco" }
				// TUNISIA
				,
				{ "al-Kaf", "al-Mahdiyah", "al-Munastir", "al-Qasrayn",
						"al-Qayrawan", "Aryanah", "Bajah", "Bin 'Arus",
						"Binzart", "Jundubah", "Madaniyin", "Manubah", "Nabul",
						"Qabis", "Qafsah", "Qibili", "Safaqis", "Sidi Bu Zayd",
						"Silyanah", "Susah", "Tatawin", "Tawzar", "Tunis",
						"Zagwan" }
				// TURKEY
				,
				{ "Adana", "Adiyaman", "Afyonkarahisar", "Agri", "Aksaray",
						"Amasya", "Ankara", "Antalya", "Ardahan", "Artvin",
						"Aydin", "Balikesir", "Bartin", "Batman", "Bayburt",
						"Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur",
						"Bursa", "Çanakkale", "Çankiri", "Çorum", "Denizli",
						"Diyarbakir", "Düzce", "Edirne", "Elazig", "Erzincan",
						"Erzurum", "Eskisehir", "Gaziantep", "Giresun",
						"Gümüshane", "Hakkari", "Hatay", "Igdir", "Isparta",
						"Istanbul", "Izmir", "Kahramanmaras", "Karabük",
						"Karaman", "Kars", "Kastamonu", "Kayseri", "Kilis",
						"Kirikkale", "Kirklareli", "Kirsehir", "Kocaeli",
						"Konya", "Kütahya", "Malatya", "Manisa", "Mardin",
						"Mersin", "Mugla", "Mus", "Nevsehir", "Nigde", "Ordu",
						"Osmaniye", "Rize", "Sakarya", "Samsun", "Sanliurfa",
						"Siirt", "Sinop", "Sirnak", "Sivas", "Tekirdag",
						"Tokat", "Trabzon", "Tunceli", "Usak", "Van", "Yalova",
						"Yozgat", "Zonguldak" }
				// TURKMENISTAN
				,
				{ "Ahal", "Asgabat", "Balkan", "Dasoguz", "Lebap", "Mari" }
				// TURKS AND CAICOS ISLANDS
				,
				{ "Grand Turk", "Middle Caicos", "North Caicos",
						"Providenciales and West Caicos", "Salt Cay",
						"South Caicos and East Caicos" }
				// TUVALU
				,
				{ "Funafuti", "Nanumanga", "Nanumea", "Niutao", "Nui",
						"Nukufetau", "Nukulaelae", "Vaitupu" }
				// UGANDA
				,
				{ "Central", "Eastern", "Northern", "Western" }
				// UKRAINE
				,
				{ "Central", "Eastern", "Northern", "Western" }
				// UNITED ARAB EMIRATES
				,
				{ "Abu Dhabi", "'Ajman", "al-Fujayrah", "aš-Šariqah", "Dubai",
						"Ra's al-H_aymah", "Umm al-Qaywayn" }
				// URUGUAY
				,
				{ "Artigas", "Canelones", "Cerro Largo", "Colonia", "Durazno",
						"Flores", "Florida", "Lavalleja", "Maldonado",
						"Montevideo", "Paysandú", "Río Negro", "Rivera",
						"Rocha", "Salto", "San José", "Soriano", "Tacuarembó",
						"Treinta y Tres" }
				// UZBEKISTAN
				,
				{ "Andijon", "Buhoro", "Cizah", "Fargona", "Horazm",
						"Kaskadarya", "Korakalpogiston", "Namangan", "Navoi",
						"Samarkand", "Sirdare", "Surhondar", "Taschkent" }
				// VANUATU
				,
				{ "Malampa", "Penama", "Sanma", "Shefa", "Tafea", "Torba" }
				// VENEZUELA
				,
				{ "Amazonas", "Anzoátegui", "Apure", "Aragua", "Barinas",
						"Bolívar", "Carabobo", "Cojedes", "Delta Amacuro",
						"Distrito Capital", "Falcón", "Guárico", "Lara",
						"Mérida", "Miranda", "Monagas", "Nueva Esparta",
						"Portuguesa", "Sucre", "Táchira", "Trujillo", "Vargas",
						"Yaracuy", "Zulia" }
				// VIETNAM
				,
				{ "B?c Trung B?", "Ð?ng b?ng sông C?u Long",
						"Ð?ng b?ng sông H?ng", "Ðông B?c B?", "Ðông Nam B?",
						"Duyên h?i Nam Trung B?", "Tây B?c B?", "Tây Nguyên" }
				// VIRGIN ISLANDS
				,
				{ "Saint Croix", "Saint John", "Saint Thomas" }
				// WALLIS AND FUTUNA
				,
				{ "Alo", "Hahake", "Hihifo", "Mua", "Sigave" }
				// WESTERN SAHARA
				,
				{ "al-'Ayun", "as-Samarah", "Bu Jaydur", "Wad-ad-Ðahab" }
				// YEMEN
				,
				{ "Abyan", "ad-Dali'", "Aden", "al-Bayda", "al-H?udaydah",
						"al-Jawf", "al-Mahrah", "al-Mahwit",
						"Amanah al-'Asmah", "Amran", "Ðamar", "Hadramaut",
						"Hajjah", "Ibb", "Lahij", "Ma'rib", "Raymah", "Šabwah",
						"Sa'dah", "San'a", "Ta'izz" }
				// ZAMBIA
				,
				{ "Central", "Copperbelt", "Eastern", "Luapala", "Lusaka",
						"Northern", "North-Western", "Southern", "Western" },
				// ZIMBABWE,
				{ "Bulawayo", "Harare", "Manicaland", "Mashonaland Central",
						"Mashonaland East", "Mashonaland West", "Masvingo",
						"Matabeleland North", "Matabeleland South", "Midlands" } };

		// cities = new String[][][] {
		//
		// };

		statesList = new ArrayList<String>();
		for (int i = 0; i < states[countriesList.indexOf(country)].length; i++) {
			statesList.add(states[countriesList.indexOf(country)][i]);
		}
		return statesList;
	}

	@Override
	public List<AccountsTemplate> getAccountsTemplate()
			throws AccounterException {
		AccountsTemplateManager manager = new AccountsTemplateManager();
		try {
			return manager.loadAccounts(ServerLocal.get().getLanguage());
		} catch (Exception e) {
			throw new AccounterException(e);
		}
	}

	@Override
	public List<ClientTransaction> getAllTransactionsOfAccount(long id,
			ClientFinanceDate startDate, ClientFinanceDate endDate)
			throws AccounterException {
		return getFinanceTool().getAllTransactionsOfAccount(id, startDate,
				endDate);
	}

	@Override
	public List<ClientReconciliation> getReconciliationsByBankAccountID(
			long accountID) throws AccounterException {
		return getFinanceTool().getReconciliationsByBankAccountID(accountID);
	}

	@Override
	public double getOpeningBalanceforReconciliation(long accountID)
			throws AccounterException {
		return getFinanceTool().getOpeningBalanceforReconciliation(accountID);
	}

	@Override
	public List<ClientActivity> getTransactionHistory(long transactionId)
			throws AccounterException {
		return getFinanceTool().getTransactionHistory(transactionId);
	}
}