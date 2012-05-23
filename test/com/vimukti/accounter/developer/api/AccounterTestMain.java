package com.vimukti.accounter.developer.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.atlassian.extras.decoder.v2.Version2LicenseDecoder;
import com.vimukti.accounter.developer.api.core.ApiResult;
import com.vimukti.accounter.developer.api.core.ITest;
import com.vimukti.accounter.developer.api.report.CrudTest;

public class AccounterTestMain {
	private static List<ITest> tests = new ArrayList<ITest>();

	public static void main(String[] args) {
		String licenseString = "AAABPw0ODAoPeNptkVFPgzAUhd/7K5r43IUynLikiQ66yAQ2Ge7B+FLxutVBIS0Q9++FsUVnfGjS9N7znXNvr+Za4kgcMHWx5UzHt1NnjD0/xbZFbeSDybSsalkqtgiS+9cp5q3IG9G/IE/D8eKLGljfT6xrQl3klaoWWR2LAthKCyOUEvixKYRGn1KLUSgzUAb4uzyCeZzyZJUEa35W8kjInFUn6b5XjrZ3rSyafS1HWVmgnxSs1g1ccNNDBUdvbxlFPPGC+3Cod2TZwiDIh94NaNNDbNRZqhqUUBnwr0rqw6+pJoTeoKXeCiXNYLoZsuAUsp0q83IrwQwmcVO8gV5+PJsOzQhFax6z7pCQTmzXtR20Bt2CDnw2e6AuiZ/4glhzxyKzFy9Gpxm6ahj4F7Lz1v4Nt2p0thMG/n7FN68/nKkwLAIUGbIvLkKAdSiWleLmd9IE19tcKZQCFGAowo5SHKgb9UbMKW8PKgUC7wwGX02fr";

		Version2LicenseDecoder decoder = new Version2LicenseDecoder();
		Properties doDecode = decoder.doDecode(licenseString);

		System.out.println(doDecode);
	}

	private static void test() {
		for (ITest test : tests) {
			try {
				test.before();
				test.test();
			} catch (Exception e) {
				ApiResult result = test.getResult();
				System.out.println(result.getResult());
				e.printStackTrace();
			}
		}
	}

	private static void loadTest() {
		tests.add(new CrudTest());
	}
}
