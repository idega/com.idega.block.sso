/*
 * $Id: TicketValidator.java,v 1.1 2006/05/18 17:09:30 thomas Exp $
 * Created on Mar 29, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.sso.data;

import java.util.Collections;
import java.util.Map;
import com.idega.repository.data.Instantiator;
import com.idega.repository.data.Singleton;
import com.idega.repository.data.SingletonRepository;
import com.idega.util.datastructures.map.TimeLimitedMap;


/**
 * 
 *  Last modified: $Date: 2006/05/18 17:09:30 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
class TicketValidator implements Singleton {
	
	private static Instantiator instantiator = new Instantiator() { public Object getInstance() { return new TicketValidator();}};

	protected static TicketValidator getInstance(){
		return (TicketValidator) SingletonRepository.getRepository().getInstance(TicketValidator.class, instantiator);
	}
	
//	private LoginBusinessBean loginBusiness = null;
//	private UserBusiness userBusiness = null;
	
	private Map keyTicket = Collections.synchronizedMap(TimeLimitedMap.getInstanceWithTimeLimitInMinutes(IWTicket.EXPIRATION_TIME_IN_MIN + 10));
	
	/**
	 * Should be called if the expiration time has changed
	 * 
	 * @param ticket
	 */
	protected synchronized void expirationTimeHasChanged(IWTicket ticket) {
		put(ticket);
	}
		
	protected synchronized void put(IWTicket ticket) {
		String key = ticket.getKey();
		keyTicket.put(key, ticket);
	}

	/** 
	 * returns the stored IWTicket
	 * 
	 * @param ticket
	 * @return
	 */
	protected IWTicket get(String ticket) {	
		return (IWTicket) this.keyTicket.get(ticket);
	}
}
