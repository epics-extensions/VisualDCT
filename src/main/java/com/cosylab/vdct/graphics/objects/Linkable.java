package com.cosylab.vdct.graphics.objects;

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

/**
 * Insert the type's description here.
 * Creation date: (19.12.2000 20:18:41)
 * @author Matej Sekoranja
 */
public interface Linkable {
/**
 * Insert the method's description here.
 * Creation date: (29.1.2001 17:46:17)
 * @param disconnector disconnector
 */
void disconnect(Linkable disconnector);
/**
 * Insert the method's description here.
 * Creation date: (23.4.2001 20:33:36)
 * @return java.lang.String
 */
String getID();
/**
 * Insert the method's description here.
 * Creation date: (29.1.2001 17:57:19)
 * @return java.lang.String
 */
String getLayerID();
/**
 * Insert the method's description here.
 * Creation date: (29.1.2001 17:51:40)
 * @return boolean
 */
boolean isConnectable();
/**
 * Insert the method's description here.
 * Creation date: (29.1.2001 17:52:22)
 * @return boolean
 */
boolean isDisconnected();
/**
 * Insert the method's description here.
 * Creation date: (29.1.2001 17:57:37)
 * @param id java.lang.String
 */
void setLayerID(String id);
}
