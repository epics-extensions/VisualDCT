package com.cosylab.vdct.db;

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

import java.io.*;
import java.util.*;
import com.cosylab.vdct.Console;
import com.cosylab.vdct.util.StringUtils;

/**
 * This type was created in VisualAge.
 */
public class DBResolver {
	private static final String errorString = "Invalid VisualDCT layout data..."; 

	private static String nullString = "";

	private static String FIELD   = "field";
	private static String RECORD  = "record";
	private static String GRECORD = "grecord";
	private static String INCLUDE = "include";

	private static String PATH  	 = "path";
	private static String ADDPATH  	 = "addpath";

	private static String ENDSTR = "}";
	private static String SPACE = " ";
	private static String NL = "\n";

	private static String VDCTRECORD = "Record";
	private static String VDCTGROUP = "Group";
	private static String VDCTFIELD = "Field";
	private static String VDCTLINK = "Link";
	private static String VDCTCONNECTOR = "Connector";
/**
 * This method was created in VisualAge.
 * @return java.io.StreamTokenizer
 * @param fileName java.lang.String
 */
public static StreamTokenizer getStreamTokenizer(String fileName) {

	FileInputStream fi = null;
	StreamTokenizer tokenizer = null;
	
	try	{
		fi = new FileInputStream(fileName);
		tokenizer = new StreamTokenizer(new BufferedReader(new InputStreamReader(fi)));
		initializeTokenizer(tokenizer);
	} catch (IOException e) {
		Console.getInstance().println("\no) Error occured while opening file '"+fileName+"'");
		Console.getInstance().println(e);
	}

	return tokenizer;
}
/**
 * This method was created in VisualAge.
 * @param st java.io.StreamTokenizer
 */
public static void initializeTokenizer(StreamTokenizer tokenizer) {
	tokenizer.whitespaceChars(0, 32);
	tokenizer.wordChars(33, 255);			// reset
	tokenizer.eolIsSignificant(true);
	tokenizer.quoteChar(DBConstants.quoteChar);
	tokenizer.whitespaceChars(',', ',');
	tokenizer.whitespaceChars('{', '{');
	tokenizer.whitespaceChars('(', '(');
	tokenizer.whitespaceChars(')',')');
}
/**
 * VisualDCT layout data is also processed here
 * @param data com.cosylab.vdct.db.DBData
 * @param tokenizer java.io.StreamTokenizer
 */
public static String processComment(DBData data, StreamTokenizer tokenizer, String fileName) throws Exception {
  
 if ((data==null) || !tokenizer.sval.equals(DBConstants.layoutDataString)) {	// comment
	 String comment = tokenizer.sval;
	 
	 tokenizer.wordChars(32, 255);		
	 tokenizer.wordChars('\t', '\t');
	 
	 while ((tokenizer.nextToken() != tokenizer.TT_EOL) &&						// read till EOL
		    (tokenizer.ttype != tokenizer.TT_EOF))
	 	if (tokenizer.ttype == tokenizer.TT_NUMBER) {
		 	if (!comment.equals(nullString)) comment+=SPACE;
	 		comment=comment+tokenizer.nval;
	 	}
		else {
		 	if (!comment.equals(nullString)) comment+=SPACE;
			comment=comment+tokenizer.sval;
		}

	 initializeTokenizer(tokenizer);

	 return comment+NL;

 }
 else {																		// graphics layout data

	 DBRecordData rd;
	 DBFieldData fd;
 	 String str, str2, desc; int t, tx, tx2, ty;
 	 boolean r1, r2;

 	 while ((tokenizer.nextToken() != tokenizer.TT_EOL) &&
		    (tokenizer.ttype != tokenizer.TT_EOF)) 
		if (tokenizer.ttype == tokenizer.TT_WORD) 


				if (tokenizer.sval.equalsIgnoreCase(VDCTRECORD)) {

					// read record_name
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD)||
						(tokenizer.ttype == DBConstants.quoteChar)) str=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					rd = (DBRecordData)(data.getRecords().get(str));
					if (rd!=null) {
						// read x pos
						tokenizer.nextToken();
						if (tokenizer.ttype == tokenizer.TT_NUMBER) rd.setX((int)tokenizer.nval);
						else throw (new DBGParseException(errorString, tokenizer, fileName));
					
						// read y pos
						tokenizer.nextToken();
						if (tokenizer.ttype == tokenizer.TT_NUMBER) rd.setY((int)tokenizer.nval);
						else throw (new DBGParseException(errorString, tokenizer, fileName));

						// read color
						tokenizer.nextToken();
						if (tokenizer.ttype == tokenizer.TT_NUMBER) rd.setColor(StringUtils.int2color((int)tokenizer.nval));
						else throw (new DBGParseException(errorString, tokenizer, fileName));

						// read rotation
						tokenizer.nextToken();
						if (tokenizer.ttype == tokenizer.TT_NUMBER) rd.setRotated(((int)tokenizer.nval)!=0);
						else throw (new DBGParseException(errorString, tokenizer, fileName));
						
						// read description
						tokenizer.nextToken();
						if ((tokenizer.ttype == tokenizer.TT_WORD)||
							(tokenizer.ttype == DBConstants.quoteChar)) rd.setDescription(tokenizer.sval);
						else throw (new DBGParseException(errorString, tokenizer, fileName));

					}
				}

