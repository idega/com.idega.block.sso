/*
 * $Id: TicketCredential.java,v 1.1 2006/05/18 17:09:30 thomas Exp $
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
 *  Last modified: $Date: 2006/05/18 17:09:30 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
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
		return ticket;
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
		ticket = null;
	}

	/* (non-Javadoc)
	 * @see javax.security.auth.Destroyable#isDestroyed()
	 */
	public boolean isDestroyed() {
		return ticket == null;
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
		if (anotherTicket == null && ticket == null) {
			return true;
		}
		if (anotherTicket == null) {
			return false;
		}
		return anotherTicket.equals(ticket);
	}

	public int hashCode() {
		return ticket.hashCode();
	}
}
