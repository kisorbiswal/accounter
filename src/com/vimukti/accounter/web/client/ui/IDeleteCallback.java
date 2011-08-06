/**
 * 
 */
package com.vimukti.accounter.web.client.ui;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IDeleteCallback {

	public void deleteFailed(Throwable caught);

	public void deleteSuccess(Boolean result);
}
