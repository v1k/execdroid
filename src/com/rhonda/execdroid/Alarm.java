package com.rhonda.execdroid;

import java.util.Calendar;
import java.util.List;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.KeyEvent;

public class Alarm{
	private static final String TAG = "ALARM";
	
	private String type = null;
	private int am_pm = 0;
	private int hours = 0;
	private int minutes = 0;
	private int minutesToAdd = 0;
	private Intent alarmIntent = null;
	
	public Alarm(List <String> params){
		type = params.get(0);
		if(params.size() > 2){
			hours = Integer.valueOf(params.get(1));
			minutes = Integer.valueOf(params.get(2));
		}else if (params.size() == 2){
			minutesToAdd = Integer.valueOf(params.get(1));
		}
	}
	
	private void setAlarm(){
		
		if(minutesToAdd > 0){
			Calendar mCalendar = Calendar.getInstance();
			int minutesSumm = mCalendar.get(Calendar.MINUTE) + minutesToAdd;
			if (minutesSumm > 60){
				if(mCalendar.get(Calendar.HOUR) != 24)
					hours = mCalendar.get(Calendar.HOUR) + 1;
				else
					hours = 0;
				minutes = minutesSumm - 60;
			}else{
				am_pm = mCalendar.get(Calendar.AM_PM);
				hours = mCalendar.get(Calendar.HOUR);
				hours = am_pm == 0 ? hours : hours + 12;
				minutes = minutesSumm;
			}
		}
		alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
		alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hours);
		alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);
		alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		MainService.getContext().startActivity(alarmIntent);
	}
	
	private Result getAlarm(){

		final String ALARM_ALERT_ACTION = "com.android.deskclock.ALARM_ALERT";
		AlarmReceiver mAlarmReceiver = new AlarmReceiver(this.type);
		IntentFilter filter = new IntentFilter(ALARM_ALERT_ACTION);
		MainService.getContext().registerReceiver(mAlarmReceiver, filter);
		
		Result alarmStarted = new Result(true);
		
		try {
			for(int i = 0; !mAlarmReceiver.isAlarmCatched() && i < minutesToAdd * 60; i++)
				Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		if (!mAlarmReceiver.isAlarmCatched())
			alarmStarted.setResult(false, "Alarm start failed.");
		
		return alarmStarted;
	}
	
	public Result verifyAlarm(){
		Result res = null; 
		this.setAlarm();
		if(this.type.equals("Power")){
			//Log.i(TAG, "Should press powerkey");
			Instrumentation mInstrumentation = new Instrumentation();
			mInstrumentation.sendKeySync(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_POWER));
			mInstrumentation.sendKeySync(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_POWER));
		}
		res = this.getAlarm();
		return res;
	}
}
