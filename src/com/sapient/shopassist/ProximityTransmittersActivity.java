package com.sapient.shopassist;

import java.util.LinkedHashMap;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.sapient.shopassist.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class ProximityTransmittersActivity extends ListActivity {

    private TransmitterListAdapter adapter;
    private final LinkedHashMap<String, TransmitterAttributes> transmitters = new LinkedHashMap<String, TransmitterAttributes>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(ProximityTransmittersActivity.class.getSimpleName(), "onCreate()");
        setContentView(R.layout.transmitter_list_layout);
    
       
    }

    private void showToastMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
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
        Log.i(ProximityTransmittersActivity.class.getSimpleName(), "onResume()");

        setProgressBarVisibility(true);
        
        Intent intent = getIntent();        
        Bundle bundle = intent.getExtras(); 
        
        adapter = new TransmitterListAdapter(this, this);
        setListAdapter(adapter);
        
        if(bundle != null){
        	String name = Integer.toString(bundle.getInt("notificationId"));
            TransmitterAttributes attributes = new TransmitterAttributes();
            attributes.setIdentifier(bundle.getString("beacon"));
            attributes.setName(bundle.getString("userId"));
            attributes.setOfferTitle(bundle.getString("message"));
            //attributes.setOfferText("Offer Text");
            transmitters.put(name, attributes);
            addDevice(transmitters);
        }

         
        ImageButton imageButtonRefresh = (ImageButton) findViewById(R.id.imageButton_refresh);

        imageButtonRefresh.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
		        new HttpRequestHandler() {
		            @Override
		            public HttpUriRequest getHttpRequestMethod() {

		            	//return new HttpGet("http://www.google.com");
		                return new HttpGet("http://shopassist-shopassist.rhcloud.com/api/v1/ping");

		                 //return new HttpPost(url)
		            }
		            @Override
		            public void onResponse(String result) {
		                Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
		                //etResponse.setText(result);
		            }

		        }.execute();
		        
				showToastMessage("Please wait...A sales representative would arrive soon!");
				
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