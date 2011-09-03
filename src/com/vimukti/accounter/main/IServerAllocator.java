/**
 * 
 */
package com.vimukti.accounter.main;

import com.vimukti.accounter.core.Server;

/**
 * 
 * Allocates Servers For Companies
 * 
 * @author Prasanna Kumar G
 * 
 */
public interface IServerAllocator {

	public Server allocateServer(int companyType, String companyName,
			String sourceAddr);
}
