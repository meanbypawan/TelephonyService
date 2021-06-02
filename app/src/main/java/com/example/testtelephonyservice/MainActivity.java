package com.example.testtelephonyservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.testtelephonyservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    TelephonyManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        manager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},111);
        }
        manager.listen(new TestStateListener(),PhoneStateListener.LISTEN_CALL_STATE);
        binding.GetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              try {
                  //String deviceId = manager.getDeviceId();
                  //String simNo = manager.getSimSerialNumber();
                  String netwrokOperator = manager.getNetworkOperator();
                  String nType = "";
                  int nt = manager.getNetworkType();
                  if (nt == TelephonyManager.NETWORK_TYPE_LTE)
                      nType = "4G";
                  else
                      nType = "Not 4G";
                  int simState = manager.getSimState();
                  String ss = "";
                  if(simState == TelephonyManager.SIM_STATE_READY)
                      ss = "READY";
                  else if(simState == TelephonyManager.SIM_STATE_ABSENT)
                      ss = "ABSENT";
                  else if(simState == TelephonyManager.SIM_STATE_UNKNOWN)
                      ss = "UNKnown";

                  String modelNumber = Build.MODEL;
                  String manufacturer = Build.MANUFACTURER;
                  String brand = Build.BRAND;
                  String device = Build.DEVICE;
                  String info = "Op Name : "+netwrokOperator+
                          "\nNet. Type : "+nType+
                          "\nSim state : "+ss+
                          "\nModel Number : "+modelNumber+
                          "\nBrand : "+brand+
                          "\nManu. : "+manufacturer+
                          "\nDevice : "+device;
                  binding.textView.setText(info);
              }
              catch (SecurityException e){
                  Toast.makeText(MainActivity.this, ""+e, Toast.LENGTH_SHORT).show();
              }
            }
        });
    }
    class TestStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);
            if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
                Toast.makeText(MainActivity.this, "OFFHOOK", Toast.LENGTH_SHORT).show();
                Log.e("================>","OFFHOOK");
            }
            else if(state == TelephonyManager.CALL_STATE_IDLE) {
                Toast.makeText(MainActivity.this, "IDLE", Toast.LENGTH_SHORT).show();
                Log.e("================>","IDLE");
            }
            else if(state == TelephonyManager.CALL_STATE_RINGING) {
                Toast.makeText(MainActivity.this, "RINGING", Toast.LENGTH_SHORT).show();
                Log.e("================>","RINGING");
            }
         }
    }
}