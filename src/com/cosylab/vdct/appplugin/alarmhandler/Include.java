/*
 * Copyright (c) 2004 by Cosylab d.o.o.
 *
 * The full license specifying the redistribution, modification, usage and other
 * rights and obligations is included with the distribution of this project in
 * the file license.html. If the license is not included you may find a copy at
 * http://www.cosylab.com/legal/abeans_license.htm or may write to Cosylab, d.o.o.
 *
 * THIS SOFTWARE IS PROVIDED AS-IS WITHOUT WARRANTY OF ANY KIND, NOT EVEN THE
 * IMPLIED WARRANTY OF MERCHANTABILITY. THE AUTHOR OF THIS SOFTWARE, ASSUMES
 * _NO_ RESPONSIBILITY FOR ANY CONSEQUENCE RESULTING FROM THE USE, MODIFICATION,
 * OR REDISTRIBUTION OF THIS SOFTWARE.
 */

package com.cosylab.vdct.appplugin.alarmhandler;

import com.cosylab.vdct.appplugin.AppTreeElement;


/**
 * <code>Include</code> ...  DOCUMENT ME!
 *
 * @author <a href="mailto:jaka.bobnar@cosylab.com">Jaka Bobnar</a>
 * @version $Id$
 *
 * @since VERSION
 */
public class Include extends AppTreeElement
{
	/**
	 * TODO DOCUMENT ME!
	 *
	 * @param name
	 */
	public Include(String name)
	{
		super(name);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.cosylab.vdct.appplugin.AppTreeElement#getName()
	 * removes the .alhInclude extension from being viewed in the tree
	 */

	//    public String getName() {
	//        return super.getName().substring(0, super.getName().length()-11);
	//    }
}

/* __oOo__ */
