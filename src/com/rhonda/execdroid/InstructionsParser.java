package com.rhonda.execdroid;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class InstructionsParser {
	//final private String TAG = "InstructionParser";
	
	private static final String ns = null;
	
	
	public List<Instruction> parse(InputStream in) 
			throws XmlPullParserException, IOException{
		try{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readInstructions(parser);
		}finally{
			in.close();
		}
	}
	
	private List<Instruction> readInstructions(XmlPullParser parser) 
			throws XmlPullParserException, IOException {
	    List<Instruction> instructions = new ArrayList<Instruction>();

	    parser.require(XmlPullParser.START_TAG, ns, "instructions");
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        // Starts by looking for the entry tag
	        if (name.equals("instruction")) {
	            instructions.add(readInstruction(parser));
	        } else {
	            skip(parser);
	        }
	    }
	    return instructions;
	}
	private Instruction readInstruction(XmlPullParser parser) 
			throws XmlPullParserException, IOException {
		String instructionName = null;
		List <String> instructionArgs = null;
		parser.require(XmlPullParser.START_TAG, ns, "instruction");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("name")) {
				instructionName = readName(parser);
			} else if (name.equals("arg")) {
				if (instructionArgs == null){
					instructionArgs = new ArrayList<String>();
				}
				instructionArgs.add(readArg(parser));
			} else {
				skip(parser);
			}
		}
		return new Instruction(instructionName, instructionArgs);
	}
	
	// Processes name tags in the instruction.
	private String readName(XmlPullParser parser) 
			throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, ns, "name");
	    String name = readText(parser);
	    parser.require(XmlPullParser.END_TAG, ns, "name");
	    return name;
	}
	
	private String readArg(XmlPullParser parser) 
			throws IOException, XmlPullParserException{
		parser.require(XmlPullParser.START_TAG, ns, "arg");
		String arg = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "arg");
		return arg;
	}
	
	// For the tags name and ..., extracts their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
}
