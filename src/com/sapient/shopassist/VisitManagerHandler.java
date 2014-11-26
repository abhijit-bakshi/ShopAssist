package com.sapient.shopassist;

import java.util.Date;
import java.util.LinkedHashMap;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gimbal.proximity.Proximity;
import com.gimbal.proximity.ProximityFactory;
import com.gimbal.proximity.ProximityOptions;
import com.gimbal.proximity.Visit;
import com.gimbal.proximity.VisitListener;
import com.gimbal.proximity.VisitManager;

public class VisitManagerHandler implements VisitListener {
	
    private static final String PROXIMITY_APP_ID = "e2ca14f4135a384947ed454147e38d0eb51c3c47f036f3311d33854ee00c605d";
    private static final String PROXIMITY_APP_SECRET = "4f8b636d87cade1ef245fdffa0fea8222a10666287af35aa69bb2f0039210a54";
    private String userId = "testShopper";
    private Boolean sb1Notified = false;
    private Boolean sb2Notified = false;
    
    
    private static final String TAG = "VisitManagerHandler";

    private final LinkedHashMap<String, TransmitterAttributes> transmitters = new LinkedHashMap<String, TransmitterAttributes>();
    private final VisitManager visitManager = ProximityFactory.getInstance().createVisitManager();
    private Context context;
    private NotificationManager mNotificationManager;
    
    public void init(Context ctxt) {
        this.context = ctxt;
    }

    public void stopScanning() {
        visitManager.stop();
    }

    public void startScanning() {
        startScanningWithOptions();
    }

    private void startScanningWithOptions() {
        visitManager.setVisitListener(this);
        ProximityOptions options = new ProximityOptions();
        options.setOption(ProximityOptions.VisitOptionSignalStrengthWindowKey,
                ProximityOptions.VisitOptionSignalStrengthWindowNone);
        options.setOption(ProximityOptions.VisitOptionArrivalRSSIKey,
                -75);
        options.setOption(ProximityOptions.VisitOptionDepartureRSSIKey,
                -100);
        visitManager.startWithOptions(options);
    }

    @SuppressLint("DefaultLocale")
	@Override
    public void receivedSighting(Visit visit, Date updateTime, Integer rssi) {
  
        //Need to call recommender service to generate offer based on user profile and coupon service to generate unique coupon.
    	
    	String beacon = visit.getTransmitter().getName();
    	
        if(beacon.equalsIgnoreCase("SB1")  && rssi > -70 && !sb1Notified) {
        String msg = "Flat 50% off on all leather laptop bags at our store for the next 4 hours only. Use coupon code: " + visit.getTransmitter().getName() + rssi + " at checkout.";
        Log.d(TAG, msg);
        sendNotification(10,msg,beacon,userId);
        sb1Notified = true;
        }
        if(beacon.equalsIgnoreCase("SB2") && rssi > -70 && !sb2Notified){
        String msg = "Upto 50% off on all Lego toys at our store for the next 2 hours only. Use coupon code: " + visit.getTransmitter().getName() + rssi + " at checkout.";
        Log.d(TAG, msg);
        sendNotification(20,msg,beacon,userId);
        sb2Notified = true;
        }
    }

    @Override
    public void didArrive(Visit visit) {
    	String msg = "Arrived near " + visit.getTransmitter().getName();
        Log.d(TAG, msg);
    }

    @Override
    public void didDepart(Visit visit) {
    	String msg = "I got DEPART for " + visit.getTransmitter().getName();
        Log.d(TAG, msg);
        String name = visit.getTransmitter().getName();
        TransmitterAttributes attributes = new TransmitterAttributes();
        attributes.setDepart(true);
        transmitters.put(name, attributes);
    }
    
    private void sendNotification(Integer notificationId, String msg, String beacon,String userId){
    	    
        mNotificationManager = (NotificationManager)
        		context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        Intent notificationIntent = new Intent(context,ProximityTransmittersActivity.class);
        notificationIntent.putExtra("notificationId",notificationId);        
        notificationIntent.putExtra("message",msg);
        notificationIntent.putExtra("beacon",beacon);
        notificationIntent.putExtra("userId",userId);
        notificationIntent.setAction(Intent.ACTION_SEND);
        
        PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId,
        		notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        
        Notification.Builder mBuilder =
                new Notification.Builder(context)
        .setSmallIcon(R.drawable.proximitylogo)
        .setContentTitle("Offer!")
        .setStyle(new Notification.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }
    
    public static void initializeProximity(Context context, Application app) {
        Log.d(TAG, "initializeProximity");

        Proximity.initialize(context, PROXIMITY_APP_ID, PROXIMITY_APP_SECRET);
        Proximity.optimizeWithApplicationLifecycle(app);
    }

}