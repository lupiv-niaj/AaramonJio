package com.aaramon.aaramonjio.syncproduct;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.aaramon.aaramonjio.dataaccess.DataStatic;

public class GSTIN_AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        PowerManager pm = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        // Acquire the lock
        wl.acquire();

        // You can do the processing here update the widget/remote views.
        // Check if Internet present
        if (DataStatic.isInternetAvailable(context)) {
            Intent intent1 = new Intent(context, TurnOver_Check.class);
            context.startService(intent1);
            // new asynctask().execute();
            // Release the lock
            wl.release();

        } else {
            Log.d("Alarm Call", "Calling Alarm");
            return;
        }
    }

    public void SetAlarm(Context context) {
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GSTIN_AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        // After after 30 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                7200000, pi);
    }

}