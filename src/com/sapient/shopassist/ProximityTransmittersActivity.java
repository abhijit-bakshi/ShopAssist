package com.sapient.shopassist;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.sapient.shopassist.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class ProximityTransmittersActivity extends ListActivity implements OnClickListener {

    private TransmitterListAdapter adapter;
    
    JSONObject jo = new JSONObject();
    
    private String beacon;
    private String userId;
    private String username;
    private String offer;
    
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
        
        username = "Abhijit Bakshi";
        
        if(bundle != null){
        	
            beacon = bundle.getString("beacon");
            userId = bundle.getString("userId");
            username = "Abhijit Bakshi";//bundle.getString("username");
            offer = bundle.getString("message");
            
        	String name = Integer.toString(bundle.getInt("notificationId"));
            TransmitterAttributes attributes = new TransmitterAttributes();
            attributes.setIdentifier(beacon);
            attributes.setName(userId);
            attributes.setOfferTitle(offer);
            //attributes.setOfferText("Offer Text");
            transmitters.put(name, attributes);
            addDevice(transmitters);

        }

         
        ImageButton imageButtonRefresh = (ImageButton) findViewById(R.id.imageButton_refresh);

        imageButtonRefresh.setOnClickListener(this);

    }
    
	@Override
	public void onClick(View v) {
			
    	
    	try {
			jo.put("userId", userId);
        	jo.put("userName", username);
        	jo.put("beaconName", beacon);
        	jo.put("offer", offer);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	        new HttpRequestHandler() {
	            @Override
	            public HttpUriRequest getHttpRequestMethod() {

	                //return new HttpGet("http://shopassist-shopassist.rhcloud.com/api/v1/ping");
	            	HttpPost httpPost = new HttpPost("https://shopassist-shopassist.rhcloud.com/api/v1/beacons");
            	
	            	try {
						httpPost.setEntity(new StringEntity(jo.toString(), "UTF8"));
						 Log.i("JSONObject", jo.toString());
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            	//httpPost.setEntity(new ByteArrayEntity(jo.toString().getBytes("UTF8")));
	                httpPost.setHeader("Content-type", "application/json");
        	
	                return httpPost;
	            }
	            @Override
	            public void onResponse(String result) {
	                Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
	            }

	        }.execute();
	        
			showToastMessage("Please wait...A sales representative would arrive soon!");
			
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