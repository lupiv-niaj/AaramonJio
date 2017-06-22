package com.aaramon.aaramonjio.syncproduct;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GSTIN_StartService extends Service {
    private Context mContext;
    private GSTIN_AlarmManagerBroadcastReceiver alarm;

    public GSTIN_StartService(Context context) {
        this.mContext = context;
    }

    public GSTIN_StartService() {

    }

    @Override
    public void onCreate() {
        alarm = new GSTIN_AlarmManagerBroadcastReceiver();
        Context context = this.getApplicationContext();
        if (alarm != null) {
            alarm.SetAlarm(context);
            Log.d("Alarm Start", "Successfully Alarm");
        } else {
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // return super.onStartCommand(intent, flags, startId);
        Log.i("Start", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}