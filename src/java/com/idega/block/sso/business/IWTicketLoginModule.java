/*
 * $Id: IWTicketLoginModule.java,v 1.4 2009/05/15 07:23:47 valdas Exp $
 * Created on May 9, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.sso.business;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.RefreshFailedException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.idega.block.sso.data.IWTicket;
import com.idega.block.sso.data.TicketCredential;
import com.idega.core.accesscontrol.business.LoggedOnInfo;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.accesscontrol.business.LoginSession;
import com.idega.core.accesscontrol.jaas.IWCredential;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.servlet.filter.IWAuthenticator;
import com.idega.user.data.bean.User;
import com.idega.util.expression.ELUtil;

/**
 *
 *  Last modified: $Date: 2009/05/15 07:23:47 $ by $Author: valdas $
 *
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.4 $
 */
public class IWTicketLoginModule implements LoginModule {

	public static final String ORIGINATOR_NAME = IWTicketLoginModule.class.getName();

	Map sharedState = null;
	CallbackHandler callbackHandler = null;

	private boolean isLoggedIn = false;
	private IWTicket ticket= null;
	private boolean credentialIsSet = false;
	private boolean loginSessionIsSet = false;

	private LoginBusinessBean loginBusiness = null;

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	@Override
	public boolean abort() throws LoginException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	@Override
	public boolean commit() throws LoginException {
		// nothing to do if
		// either
		// + login failed
		// or
		// + credential (and login session) is already set
		if (! this.isLoggedIn || this.credentialIsSet) {
			// see javadoc!
			return true;
		}
		HttpSession session = (HttpSession) this.sharedState.get(IWAuthenticator.SESSION_KEY);

		// is there a ticket?
		if (this.ticket == null) {
			// ticket is null if the user logged in not using a ticket
			LoginBusinessBean loginBean = LoginBusinessBean.getLoginBusinessBean(session);
			User user = loginBean.getCurrentUser(session);
			// set ticket
			this.ticket = IWTicket.create(user, session, ORIGINATOR_NAME);
		}
		else {
			try {
				this.ticket.refresh();
			}
			catch (RefreshFailedException ex) {
				// see javadoc
				return true;
			}
		}

		// if the user isn't logged in
		if (! this.loginSessionIsSet ) {
			// first login in idega
			HttpServletRequest request = (HttpServletRequest)   this.sharedState.get(IWAuthenticator.REQUEST_KEY);
			String personalId = this.ticket.getPersonalId();
			login(request, session, personalId);
		}

		LoginSession loginSession;
		try {
			loginSession = ELUtil.getInstance().getBean(LoginSession.class);
			LoggedOnInfo loggedOnInfo = loginSession.getLoggedOnInfo();
			if (loggedOnInfo != null) {
				Map credentials = loggedOnInfo.getCredentials();
				// set credential with help of the ticket
				if (credentials.containsKey(IWTicketLoginModule.ORIGINATOR_NAME)) {
					return true;
				}
				IWCredential ticketCredential = new TicketCredential(this.ticket.getKey());
				credentials.put(IWTicketLoginModule.ORIGINATOR_NAME, ticketCredential);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		// see javadoc!
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	@Override
	public boolean login() throws LoginException {
		// if the user is already logged in:
		// 1. we do not have to check for a ticket
		// 2. we accept the given login even if there is no ticket
		HttpSession session = (HttpSession) this.sharedState.get(IWAuthenticator.SESSION_KEY);
		if (session != null) {
			LoginSession loginSession;
			try {
				loginSession = ELUtil.getInstance().getBean(LoginSession.class);
				LoggedOnInfo loggedOnInfo = loginSession.getLoggedOnInfo();
				if (loggedOnInfo != null) {
					Map credentials = loggedOnInfo.getCredentials();
					this.credentialIsSet = credentials.containsKey(IWTicketLoginModule.ORIGINATOR_NAME);
					this.isLoggedIn = true;
					this.loginSessionIsSet = true;
					return true;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		// second case: user is not logged in
		// check for a ticket
		Callback[] callbacks = new Callback[2];
		callbacks[0] = new PasswordCallback(TicketCredential.NAME, true);
		callbacks[1] = new NameCallback("PersonalId");
		try {
			this.callbackHandler.handle(callbacks);
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		catch (UnsupportedCallbackException e) {
			e.printStackTrace();
			return false;
		}
		IWTicket tempTicket = null;
		char[] requestTicketKey = ((PasswordCallback) callbacks[0]).getPassword();
		if (requestTicketKey != null) {
			String ticketKey = String.valueOf(requestTicketKey);
			String name = ((NameCallback) callbacks[1]).getName();
			tempTicket = IWTicket.get(ticketKey);
			if (tempTicket == null) {
				this.isLoggedIn = false;
			}
			// if the name is given check if the name belongs to the ticket
			else if (name == null) {
				this.isLoggedIn = (tempTicket.isCurrent());
			}
			else {
				this.isLoggedIn = tempTicket.isValidFor(name);
			}
		}
		if (this.isLoggedIn) {
			// use the existing valid ticket
			this.ticket = tempTicket;
		}
		return this.isLoggedIn;
 	}



	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	@Override
	public boolean logout() throws LoginException {
		// nothing to do since credentials are stored in LogOnInfo
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject, javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
	 */
	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
		this.sharedState = sharedState;
		this.callbackHandler = callbackHandler;
	}

	private void login(HttpServletRequest request, HttpSession session, String personalID) {
		ServletContext myServletContext = session.getServletContext();
	   	// getting the application context
    	IWMainApplication mainApplication = IWMainApplication.getIWMainApplication(myServletContext);
    	IWApplicationContext iwac = mainApplication.getIWApplicationContext();
    	LoginBusinessBean loginBiz = getLoginBusiness(iwac);
    	try {
    		// note: the request is only needed because the remote address is logged
    		// would be nice if the session is only needed
    		loginBiz.logInByPersonalID(request, personalID);
    	}
    	catch (Exception ex) {
    		// empty
    	}
	}

    private LoginBusinessBean getLoginBusiness(IWApplicationContext iwac) {
    	if (this.loginBusiness == null) {
        	this.loginBusiness = LoginBusinessBean.getLoginBusinessBean(iwac);
    	}
    	return this.loginBusiness;
	}

}

