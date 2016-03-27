package com.cosylab.vdct.inspector;

/**
 * Copyright (c) 2002, Cosylab, Ltd., Control System Laboratory, www.cosylab.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution. 
 * Neither the name of the Cosylab, Ltd., Control System Laboratory nor the names
 * of its contributors may be used to endorse or promote products derived 
 * from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import javax.swing.*;

/**
 * Insert the type's description here.
 * Creation date: (10.1.2001 14:47:33)
 * @author Matej Sekoranja
 */
public interface Inspectable {

/**
 * 
 * @return com.cosylab.vdct.inspector.InspectableProperty
 */
InspectableProperty getCommentProperty();
/**
 * Insert the method's description here.
 * Creation date: (10.1.2001 15:14:56)
 * @return javax.swing.Icon
 */
public Icon getIcon();
/**
 * Insert the method's description here.
 * Creation date: (10.1.2001 14:47:43)
 * @return java.lang.String
 */
public String getName();
/**
 * 
 * @return com.cosylab.vdct.inspector.InspectableProperty[]
 */
com.cosylab.vdct.inspector.InspectableProperty[] getProperties(int mode, boolean spreadsheet);
/**
 * Obtains list of all mode names for this particular property.
 * Modes are numbered from 0-n.
 * Creation date: (11.1.2001 21:30:04)
 * @return java.util.ArrayList array of mode names - obtained using toString().
 */
public java.util.ArrayList getModeNames();
/**
 * Insert the method's description here.
 * Creation date: (10.1.2001 14:48:10)
 * @return java.lang.String
 */
public String toString();

public Object getDsId();
}