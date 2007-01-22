/*
 * $Id: WSTicketBusinessHome.java,v 1.1 2007/01/22 08:07:35 tryggvil Exp $
 * Created on May 10, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.sso.business;

import com.idega.business.IBOHome;


/**
 * 
 *  Last modified: $Date: 2007/01/22 08:07:35 $ by $Author: tryggvil $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface WSTicketBusinessHome extends IBOHome {

	public WSTicketBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
