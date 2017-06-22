package com.aaramon.aaramonjio.syncproduct;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DELL STORE on 08-Mar-17.
 */

public class JsonArray_Creator {
    //    public String cursorToString(Cursor crs, String array_object) {
    public String cursorToString(Cursor crs) {
        JSONArray arr = new JSONArray();
        try {
            crs.moveToFirst();
            while (!crs.isAfterLast()) {
                int nColumns = crs.getColumnCount();
                JSONObject row = new JSONObject();
                for (int i = 0; i < nColumns; i++) {
                    String colName = crs.getColumnName(i);
                    if (colName != null) {
                        String val = "";
                        switch (crs.getType(i)) {
                            case Cursor.FIELD_TYPE_BLOB:
                                row.put(colName, crs.getBlob(i).toString());
                                break;
                            case Cursor.FIELD_TYPE_FLOAT:
                                row.put(colName, crs.getDouble(i));
                                break;
                            case Cursor.FIELD_TYPE_INTEGER:
                                row.put(colName, crs.getLong(i));
                                break;
                            case Cursor.FIELD_TYPE_NULL:
                                row.put(colName, null);
                                break;
                            case Cursor.FIELD_TYPE_STRING:
                                row.put(colName, crs.getString(i));
                                break;
                        }
                    }
                }
                arr.put(row);
                if (!crs.moveToNext())
                    break;
            }
//            if(array_object.equalsIgnoreCase(""))
//            {
//
//            }
//            else
//            {
//                JSONObject json = new JSONObject();
//                json.put(array_object, arr.toString());
//                return json.toString();
//            }
        } catch (JSONException e) {
        }
        return arr.toString();
    }
}