				else if (tokenizer.sval.equalsIgnoreCase(VDCTFIELD)) {

					// read name
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD)||
						(tokenizer.ttype == DBConstants.quoteChar)) str=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					int pos = str.lastIndexOf(com.cosylab.vdct.Constants.FIELD_SEPARATOR);
					str2 = str.substring(pos+1);
					str = str.substring(0, pos);

					rd = (DBRecordData)data.getRecords().get(str);
					if (rd!=null)
					{
						fd=(DBFieldData)rd.getFields().get(str2);
						if (fd==null)
						{
							fd = new DBFieldData(str2, nullString);
							rd.addField(fd);
						}
							
						// read color
						tokenizer.nextToken();
						if (tokenizer.ttype == tokenizer.TT_NUMBER) fd.setColor(StringUtils.int2color((int)tokenizer.nval));
						else throw (new DBGParseException(errorString, tokenizer, fileName));

						// read rotation
						tokenizer.nextToken();
						if (tokenizer.ttype == tokenizer.TT_NUMBER) fd.setRotated(((int)tokenizer.nval)!=0);
						else throw (new DBGParseException(errorString, tokenizer, fileName));
						
						// read description
						tokenizer.nextToken();
						if ((tokenizer.ttype == tokenizer.TT_WORD)||
							(tokenizer.ttype == DBConstants.quoteChar)) fd.setDescription(tokenizer.sval);
						else throw (new DBGParseException(errorString, tokenizer, fileName));
							
					}
				}
				
				else if (tokenizer.sval.equalsIgnoreCase(VDCTCONNECTOR)) {

					// read connector id
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD)||
						(tokenizer.ttype == DBConstants.quoteChar)) str=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					// read target id
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD)||
						(tokenizer.ttype == DBConstants.quoteChar)) str2=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					// read x pos
					tokenizer.nextToken();
					if (tokenizer.ttype == tokenizer.TT_NUMBER) tx=(int)tokenizer.nval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));
					
					// read y pos
					tokenizer.nextToken();
					if (tokenizer.ttype == tokenizer.TT_NUMBER) ty=(int)tokenizer.nval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					// read color
					tokenizer.nextToken();
					if (tokenizer.ttype == tokenizer.TT_NUMBER) t=(int)tokenizer.nval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					// read description
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD)||
						(tokenizer.ttype == DBConstants.quoteChar)) desc=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					data.addConnector(new DBConnectorData(str, str2, tx, ty, StringUtils.int2color(t), desc));

				}


				else if (tokenizer.sval.equalsIgnoreCase(VDCTLINK)) {
					
					// read name
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD)||
						(tokenizer.ttype == DBConstants.quoteChar)) str=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));
				
					// read target
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD)||
						(tokenizer.ttype == DBConstants.quoteChar)) str2=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					data.addLink(new DBLinkData(str, str2));
						
				}
				
				else if (tokenizer.sval.equalsIgnoreCase(VDCTGROUP)) {

					// read group_name
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD)||
						(tokenizer.ttype == DBConstants.quoteChar)) str=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					// read x pos
					tokenizer.nextToken();
					if (tokenizer.ttype == tokenizer.TT_NUMBER) tx=(int)tokenizer.nval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));
					
					// read y pos
					tokenizer.nextToken();
					if (tokenizer.ttype == tokenizer.TT_NUMBER) ty=(int)tokenizer.nval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					// read color
					tokenizer.nextToken();
					if (tokenizer.ttype == tokenizer.TT_NUMBER) t=(int)tokenizer.nval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					// read description
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD)||
						(tokenizer.ttype == DBConstants.quoteChar)) desc=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					data.addGroup(new DBGroupData(str, tx, ty, StringUtils.int2color(t), desc));

				}

				/***************************************************/
				/************* Version v1.0 support ****************/
				/***************************************************/
				
				/****************** layout data ********************/

				else if (tokenizer.sval.equalsIgnoreCase("VDCTRecordPos")) {

					// read record_name
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD)||
						(tokenizer.ttype == DBConstants.quoteChar)) str=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					rd = (DBRecordData)(data.records.get(str));
					if (rd!=null) {
						// read x pos
						tokenizer.nextToken();
						if (tokenizer.ttype == tokenizer.TT_NUMBER) rd.setX((int)tokenizer.nval);
						else throw (new DBGParseException(errorString, tokenizer, fileName));
					
						// read y pos
						tokenizer.nextToken();
						if (tokenizer.ttype == tokenizer.TT_NUMBER) rd.setY((int)tokenizer.nval);
						else throw (new DBGParseException(errorString, tokenizer, fileName));

						// read rotation
						tokenizer.nextToken();
						if (tokenizer.ttype == tokenizer.TT_NUMBER) rd.setRotated(((int)tokenizer.nval)!=0);
						else throw (new DBGParseException(errorString, tokenizer, fileName));

						rd.setColor(java.awt.Color.black);
					}
				}

				else if (tokenizer.sval.equalsIgnoreCase("VDCTGroupPos")) {

					// read group_name
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD)||
						(tokenizer.ttype == DBConstants.quoteChar)) str=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					// read x pos
					tokenizer.nextToken();
					if (tokenizer.ttype == tokenizer.TT_NUMBER) tx=(int)tokenizer.nval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));
					
					// read y pos
					tokenizer.nextToken();
					if (tokenizer.ttype == tokenizer.TT_NUMBER) ty=(int)tokenizer.nval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					data.addGroup(new DBGroupData(str, tx, ty, java.awt.Color.black, nullString));

				}
				else if (tokenizer.sval.equalsIgnoreCase("VDCTLinkData")) {

					// read linkID
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD)||
						(tokenizer.ttype == DBConstants.quoteChar)) str=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					// read desc
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD) ||
						(tokenizer.ttype == DBConstants.quoteChar)) desc=tokenizer.sval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					// read x pos
					tokenizer.nextToken();
					if (tokenizer.ttype == tokenizer.TT_NUMBER) tx=(int)tokenizer.nval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));
					
					// read x2 pos
					tokenizer.nextToken();
					if (tokenizer.ttype == tokenizer.TT_NUMBER) tx2=(int)tokenizer.nval;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					// read obj1rotated
					tokenizer.nextToken();
					if (tokenizer.ttype == tokenizer.TT_NUMBER) r1=((int)tokenizer.nval)!=0;
					else throw (new DBGParseException(errorString, tokenizer, fileName));

					// read obj2rotated
					tokenizer.nextToken();
					if (tokenizer.ttype == tokenizer.TT_NUMBER) r2=((int)tokenizer.nval)!=0;
					else throw (new DBGParseException(errorString, tokenizer, fileName));
