package com.sapient.shopassist;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class StoreNotificationService extends Service {
	
    //private static final String PROXIMITY_APP_ID = "e2ca14f4135a384947ed454147e38d0eb51c3c47f036f3311d33854ee00c605d";
    //private static final String PROXIMITY_APP_SECRET = "4f8b636d87cade1ef245fdffa0fea8222a10666287af35aa69bb2f0039210a54";

	
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
