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

import java.awt.*;
import java.util.*;
import com.cosylab.vdct.Constants;
import com.cosylab.vdct.graphics.*;
import com.cosylab.vdct.vdb.*;

/**
 * Insert the type's description here.
 * Creation date: (26.01.2000 17:00:35)
 * @author: Matej Sekoranja
 */
 
public class Field extends VisibleObject {
	protected VDBFieldData fieldData = null;
/**
 * Group constructor comment.
 * @param parent com.cosylab.vdct.graphics.objects.ContainerObject
 */
public Field(ContainerObject parent, VDBFieldData fieldData) {
	super(parent);
	this.fieldData=fieldData;
	setWidth(Constants.FIELD_WIDTH);
	setHeight(Constants.FIELD_HEIGHT);
}
/**
 * Insert the method's description here.
 * Creation date: (21.12.2000 20:46:35)
 * @param visitor com.cosylab.vdct.graphics.objects.Visitor
 */
public void accept(Visitor visitor) {
	visitor.visitGroup();
}
/**
 * Insert the method's description here.
 * Creation date: (21.12.2000 20:46:35)
 * @param g java.awt.Graphics
 * @param hilited boolean
 */
protected void draw(Graphics g, boolean hilited) {

	ViewState view = ViewState.getInstance();

	int rrx = getRx()-view.getRx();
	int rry = getRy()-view.getRy();
	int rwidth = getRwidth();
	int rheight = getRheight();
		
	// clipping
	if ((rrx>view.getViewWidth()) || (rry>view.getViewHeight())
	    || ((rrx+rwidth)<0) || ((rry+rheight)<0)) return;

	if (!hilited) g.setColor(Constants.RECORD_COLOR);
	else if (view.isPicked(this)) g.setColor(Constants.PICK_COLOR);
	else if (view.isSelected(this)) g.setColor(Constants.SELECTION_COLOR);
	else g.setColor(Constants.RECORD_COLOR);
	
	g.fillRect(rrx, rry, rwidth, rheight);
	if (!hilited) g.setColor(Constants.FRAME_COLOR);
	else g.setColor((this==view.getHilitedObject()) ? 
					Constants.HILITE_COLOR : Constants.FRAME_COLOR);

	g.drawRect(rrx, rry, rwidth, rheight);

	if (getFont()!=null) {
		g.setFont(getFont());
		g.drawString(getLabel(), rrx+getRlabelX(), rry+getRlabelY());
	}

}
/**
 * Insert the method's description here.
 * Creation date: (26.1.2001 17:14:17)
 * @return com.cosylab.vdct.vdb.VDBFieldData
 */
public com.cosylab.vdct.vdb.VDBFieldData getFieldData() {
	return fieldData;
}
/**
 * Insert the method's description here.
 * Creation date: (3.5.2001 16:42:50)
 * @return java.lang.String
 */
public java.lang.String getHashID() {
	return getFieldData().getName();
}
/**
 * Insert the method's description here.
 * Creation date: (25.4.2001 17:58:03)
 * @return int
 */
public int getY() {
	getParent().forceValidation();
	return super.getY();
}
/**
 * Insert the method's description here.
 * Creation date: (21.12.2000 21:22:45)
 */
public void revalidatePosition() {
  setRx((int)(getX()*getRscale()));
  setRy((int)(getY()*getRscale()));
}
/**
 * Insert the method's description here.
 * Creation date: (26.1.2001 17:24:36)
 * @param nx int
 * @param ny int
 */
public void revalidatePosition(int nx, int ny) {
	setX(nx); setY(ny);
	revalidatePosition();
}
/**
 * Insert the method's description here.
 * Creation date: (21.12.2000 20:46:35)
 */
protected void validate() {

  revalidatePosition();
	
  double scale = getRscale();
  int rwidth = (int)(getWidth()*scale);
  int rheight = (int)(getHeight()*scale);
  setRwidth(rwidth);
  setRheight(rheight);

  // set appropriate font size
  int x0 = (int)(8*scale);		// insets
  int y0 = (int)(4*scale);

  setLabel(fieldData.getName());

  Font font;
  if (rwidth<(2*x0)) font = null;
  else
  font = FontMetricsBuffer.getInstance().getAppropriateFont(
	  			Constants.DEFAULT_FONT, Font.PLAIN, 
	  			getLabel(), rwidth-x0, rheight-y0);

  if (font!=null) {
	  FontMetrics fm = FontMetricsBuffer.getInstance().getFontMetrics(font);
	  setRlabelX((rwidth-fm.stringWidth(getLabel()))/2);
 	  setRlabelY((rheight-fm.getHeight())/2+fm.getAscent());
  }
  setFont(font);

}
}