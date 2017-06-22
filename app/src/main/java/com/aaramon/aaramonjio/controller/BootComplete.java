package com.aaramon.aaramonjio.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aaramon.aaramonjio.order.DatabaseHandlerService;
import com.aaramon.aaramonjio.order.MainDashboard;
import com.aaramon.aaramonjio.syncproduct.GSTIN_StartService;
import com.aaramon.aaramonjio.syncproduct.StartService;


public class BootComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
//			Intent serviceIntent = new Intent(context, CallDetectService.class);
//			context.startService(serviceIntent);
//			Intent intent1 = new Intent(context, AlarmBroad.class);
//			context.startService(intent1);

            DatabaseHandlerService.InitializeDataBase(context);
            Intent serviceIntent = new Intent(context, StartService.class);
            context.startService(serviceIntent);
            Intent turnoverIntent = new Intent(context, GSTIN_StartService.class);
           context.startService(turnoverIntent);
        }
    }

}
