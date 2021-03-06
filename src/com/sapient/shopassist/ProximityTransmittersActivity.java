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
import android.widget.ImageView;
import android.widget.Toast;

public class ProximityTransmittersActivity extends ListActivity implements OnClickListener {

    private TransmitterListAdapter adapter;
    
    JSONObject jsonObject = new JSONObject();
    
    private String beacon;
    private String userId;
    private String username;
    private String offerTitle;
    private String offer;
    private Integer pictureId;
    
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
        
        //username = "Abhijit Bakshi";
        
        if(bundle != null){
        	
            beacon = bundle.getString("beacon");
            userId = bundle.getString("userId");
            username = bundle.getString("username");
            offerTitle = bundle.getString("title");
            offer = bundle.getString("message");
            pictureId = bundle.getInt("pictureId");
            
        	String name = Integer.toString(bundle.getInt("notificationId"));
            TransmitterAttributes attributes = new TransmitterAttributes();
            attributes.setIdentifier(beacon);
            attributes.setName(userId);
            attributes.setOfferTitle(offer);
            attributes.setPictureId(pictureId);
            transmitters.put(name, attributes);
            addPromotion(transmitters);
            
           // ImageView qImageView = (ImageView) findViewById(R.id.promopicture);
           // qImageView.setImageResource(pictureId);

        }

         
        ImageButton imageButtonRefresh = (ImageButton) findViewById(R.id.imageButton_refresh);

        imageButtonRefresh.setOnClickListener(this);

    }
    
	@Override
	public void onClick(View v) {
			
    	
    	try {
			jsonObject.put("userId", userId);
        	jsonObject.put("userName", username);
        	jsonObject.put("beaconName", beacon);
        	jsonObject.put("offer", offer);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	        new HttpRequestHandler() {
	            @Override
	            public HttpUriRequest getHttpRequestMethod() {

	            	HttpPost httpPost = new HttpPost("https://shopassist-shopassist.rhcloud.com/api/v1/beacons");
            	
	            	try {
						httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF8"));
						 Log.i("JSONObject", jsonObject.toString());
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
	                //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
	            	showToastMessage("Please wait...A sales representative would arrive soon!");
	            }

	        }.execute();
	        
			showToastMessage("Sending Request...!");
			
		}
    
    protected synchronized void addPromotion(final LinkedHashMap<String, TransmitterAttributes> entries) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            	adapter.addTransmitters(entries);
            	adapter.notifyDataSetChanged();
            }
        });
    }
}