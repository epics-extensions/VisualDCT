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
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import com.cosylab.vdct.*;
import com.cosylab.vdct.graphics.*;
import com.cosylab.vdct.graphics.popup.*;
import com.cosylab.vdct.events.*;
import com.cosylab.vdct.util.*;

/**
 * @author ssah
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class Box extends VisibleObject implements Flexible, Movable, Popupable, Selectable, Clipboardable
{

class PopupMenuHandler implements ActionListener
{
	
public void actionPerformed(ActionEvent event)
{
	String action = event.getActionCommand();
	if(action.equals(colorString))
	{
		Color newColor = JColorChooser.showDialog(null, colorTitleString, getColor());
		
		if(newColor != null)
		{
			setColor(newColor);
			currentColor = newColor;
		}
		
		CommandManager.getInstance().execute("RepaintWorkspace");
	}
	else if(action.equals(dashedString))
	{
		dashed = !dashed;
		currentIsDashed = dashed;

		CommandManager.getInstance().execute("RepaintWorkspace");
	}
}

}
	
private String hashId;
private String name;
private Vertex startVertex;
private Vertex endVertex;

private boolean dashed;

private static final String nullString = "";
private static final String colorString = "Color...";
private static final String colorTitleString = "Box color";
private static final String dashedString = "Dashed";

private static Color currentColor = Constants.LINE_COLOR;
private static boolean currentIsDashed = false;

private static final String hashIdPrefix = "Box";

private String getAvailableHashId()
{
	int number = 0;
	String testHashId = hashIdPrefix + String.valueOf(number);
	
	while(getParent().containsObject(testHashId))
	{
		number++;
		testHashId = hashIdPrefix + String.valueOf(number);		
	}

	return testHashId;
}

public Box(String parName, Group parentGroup, int posX, int posY, int posX2, int posY2)
{
	super(parentGroup);

	startVertex = new Vertex(this, posX, posY);
	endVertex = new Vertex(this, posX2, posY2);

	revalidatePosition();
	
	setWidth(Constants.CONNECTOR_WIDTH);
	setHeight(Constants.CONNECTOR_HEIGHT);
	
	setColor(currentColor);
	dashed = currentIsDashed;
	
	if(parName == null)
	{
		hashId = getAvailableHashId();
	
		if(parentGroup.getAbsoluteName().length() > 0)
			name = parentGroup.getAbsoluteName() + Constants.GROUP_SEPARATOR + hashId;
		else
			name = hashId;
	}
	else
		name = parName;	
}

public void accept(Visitor visitor)
{
	visitor.visitGroup();
}

public boolean checkMove(int dx, int dy)
{
	if(startVertex.checkMove(dx, dy) && endVertex.checkMove(dx, dy))
		return true;
	
	return false;
}

public boolean copyToGroup(String group)
{

	String newName;
	if(group.equals(nullString))
		newName = Group.substractObjectName(getName());
	else
		newName = group + Constants.GROUP_SEPARATOR + Group.substractObjectName(getName());

// object with new name already exists, add suffix ///!!!
	while(Group.getRoot().findObject(newName, true) != null)
		newName = StringUtils.incrementName(newName, Constants.COPY_SUFFIX);

	Box grBox = new Box(newName, null,
						startVertex.getX(), startVertex.getY(),
						endVertex.getX(), endVertex.getY());
	Group.getRoot().addSubObject(newName, grBox, true);

	ViewState view = ViewState.getInstance();
	grBox.move(20 - view.getRx(), 20 - view.getRy());

	unconditionalValidation();
	return true;
}

public void destroy()
{
	super.destroy();
	if(getParent() != null)
		getParent().removeObject(Group.substractObjectName(name));
		
	if(!startVertex.isDestroyed())
		startVertex.destroy();
		
	if(!endVertex.isDestroyed())
		endVertex.destroy();
}

protected void draw(Graphics g, boolean hilited)
{
	ViewState view = ViewState.getInstance();

	int offsetX = view.getRx();
	int offsetY = view.getRy();
	
	int posX = getRx() - offsetX;
	int posY = getRy() - offsetY;
	int rwidth = getRwidth();
	int rheight = getRheight();

	if((hilited) && (!((posX > view.getViewWidth()) || (posY > view.getViewHeight())
		|| ((posX + rwidth) < 0) || ((posY + rheight) < 0))))
	{
		g.setColor(Constants.HILITE_COLOR);
		g.drawRect(posX, posY, rwidth, rheight);
	}

	g.setColor(getColor());

	double scale = view.getScale();
	
	int rw = (int)(Constants.CONNECTOR_WIDTH * scale / 2);
	int rh = (int)(Constants.CONNECTOR_HEIGHT * scale / 2);
		
	posX = startVertex.getRx() + rw - offsetX;
	posY = startVertex.getRy() + rh - offsetY;
		
	int posX2 = endVertex.getRx() + rw - offsetX;
	int posY2 = endVertex.getRy() + rh - offsetY;

	int t;
	if(posX > posX2)
	{
		t = posX;
		posX = posX2;
		posX2 = t;
	}
	if(posY > posY2)
	{
		t = posY;
		posY = posY2;
		posY2 = t;
	}
		
	if((dashed) && ((posX != posX2) || (posY != posY2)))
	{
		int curX = posX;
		while(curX <= posX2)
		{
			int curX2 = curX + Constants.DASHED_LINE_DENSITY;
				
			if(curX2 > posX2)
				curX2 = posX2;
				
			g.drawLine(curX, posY, curX2, posY);
			g.drawLine(curX, posY2, curX2, posY2);
				
			curX += 2 * Constants.DASHED_LINE_DENSITY;
		}

		int curY = posY;
		while(curY <= posY2)
		{
			int curY2 = curY + Constants.DASHED_LINE_DENSITY;
				
			if(curY2 > posY2)
				curY2 = posY2;
				
			g.drawLine(posX, curY, posX, curY2);
			g.drawLine(posX2, curY, posX2, curY2);
				
			curY += 2 * Constants.DASHED_LINE_DENSITY;
		}
	}
	else
		g.drawRect(posX, posY, posX2 - posX, posY2 - posY);
}

public Vertex getEndVertex()
{
	return endVertex;
}

public String getFlexibleName()
{
	return name;
}

public boolean getIsDashed()
{
	return dashed;
}

public String getHashID()
{
	return hashId;
}

public Vector getItems()
{
	Vector items = new Vector();

	ActionListener al = new PopupMenuHandler();

	JMenuItem colorItem = new JMenuItem(colorString);
	colorItem.addActionListener(al);
	items.addElement(colorItem);

	JCheckBoxMenuItem dashedItem = new JCheckBoxMenuItem(dashedString);
	dashedItem.setSelected(dashed);
	dashedItem.addActionListener(al);
	items.addElement(dashedItem);

	return items;
}

public String getName()
{
	return name;
}

public Vertex getStartVertex()
{
	return startVertex;
}

public int getX()
{
	int posX = super.getX();
	if(Settings.getInstance().getSnapToGrid())
		return (posX + Constants.CONNECTOR_WIDTH / 2) - (posX + Constants.CONNECTOR_WIDTH / 2)
			% Constants.GRID_SIZE - Constants.CONNECTOR_WIDTH / 2;
		
	return posX;
}

public int getY()
{
	int posY = super.getY();
	if(Settings.getInstance().getSnapToGrid())
		return (posY + Constants.CONNECTOR_HEIGHT / 2) - (posY + Constants.CONNECTOR_HEIGHT / 2)
			% Constants.GRID_SIZE - Constants.CONNECTOR_HEIGHT / 2;
		
	return posY;
}

public boolean move(int dx, int dy)
{
	if(checkMove(dx, dy))
	{
		startVertex.move(dx, dy);
		endVertex.move(dx, dy);
		
		revalidatePosition();
		
		return true;
	}

	return false;
}

public boolean moveToGroup(String group)
{
	String currentParent = Group.substractParentName(getName());
	if(group.equalsIgnoreCase(currentParent))
		return false;
	
	String oldName = getName();
	String newName;
	if (group.equals(nullString))
		newName = Group.substractObjectName(getName());
	else
		newName = group + Constants.GROUP_SEPARATOR + Group.substractObjectName(getName());

	// object with new name already exists, add suffix // !!!
	Object obj;
	while((obj = Group.getRoot().findObject(newName, true)) != null)
	{
		if(obj == this)	// it's me :) already moved, fix data
		{
			name = newName;
			return true;
		}
		else
			newName = StringUtils.incrementName(newName, Constants.MOVE_SUFFIX);
			
		return rename(newName);
	}
	
	getParent().removeObject(Group.substractObjectName(getName()));
	setParent(null);
	Group.getRoot().addSubObject(newName, this, true);

	name = newName;
	unconditionalValidation();

	return true;
}

public boolean rename(String newName)
{
	String newObjName = Group.substractObjectName(newName);
	String oldObjName = Group.substractObjectName(getName());

	if(!oldObjName.equals(newObjName))
	{
		getParent().removeObject(oldObjName);
		String fullName = StringUtils.replaceEnding(getName(), oldObjName, newObjName);
		name = fullName;
		getParent().addSubObject(newObjName, this);
	}
	
// move if needed
	moveToGroup(Group.substractParentName(newName));

	return true;
}
	
public void revalidatePosition()
{
	setX((((VisibleObject)startVertex).getX() + ((VisibleObject)endVertex).getX()) / 2);
	setY(Math.min(((VisibleObject)startVertex).getY(), ((VisibleObject)endVertex).getY()));

	double rscale = getRscale();

	setRx((int)(getX() * rscale));
	setRy((int)(getY() * rscale));
}

public void setIsDashed(boolean parIsDashed)
{
	dashed = parIsDashed;
}

protected void validate()
{
	revalidatePosition();
	
	double rscale = getRscale();

	setRwidth((int)(getWidth() * rscale));
	setRheight((int)(getHeight() * rscale));
}

/**
 * Returned value inicates change
 * Creation date: (21.12.2000 22:21:12)
 * @return com.cosylab.visible.objects.VisibleObject
 * @param x int
 * @param y int
 */
public VisibleObject hiliteComponentsCheck(int x, int y) {

	if (startVertex.intersects(x, y)!=null)
		return startVertex;
	if (endVertex.intersects(x, y)!=null)
		return endVertex;
	return null;
}


/**
 * Default impmlementation for square (must be rescaled)
 * Creation date: (19.12.2000 20:20:20)
 * @return com.cosylab.visible.objects.VisibleObject
 * @param px int
 * @param py int
 */
public VisibleObject intersects(int px, int py) {

	// first check on small sub-objects like connectors
	VisibleObject spotted = hiliteComponentsCheck(px, py);
  	if ((spotted==null) &&
  		(getRx()<=px) && (getRy()<=py) && 
		((getRx()+getRwidth())>=px) && 
		((getRy()+getRheight())>=py))
		spotted = this;
	return spotted;
}
	
}