/*
					// transformation to v2 visual data
					
					int pos = str.lastIndexOf('/');
					if (pos<0) continue;	// invalid, skip
					StringBuffer fieldName = new StringBuffer(str);
					fieldName.setCharAt(pos, '.');

					pos = fieldName.toString().lastIndexOf(com.cosylab.vdct.Constants.FIELD_SEPARATOR);
					String field = fieldName.substring(pos+1);
					String record = fieldName.substring(0, pos);

					rd = (DBRecordData)data.getRecords().get(record);
					if (rd==null) continue;

					fd = (DBFieldData)rd.getFields().get(field);
					if (fd==null) continue;

					String target = com.cosylab.vdct.vdb.LinkProperties.getTargetFromString(fd.getValue());
					if (target==null) continue;


					if (!com.cosylab.vdct.graphics.objects.Group.substractParentName(fieldName.toString()).equals(com.cosylab.vdct.graphics.objects.Group.substractParentName(target)))
					{
						// intergroup link, no connector needed
						data.addLink(new DBLinkData(fieldName.toString(), target));
					}
					else
					{
						String connectorName = com.cosylab.vdct.graphics.objects.Group.substractObjectName(str);
						data.addLink(new DBLinkData(fieldName.toString(), connectorName));

						int y = rd.getX()+com.cosylab.vdct.Constants.RECORD_HEIGHT;
						pos = target.lastIndexOf(com.cosylab.vdct.Constants.FIELD_SEPARATOR);
						record = target.substring(0, pos);
						rd = (DBRecordData)data.getRecords().get(record);
						if (rd!=null)
							y = (y+rd.getX()+com.cosylab.vdct.Constants.RECORD_HEIGHT)/2;
						
						data.addConnector(new DBConnectorData(connectorName, target, tx, y, java.awt.Color.black, nullString));
					}
					fd.setRotated(r1);
*/
				}

		return nullString;
  }
}
/**
 * This method was created in VisualAge.
 * @param data com.cosylab.vdct.db.DBData
 * @param tokenizer java.io.StreamTokenizer
 */
