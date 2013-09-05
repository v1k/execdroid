package com.rhonda.execdroid;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Service;
import android.os.Environment;
import android.widget.Toast;

public class InstructionManager {
	private List <Instruction> instructions = null;
	private TestLog mTestLog = null;

	InstructionManager(){
		instructions = getInstructionsFromFile();
		mTestLog = new TestLog();
	}
	
	private List<Instruction> getInstructionsFromFile(){
		List <Instruction> instructions = null;
        //Open xml instructions file.
        String filePathName = Environment.getExternalStorageDirectory().getPath() 
        		+ "/instructions.xml";
        File instructionFile = 
        		new File(filePathName);
        //Verify for instruction file exist on sd card.
        if (!instructionFile.exists()){
        	CharSequence text = "There is no instructions.xml file";
        	Toast.makeText(MainService.getContext(), text, Toast.LENGTH_SHORT).show();
        	return instructions;
        }
        InputStream in = null;
        try {
        	in = new BufferedInputStream(new FileInputStream(instructionFile));
        	InstructionsParser ip = new InstructionsParser();
        	//Parse instructions to instructions list.
        	instructions = ip.parse(in);
        	in.close();
        } catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instructions;
	}
	
	public List<Instruction> getInstructions(){
		return this.instructions;
	}
	
	public void runInstructions(final Service mService){
		if(instructions != null){
			Thread t = new Thread(){
				
				@Override
				public void run(){
					for (int i = 0; i < instructions.size(); ++i){
						instructions.get(i).run();
						mTestLog.addResult(instructions.get(i));
					}
					mService.stopSelf();
				}
			};
			t.start();
		}
	}
}
