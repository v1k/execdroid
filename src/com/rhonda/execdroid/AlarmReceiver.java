package com.rhonda.execdroid;

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.DisplayMetrics;
//import android.util.Log;
import android.view.MotionEvent;

public class AlarmReceiver extends BroadcastReceiver {
	
	//private static final String TAG = "ALARM RECEIVER";
	private static final String ALARM_ALERT_ACTION = "com.android.deskclock.ALARM_ALERT";
	private boolean alarmCatched;
	private String alarmCancelType;
	
	public AlarmReceiver(String alarmCancelType) {
		alarmCatched = false;
		this.alarmCancelType = alarmCancelType;
	}

	@Override
	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();
        if (action.equals(ALARM_ALERT_ACTION)) 
        {
        	//Log.i(TAG, "Broadcast catches alarm");
        	Thread thread = new Thread(){
           		
           		@Override
           		public void run(){
           			try {
    					Thread.sleep(2000);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
           			DisplayMetrics metrics = context.getResources().getDisplayMetrics();
           			int width = metrics.widthPixels;
           			int height = metrics.heightPixels / 100 * 92;
           			int posx = width / 2;
           			Instrumentation m_Instrumentation = new Instrumentation();
        			m_Instrumentation.sendPointerSync(MotionEvent.obtain(
        					SystemClock.uptimeMillis(),
        					SystemClock.uptimeMillis(),
            				MotionEvent.ACTION_DOWN,posx, height, 0));
        			if ((alarmCancelType.equals("Dismiss"))||(alarmCancelType.equals("Power")))
        				posx = width * 4 / 5;
        			else if (alarmCancelType.equals("Snooze"))
        				posx = width / 5;
            		m_Instrumentation.sendPointerSync(MotionEvent.obtain(
            				SystemClock.uptimeMillis(),
            				SystemClock.uptimeMillis(),
           					MotionEvent.ACTION_UP, posx, height, 0));
           		}
           	};
           	thread.start();
           	alarmCatched = true;
        }
	}
	
	public boolean isAlarmCatched(){
		return alarmCatched;
	}
}