public static void processDB(DBData data, StreamTokenizer tokenizer, String fileName) {
	
	DBRecordData rd;

	String comment = "";
	String str; int t;
	String include_filename;
	StreamTokenizer inctokenizer = null;

	if (data!=null)
	
	try	{
		
		while (tokenizer.nextToken() != tokenizer.TT_EOF)
  		  if (tokenizer.ttype == tokenizer.TT_WORD)
			if (tokenizer.sval.startsWith(DBConstants.commentString)) 
				comment+=processComment(data, tokenizer, fileName);
			else

				/****************** records ********************/

				if (tokenizer.sval.equalsIgnoreCase(RECORD) ||
					tokenizer.sval.equalsIgnoreCase(GRECORD)) {
			
					rd = new DBRecordData();

					// read record_type
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD) ||
						(tokenizer.ttype == DBConstants.quoteChar)) rd.setRecord_type(tokenizer.sval);
					else throw (new DBParseException("Invalid record_type...", tokenizer, fileName));

					// read record_name
					tokenizer.nextToken();
					if ((tokenizer.ttype == tokenizer.TT_WORD) ||
						(tokenizer.ttype == DBConstants.quoteChar)) rd.setName(tokenizer.sval);
					else throw (new DBParseException("Invalid record_name...", tokenizer, fileName));

					rd.setComment(comment);	comment = "";
					
					processFields(rd, tokenizer, fileName);
					data.addRecord(rd);
					
				}

				/****************** records ********************/
				
				else if (tokenizer.sval.equalsIgnoreCase(INCLUDE)) {

					// read incude_filename
					tokenizer.nextToken();
					if (tokenizer.ttype == DBConstants.quoteChar) include_filename=tokenizer.sval;
					else throw (new DBParseException("Invalid include_filename...", tokenizer, fileName));

					include_filename = com.cosylab.vdct.util.StringUtils.replaceFileName(fileName, include_filename);
					
					inctokenizer = getStreamTokenizer(include_filename);
					if (inctokenizer!=null) processDB(data, inctokenizer, include_filename);
				}
			
				/****************** path ********************/

				 else if (tokenizer.sval.equalsIgnoreCase(PATH) ||
  						  tokenizer.sval.equalsIgnoreCase(ADDPATH))
					Console.getInstance().println("Warning: 'path' and 'addpath' commands are not supported...");
				
	} catch (Exception e) {
		Console.getInstance().println("\n"+e);
	}	
	

}
/**
 * This method was created in VisualAge.
 * @param rd com.cosylab.vdct.db.DBRecordData
 * @param tokenizer java.io.StreamTokenizer
 * @exception java.lang.Exception The exception description.
 */
