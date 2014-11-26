package com.sapient.shopassist;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class StoreNotificationService extends Service {

	private VisitManagerHandler manager;
	
	public StoreNotificationService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Context context = getApplicationContext();
		VisitManagerHandler.initializeProximity(context, getApplication());
		if(manager == null){
        manager = new VisitManagerHandler();
        manager.init(context);
        }
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Thread t = new Thread(new Runnable() {
			public void run() {
			manager.startScanning();
			}
			});
			t.start();

		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
