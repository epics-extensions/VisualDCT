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

import java.util.*;
import java.awt.*;
import com.cosylab.vdct.Constants;
import javax.swing.*;
import java.awt.event.*;
import com.cosylab.vdct.vdb.*;
import com.cosylab.vdct.graphics.*;
import com.cosylab.vdct.inspector.*;
import com.cosylab.vdct.graphics.popup.*;

/**
 * Insert the type's description here.
 * Creation date: (30.1.2001 12:26:07)
 * @author: Matej Sekoranja 
 */
public abstract class EPICSLinkOut extends EPICSLink implements OutLink, Popupable, Inspectable {

	class PopupMenuHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String action = e.getActionCommand();
			if (action.equals(colorString))
			{			
				Color newColor = JColorChooser.showDialog(null, selectTitle, getColor());
				setColor(newColor);
				com.cosylab.vdct.events.CommandManager.getInstance().execute("RepaintWorkspace");
			}
			else if (action.equals(addConnectorString))
			{
				addConnector();
				com.cosylab.vdct.events.CommandManager.getInstance().execute("RepaintWorkspace");
			}
			else if (action.equals(moveUpString))
			{
				((Record)getParent()).moveFieldUp(EPICSLinkOut.this);
			}
			else if (action.equals(moveDownString))
			{
				((Record)getParent()).moveFieldDown(EPICSLinkOut.this);
			}
			else if (action.equals(removeString))
			{
				destroy();
			}
			
		}
	}
	private static javax.swing.ImageIcon icon = null;
	public static final char LINK_SEPARATOR = '/';
	private static final String nullString = "";
	private static final String selectTitle = "Select link color...";
	private static final String addConnectorString = "Add connector";
	private static final String colorString = "Color...";
	private static final String moveUpString = "Move Up";
	private static final String moveDownString = "Move Down";
	private static final String removeString = "Remove Link";
	private static GUISeparator recordSeparator = null;
	private static GUISeparator fieldSeparator = null;
	protected InLink inlink = null;
	protected LinkProperties properties;
	private final static String maxLenStr = "NPP NMS";
	protected String label2;
	protected Font font2 = null;
	protected int realLabelLen = Constants.LINK_LABEL_LENGTH;
	protected int labelLen = Constants.LINK_LABEL_LENGTH;
	protected int realHalfHeight = Constants.FIELD_HEIGHT/2;
	private boolean hasEndpoint = false;
/**
 * EPICSOutLink constructor comment.
 * @param parent com.cosylab.vdct.graphics.objects.ContainerObject
 * @param fieldData com.cosylab.vdct.vdb.VDBFieldData
 */
protected EPICSLinkOut(ContainerObject parent, VDBFieldData fieldData) {
	super(parent, fieldData);
	properties = new LinkProperties(fieldData);
	updateLink();
}
/**
 * Insert the method's description here.
 * Creation date: (4.2.2001 12:50:51)
 */
public Connector addConnector() {
	String id = generateConnectorID(this);
	Connector connector = new Connector(id, (Record)getParent(), this, getInput());
	getParent().addSubObject(id, connector);
	return connector;
}
/**
 * Insert the method's description here.
 * Creation date: (2.2.2001 23:00:51)
 * @return com.cosylab.vdct.graphics.objects.EPICSLinkOut.PopupMenuHandler
 */
private com.cosylab.vdct.graphics.objects.EPICSLinkOut.PopupMenuHandler createPopupmenuHandler() {
	return new PopupMenuHandler();
}
/**
 * Insert the method's description here.
 * Creation date: (29.1.2001 22:11:34)
 */
