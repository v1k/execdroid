package com.rhonda.execdroid;

import java.util.List;

import android.widget.Toast;

public class Instruction {
	
	private String name;
	private List<String> args;
	private boolean running;
	private Result res;
	
	public Instruction(String name, List<String> args){
		this.name = name;
		this.args = args;
		this.running = false;
		this.res = new Result(false, "");
		this.running = false;
	}
	
	public String getName(){
		return name;
	}
	
	public List <String> getArgs(){
		return args;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public Result getResult(){
		return res;
	}
	
	public void run(){
		running = true;
		if (this.name.equals("Alarm")){
			Alarm al = new Alarm(this.args);
			this.res = al.verifyAlarm();
		}
		else{
			CharSequence text = "Instruction name is incorrect: ".concat(this.name);
            Toast.makeText(MainService.getContext(), text, Toast.LENGTH_SHORT).show();
            running = false;
		}
	}
}
