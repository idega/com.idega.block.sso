/*
 * $Id: IWTicket.java,v 1.2 2006/05/19 07:37:51 laddi Exp $
 * Created on May 11, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.sso.data;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import javax.security.auth.RefreshFailedException;
import javax.security.auth.Refreshable;
import javax.servlet.http.HttpSession;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2006/05/19 07:37:51 $ by $Author: laddi $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public class IWTicket implements Destroyable, Refreshable {
	
	public final static long EXPIRATION_TIME_IN_MIN = 90;
	
	// avoid miscalculations
	private final static long EXPIRATION_TIME_IN_MILLISECOND = EXPIRATION_TIME_IN_MIN *60 * 1000;
	
	public static IWTicket create(User user, HttpSession session, String originator) {
		String sessionId = session.getId();
		// has to be set in milliseconds
		long expirationTime = EXPIRATION_TIME_IN_MILLISECOND;
		long authTime = System.currentTimeMillis();
		IWTicket ticket = new IWTicket(user, sessionId, originator, authTime, expirationTime);
		TicketValidator.getInstance().put(ticket);
		return ticket;
	}
	
	public static IWTicket get(String ticketKey) {
		return TicketValidator.getInstance().get(ticketKey);
	}
		 
	
	private String originator = null;
	
	private String myPersonalId = null;
	
	private String key = null;
	
	private long authTime = -1;
	
	private long startTime = -1;
	
	private long expirationTime = -1;
	
	public IWTicket(User user, String key, String originator, long authTime, long expirationTime) {
		this(user.getPersonalID(), key, originator, authTime, expirationTime);
	}
	
	public IWTicket(String personalId, String key, String originator, long authTime, long expirationTime) {
		this.authTime = authTime;
		this.startTime = authTime;
		this.expirationTime = expirationTime;
		this.myPersonalId = personalId;
		this.key = key;
		this.originator = originator;
	}
	
	public boolean isValidFor(String personalId) {
		if (! isCurrent()) {
			return false;
		}
		if (personalId == null && this.myPersonalId == null) {
			return true;
		}
		if (this.myPersonalId == null) {
			return false;
		}
		return this.myPersonalId.equals(personalId);
	}
	
	/**
	 * @return Returns the key.
	 */
	public String getKey() {
		return this.key;
	}

	
	/**
	 * @param key The key to set.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	
	/**
	 * @return Returns the personalId.
	 */
	public String getPersonalId() {
		return this.myPersonalId;
	}

	
	/**
	 * @param personalId The personalId to set.
	 */
	public void setPersonalId(String personalId) {
		this.myPersonalId = personalId;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.Destroyable#destroy()
	 */
	public void destroy() throws DestroyFailedException {
		this.originator = null;
		this.myPersonalId = null;
		this.key = null;
		this.authTime = -1;
		this.startTime = -1l;
		this.expirationTime = -1;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.Destroyable#isDestroyed()
	 */
	public boolean isDestroyed() {
		return this.key == null;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.Refreshable#refresh()
	 */
	public void refresh() throws RefreshFailedException {
		this.startTime = System.currentTimeMillis();
		// notify registry
		TicketValidator.getInstance().expirationTimeHasChanged(this);
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.Refreshable#isCurrent()
	 */
	public boolean isCurrent() {
		long currentTime = System.currentTimeMillis();
		return this.startTime + this.expirationTime > currentTime;
	}

	
	/**
	 * @return Returns the authTime.
	 */
	public long getAuthTime() {
		return this.authTime;
	}

	
	/**
	 * @return Returns the originator.
	 */
	public String getOriginator() {
		return this.originator;
	}
}