public void destroy() {
	if (!isDestroyed()) {
		super.destroy();
		destroyChain();
		setInput(null);
		getFieldData().setValue(nullString);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (4.2.2001 21:32:42)
 */
public void destroyChain() {
	Linkable link = inlink;
	OutLink out = this;
	while (link instanceof OutLink) {
		out = (OutLink)link;
		link = out.getInput();
		if (out instanceof Connector)
			((Connector)out).destroy();			// connectors
	}
//	if (link instanceof EPICSLink)
		//((VisibleObject)link).destroy();
	if (link!=null)
		link.disconnect(out);
}
/**
 * Insert the method's description here.
 * Creation date: (29.1.2001 21:44:32)
 */
public void disconnect(Linkable disconnector) {
	if (!disconnected && (disconnector==inlink)) {
		super.disconnect(disconnector);
		//setInput(null);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (30.1.2001 12:25:44)
 */
public void fixLinkProperties() {
	LinkProperties newProperties = new LinkProperties(fieldData);
	properties = newProperties;
	setLabel(properties.getOptions());
}
/**
 * Insert the method's description here.
 * Creation date: (4.2.2001 13:01:01)
 * @return java.lang.String
 * @param outlink com.cosylab.vdct.graphics.objects.EPICSLinkOut
 */
public static String generateConnectorID(EPICSLinkOut outlink) {
	String rootName = Group.substractObjectName(outlink.getFieldData().getRecord().getName())+
					  LINK_SEPARATOR+outlink.getFieldData().getName();
	if (!outlink.getParent().containsObject(rootName))
		return rootName;
	else {
		String name;
		int count = 0;
		do {
			count++;
			name = rootName+(new Integer(count)).toString();
		} while (outlink.getParent().containsObject(name));
		return name;
	}
}
/**
 * Insert the method's description here.
 * Creation date: (1.2.2001 22:22:37)
 * @return com.cosylab.vdct.inspector.InspectableProperty
 */
public com.cosylab.vdct.inspector.InspectableProperty getCommentProperty() {
	return null;
}
/**
 * Insert the method's description here.
 * Creation date: (1.2.2001 11:26:33)
 * @return com.cosylab.vdct.graphics.objects.InLink
 * @param link com.cosylab.vdct.graphics.objects.Linkable
 */
public static InLink getEndPoint(Linkable link) {
	while (link instanceof OutLink)
		link = ((OutLink)link).getInput();
	return (InLink)link;
}
/**
 * Insert the method's description here.
 * Creation date: (3.2.2001 13:07:04)
 * @return com.cosylab.vdct.vdb.GUISeparator
 */
public static com.cosylab.vdct.vdb.GUISeparator getFieldSeparator() {
	if (fieldSeparator==null) fieldSeparator = new GUISeparator("Field");
	return fieldSeparator;
}
/**
 * Insert the method's description here.
 * Creation date: (1.2.2001 22:22:37)
 * @return javax.swing.Icon
 */
public javax.swing.Icon getIcon() {
	if (icon==null)
		icon = new javax.swing.ImageIcon(getClass().getResource("/images/link.gif"));
	return icon;
}
/**
 * Insert the method's description here.
 * Creation date: (29.1.2001 22:22:14)
 * @return com.cosylab.vdct.graphics.objects.InLink
 */
public InLink getInput() {
	return inlink;
}
/**
 * Insert the method's description here.
 * Creation date: (3.2.2001 11:23:59)
 * @return java.util.Vector
 */
public java.util.Vector getItems() {
	Vector items = new Vector();

	JMenuItem colorItem = new JMenuItem(colorString);
	colorItem.addActionListener(createPopupmenuHandler());
	items.addElement(colorItem);

	JMenuItem addItem = new JMenuItem(addConnectorString);
	addItem.addActionListener(createPopupmenuHandler());
	items.addElement(addItem);

	items.add(new JSeparator());

	Record parRec = (Record)getParent();
	boolean isFirst = parRec.isFirstField(this);
	boolean isLast = parRec.isLastField(this);
	

	if (!isFirst)
	{
		JMenuItem upItem = new JMenuItem(moveUpString);
		upItem.addActionListener(createPopupmenuHandler());
		upItem.setIcon(new ImageIcon(getClass().getResource("/up.gif")));
		items.addElement(upItem);
	}

	if (!isLast)
	{
		JMenuItem downItem = new JMenuItem(moveDownString);
		downItem.addActionListener(createPopupmenuHandler());
		downItem.setIcon(new ImageIcon(getClass().getResource("/down.gif")));
		items.addElement(downItem);
	}

	if (!(isFirst && isLast))
		items.add(new JSeparator());

	JMenuItem removeItem = new JMenuItem(removeString);
	removeItem.addActionListener(createPopupmenuHandler());
	items.addElement(removeItem);

	return items;
}
/**
 * Insert the method's description here.
 * Creation date: (30.1.2001 12:23:39)
 * @return com.cosylab.vdct.vdb.LinkProperties
 */
public com.cosylab.vdct.vdb.LinkProperties getLinkProperties() {
	return properties;
}
/**
 * Insert the method's description here.
 * Creation date: (1.2.2001 22:22:37)
 * @return java.lang.String
 */
public String getName() {
	return fieldData.getRecord().getName()+Constants.FIELD_SEPARATOR+fieldData.getName();
}
/**
 * Insert the method's description here.
 * Creation date: (29.1.2001 22:22:13)
 * @return int
 */
public int getOutX() {
	if (isRight())
		return getX()+getWidth()+Constants.TAIL_LENGTH;
	else 
		return getX()-Constants.TAIL_LENGTH;
}
/**
 * Insert the method's description here.
 * Creation date: (29.1.2001 22:22:13)
 * @return int
 */
public int getOutY() {
	return getY()+getHeight()/2;
}
/**
 * Return properties to be inspected
 * Creation date: (1.2.2001 22:22:37)
 * @return com.cosylab.vdct.inspector.InspectableProperty[]
 */
public com.cosylab.vdct.inspector.InspectableProperty[] getProperties() {
	InspectableProperty[] properties = new InspectableProperty[4];
	properties[0]=getRecordSeparator();
	properties[1]=new FieldInfoProperty(fieldData.getRecord().getField("DTYP"));
	properties[2]=getFieldSeparator();
	properties[3]=fieldData;
	return properties;
}
/**
 * Insert the method's description here.
 * Creation date: (30.1.2001 14:48:39)
 * @return int
 */
public int getQueueCount() {
	return 0;
}
/**
 * Insert the method's description here.
 * Creation date: (3.2.2001 13:07:04)
 * @return com.cosylab.vdct.vdb.GUISeparator
 */
public static com.cosylab.vdct.vdb.GUISeparator getRecordSeparator() {
	if (recordSeparator==null) recordSeparator = new GUISeparator("Record");
	return recordSeparator;
}
/**
 * Insert the method's description here.
 * Creation date: (1.2.2001 11:25:46)
 * @return com.cosylab.vdct.graphics.objects.OutLink
 * @param link com.cosylab.vdct.graphics.objects.Linkable
 */
public static OutLink getStartPoint(Linkable link) {
	while (link instanceof InLink)
		link = ((InLink)link).getOutput();
	return (OutLink)link;
}
/**
 * get/create target link field
 * Creation date: (30.1.2001 13:40:51)
 * @return com.cosylab.vdct.graphics.objects.InLink
 * @param link com.cosylab.vdct.vdb.LinkProperties
 */
public static InLink getTarget(LinkProperties link) {

	String recName = link.getRecord();
	// !!! check for getType()==LinkProperties.NOT_VALID
	if ((recName==null) || recName.equals(nullString)) return null;

	Record record = (Record)Group.getRoot().findObject(recName, true);
	if (record==null) return null;
	else if (link.getType()==link.FWDLINK_FIELD) {
		if (!link.getVarName().equalsIgnoreCase("PROC"))		// !!! proc
			return record;
	}

	EPICSVarLink var;
	if (record.containsObject(link.getVarName()))
		var = (EPICSVarLink)record.getSubObject(link.getVarName());
	else {
		VDBFieldData target = record.getRecordData().getField(link.getVarName());
		if ((target==null) ||
			LinkProperties.getType(target)!=LinkProperties.VARIABLE_FIELD) return null;
		else { 
			var = new EPICSVarLink(record, target);
			record.addLink(var);
		}
	}
	return var;
}
/**
 * Insert the method's description here.
 * Creation date: (30.1.2001 16:58:58)
 * @return boolean
 */
public boolean isRight() {
	if (disconnected || inlink==null ||
		!inlink.getLayerID().equals(getLayerID())) 
		return super.isRight();
	else {
		if (inlink instanceof Connector) {	
			return (inlink.getInX()>(getX()+getWidth()/2));
		}
		else if (inlink instanceof VisibleObject) {			// do not cycle !!!
			VisibleObject obj = (VisibleObject)inlink;
			return ((obj.getX()+obj.getWidth()/2)>(getX()+getWidth()/2));
		}
		else 
			return super.isRight();
	}
}
/**
 * Insert the method's description here.
 * Creation date: (4.2.2001 12:46:30)
 * @param newColor java.awt.Color
 */
public void setColor(Color newColor) {
		super.setColor(newColor);
		Linkable link = this;
		while (link instanceof OutLink) {
			link = ((OutLink)link).getInput();
			if (link instanceof VisibleObject) 
				((VisibleObject)link).setColor(newColor);
		}
		if (link instanceof VisibleObject) 
			((VisibleObject)link).setColor(newColor);
}
/**
 * Insert the method's description here.
 * Creation date: (29.1.2001 22:22:13)
 */
public void setInput(InLink input) {
	if (inlink==input) return;
	if (inlink!=null) inlink.disconnect(this);
	inlink=input;
	if (inlink!=null) disconnected=false;
}
/**
 * Insert the method's description here.
 * Creation date: (1.2.2001 22:31:14)
 */
public String toString() {
	return getName();
}
/**
 * Insert the method's description here.
 * Creation date: (30.1.2001 12:25:44)
 */
private void updateLink() {
	LinkProperties newProperties = new LinkProperties(fieldData);

	if (newProperties.getRecord()==null) {			// empty field
		destroy();
		return;
	}
	else if (!newProperties.getRecord().equals(properties.getRecord()) ||
			 !newProperties.getVarName().equals(properties.getVarName()) ||
			 !hasEndpoint) {
		// find endpoint
		Linkable preendpoint = this;
		Linkable endpoint = getInput();
		while ((endpoint instanceof InLink) && (endpoint instanceof OutLink)) {
			preendpoint = endpoint;
			endpoint = ((OutLink)endpoint).getInput();
		}
		if ((endpoint!=null) && hasEndpoint) ((InLink)endpoint).disconnect(preendpoint);
		//OutLink lol = getTarget(properties).getOutput();
		InLink il = getTarget(newProperties);
		OutLink ol = (OutLink)preendpoint;
		ol.setInput(il);
		if (il!=null) { 
			il.setOutput(ol, null);
			hasEndpoint = true;
		}
		else hasEndpoint = false;
	}

	properties = newProperties;
	setLabel(properties.getOptions());
}
/**
 * Insert the method's description here.
 * Creation date: (31.1.2001 18:27:35)
 */
public void validate() {
	super.validate();
	
	label2 = properties.getOptions();
	labelLen = (int)(Constants.LINK_LABEL_LENGTH*getRscale());
	
	if (labelLen<15) font2 = null;
	else {
	  font2 = FontMetricsBuffer.getInstance().getAppropriateFont(
		  			Constants.DEFAULT_FONT, Font.PLAIN, 
	 	 			label2, labelLen, getRheight());
	  if (font2!=null) {
		  FontMetrics fm = FontMetricsBuffer.getInstance().getFontMetrics(font2);
		  realLabelLen = fm.stringWidth(label2);
		  realHalfHeight = fm.getAscent()-fm.getHeight()/2;
	  }
	}
  

}
/**
 * Insert the method's description here.
 * Creation date: (30.1.2001 12:24:26)
 */
public void valueChanged() {
	updateLink();
}
}