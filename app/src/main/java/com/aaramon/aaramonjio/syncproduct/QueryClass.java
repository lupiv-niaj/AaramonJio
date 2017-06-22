package com.aaramon.aaramonjio.syncproduct;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.aaramon.aaramonjio.order.DatabaseManager;
import com.aaramon.aaramonjio.order.StaticVariable;
import com.aaramon.aaramonjio.supplier.ProductBatchModel;
import com.aaramon.aaramonjio.supplier.PurchaseEntryDetailModel;
import com.aaramon.aaramonjio.supplier.PurchaseEntryModel;
import com.aaramon.aaramonjio.supplier.TaxScheduleModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DELL STORE on 16-May-17.
 */

public class QueryClass implements StaticVariable {
    public static boolean CheckIsDataAlreadyInDBorNot(String TableName,
                                                      String dbfield, String fieldValue) {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void DeleteProductEntryDetail(long PurchaseEntryDetailId) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {

            db.delete(TABLE_AS_TEMP_PURCHASE_DETAIL, "" + COLUMN_PURCHASE_DETAIL_ID + "= '" + PurchaseEntryDetailId + "'", null);
            DatabaseManager.getInstance().closeDatabase();
            Log.d("RAGHU", "PURCAHSEENTRYDETAIL Deleted");
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public long InsertBrand(String server_brand_id, String brand_name, long company_id, String brand_image) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        long lastId = 0;
        try {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            long isinserted = 0;
            ContentValues values = new ContentValues();
            values.put(ASBRAND_SERVER_BRAND_ID, server_brand_id);
            values.put(ASBRAND_BRAND_NAME, brand_name);
            values.put(ASBRAND_COMPANY_ID, company_id);
            values.put(ASBRAND_IMAGE, brand_image);
            values.put(ASBRAND_DATE_ADDED, strDate);
            values.put(ASBRAND_SYNC, "0");
            values.put(ASBRAND_STATUS, "1");
            isinserted = db.insert(TABLE_AS_BRAND, null, values);
            Log.e("Brand Inserted ", "" + isinserted);
            String query = "SELECT " + ASBRAND_BRAND_ID + " from " + TABLE_AS_BRAND + " order by " + ASBRAND_BRAND_ID + " DESC limit 1";
            Cursor c = db.rawQuery(query.toString(), null);
            if (c != null && c.moveToFirst()) {
                lastId = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
            }
            DatabaseManager.getInstance().closeDatabase();
            return lastId;

            //return json_obj.toString();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public long InsertCompany(String server_company_id, String company_name, String company_image) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        long lastId = 0;
        try {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            long isinserted = 0;
            ContentValues values = new ContentValues();
            values.put(ASCOMPANY_SERVER_COMPANY_ID, server_company_id);
            values.put(ASCOMPANY_COMPANY_NAME, company_name);
            values.put(ASCOMPANY_DATE_ADDED, strDate);
            values.put(ASCOMPANY_IMAGE, company_image);
            values.put(ASCOMPANY_STATUS, "1");
            values.put(ASCOMPANY_SYNC, "0");
            isinserted = db.insert(TABLE_AS_COMPANY, null, values);
            Log.e("Company Inserted ", "" + isinserted);
            String query = "SELECT " + ASCOMPANY_COMPANY_ID + " from " + TABLE_AS_COMPANY + " order by " + ASCOMPANY_COMPANY_ID + " DESC limit 1";
            Cursor c = db.rawQuery(query.toString(), null);
            if (c != null && c.moveToFirst()) {
                lastId = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
            }
            DatabaseManager.getInstance().closeDatabase();
            return lastId;
            //return json_obj.toString();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }


    public long FindCompanyID(String server_company_id) {
        long company_id = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append(" SELECT " + ASCOMPANY_COMPANY_ID + " FROM " + TABLE_AS_COMPANY);
            Query.append(" WHERE " + ASCOMPANY_SERVER_COMPANY_ID + " = '" + server_company_id.toString().trim() + "'");
            Cursor cursor = db.rawQuery(Query.toString(), null);
            cursor.moveToNext();
            company_id = cursor.getLong(0);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
        return company_id;
    }


    public long FindBrandID(String server_brand_id) {
        long brand_id = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append(" SELECT " + ASBRAND_BRAND_ID + " FROM " + TABLE_AS_BRAND);
            Query.append(" WHERE " + ASBRAND_SERVER_BRAND_ID + " = '" + server_brand_id.toString().trim() + "'");
            Cursor cursor = db.rawQuery(Query.toString(), null);
            cursor.moveToNext();
            brand_id = cursor.getLong(0);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
        return brand_id;
    }


    public long FindMasterCategoryId(String server_master_id) {
        long brand_id = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append(" SELECT " + ASCATEGORY_CATEGORY_ID + " FROM " + TABLE_AS_CATEGORY);
            Query.append(" WHERE " + ASCATEGORY_SERVER_CATEGORY_ID + " = '" + server_master_id.toString().trim() + "'");
            Cursor cursor = db.rawQuery(Query.toString(), null);
            cursor.moveToNext();
            brand_id = cursor.getLong(0);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
        return brand_id;
    }


    public long InsertCategory(String server_category_id, String master_name, long parent_id, String level, String type, String category_image, String cgst, String sgst, String igst) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        long lastId = 0;
        try {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            long isinserted = 0;
            ContentValues values = new ContentValues();
            values.put(ASCATEGORY_SERVER_CATEGORY_ID, server_category_id);
            values.put(ASCATEGORY_CATEGORY_NAME, master_name);
            values.put(ASCATEGORY_PARENT_ID, parent_id);
            values.put(ASCATEGORY_CATEGORY_LEVEL, level);
            values.put(ASCATEGORY_CATEGORY_IMAGE, category_image);
            values.put(ASCATEGORY_CATEGORY_TYPE, type);
            values.put(ASCATEGORY_DATE_ADDED, strDate);
            values.put(ASCATEGORY_SYNC, "0");
            values.put(ASCATEGORY_STATUS, "1");
            values.put(ASCATEGORY_CGST_RATE, cgst);
            values.put(ASCATEGORY_SGST_RATE, sgst);
            values.put(ASCATEGORY_IGST_RATE, igst);
            isinserted = db.insert(TABLE_AS_CATEGORY, null, values);
            Log.e("Catergory Inserted ", "" + isinserted);
            String query = "SELECT " + ASCATEGORY_CATEGORY_ID + " from " + TABLE_AS_CATEGORY + " order by " + ASCATEGORY_CATEGORY_ID + " DESC limit 1";
            Cursor c = db.rawQuery(query.toString(), null);
            if (c != null && c.moveToFirst()) {
                lastId = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
            }
            DatabaseManager.getInstance().closeDatabase();
            return lastId;
            //return json_obj.toString();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }


    public long InsertLastProductid() {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        long lastId = 0;
        try {
            String query = "SELECT " + ASPROD_SERVER_PRODUCT_ID + " from " + TABLE_AS_PRODUCT + " order by " + ASPROD_SERVER_PRODUCT_ID + " DESC limit 1";
            Cursor c = db.rawQuery(query.toString(), null);
            if (c != null && c.moveToFirst()) {
                lastId = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
            }
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
        }
        return lastId;
    }

    public long InsertProduct(String server_product_id, long brand_id, long category_id, String product_name, String product_price, String ean_code, String aaramshop_ean_code, String image_url, String tax, String cess) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        long lastId = 0;
        try {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            long isinserted = 0;
            ContentValues values = new ContentValues();
            values.put(ASPROD_SERVER_PRODUCT_ID, server_product_id);
            values.put(ASPROD_BRAND_ID, brand_id);
            values.put(ASPROD_CATEGORY_ID, category_id);
            values.put(ASPROD_PRODUCT_NAME, product_name);
            values.put(ASPROD_PRODUCT_PRICE, product_price);
            values.put(ASPROD_EAN_CODE, ean_code);
            values.put(ASPROD_AARAMSHOP_CODE, aaramshop_ean_code);
            values.put(ASPROD_IMAGE_URL, image_url);
            values.put(ASPROD_DATE_ADDED, strDate);
            values.put(ASPROD_SYNC, "0");
            values.put(ASPROD_STATUS, "1");
            values.put(ASPROD_TAX_SCHEDULE_ID, tax);
            values.put(ASPROD_CESS_RATE, cess);
            isinserted = db.insert(TABLE_AS_PRODUCT, null, values);
            Log.e("Product Inserted ", "" + isinserted);
            String query = "SELECT " + ASPROD_PRODUCT_ID + " from " + TABLE_AS_PRODUCT + " order by " + ASPROD_PRODUCT_ID + " DESC limit 1";
            Cursor c = db.rawQuery(query.toString(), null);
            if (c != null && c.moveToFirst()) {
                lastId = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
            }
            DatabaseManager.getInstance().closeDatabase();
            return lastId;
            //return json_obj.toString();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public long FindMasterProductID(String server_product_id) {
        long brand_id = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append(" SELECT " + ASPROD_PRODUCT_ID + " FROM " + TABLE_AS_PRODUCT);
            Query.append(" WHERE " + ASPROD_SERVER_PRODUCT_ID + " = '" + server_product_id.toString().trim() + "'");
            Cursor cursor = db.rawQuery(Query.toString(), null);
            cursor.moveToNext();
            brand_id = cursor.getLong(0);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
        return brand_id;
    }


    public long FindMasterProductBarcodeID(String ean_code) {
        long brand_id = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append(" SELECT " + ASPBAR_PRODUCT_BARCODE_ID + " FROM " + TABLE_AS_PRODUCT_BARCODES);
            Query.append(" WHERE " + ASPBAR_PRODUCT_BARCODE_ID + " = '" + ean_code.toString().trim() + "'");
            Cursor cursor = db.rawQuery(Query.toString(), null);
            cursor.moveToNext();
            brand_id = cursor.getLong(0);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
        return brand_id;
    }

    public long InsertProductBarcode(String ean_code, long product_id) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        long lastId = 0;
        try {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            long isinserted = 0;
            ContentValues values = new ContentValues();
            values.put(ASPBAR_EAN_CODE, ean_code);
            values.put(ASPBAR_PRODUCT_ID, product_id);
            values.put(ASPBAR_DATETIME, strDate);
            isinserted = db.insert(TABLE_AS_PRODUCT_BARCODES, null, values);
            //Log.e("Barcode Inserted ", "" + isinserted);
            String query = "SELECT " + ASPBAR_PRODUCT_BARCODE_ID + " from " + TABLE_AS_PRODUCT_BARCODES + " order by " + ASPBAR_PRODUCT_BARCODE_ID + " DESC limit 1";
            Cursor c = db.rawQuery(query.toString(), null);
            if (c != null && c.moveToFirst()) {
                lastId = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
            }
            DatabaseManager.getInstance().closeDatabase();
            return lastId;
            //return json_obj.toString();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }


    public long InsertProductBatch(String merchant_id, String sequence, String batch_no, String expiry, String cost_price, String product_price, String offerprice, String offer_type, String offer_start_date, String offer_end_date, String stock, long product_barcode_id, long product_id) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        long lastId = 0;
        try {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            long isinserted = 0;
            ContentValues values = new ContentValues();
            values.put(ASPB_MERCHANT_ID, merchant_id);
            values.put(ASPB_SEQUENCE, sequence);
            values.put(ASPB_BATCH_NO, batch_no);
            values.put(ASPB_EXPIRY, expiry);
            values.put(ASPB_COST_PRICE, cost_price);
            values.put(ASPB_PRODUCT_PRICE, product_price);
            values.put(ASPB_OFFER_PRICE, offerprice);
            values.put(ASPB_OFFER_TYPE, offer_type);
            values.put(ASPB_OFFER_START_DATE, offer_start_date);
            values.put(ASPB_OFFER_END_DATE, offer_end_date);
            values.put(ASPB_STOCK, stock);
            values.put(ASPB_PRODUCT_BARCODE_ID, product_barcode_id);
            values.put(ASPB_PRODUCT_ID, product_id);
            values.put(ASPB_DATE_TIME, strDate);
            values.put(ASPB_SYNC, strDate);
            isinserted = db.insert(TABLE_AS_PRODUCT_BATCHES, null, values);
            // Log.e("Brand Inserted ", "" + isinserted);
            String query = "SELECT " + ASPB_PRODUCT_BATCH_ID + " from " + TABLE_AS_PRODUCT_BATCHES + " order by " + ASPB_PRODUCT_BATCH_ID + " DESC limit 1";
            Cursor c = db.rawQuery(query.toString(), null);
            if (c != null && c.moveToFirst()) {
                lastId = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
            }
            DatabaseManager.getInstance().closeDatabase();
            return lastId;
            //return json_obj.toString();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    //Change tax
    public String GetProduct(String ean_code) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " + TABLE_AS_PRODUCT_BARCODES + "." + ASPROD_PRODUCT_ID + ", " + ASPROD_PRODUCT_NAME + ","
                    + ASPROD_IMAGE_URL + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_PRICE + ","
                    + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BATCH_ID + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_BATCH_NO
                    + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_PRICE + "," + TABLE_AS_PRODUCT_BATCHES + "."
                    + ASPB_OFFER_START_DATE + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_END_DATE + "," + ASPROD_CESS_RATE + "," + TABLE_AS_CATEGORY + "."
                    + ASCATEGORY_CGST_RATE + "," + TABLE_AS_CATEGORY + "." + ASCATEGORY_SGST_RATE + "," + TABLE_AS_CATEGORY + "."
                    + ASCATEGORY_IGST_RATE + " FROM  " + TABLE_AS_PRODUCT_BARCODES
                    + " INNER JOIN " + TABLE_AS_PRODUCT + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_ID
                    + " = " + TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_ID + " INNER JOIN " + TABLE_AS_PRODUCT_BATCHES
                    + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_BARCODE_ID + " = " + TABLE_AS_PRODUCT_BATCHES
                    + "." + ASPB_PRODUCT_BARCODE_ID + " INNER JOIN " + TABLE_AS_CATEGORY + " ON " + TABLE_AS_PRODUCT + "." + ASPROD_CATEGORY_ID
                    + " = " + TABLE_AS_CATEGORY + "." + ASCATEGORY_CATEGORY_ID + " WHERE " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_EAN_CODE + " = '"
                    + ean_code + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    //change tax
    public String GetProductEanBlank(String ean_code) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " + TABLE_AS_PRODUCT_BARCODES + "." + ASPROD_PRODUCT_ID + ", " + ASPROD_PRODUCT_NAME + ","
                    + ASPROD_IMAGE_URL + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_PRICE + "," + TABLE_AS_PRODUCT_BATCHES
                    + "." + ASPB_OFFER_PRICE + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_START_DATE + ","
                    + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BATCH_ID + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_BATCH_NO + ","
                    + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_END_DATE + "," + ASPROD_CESS_RATE + "," + TABLE_AS_CATEGORY + "."
                    + ASCATEGORY_CGST_RATE + "," + TABLE_AS_CATEGORY + "." + ASCATEGORY_SGST_RATE + "," + TABLE_AS_CATEGORY + "."
                    + ASCATEGORY_IGST_RATE + " FROM  " + TABLE_AS_PRODUCT_BARCODES
                    + " INNER JOIN " + TABLE_AS_PRODUCT + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_ID
                    + " = " + TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_ID + " INNER JOIN " + TABLE_AS_PRODUCT_BATCHES
                    + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_BARCODE_ID + " = " + TABLE_AS_PRODUCT_BATCHES
                    + "." + ASPB_PRODUCT_BARCODE_ID + " INNER JOIN " + TABLE_AS_CATEGORY + " ON " + TABLE_AS_PRODUCT + "." + ASPROD_CATEGORY_ID
                    + " = " + TABLE_AS_CATEGORY + "." + ASCATEGORY_CATEGORY_ID + " WHERE " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_EAN_CODE + " = '"
                    + ean_code + "' LIMIT 20";
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    //change tax
    public String GetProductSearch(String name) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " + TABLE_AS_PRODUCT_BARCODES + "." + ASPROD_PRODUCT_ID + ", " + ASPROD_PRODUCT_NAME + ","
                    + ASPROD_IMAGE_URL + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_PRICE + "," + TABLE_AS_PRODUCT_BATCHES
                    + "." + ASPB_OFFER_PRICE + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_START_DATE + ","
                    + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BATCH_ID + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_BATCH_NO + ","
                    + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_END_DATE + "," + ASPROD_CESS_RATE + "," + TABLE_AS_CATEGORY + "."
                    + ASCATEGORY_CGST_RATE + "," + TABLE_AS_CATEGORY + "." + ASCATEGORY_SGST_RATE + "," + TABLE_AS_CATEGORY + "."
                    + ASCATEGORY_IGST_RATE + " FROM  " + TABLE_AS_PRODUCT_BARCODES
                    + " INNER JOIN " + TABLE_AS_PRODUCT + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_ID
                    + " = " + TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_ID + " INNER JOIN " + TABLE_AS_PRODUCT_BATCHES
                    + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_BARCODE_ID + " = " + TABLE_AS_PRODUCT_BATCHES
                    + "." + ASPB_PRODUCT_BARCODE_ID + " INNER JOIN " + TABLE_AS_CATEGORY + " ON " + TABLE_AS_PRODUCT + "." + ASPROD_CATEGORY_ID
                    + " = " + TABLE_AS_CATEGORY + "." + ASCATEGORY_CATEGORY_ID + " WHERE " + TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_NAME + " like '%"
                    + name + "%'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    //change tax
    public String GetProductByCategory(int category_id) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " + TABLE_AS_PRODUCT_BARCODES + "." + ASPROD_PRODUCT_ID + ", " + ASPROD_PRODUCT_NAME + "," + ASPROD_IMAGE_URL
                    + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_PRICE + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_PRICE + "," + TABLE_AS_PRODUCT_BATCHES + "."
                    + ASPB_OFFER_START_DATE + ","
                    + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BATCH_ID + "," + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_BATCH_NO + ","
                    + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_END_DATE
                    + "," + ASPROD_CESS_RATE + "," + TABLE_AS_CATEGORY + "."
                    + ASCATEGORY_CGST_RATE + "," + TABLE_AS_CATEGORY + "." + ASCATEGORY_SGST_RATE + "," + TABLE_AS_CATEGORY + "."
                    + ASCATEGORY_IGST_RATE + " FROM  " + TABLE_AS_PRODUCT_BARCODES + " INNER JOIN " + TABLE_AS_PRODUCT + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_ID
                    + " = " + TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_ID + " INNER JOIN " + TABLE_AS_PRODUCT_BATCHES + " ON " + TABLE_AS_PRODUCT_BARCODES + "."
                    + ASPBAR_PRODUCT_BARCODE_ID + " = " + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BARCODE_ID + " INNER JOIN " + TABLE_AS_CATEGORY + " ON " + TABLE_AS_PRODUCT + "." + ASPROD_CATEGORY_ID
                    + " = " + TABLE_AS_CATEGORY + "." + ASCATEGORY_CATEGORY_ID + " WHERE " + TABLE_AS_PRODUCT_BARCODES + "."
                    + ASPBAR_EAN_CODE + " != '' AND " + TABLE_AS_PRODUCT + "." + ASPROD_CATEGORY_ID + " = " + category_id;
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }


    public void DeleteOrderCart(long cart_id) {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            db.delete(TABLE_AS_TEMP_CART, COLUMN_TEMP_CART_ID + " = " + cart_id, null);
            db.delete(TABLE_AS_TEMP_CART_ITEM, COLUMN_TEMP_CART_ID + " = " + cart_id, null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public int GetProductCount(long cart_id) {
        String countQuery = "SELECT  * FROM " + TABLE_AS_TEMP_CART_ITEM + " WHERE " + COLUMN_TEMP_CART_ID + " = " + cart_id + "";
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public void DeleteCartProduct(int cart_item_id) {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            db.delete(TABLE_AS_TEMP_CART_ITEM, COLUMN_TEMP_CART_ITEM_ID + " = " + cart_item_id, null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
        }
    }


    public void DeleteCart(long cart_id) {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            db.delete(TABLE_AS_TEMP_CART, COLUMN_TEMP_CART_ID + " = " + cart_id, null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
        }
    }


    public void DeleteCartProductFromAdd(long cart_id, String product_id) {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            db.delete(TABLE_AS_TEMP_CART_ITEM, COLUMN_TEMP_CART_ID + "= '" + cart_id + "' AND " + COLUMN_PRODUCT_ID + "='" + product_id + "'", null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public void UpdateQty(long cart_id, String product_id, long quantity, double total_amt, double tax_amt) {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_QUANTITY, quantity);//I am  updating flag her\
            cv.put(COLUMN_TOTAL_AMOUNT, total_amt);
            cv.put(COLUMN_TOTAL_TAX_AMOUNT, tax_amt);
            db.update(TABLE_AS_TEMP_CART_ITEM, cv, "" + COLUMN_TEMP_CART_ID + "= '" + cart_id + "' AND " + COLUMN_PRODUCT_ID + "='" + product_id + "'", null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
    }

    public void UpdateBatchQty(long cart_id, String product_id, long quantity, double offerprice, double total_amt, double tax_amt) {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_QUANTITY, quantity);//I am  updating flag her\
            cv.put(COLUMN_TOTAL_AMOUNT, total_amt);
            cv.put(COLUMN_TOTAL_TAX_AMOUNT, tax_amt);
            db.update(TABLE_AS_TEMP_CART_ITEM, cv, "" + COLUMN_TEMP_CART_ID + "= '" + cart_id + "' AND " + COLUMN_PRODUCT_ID + "='" + product_id + "' AND " + COLUMN_OFFER_PRICE + " = " + offerprice + "", null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
    }


    public void UpdateCart(long cart_id, long quantity, double amount) {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_CART_ITEM, quantity);//I am  updating flag her\
            cv.put(COLUMN_CART_VALUE, amount);
            db.update(TABLE_AS_TEMP_CART, cv, "" + COLUMN_TEMP_CART_ID + "= '" + cart_id + "'", null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
    }

    public void Updateean() {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ASPROD_EAN_CODE, "");//I am  updating flag her\
            db.update(TABLE_AS_PRODUCT, cv, "" + ASPROD_PRODUCT_ID + "= 1", null);
            ContentValues cv1 = new ContentValues();
            cv1.put(ASPBAR_EAN_CODE, "");//I am  updating flag her\
            db.update(TABLE_AS_PRODUCT_BARCODES, cv1, "" + ASPBAR_PRODUCT_BARCODE_ID + "= 1", null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
    }

    public void UpdateProductInfo(String name, double product_price, String product_id, String ean, String offer_type, String offerprice) {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ASPROD_PRODUCT_NAME, name);//I am  updating flag her\
            cv.put(ASPROD_PRODUCT_PRICE, product_price);
            db.update(TABLE_AS_PRODUCT, cv, "" + ASPROD_PRODUCT_ID + "= " + product_id + "", null);
            String selectQuery = "SELECT " + ASPBAR_PRODUCT_BARCODE_ID + " FROM " + TABLE_AS_PRODUCT_BARCODES + " WHERE " + ASPBAR_EAN_CODE + " = '" + ean + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToNext();
            long barcode_id = cursor.getLong(0);
            cursor.close();
            ContentValues cv1 = new ContentValues();
            cv1.put(ASPB_OFFER_PRICE, offerprice);
            cv1.put(ASPB_PRODUCT_PRICE, product_price);
            cv1.put(ASPB_OFFER_TYPE, offer_type);//I am  updating flag her\
            db.update(TABLE_AS_PRODUCT_BATCHES, cv1, "" + ASPB_PRODUCT_BARCODE_ID + "= " + barcode_id + "", null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
    }


    public void UpdateProductInfoInCart(String name, double product_price, String product_id, String offerprice, long cart_id, double old_offerprice, double total_Amt, double tax_amt) {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_PRODUCT_NAME, name);//I am  updating flag her\
            cv.put(COLUMN_PRODUCT_PRICE, product_price);
            cv.put(COLUMN_OFFER_PRICE, offerprice);
            cv.put(COLUMN_TOTAL_AMOUNT, total_Amt);
            cv.put(COLUMN_TOTAL_TAX_AMOUNT, tax_amt);
            db.update(TABLE_AS_TEMP_CART_ITEM, cv, COLUMN_TEMP_CART_ID + "= '" + cart_id + "' AND " + COLUMN_PRODUCT_ID + "='" + product_id + "' AND " + COLUMN_OFFER_PRICE + " = " + old_offerprice + "", null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
    }


    public String GetActiveCart() throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " + COLUMN_TEMP_CART_ID + ", " + COLUMN_CART_NAME + "," + COLUMN_CART_VALUE + "," + COLUMN_CART_ITEM + "," + COLUMN_ADDED_DATE + " FROM " + TABLE_AS_TEMP_CART + " ORDER BY " + COLUMN_ADDED_DATE + " DESC";
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public String GetSubCategory() throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "select as_product.category_id,category_name from as_product inner join as_category on as_product.category_id=as_category.category_id where ean_code<>'' group by as_product.category_id LIMIT 20";
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }

    }

    public String GetCartValue(String cart_id) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " + COLUMN_CART_NAME + ", " + COLUMN_CART_VALUE + ", " + COLUMN_CART_ITEM + " FROM " + TABLE_AS_TEMP_CART + " WHERE " + COLUMN_TEMP_CART_ID + " = " + cart_id + "";
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }


    public String GetCartDetail(long cart_id) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " + COLUMN_TEMP_CART_ITEM_ID + ", " + COLUMN_TEMP_CART_ID + "," + COLUMN_PRODUCT_ID + ", " + COLUMN_PRODUCT_NAME + ", "
                    + COLUMN_PRODUCT_PRICE + ", " + COLUMN_OFFER_PRICE + ", " + COLUMN_IMAGE_URL + ",SUM(" + COLUMN_QUANTITY + ") AS " + COLUMN_QUANTITY + "," + COLUMN_CGST_RATE + ","
                    + COLUMN_SGST_RATE + "," + COLUMN_IGST_RATE + "," + COLUMN_CESS_RATE + "," + COLUMN_TOTAL_AMOUNT + "," + COLUMN_TOTAL_TAX_AMOUNT + "," + COLUMN_BATCH_ID + "," + COLUMN_BATCH_NO + " FROM " + TABLE_AS_TEMP_CART_ITEM
                    + " WHERE " + COLUMN_TEMP_CART_ID + " = " + cart_id + " GROUP BY " + COLUMN_PRODUCT_ID + "," + COLUMN_OFFER_PRICE
                    + " ORDER BY " + COLUMN_TEMP_CART_ITEM_ID + " DESC";
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }


    public String GetCartForOrder(long cart_id) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " + ASPROD_SERVER_PRODUCT_ID + ", " + TABLE_AS_TEMP_CART_ITEM + "."
                    + COLUMN_PRODUCT_NAME + ", " + TABLE_AS_TEMP_CART_ITEM + "." + COLUMN_PRODUCT_PRICE + ", "
                    + TABLE_AS_TEMP_CART_ITEM + "." + COLUMN_OFFER_PRICE + ", " + TABLE_AS_TEMP_CART_ITEM + "."
                    + COLUMN_QUANTITY + " FROM " + TABLE_AS_TEMP_CART_ITEM + "  LEFT JOIN " + TABLE_AS_PRODUCT + " ON "
                    + TABLE_AS_TEMP_CART_ITEM + "." + COLUMN_PRODUCT_ID + " = " + TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_ID
                    + " WHERE " + COLUMN_TEMP_CART_ID + " = " + cart_id + " GROUP BY " +
                    COLUMN_TEMP_CART_ITEM_ID + " ORDER BY " + COLUMN_TEMP_CART_ITEM_ID + " DESC";
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }


    public String GetCartForFMCGProduct(long cart_id) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " + ASPROD_SERVER_PRODUCT_ID + ", " + ASPROD_EAN_CODE + "," + TABLE_AS_TEMP_CART_ITEM + "."
                    + COLUMN_PRODUCT_NAME + ", " + TABLE_AS_TEMP_CART_ITEM + "." + COLUMN_PRODUCT_PRICE + ", "
                    + TABLE_AS_TEMP_CART_ITEM + "." + COLUMN_OFFER_PRICE + ", " + TABLE_AS_TEMP_CART_ITEM + "."
                    + COLUMN_QUANTITY + "," + TABLE_AS_TEMP_CART_ITEM + "." + COLUMN_IGST_RATE + "," + TABLE_AS_TEMP_CART_ITEM + "."
                    + COLUMN_SGST_RATE + "," + TABLE_AS_TEMP_CART_ITEM + "." + COLUMN_CGST_RATE + "," + TABLE_AS_TEMP_CART_ITEM + "."
                    + COLUMN_CESS_RATE + "," + TABLE_AS_TEMP_CART_ITEM + "." + COLUMN_BATCH_NO + "," + TABLE_AS_TEMP_CART_ITEM + "." + COLUMN_BATCH_ID
                    + " FROM " + TABLE_AS_TEMP_CART_ITEM + "  LEFT JOIN " + TABLE_AS_PRODUCT
                    + " ON " + TABLE_AS_TEMP_CART_ITEM + "." + COLUMN_PRODUCT_ID + " = " + TABLE_AS_PRODUCT + "."
                    + ASPROD_PRODUCT_ID + " WHERE " + COLUMN_TEMP_CART_ID + " = " + cart_id + " GROUP BY " +
                    COLUMN_TEMP_CART_ITEM_ID + " ORDER BY "
                    + COLUMN_TEMP_CART_ITEM_ID + " DESC";
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }


    public long FindCartID(String cart_name) {
        long cart_id = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append(" SELECT " + COLUMN_TEMP_CART_ID + " FROM " + TABLE_AS_TEMP_CART);
            Query.append(" WHERE " + COLUMN_CART_NAME + " = '" + cart_name.toString().trim() + "'");
            Cursor cursor = db.rawQuery(Query.toString(), null);
            cursor.moveToNext();
            cart_id = cursor.getLong(0);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
        return cart_id;
    }

    public long FindCartitemID(long cart_id, String product) {
        long exists = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append(" SELECT " + COLUMN_QUANTITY + " FROM " + TABLE_AS_TEMP_CART_ITEM);
            Query.append(" WHERE " + COLUMN_TEMP_CART_ID + " = " + cart_id + " AND " + COLUMN_PRODUCT_ID + " = '" + product + "'");
            Cursor cursor = db.rawQuery(Query.toString(), null);
            cursor.moveToNext();
            exists = cursor.getLong(0);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
        return exists;
    }

    public long FindCartitemOfferID(long cart_id, String product, double offerprice) {
        long exists = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append(" SELECT " + COLUMN_QUANTITY + " FROM " + TABLE_AS_TEMP_CART_ITEM);
            Query.append(" WHERE " + COLUMN_TEMP_CART_ID + " = " + cart_id + " AND " + COLUMN_PRODUCT_ID + " = '" + product + "' AND " + COLUMN_OFFER_PRICE + " = " + offerprice + "");
            Cursor cursor = db.rawQuery(Query.toString(), null);
            cursor.moveToNext();
            exists = cursor.getLong(0);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
        return exists;
    }


    public String FindEAN_Code(String product) {
        String exists = "";
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append(" SELECT " + ASPROD_EAN_CODE + " FROM " + TABLE_AS_PRODUCT);
            Query.append(" WHERE " + ASPROD_PRODUCT_ID + " = '" + product + "'");
            Cursor cursor = db.rawQuery(Query.toString(), null);
            cursor.moveToNext();
            exists = cursor.getString(0);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
        return exists;
    }


    public long FindMaxCartID() {
        long cart_id = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append(" SELECT MAX(" + COLUMN_TEMP_CART_ID + ") FROM " + TABLE_AS_TEMP_CART);
            Cursor cursor = db.rawQuery(Query.toString(), null);
            cursor.moveToNext();
            cart_id = cursor.getLong(0);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
        return cart_id;
    }


    public int FindCartCount() {
        int cart_id = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append(" SELECT * FROM " + TABLE_AS_TEMP_CART);
            Cursor cursor = db.rawQuery(Query.toString(), null);
            cart_id = cursor.getCount();
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {

        }
        return cart_id;
    }


    public long InsertCart(String cart_name, double cart_value, int cart_item, String cart_user) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        long lastId = 0;
        try {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            String strDate = sdfDate.format(now);
            long isinserted = 0;
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_NAME, cart_name);
            values.put(COLUMN_CART_VALUE, cart_value);
            values.put(COLUMN_CART_ITEM, cart_item);
            values.put(COLUMN_USER_ID, cart_user);
            values.put(COLUMN_ADDED_DATE, strDate);
            isinserted = db.insert(TABLE_AS_TEMP_CART, null, values);
            // Log.e("Brand Inserted ", "" + isinserted);
            String query = "SELECT " + COLUMN_TEMP_CART_ID + " from " + TABLE_AS_TEMP_CART + " order by " + COLUMN_TEMP_CART_ID + " DESC limit 1";
            Cursor c = db.rawQuery(query.toString(), null);
            if (c != null && c.moveToFirst()) {
                lastId = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
            }
            DatabaseManager.getInstance().closeDatabase();
            return lastId;
            //return json_obj.toString();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public void InsertCartDetail(long cart_id, String product_id, String product_name, double product_price, double offer_price, int qty, String image_url, double cgst, double sgst, double igst, double cess, double total_amt, double total_tax_amt, String batch_no, String batch_id) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        long lastId = 0;
        try {
            long isinserted = 0;
            ContentValues values = new ContentValues();
            values.put(COLUMN_TEMP_CART_ID, cart_id);
            values.put(COLUMN_PRODUCT_ID, product_id);
            values.put(COLUMN_PRODUCT_NAME, product_name);
            values.put(COLUMN_PRODUCT_PRICE, product_price);
            values.put(COLUMN_OFFER_PRICE, offer_price);
            values.put(COLUMN_QUANTITY, qty);
            values.put(COLUMN_IMAGE_URL, image_url);
            values.put(COLUMN_CGST_RATE, cgst);
            values.put(COLUMN_SGST_RATE, sgst);
            values.put(COLUMN_IGST_RATE, igst);
            values.put(COLUMN_CESS_RATE, cess);
            values.put(COLUMN_TOTAL_AMOUNT, total_amt);
            values.put(COLUMN_TOTAL_TAX_AMOUNT, total_tax_amt);
            values.put(COLUMN_BATCH_NO, batch_no);
            values.put(COLUMN_BATCH_ID, batch_id);
            isinserted = db.insert(TABLE_AS_TEMP_CART_ITEM, null, values);
            //Log.e("Brand Inserted ", "" + isinserted);

//            String query = "SELECT " + COLUMN_TEMP_CART_ITEM_ID + " from " + TABLE_AS_TEMP_CART_ITEM + " order by " + COLUMN_TEMP_CART_ITEM_ID + " DESC limit 1";
//            Cursor c = db.rawQuery(query.toString(), null);
//            if (c != null && c.moveToFirst()) {
//                lastId = c.getLong(0); //The 0 is the column index, we only have 1 column, so the index is 0
//                if (product_id.equalsIgnoreCase("0")) {
//                    ContentValues cv = new ContentValues();
//                    cv.put(COLUMN_PRODUCT_ID, lastId);//I am  updating flag her\
//                    db.update(TABLE_AS_TEMP_CART_ITEM, cv, COLUMN_TEMP_CART_ITEM_ID + "= '" + lastId + "'", null);
//                }
//            }
            DatabaseManager.getInstance().closeDatabase();
            //return json_obj.toString();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }


    // Added By Vijay on 22-05-2017

    public void DescribeTable(String TableName) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE tbl_name = " + TableName + " AND type='table'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                Log.d("Column", c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
    }

    public Boolean haveTaxSchedules() throws Exception {
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append("SELECT * FROM " + TABLE_AS_TAX_SCHEDULES);
            Cursor cursor = db.rawQuery(Query.toString(), null);
            int Rows = cursor.getCount();
            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
            return (Rows <= 0) ? false : true;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public String[] GetTotalItemsAndAmountPurchaseEntry(long PurchaseEntryId) throws Exception {
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append("SELECT " + COLUMN_PURCHASE_DETAIL_RATE + "," + COLUMN_PURCHASE_DETAIL_QUANTITY + "," + COLUMN_PURCHASE_DETAIL_TOTAL_TAX_AMOUNT + " FROM " + TABLE_AS_TEMP_PURCHASE_DETAIL + " WHERE " + COLUMN_PURCHASE_ID + "=" + PurchaseEntryId);
            Cursor cursor = db.rawQuery(Query.toString(), null);
            int Qty = 0;
            Double Amount = 0d;
            Double Tax_amt = 0d;
            while (cursor.moveToNext()) {
                Amount += cursor.getInt(0) * cursor.getInt(1);
                Qty += cursor.getInt(1);
                Tax_amt += cursor.getInt(2);
            }
            ;

            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
            return new String[]{String.valueOf(Amount), String.valueOf(Qty), String.valueOf(Tax_amt)};

        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }

    }


    public void insertTaxSchedules(int MerchantStoreId) throws Exception {
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().write_database();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdfDate.format(new Date());

            StringBuilder Query = new StringBuilder();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TAX_SCHEDULE_MERCHANT_STORE_ID, MerchantStoreId);
            values.put(COLUMN_TAX_SCHEDULE_TITLE, "I");
            values.put(COLUMN_TAX_SCHEDULE_PERCENTAGE, 0.00D);
            values.put(COLUMN_TAX_SCHEDULE_ACTIVE, 1);
            values.put(COLUMN_TAX_SCHEDULE_UPDATED_DATE, strDate);
            long InsertedId = db.insert(TABLE_AS_TAX_SCHEDULES, null, values);
            Log.d("Tax_Schedule_id ", String.valueOf(InsertedId));

            Query = new StringBuilder();
            values = new ContentValues();
            values.put(COLUMN_TAX_SCHEDULE_MERCHANT_STORE_ID, MerchantStoreId);
            values.put(COLUMN_TAX_SCHEDULE_TITLE, "II");
            values.put(COLUMN_TAX_SCHEDULE_PERCENTAGE, 1.00D);
            values.put(COLUMN_TAX_SCHEDULE_ACTIVE, 1);
            values.put(COLUMN_TAX_SCHEDULE_UPDATED_DATE, strDate);
            InsertedId = db.insert(TABLE_AS_TAX_SCHEDULES, null, values);
            Log.d("Tax_Schedule_id ", String.valueOf(InsertedId));


            Query = new StringBuilder();
            values = new ContentValues();
            values.put(COLUMN_TAX_SCHEDULE_MERCHANT_STORE_ID, MerchantStoreId);
            values.put(COLUMN_TAX_SCHEDULE_TITLE, "III");
            values.put(COLUMN_TAX_SCHEDULE_PERCENTAGE, 5.00D);
            values.put(COLUMN_TAX_SCHEDULE_ACTIVE, 1);
            values.put(COLUMN_TAX_SCHEDULE_UPDATED_DATE, strDate);
            InsertedId = db.insert(TABLE_AS_TAX_SCHEDULES, null, values);
            Log.d("Tax_Schedule_id ", String.valueOf(InsertedId));

            Query = new StringBuilder();
            values = new ContentValues();
            values.put(COLUMN_TAX_SCHEDULE_MERCHANT_STORE_ID, MerchantStoreId);
            values.put(COLUMN_TAX_SCHEDULE_TITLE, "IV");
            values.put(COLUMN_TAX_SCHEDULE_PERCENTAGE, 12.50D);
            values.put(COLUMN_TAX_SCHEDULE_ACTIVE, 1);
            values.put(COLUMN_TAX_SCHEDULE_UPDATED_DATE, strDate);
            InsertedId = db.insert(TABLE_AS_TAX_SCHEDULES, null, values);
            Log.d("Tax_Schedule_id ", String.valueOf(InsertedId));

            Query = new StringBuilder();
            values = new ContentValues();
            values.put(COLUMN_TAX_SCHEDULE_MERCHANT_STORE_ID, MerchantStoreId);
            values.put(COLUMN_TAX_SCHEDULE_TITLE, "V");
            values.put(COLUMN_TAX_SCHEDULE_PERCENTAGE, 20.00D);
            values.put(COLUMN_TAX_SCHEDULE_ACTIVE, 1);
            values.put(COLUMN_TAX_SCHEDULE_UPDATED_DATE, strDate);
            InsertedId = db.insert(TABLE_AS_TAX_SCHEDULES, null, values);
            Log.d("Tax_Schedule_id ", String.valueOf(InsertedId));

            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public ArrayList<TaxScheduleModel> getTaxSchedules(int Status) throws Exception {
        try {
            ArrayList<TaxScheduleModel> TaxScheduleList = new ArrayList<TaxScheduleModel>();
            SQLiteDatabase db = DatabaseManager.getInstance().write_database();
            StringBuilder Query = new StringBuilder();
            Query.append("SELECT * FROM " + TABLE_AS_TAX_SCHEDULES + " WHERE " + COLUMN_TAX_SCHEDULE_ACTIVE + " = " + Status);
            Cursor cursor = db.rawQuery(Query.toString(), null);
            while (cursor.moveToNext()) {
                TaxScheduleModel TaxScheduleModel = new TaxScheduleModel();
                TaxScheduleModel.setTaxScheduleID(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TAX_SCHEDULE_ID)));
                TaxScheduleModel.setTaxScheduleName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TAX_SCHEDULE_TITLE)));
                TaxScheduleModel.setTaxPercentage(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TAX_SCHEDULE_PERCENTAGE)));
                TaxScheduleModel.setIsActive(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TAX_SCHEDULE_ACTIVE)));
                TaxScheduleList.add(TaxScheduleModel);
            }
            DatabaseManager.getInstance().closeDatabase();
            return TaxScheduleList;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public int insertPurchaseEntry(PurchaseEntryModel PurchaseEntryModel, int MerchantStoreId) throws Exception {
        try {
            Log.d("POPO", "DAL Layer");
            SQLiteDatabase db = DatabaseManager.getInstance().write_database();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StringBuilder Query = new StringBuilder();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PURCHASE_MERCHANT_STORE_ID, MerchantStoreId);
            values.put(COLUMN_PURCHASE_SUPPLIER_ID, PurchaseEntryModel.getSupplierModel().getSupplierId());
            values.put(COLUMN_PURCHASE_BILL_NO, PurchaseEntryModel.getBillNumber());
            values.put(COLUMN_PURCHASE_BILL_DATE, PurchaseEntryModel.getBillDate());
            values.put(COLUMN_PURCHASE_PAYMENT_TYPE, PurchaseEntryModel.getPaymentMode());
            values.put(COLUMN_PURCHASE_DATE, sdfDate.format(new Date()));
            values.put(COLUMN_PURCHASE_PAYMENT_TERM, PurchaseEntryModel.getPaymentTerm());
            values.put(COLUMN_PURCHASE_DATE_ADDED, sdfDate.format(new Date()));
            db.insert(TABLE_AS_TEMP_PURCHASE, null, values);

            long InsertedId = 0;
            String query = "SELECT MAX(" + (COLUMN_PURCHASE_ID) + ") AS PK FROM " + TABLE_AS_TEMP_PURCHASE;
            Log.d("query", query);
            Cursor c = db.rawQuery(query.toString(), null);
            if (c != null && c.moveToFirst()) {
                InsertedId = c.getInt(c.getColumnIndexOrThrow("PK"));
            }
            Log.d("purchaseentryid", String.valueOf(InsertedId));
            DatabaseManager.getInstance().closeDatabase();
            return (int) InsertedId;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }


    //CHANGE KARNA HAI
    public int insertPurchaseEntryDetail(PurchaseEntryDetailModel PurchaseEntryDetailModel) throws Exception {
        try {
            Log.d("POPO", "DAL Layer");
            SQLiteDatabase db = DatabaseManager.getInstance().write_database();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StringBuilder Query = new StringBuilder();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PURCHASE_ID, PurchaseEntryDetailModel.getPurchaseId());
            values.put(COLUMN_PURCHASE_DETAIL_PRODUCT_BATCH_ID, PurchaseEntryDetailModel.getProductBatchModel().getProductBatchId());
            values.put(COLUMN_PURCHASE_DETAIL_RATE, PurchaseEntryDetailModel.getPurchaseRate());
            values.put(COLUMN_PURCHASE_DETAIL_MRP, PurchaseEntryDetailModel.getMRP());
            values.put(COLUMN_PURCHASE_DETAIL_SP, PurchaseEntryDetailModel.getSP());
            values.put(COLUMN_PURCHASE_DETAIL_QUANTITY, PurchaseEntryDetailModel.getQty());
            values.put(COLUMN_PURCHASE_DETAIL_TAX_PERCENTAGE, PurchaseEntryDetailModel.getTaxPercentage());
            values.put(COLUMN_PURCHASE_DETAIL_CREATED_AT, sdfDate.format(new Date()));
            values.put(COLUMN_PURCHASE_STATUS, "1");
            values.put(COLUMN_PURCHASE_DETAIL_CGST_RATE, PurchaseEntryDetailModel.getCgst());
            values.put(COLUMN_PURCHASE_DETAIL_SGST_RATE, PurchaseEntryDetailModel.getSgst());
            values.put(COLUMN_PURCHASE_DETAIL_IGST_RATE, PurchaseEntryDetailModel.getIgst());
            values.put(COLUMN_PURCHASE_DETAIL_CESS_RATE, PurchaseEntryDetailModel.getCess());
            values.put(COLUMN_PURCHASE_DETAIL_TOTAL_AMOUNT, PurchaseEntryDetailModel.getTotal_amt());
            values.put(COLUMN_PURCHASE_DETAIL_TOTAL_TAX_AMOUNT, PurchaseEntryDetailModel.getTax_amt());
            values.put(COLUMN_PURCHASE_DETAIL_BATCH_NO, PurchaseEntryDetailModel.getBatchNo());
            values.put(COLUMN_PURCHASE_DETAIL_NEWPURCHASE_PRICE, PurchaseEntryDetailModel.getNewPurchaseRate());
            Log.d("SCAN1975", values.toString());
            long i = db.insert(TABLE_AS_TEMP_PURCHASE_DETAIL, null, values);
            Log.e("Insert", i + "");
            long InsertedId = 0;
            String query = "SELECT MAX(" + (COLUMN_PURCHASE_DETAIL_ID) + ") AS PK FROM " + TABLE_AS_TEMP_PURCHASE_DETAIL;
            Log.d("SCAN1975", query);
            Cursor c = db.rawQuery(query.toString(), null);
            if (c != null && c.moveToFirst()) {
                InsertedId = c.getInt(c.getColumnIndexOrThrow("PK"));
            }
            Log.d("SCAN1975", String.valueOf(InsertedId));
            DatabaseManager.getInstance().closeDatabase();
            return (int) InsertedId;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    //CHANGE KARNA HAI
    public void UpdateProductEntryDetail(PurchaseEntryDetailModel PurchaseEntryDetailModel) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PURCHASE_DETAIL_RATE, PurchaseEntryDetailModel.getPurchaseRate());
            values.put(COLUMN_PURCHASE_DETAIL_MRP, PurchaseEntryDetailModel.getMRP());
            values.put(COLUMN_PURCHASE_DETAIL_SP, PurchaseEntryDetailModel.getSP());
            values.put(COLUMN_PURCHASE_DETAIL_QUANTITY, PurchaseEntryDetailModel.getQty());
            values.put(COLUMN_PURCHASE_DETAIL_TAX_PERCENTAGE, PurchaseEntryDetailModel.getTaxPercentage());
            values.put(COLUMN_TOTAL_AMOUNT, PurchaseEntryDetailModel.getTotal_amt());
            values.put(COLUMN_TOTAL_TAX_AMOUNT, PurchaseEntryDetailModel.getTax_amt());
            db.update(TABLE_AS_TEMP_PURCHASE_DETAIL, values, "" + COLUMN_PURCHASE_DETAIL_ID + "= '" + PurchaseEntryDetailModel.getPurchaseEntryDetailId() + "'", null);
            DatabaseManager.getInstance().closeDatabase();
            Log.d("RAGHU", "PURCAHSEENTRYDETAIL UPDATED");
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    //CHANGE KARNA HAI
    public void UpdatePurchaseEntryQty(long PurchaseEntryDetailId, int Qty, double total_amt, double tax_amt) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PURCHASE_DETAIL_QUANTITY, Qty);
            values.put(COLUMN_TOTAL_AMOUNT, total_amt);
            values.put(COLUMN_TOTAL_TAX_AMOUNT, tax_amt);
            db.update(TABLE_AS_TEMP_PURCHASE_DETAIL, values, "" + COLUMN_PURCHASE_DETAIL_ID + "= '" + PurchaseEntryDetailId + "'", null);
            DatabaseManager.getInstance().closeDatabase();
            Log.d("RAGHU", "PURCAHSEENTRYDETAIL QTY UPDATED");
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public Boolean haveProducts() throws Exception {
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append("SELECT * FROM " + TABLE_AS_PRODUCT);
            Cursor cursor = db.rawQuery(Query.toString(), null);
            int Rows = cursor.getCount();
            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
            return (Rows <= 0) ? false : true;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }

    }

    public String GetPurchaseEntry(int PurchaseEntryId) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " +
                    TABLE_AS_TEMP_PURCHASE + "." + COLUMN_PURCHASE_ID + ", " +
                    TABLE_AS_TEMP_PURCHASE + "." + COLUMN_PURCHASE_BILL_NO + "," +
                    TABLE_AS_TEMP_PURCHASE + "." + COLUMN_PURCHASE_BILL_DATE + "," +
                    TABLE_AS_TEMP_PURCHASE + "." + COLUMN_PURCHASE_PAYMENT_TERM + "," +
                    TABLE_AS_TEMP_PURCHASE + "." + COLUMN_PURCHASE_PAYMENT_TYPE + "," +
                    TABLE_AS_TEMP_PURCHASE + "." + COLUMN_PURCHASE_SUPPLIER_ID + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_ID + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_PRODUCT_BATCH_ID + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_BATCH_NO + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_EXPIRY + "," +
                    TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_NAME + "," +
                    TABLE_AS_PRODUCT + "." + ASPROD_IMAGE_URL + "," +
                    TABLE_AS_PRODUCT + "." + ASPROD_SERVER_PRODUCT_ID + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_RATE + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_MRP + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_SP + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_QUANTITY + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_IGST_RATE + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_SGST_RATE + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_CGST_RATE + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_CESS_RATE + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_TOTAL_AMOUNT + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_TOTAL_TAX_AMOUNT + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_BATCH_NO + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_NEWPURCHASE_PRICE +
                    " FROM  " + TABLE_AS_TEMP_PURCHASE +
                    " INNER JOIN " + TABLE_AS_TEMP_PURCHASE_DETAIL + " ON " + TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_ID + " = " + TABLE_AS_TEMP_PURCHASE + "." + COLUMN_PURCHASE_ID +
                    " INNER JOIN " + TABLE_AS_PRODUCT_BATCHES + " ON " + TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_PRODUCT_BATCH_ID + " = " + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BATCH_ID +
                    " INNER JOIN " + TABLE_AS_PRODUCT_BARCODES + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_BARCODE_ID + " = " + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BATCH_ID +
                    " INNER JOIN " + TABLE_AS_PRODUCT + " ON " + TABLE_AS_PRODUCT + "." + ASPBAR_PRODUCT_ID + " = " + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_ID +
                    " WHERE " + TABLE_AS_TEMP_PURCHASE + "." + COLUMN_PURCHASE_ID + " = " + PurchaseEntryId + "";

            Log.d("Query1975", selectQuery);
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            Log.d("Query1975", convert_data);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public String GetPurchaseEntryDetail(int PurchaseEntryDetailId) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_PRODUCT_BATCH_ID + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_BATCH_NO + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_EXPIRY + "," +
                    TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_NAME + "," +
                    TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_ID + "," +
                    TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_EAN_CODE + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_RATE + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_MRP + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_SP + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_TAX_PERCENTAGE + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_QUANTITY + "," +
                    TABLE_AS_PRODUCT + "." + ASPROD_CESS_RATE + "," +
                    TABLE_AS_CATEGORY + "." + ASCATEGORY_CGST_RATE + "," +
                    TABLE_AS_CATEGORY + "." + ASCATEGORY_IGST_RATE + "," +
                    TABLE_AS_CATEGORY + "." + ASCATEGORY_SGST_RATE + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_BATCH_NO + "," +
                    TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_NEWPURCHASE_PRICE +
                    " FROM  " + TABLE_AS_TEMP_PURCHASE_DETAIL +
                    " INNER JOIN " + TABLE_AS_PRODUCT_BATCHES + " ON " + TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_PRODUCT_BATCH_ID + " = " + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BATCH_ID +
                    " INNER JOIN " + TABLE_AS_PRODUCT_BARCODES + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_BARCODE_ID + " = " + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BATCH_ID +
                    " INNER JOIN " + TABLE_AS_PRODUCT + " ON " + TABLE_AS_PRODUCT + "." + ASPBAR_PRODUCT_ID + " = " + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_ID +
                    " INNER JOIN " + TABLE_AS_CATEGORY + " ON " + TABLE_AS_PRODUCT + "." + ASPROD_CATEGORY_ID + " = " + TABLE_AS_CATEGORY + "." + ASCATEGORY_CATEGORY_ID +
                    " WHERE " + TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_ID + " = " + PurchaseEntryDetailId + "";

            Log.d("Query1975", selectQuery);
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            Log.d("Query1975", convert_data);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public String GetProductPurchase(String ean_code) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " + TABLE_AS_PRODUCT_BARCODES + "." + ASPROD_PRODUCT_ID + ", " +
                    ASPROD_PRODUCT_NAME + "," +
                    ASPROD_IMAGE_URL + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BATCH_ID + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_STOCK + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_BATCH_NO + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_EXPIRY + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_COST_PRICE + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_PRICE + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_PRICE + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_START_DATE + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_END_DATE + "," +
                    TABLE_AS_PRODUCT + "." + ASPROD_CESS_RATE + "," +
                    TABLE_AS_CATEGORY + "." + ASCATEGORY_CGST_RATE + "," +
                    TABLE_AS_CATEGORY + "." + ASCATEGORY_IGST_RATE + "," +
                    TABLE_AS_CATEGORY + "." + ASCATEGORY_SGST_RATE +
                    " FROM  " +
                    TABLE_AS_PRODUCT_BARCODES +
                    " INNER JOIN " + TABLE_AS_PRODUCT + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_ID + " = " + TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_ID +
                    " INNER JOIN " + TABLE_AS_PRODUCT_BATCHES + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_BARCODE_ID + " = " + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BARCODE_ID +
                    " INNER JOIN " + TABLE_AS_CATEGORY + " ON " + TABLE_AS_PRODUCT + "." + ASPROD_CATEGORY_ID + " = " + TABLE_AS_CATEGORY + "." + ASCATEGORY_CATEGORY_ID +
                    " WHERE " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_EAN_CODE + " = " + ean_code + "";

            Log.d("Query", selectQuery);
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public String[] GetProductPurchaseSearch(String ProductName) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();

        try {
            String selectQuery = "SELECT " + ASPROD_PRODUCT_NAME + " FROM  " + TABLE_AS_PRODUCT +
                    " WHERE " + TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_NAME + " like '%" + ProductName + "%'";

            Log.d("Query", selectQuery);
            Cursor cursor = db.rawQuery(selectQuery, null);
            String[] ProductNames = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                ProductNames[i] = cursor.getString(0);
                i++;
            }
            DatabaseManager.getInstance().closeDatabase();
            return ProductNames;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public String GetProductEanCode(String ProductName) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();

        try {
            String selectQuery = "SELECT " + ASPROD_EAN_CODE + " FROM  " + TABLE_AS_PRODUCT +
                    " WHERE " + TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_NAME + " = '" + ProductName + "'";
            Log.d("Query", selectQuery);
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToNext();
            String ProductNames = cursor.getString(0);
            DatabaseManager.getInstance().closeDatabase();
            return ProductNames;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public long ProductBatchExists(long ProductBatchId, long PurchaseEntryId) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        long PurchaseEntryDetailId = 0;
        try {
            String selectQuery = "SELECT " + TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_ID + " FROM " + TABLE_AS_TEMP_PURCHASE_DETAIL +
                    " WHERE " + TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_DETAIL_PRODUCT_BATCH_ID + " = " + ProductBatchId +
                    " AND " + TABLE_AS_TEMP_PURCHASE_DETAIL + "." + COLUMN_PURCHASE_ID + " = " + PurchaseEntryId;
            Log.d("ALKA", selectQuery);
            Cursor cursor = db.rawQuery(selectQuery.toString(), null);
            int Rows = cursor.getCount();
            if (Rows > 0) {
                cursor.moveToNext();
                PurchaseEntryDetailId = cursor.getLong(0);
            }
            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
            return PurchaseEntryDetailId;

        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public void ChangeBatchStock(long ProductBatchId, int Stock, int Action, int ExistingStock) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        int ActualStock = 0;
        try {
            if (Action == ProductBatchModel.ADDBATCH) {
                ActualStock = ExistingStock + Stock;
            } else if (Action == ProductBatchModel.SUBTRACTBATCH) {
                ActualStock = ExistingStock - Stock;
            } else if (Action == ProductBatchModel.UPDATEBATCH) {
                ActualStock = Stock;
            }
            ContentValues cv = new ContentValues();
            cv.put(ASPB_STOCK, ActualStock);
            db.update(TABLE_AS_PRODUCT_BATCHES, cv, "" + ASPB_PRODUCT_BATCH_ID + "= '" + ProductBatchId + "'", null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public String GetProductBatch(long ProductBatchId) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().read_database();
        try {
            String selectQuery = "SELECT " + TABLE_AS_PRODUCT_BARCODES + "." + ASPROD_PRODUCT_ID + ", " +
                    ASPROD_PRODUCT_NAME + "," +
                    ASPROD_IMAGE_URL + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BATCH_ID + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_STOCK + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_BATCH_NO + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_EXPIRY + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_COST_PRICE + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_PRICE + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_PRICE + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_START_DATE + "," +
                    TABLE_AS_PRODUCT_BATCHES + "." + ASPB_OFFER_END_DATE + " FROM  " +
                    TABLE_AS_PRODUCT_BARCODES +
                    " INNER JOIN " + TABLE_AS_PRODUCT + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_ID + " = " + TABLE_AS_PRODUCT + "." + ASPROD_PRODUCT_ID +
                    " INNER JOIN " + TABLE_AS_PRODUCT_BATCHES + " ON " + TABLE_AS_PRODUCT_BARCODES + "." + ASPBAR_PRODUCT_BARCODE_ID + " = " + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BARCODE_ID +
                    " WHERE " + TABLE_AS_PRODUCT_BATCHES + "." + ASPB_PRODUCT_BATCH_ID + " = " + ProductBatchId;

            Log.d("Query", selectQuery);
            Cursor cursor = db.rawQuery(selectQuery, null);
            JsonArray_Creator json_creator = new JsonArray_Creator();
            String convert_data = json_creator.cursorToString(cursor);
            DatabaseManager.getInstance().closeDatabase();
            return convert_data;
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public void ClearTableData(String TableName) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            db.delete(TableName, null, null);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public int GetProductBatchStock(long ProductBatchId) {
        int Stock = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().read_database();
            StringBuilder Query = new StringBuilder();
            Query.append(" SELECT " + ASPB_STOCK + " FROM " + TABLE_AS_PRODUCT_BATCHES);
            Query.append(" WHERE " + ASPB_PRODUCT_BATCH_ID + " = " + ProductBatchId);
            Log.d("VIJAYKUMAR", Query.toString());
            Cursor cursor = db.rawQuery(Query.toString(), null);
            cursor.moveToNext();
            Stock = cursor.getInt(0);
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            Log.d("VIJAYKUMAR EXCEPTION", Stock + "");
            DatabaseManager.getInstance().closeDatabase();
        }
        Log.d("VIJAYKUMAR GETSTOCK", Stock + "");
        return Stock;
    }


    public void UpdateProductBatch(ProductBatchModel ProductBatchModel) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ASPB_COST_PRICE, ProductBatchModel.getPurchasePrice());
            cv.put(ASPB_PRODUCT_PRICE, ProductBatchModel.getMRP());
            cv.put(ASPB_OFFER_PRICE, ProductBatchModel.getSP());
            cv.put(ASPB_BATCH_NO, ProductBatchModel.getBatchNo());
            cv.put(ASPB_EXPIRY, ProductBatchModel.getExpiry());
            db.update(TABLE_AS_PRODUCT_BATCHES, cv, "" + ASPB_PRODUCT_BATCH_ID + "= '" + ProductBatchModel.getProductBatchId() + "'", null);
            DatabaseManager.getInstance().closeDatabase();
            Log.d("RAGHU", "PRODUCT BATCH UPDATED");
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    public void UpdateProductBarCode(ProductBatchModel ProductBatchModel) throws Exception {
        SQLiteDatabase db = DatabaseManager.getInstance().write_database();
        try {
            ContentValues cv = new ContentValues();
            cv.put(ASPBAR_EAN_CODE, ProductBatchModel.getProductBarCodeModel().getEanCode());
            db.update(TABLE_AS_PRODUCT_BARCODES, cv, "" + ASPBAR_PRODUCT_ID + "= '" + ProductBatchModel.getProductModel().getProductId() + "'", null);
            Log.d("RAGHU", "PRODUCT BARCODE UPDATED");
            DatabaseManager.getInstance().closeDatabase();
        } catch (Exception e) {
            DatabaseManager.getInstance().closeDatabase();
            throw new Exception(e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
