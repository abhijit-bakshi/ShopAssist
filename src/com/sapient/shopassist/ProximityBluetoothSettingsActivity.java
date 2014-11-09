
package com.sapient.shopassist;

import com.sapient.shopassist.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class ProximityBluetoothSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity_bluetooth_settings);
    }
    
    public void onSwitchToggle(View view) {
        boolean on = ((Switch) view).isChecked();
        String toggleMsg = null;
        if (on) {
            toggleMsg = "Bluetooth scanning enabled.";
        } else {
            toggleMsg = "Bluetooth scanning disabled.";
        }
        Toast toast = Toast.makeText(getApplicationContext(), toggleMsg, Toast.LENGTH_SHORT);
        toast.show();
    }

}
