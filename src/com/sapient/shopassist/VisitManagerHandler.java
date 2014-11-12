package com.sapient.shopassist;

import java.util.Date;
import java.util.LinkedHashMap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gimbal.proximity.ProximityFactory;
import com.gimbal.proximity.ProximityOptions;
import com.gimbal.proximity.Visit;
import com.gimbal.proximity.VisitListener;
import com.gimbal.proximity.VisitManager;

public class VisitManagerHandler implements VisitListener {

    private static final String TAG = "VisitManagerHandler";

    private final LinkedHashMap<String, TransmitterAttributes> transmitters = new LinkedHashMap<String, TransmitterAttributes>();
    private final VisitManager visitManager = ProximityFactory.getInstance().createVisitManager();
    private ProximityTransmittersActivity activity;
    private NotificationManager mNotificationManager;
    
    public void init(ProximityTransmittersActivity activity) {
        this.activity = activity;
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

    @Override
    public void receivedSighting(Visit visit, Date updateTime, Integer rssi) {
        Log.d(TAG, "I received a sighting! " + visit.getTransmitter().getName() + "and RSSI is:" + rssi);

        String name = visit.getTransmitter().getName();
        TransmitterAttributes attributes = new TransmitterAttributes();
        attributes.setBattery(visit.getTransmitter().getBattery());
        attributes.setIdentifier(visit.getTransmitter().getIdentifier());
        attributes.setName(visit.getTransmitter().getName());
        attributes.setTemperature(visit.getTransmitter().getTemperature());
        attributes.setRssi(rssi);
        attributes.setDepart(false);
        transmitters.put(name, attributes);
        this.activity.addDevice(transmitters);
    }

    @Override
    public void didArrive(Visit visit) {
    	String msg = "Arrived near " + visit.getTransmitter().getName();
        Log.d(TAG, msg);
    	sendNotification(msg,1);
    }

    @Override
    public void didDepart(Visit visit) {
    	String msg = "I got DEPART for " + visit.getTransmitter().getName();
        Log.d(TAG, msg);
        String name = visit.getTransmitter().getName();
        TransmitterAttributes attributes = new TransmitterAttributes();
        attributes.setDepart(true);
        transmitters.put(name, attributes);
        this.activity.addDevice(transmitters);
        sendNotification(msg,2);
    }
    
    private void sendNotification(String msg, Integer notificationId){
    	    
        mNotificationManager = (NotificationManager)
        		activity.getSystemService(Context.NOTIFICATION_SERVICE);
        
        PendingIntent contentIntent = PendingIntent.getActivity(activity, 0,
                new Intent(activity, ProximityTransmittersActivity.class), 0);
        
        Notification.Builder mBuilder =
                new Notification.Builder(activity)
        .setSmallIcon(R.drawable.proximitylogo)
        .setContentTitle("Offer!")
        .setStyle(new Notification.BigTextStyle()
        .bigText(msg))
        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }

}