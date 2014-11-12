package com.sapient.shopassist;

import java.util.LinkedHashMap;

import com.sapient.shopassist.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class ProximityTransmittersActivity extends ListActivity {

    private VisitManagerHandler manager;
    private TransmitterListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(ProximityTransmittersActivity.class.getSimpleName(), "onCreate()");
        setContentView(R.layout.transmitter_list_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.proximitytransmitters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AppExitMenuItem:
                finish();
            default:
                return false;
        }
    }
    

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(ProximityTransmittersActivity.class.getSimpleName(), "onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(ProximityTransmittersActivity.class.getSimpleName(), "onDestroy()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(ProximityTransmittersActivity.class.getSimpleName(), "onResume()");

        setProgressBarVisibility(true);
     
        if(manager == null){
            manager = new VisitManagerHandler();
            manager.init(this);
            adapter = new TransmitterListAdapter(this, this, manager);
            setListAdapter(adapter);
            manager.startScanning();
        }

        ImageButton imageButtonRefresh = (ImageButton) findViewById(R.id.imageButton_refresh);

        imageButtonRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				adapter.removeTransmitters();
            	adapter.notifyDataSetChanged();
			}
        
        });

    }

    
    protected synchronized void addDevice(final LinkedHashMap<String, TransmitterAttributes> entries) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	adapter.addTransmitters(entries);
            	adapter.notifyDataSetChanged();
            }
        });
    }
}