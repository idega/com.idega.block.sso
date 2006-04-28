/*
 * $Id: TicketValidator.java,v 1.4 2006/04/28 08:55:20 laddi Exp $
 * Created on Mar 29, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.sso.business;

import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpSession;
import com.idega.presentation.IWContext;
import com.idega.repository.data.Instantiator;
import com.idega.repository.data.Singleton;
import com.idega.repository.data.SingletonRepository;
import com.idega.util.StringHandler;
import com.idega.util.datastructures.map.TimeLimitedMap;


/**
 * 
 *  Last modified: $Date: 2006/04/28 08:55:20 $ by $Author: laddi $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.4 $
 */
public class TicketValidator implements Singleton {
	
	//private final static String SEPARATOR = "t";
	
	private final static long TIME_LIMIT_MINUTES = 80;
	
	private static Instantiator instantiator = new Instantiator() { public Object getInstance() { return new TicketValidator();}};

	public static TicketValidator getInstance(){
		return (TicketValidator) SingletonRepository.getRepository().getInstance(TicketValidator.class, instantiator);
	}
	
//	private LoginBusinessBean loginBusiness = null;
//	private UserBusiness userBusiness = null;
	
	private Map sessionIdSession = Collections.synchronizedMap(TimeLimitedMap.getInstanceWithTimeLimitInMinutes(TIME_LIMIT_MINUTES));
	
	public void removeTicket(HttpSession session) {
		String sessionId = session.getId();
		this.sessionIdSession.remove(sessionId);
	}

	public synchronized String addTicket(String personalId) {
		HttpSession session = IWContext.getInstance().getSession();
		String sessionId = session.getId();
		if (!this.sessionIdSession.containsKey(sessionId)) {
			this.sessionIdSession.put(sessionId, personalId);
		}
		return getTicket(sessionId);
	}

	public boolean isValid(String personalId, String ticket) {
		if (StringHandler.isEmpty(personalId)) {
			return false;
		}
		String ticketPersonalId = validate(ticket);
		return personalId.equals(ticketPersonalId);
	}
		
	/** 
	 * returns the stored social security number
	 * 
	 * @param ticket
	 * @return
	 */
	public String validate(String ticket) {	
		return (String) this.sessionIdSession.get(ticket);
	}

		
//		// getting a session object from this request
//    	MessageContext context = MessageContext.getCurrentContext();
//    	AxisHttpSession myAxisSession = (AxisHttpSession) context.getSession();
//    	HttpSession mySession = myAxisSession.getRep();
//    	ServletContext myServletContext = mySession.getServletContext();
//    	// getting the application context
//    	IWMainApplication mainApplication = IWMainApplication.getIWMainApplication(myServletContext);
//    	IWApplicationContext iwac = mainApplication.getIWApplicationContext();
		
    	// first try to get the user login name
//    	String userLogin = getUserLogin(personalId, iwac);
//    	if (userLogin == null) {
//    		return false;
//    	}
   	
    	// the first test  should be sufficient but we perform also the test with the session
     	// vice versa the test with the session should be sufficient
    	
    	//return isLoggedOnUsingLoggedOnMap(userLogin, mySession, iwac) && isLoggedOnUsingSession(sessionId, iwac);
    	

	
//	private String getUserLogin(String personalId, IWApplicationContext iwac) {
//		try {
//			User user = getUserBusiness(iwac).getUser(personalId);
//			LoginTable loginTable = LoginDBHandler.getUserLogin(user);
//			return loginTable.getUserLogin();
//		}
//		catch (RemoteException e) {
//			return null;
//		}
//		catch (FinderException e) {
//			return null;
//		}
//	}
//	
//	private boolean isLoggedOnUsingLoggedOnMap(String userLogin, HttpSession mySession, IWApplicationContext iwac) {
//		Map map = getLoginBusinesBean(iwac).getLoggedOnInfoMap(mySession);
//		if (map == null) {
//			// should not happen
//			return false;
//		}
//		return map.containsKey(userLogin);
//	}
//		
//	private boolean isLoggedOnUsingSession(String sessionId, IWApplicationContext iwac) {
//		HttpSession session = (HttpSession) sessionIdSession.get(sessionId);
//		if (session == null ) {
//			return false;
//		}
//		try {
//			// quite stupid test if the session is valid or not but I could not find a better method....
//			// note that usually the session is valid (see method destroy session)
//			session.getAttributeNames();
//		}
//		catch (IllegalStateException ex) {
//			// cleaning...destroy that session
//			sessionIdSession.remove(sessionId);
//			// session not valid
//			return false;
//		}
//		return getLoginBusinesBean(iwac).isLoggedOn(session);
//	}
//	
	private String getTicket(String sessionId) {		
		return sessionId;
	}
	
//	private LoginBusinessBean getLoginBusinesBean(IWApplicationContext iwac) {
//		if (loginBusiness == null) {
//			loginBusiness = LoginBusinessBean.getLoginBusinessBean(iwac);
//		}
//		return loginBusiness;
//	}
//	
//	private UserBusiness getUserBusiness(IWApplicationContext iwac) {
//		if (userBusiness == null) {
//			try {
//				userBusiness = (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
//			}
//			catch (IBOLookupException e) {
//				throw new IBORuntimeException();
//			}
//		}
//		return userBusiness;
//	}
}
