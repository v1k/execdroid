package com.rhonda.execdroid;

import com.rhonda.execdroid.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
//import android.util.Log;
import android.widget.Toast;

public class MainService extends Service{
	//private static final String TAG = "Execdroid";
	
	private NotificationManager mNM;
	public static Context mContext;
	private InstructionManager mInstManager;
	
	
	// Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.main_service_started;
	
	public class LocalBinder extends Binder {
        MainService getService() {
            return MainService.this;
        }
    }
	
	@Override
	public void onCreate() {
		mContext = getBaseContext();
		
		super.onCreate();
		
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        showNotification();
        
        mInstManager = new InstructionManager();
        if (mInstManager.getInstructions() == null) {
        	this.stopSelf();
        }
        
        mInstManager.runInstructions(this);
	}
	
	@Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        Toast.makeText(this, R.string.main_service_stopped, Toast.LENGTH_SHORT).show();
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	private final IBinder mBinder = new Binder() {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply,
                int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      return Service.START_NOT_STICKY;
    }
	
	private void showNotification() {
        CharSequence text = getText(R.string.main_service_started);

        Notification notification = new Notification(R.drawable.ic_launcher, text,
                System.currentTimeMillis());

        notification.setLatestEventInfo(this, getText(R.string.notify_subj),
                       getText(R.string.app_name), null);
        
        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }
	
	public static Context getContext(){
		return mContext;
	}
}
