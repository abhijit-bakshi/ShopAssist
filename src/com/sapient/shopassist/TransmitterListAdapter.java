package com.sapient.shopassist;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.sapient.shopassist.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TransmitterListAdapter extends BaseAdapter {

    private static final String TAG = "TransmitterListAdapter";
    
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Activity activity;
    private int position;
    private LinkedHashMap<String, TransmitterAttributes> mEntries = new LinkedHashMap<String, TransmitterAttributes>();

    public TransmitterListAdapter(Context context, Activity activity) {
        mContext = context;
        this.activity = activity;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mEntries.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mEntries.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;

        ProgressBar progressBar = (ProgressBar) this.activity.findViewById(R.id.initialProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) mLayoutInflater.inflate(R.layout.activity_proximitytransmitters, parent, false);

        }
        else {
            itemView = (RelativeLayout) convertView;
        }

        TextView titleText = (TextView) itemView.findViewById(R.id.listTitle);
        TextView tempText = (TextView) itemView.findViewById(R.id.temperatureTextField);
       
        ImageView qImageView = (ImageView) itemView.findViewById(R.id.icon);
        
        
        ArrayList<TransmitterAttributes> arrayofAllTransmitter = new ArrayList<TransmitterAttributes>(mEntries.values());
        if (position >= arrayofAllTransmitter.size()) {
            return itemView;
        }

        TransmitterAttributes transmitter = (arrayofAllTransmitter).get(position);
        Log.d(TAG, "Position is " + position + "Transmitter name is " + transmitter.getName());

        if (transmitter.isDepart() == true) {
            Log.d(TAG, "Transmitter removed graying it out");
            float alphavalue = 0.3f;
            itemView.setAlpha(alphavalue);
        }
        else {
            Log.d(TAG, "Transmitter added not graying ");
            itemView.setAlpha(1);
        }

        titleText.setText(transmitter.getOfferTitle());
        qImageView.setImageResource(transmitter.getPictureId());
       // tempText.setText(transmitter.getOfferText());

        return itemView;
    }

    public synchronized void addTransmitters(LinkedHashMap<String, TransmitterAttributes> entries) {
        mEntries = entries;
        super.notifyDataSetChanged();
    }

    public synchronized void removeTransmitters() {
        mEntries.clear();
        super.notifyDataSetChanged();
    }

    public void upDateEntries(LinkedHashMap<String, TransmitterAttributes> entries) {
        mEntries = entries;
        super.notifyDataSetChanged();
    }

    public int progressValue(int rssi) {
        double progressValue = 0;
        if (rssi >= -60) {
            progressValue = 100;
        }
        else if (rssi <= -90) {
            progressValue = 10;
        }
        else {
            double range = -60.0 - (-90.0);
            double percentage = (-60.0 - rssi) / range;
            progressValue = (1 - percentage) * 100;
        }

        int value = (int) progressValue;
        Log.d("TransmitterListAdapter", "int progressvalue is :" + value);

        return value;
    }

}