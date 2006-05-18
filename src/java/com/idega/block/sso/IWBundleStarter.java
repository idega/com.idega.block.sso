/*
 * $Id: IWBundleStarter.java,v 1.1 2006/05/18 17:09:30 thomas Exp $
 * Created on 15.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.sso;

import javax.security.auth.spi.LoginModule;
import com.idega.block.sso.business.IWTicketLoginModule;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.repository.data.ImplementorRepository;

/**
 * 
 *  Last modified: $Date: 2006/05/18 17:09:30 $ by $Author: thomas $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class IWBundleStarter implements IWBundleStartable {

    /* (non-Javadoc)
     * @see com.idega.idegaweb.IWBundleStartable#start(com.idega.idegaweb.IWBundle)
     */
    public void start(IWBundle starterBundle) {
    	ImplementorRepository.getInstance().addImplementor(LoginModule.class, IWTicketLoginModule.class);
    }

    /* (non-Javadoc)
     * @see com.idega.idegaweb.IWBundleStartable#stop(com.idega.idegaweb.IWBundle)
     */
    public void stop(IWBundle starterBundle) {
    	// nothing to do
    }
}
