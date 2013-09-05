package com.rhonda.execdroid;

public class Result {
	private boolean result;
	private String msg = "";
	
	Result(boolean res, String msg){
		this.result = res;
		this.msg = msg;
	}
	
	Result (boolean res){
		this.result = res;
	}
	
	public boolean getValue(){
		return result;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public void setResult(boolean res, String msg){
		this.result = res;
		this.msg = msg;
	}
	
	public void setResult(boolean res){
		this.result = res;
	}
}
