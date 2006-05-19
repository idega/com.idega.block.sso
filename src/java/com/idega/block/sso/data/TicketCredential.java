/*
 * $Id: TicketCredential.java,v 1.2 2006/05/19 07:37:51 laddi Exp $
 * Created on May 10, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.sso.data;

import javax.security.auth.DestroyFailedException;
import com.idega.core.accesscontrol.jaas.IWCredential;


/**
 * 
 *  Last modified: $Date: 2006/05/19 07:37:51 $ by $Author: laddi $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public class TicketCredential implements IWCredential {
	
	public static final String NAME = "ticket";
	
	private String ticket = null;
	
	public TicketCredential(String ticket) {
		this.ticket = ticket;
	}

	/* (non-Javadoc)
	 * @see com.idega.core.accesscontrol.jaas.IWCredential#getKey()
	 */
	public Object getKey() {
		return this.ticket;
	}

	/* (non-Javadoc)
	 * @see com.idega.core.accesscontrol.jaas.IWCredential#getName()
	 */
	public String getName() {
		return TicketCredential.NAME;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.Destroyable#destroy()
	 */
	public void destroy() throws DestroyFailedException {
		this.ticket = null;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.Destroyable#isDestroyed()
	 */
	public boolean isDestroyed() {
		return this.ticket == null;
	}
	
	public boolean equals(Object anObject) {
		if (anObject == null) {
			return false;
		}
		if (anObject.getClass() != (this.getClass())) {
			return false;
		}
		TicketCredential anotherTicketCredential = (TicketCredential) anObject;
		String anotherTicket = (String) anotherTicketCredential.getKey();
		if (anotherTicket == null && this.ticket == null) {
			return true;
		}
		if (anotherTicket == null) {
			return false;
		}
		return anotherTicket.equals(this.ticket);
	}

	public int hashCode() {
		return this.ticket.hashCode();
	}
}