public static void processFields(DBRecordData rd, StreamTokenizer tokenizer, String fileName) throws Exception {

	DBFieldData fd;
	
	String name;
	String value;
	String comment = nullString;
	String include_filename;
	StreamTokenizer inctokenizer = null;
	
	if (rd!=null)
	
	/********************** fields area *************************/
		
	while (tokenizer.nextToken() != tokenizer.TT_EOF) 
		if (tokenizer.ttype == tokenizer.TT_WORD) 
			if (tokenizer.sval.equals(ENDSTR)) break;
			else if (tokenizer.sval.startsWith(DBConstants.commentString)) 
				comment+=processComment(null, tokenizer, fileName);
			else if (tokenizer.sval.equalsIgnoreCase(FIELD)) {

				// read field_name
				tokenizer.nextToken();
				if ((tokenizer.ttype == tokenizer.TT_WORD) ||
					(tokenizer.ttype == DBConstants.quoteChar)) name=tokenizer.sval;
				else throw (new DBParseException("Invalid field_name...", tokenizer, fileName));
					
				// read field_value
				tokenizer.nextToken();
				if (tokenizer.ttype == DBConstants.quoteChar) value=tokenizer.sval;
				else throw (new DBParseException("Invalid field_value...", tokenizer, fileName));

				fd = new DBFieldData(name, value);
				fd.setComment(comment);	comment = nullString;
				rd.addField(fd);
			}

			else if (tokenizer.sval.equalsIgnoreCase(INCLUDE)) {

				// read incude_filename
				tokenizer.nextToken();
				if (tokenizer.ttype == DBConstants.quoteChar) include_filename=tokenizer.sval;
				else throw (new DBParseException("Invalid include_filename...", tokenizer, fileName));

				include_filename = com.cosylab.vdct.util.StringUtils.replaceFileName(fileName, include_filename);

				inctokenizer = getStreamTokenizer(include_filename);
				if (inctokenizer!=null) processFields(rd, inctokenizer, include_filename);
			}	
						
	/***********************************************************/

}
/**
 * This method was created in VisualAge.
 * @return Vector
 * @param fileName java.lang.String
 */
public static DBData resolveDB(String fileName) {
	
	DBData data = new DBData();

	StreamTokenizer tokenizer = getStreamTokenizer(fileName);
	if (tokenizer!=null) processDB(data, tokenizer, fileName);
	else return null;
	
	return data;
}
/**
 * This method was created in VisualAge.
 * @return Vector
 * @param fileName java.lang.String
 */
public static DBData resolveDBasURL(java.net.URL url) {
	
	DBData data = new DBData();

	InputStream fi = null;
	StreamTokenizer tokenizer = null;	

	try	{
		fi = url.openStream();
		tokenizer = new StreamTokenizer(new BufferedReader(new InputStreamReader(fi)));
		initializeTokenizer(tokenizer);
	} catch (Exception e) {
		Console.getInstance().println("\nError occured while opening URL '"+url.toString()+"'");
		Console.getInstance().println(e);
		return null;
	}

	if (tokenizer!=null) processDB(data, tokenizer, url.toString());
	else return null;
	
	return data;
}
}