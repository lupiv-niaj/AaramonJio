package com.aaramon.aaramonjio.order;

import android.content.Context;


public class DatabaseHandlerService
{
    public static void InitializeDataBase(Context context) {
        DatabaseHandler db = new DatabaseHandler(context);
        DatabaseManager.initializeInstance(db);
    }
}
