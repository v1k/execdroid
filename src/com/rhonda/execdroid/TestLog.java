package com.rhonda.execdroid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.os.Environment;
//import android.util.Log;

public class TestLog {
	//private static final String TAG = "LOG";
	
	File logFile;
	TestLog(){
		String logPathName = Environment.getExternalStorageDirectory().getPath() 
				+ "/ExecdroidResult.csv";
		logFile = new File(logPathName);
		try {
			logFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addResult(Instruction instruction){
		//Log.i(TAG, "Log adds result.");
		FileOutputStream fOut;
		try {
			fOut = new FileOutputStream(logFile);
			OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
	        myOutWriter.append(instruction.getName());
	        myOutWriter.append(';');
	        if (instruction.getResult().getValue()){
	        	myOutWriter.append("PASS;");
	            myOutWriter.append(instruction.getResult().getMsg());
	        } else {
	        	myOutWriter.append("FAILED;");
	        	myOutWriter.append(instruction.getResult().getMsg());
	        }
	        myOutWriter.append(";\n");
	        myOutWriter.close();
	        fOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
