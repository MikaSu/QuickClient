/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quickclient.classes;

import org.quickclient.gui.LoginFrame;

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.DfAuthenticationException;
import com.documentum.fc.client.DfIdentityException;
import com.documentum.fc.client.DfPrincipalException;
import com.documentum.fc.client.DfServiceException;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSessionManager;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfLogger;
import com.documentum.fc.common.IDfLoginInfo;


/**
 * 
 * @author Administrator
 * 
 * 
 */
public class DocuSessionManager {

	private static DocuSessionManager me = null;
	private IDfSessionManager sMgr;
	private String docbasename;
	private String userName = "";

	protected DocuSessionManager() {
	}

	public static DocuSessionManager getInstance() {
		if (me == null) {
			me = new DocuSessionManager();
		}
		return me;
	}

	public void releaseSession(IDfSession sess) {
		sMgr.release(sess);
	}

	public IDfSession getNewSession() {
		IDfSession sess = null;
		try {
			sess = getSMgr().newSession(docbasename);
		} catch (DfIdentityException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
		} catch (DfAuthenticationException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
		} catch (DfPrincipalException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
		} catch (DfServiceException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
		}
		return sess;
	}

	public IDfSession getSession() {
		IDfSession sess = null;
		try {
			if (getSMgr() == null) {
				LoginFrame lf = new LoginFrame();
				SwingHelper.centerJFrame(lf);
				lf.setVisible(true);
			}
			if (getSMgr() != null)
				sess = getSMgr().getSession(docbasename);
		} catch (DfIdentityException ex) {
			DfLogger.error(this, ex.getMessage(), null, ex);
			SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
		} catch (DfAuthenticationException ex) {
			SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
			DfLogger.error(this, ex.getMessage(), null, ex);
		} catch (DfPrincipalException ex) {
			SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
			DfLogger.error(this, ex.getMessage(), null, ex);
		} catch (DfServiceException ex) {
			SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
			DfLogger.error(this, ex.getMessage(), null, ex);
		} catch (DfException ex) {
			SwingHelper.showErrorMessage(ex.getMessage(), ex.getMessage());
			DfLogger.error(this, ex.getMessage(), null, ex);
		}

		return sess;
	}

	public void setLoginInfo(String docbase, String username, String password) throws DfException {
		IDfClientX clientx = new DfClientX();
		//IDfClient client = clientx.getLocalClient();
		IDfLoginInfo loginInfoObj = clientx.getLoginInfo();
		loginInfoObj.setUser(username);
		loginInfoObj.setPassword(password);
		loginInfoObj.setDomain(null);
		getSMgr().setIdentity(docbase, loginInfoObj);
	}

	public IDfSessionManager getSMgr() {
		return sMgr;
	}

	public void setSMgr(IDfSessionManager sMgr) {
		this.sMgr = sMgr;
	}

	public String getDocbasename() {
		return docbasename;
	}

	public void setDocbasename(String docbasename) {
		this.docbasename = docbasename;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
