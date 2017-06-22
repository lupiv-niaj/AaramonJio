package com.aaramon.aaramonjio.order;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by DELL STORE on 03-Mar-17.
 */

public class DatabaseHandler extends SQLiteOpenHelper implements StaticVariable {
    private final Context myContext;
    private static DatabaseHandler mInstance;
    private static SQLiteDatabase myWritableDb;
    private static SQLiteDatabase myReadableDb;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_AaramShop, null, DATABASE_VERSION);
        this.myContext = context;
    }

    public static DatabaseHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHandler(context);
        }
        return mInstance;
    }

    public SQLiteDatabase getMyWritableDatabase() {
        if ((myWritableDb == null) || (!myWritableDb.isOpen())) {
            myWritableDb = this.getWritableDatabase();
        }
        return myWritableDb;
    }


    public SQLiteDatabase getMyReadableDatabase() {
        if ((myReadableDb == null) || (!myReadableDb.isOpen())) {
            myReadableDb = this.getReadableDatabase();
        }
        return myReadableDb;
    }

    @Override
    public void close() {
        super.close();
        if (myWritableDb != null) {
            myWritableDb.close();
            myWritableDb = null;
        }
        if (myReadableDb != null) {
            myReadableDb.close();
            myReadableDb = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        String tax_schdule = "CREATE TABLE IF NOT EXISTS " + TABLE_TAX + " (" + TAX_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TAX_MERCHANT_STORE_ID + " INTEGER, " +
//                TAX_SCHEDULE_NAME + " TEXT, " + TAX_SCHEDULE_PERCENTAGE + " REAL, " + TAX_ACTIVE + " INTEGER, " + TAX_LAST_UPDATE + " TEXT)";
//        db.execSQL(tax_schdule);
//
//        String order_type = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_TYPE + " (" + AOT_ORDER_TYPE_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, " + AOT_ORDER_TYPE + " TEXT, " + AOT_ORDER_TITLE + " TEXT, " + AOT_IS_ACTIVE + " INTEGER, " + AOT_DATE + " TEXT)";
//        db.execSQL(order_type);

//        dropTable(TABLE_AS_TEMP_PURCHASE, db);
//        dropTable(TABLE_AS_TEMP_PURCHASE_DETAIL, db);
        String product_table = "CREATE TABLE IF NOT EXISTS " + TABLE_AS_PRODUCT + " ( " + ASPROD_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ASPROD_SERVER_PRODUCT_ID + " INTEGER," + ASPROD_BRAND_ID + " INTEGER," + ASPROD_CATEGORY_ID + " INTEGER," + ASPROD_PRODUCT_NAME + " TEXT,"
                + ASPROD_SEO_NAME + " TEXT," + ASPROD_PRODUCT_SHORT_DESCRIPTION + " TEXT," + ASPROD_PRODUCT_DESCRIPTION + " TEXT," + ASPROD_HIGHLIGHT_CHECKOUT + " TEXT," + ASPROD_DISPLAY_AS_CATEGORY + " INTEGER,"
                + ASPROD_RECOMMENDED_PRODUCT + " INTEGER," + ASPROD_PRODUCT_PRICE + " REAL," + ASPROD_EAN_CODE + " TEXT," + ASPROD_AARAMSHOP_CODE + " TEXT,"
                + ASPROD_IS_FEATURED + " INTEGER," + ASPROD_FEATURED_START_DATE + " TEXT," + ASPROD_FEATURED_END_DATE + " TEXT," + ASPROD_META_TITLE + " TEXT,"
                + ASPROD_META_KEYWORD + " TEXT," + ASPROD_META_DESCRIPTION + " TEXT, " + ASPROD_SORT_ORDER + " INTEGER," + ASPROD_ADDED_BY + " INTEGER, "
                + ASPROD_EMPLOYEE_ID + " INTEGER," + ASPROD_STATUS + " INTEGER," + ASPROD_EDIT_DELETE + " INTEGER," + ASPROD_SHOW_ON + " INTEGER,"
                + ASPROD_DATE_ADDED + " TEXT," + ASPROD_DATE + " TEXT," + ASPROD_PK_UPDATE + " INTEGER," + ASPROD_TAX_SCHEDULE_ID + " INTEGER,"
                + ASPROD_RETAILER_PRODUCT_ID + " TEXT," + ASPROD_SYNC + " INTEGER," + ASPROD_IMAGE_URL + " TEXT, " + ASPROD_CESS_RATE + " REAL)";
        db.execSQL(product_table);


        String product_batch_table = "CREATE TABLE IF NOT EXISTS " + TABLE_AS_PRODUCT_BATCHES + " ( " + ASPB_PRODUCT_BATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ASPB_SERVER_PRODUCT_BATCH_ID + " INTEGER," + ASPB_MERCHANT_ID + " INTEGER," + ASPB_SEQUENCE + " INTEGER," + ASPB_BATCH_NO + " TEXT,"
                + ASPB_EXPIRY + " TEXT," + ASPB_COST_PRICE + " REAL," + ASPB_PRODUCT_PRICE + " REAL," + ASPB_OFFER_PRICE + " REAL," + ASPB_OFFER_TYPE + " INTEGER,"
                + ASPB_FREE_PRODUCT + " TEXT," + ASPB_OFFER_START_DATE + " TEXT," + ASPB_OFFER_END_DATE + " TEXT," + ASPB_STOCK + " REAL,"
                + ASPB_PRODUCT_DESCRIPTION + " TEXT," + ASPB_PRODUCT_BARCODE_ID + " INTEGER," + ASPB_PRODUCT_ID + " INTEGER," + ASPB_IS_ACTIVE + " INTEGER,"
                + ASPB_DATE_TIME + " TEXT," + ASPB_ADDED_BY + " INTEGER, " + ASPB_UPDATE_BY + " INTEGER," + ASPB_PK_UPDATE + " INTEGER, "
                + ASPB_SOURCE + " INTEGER," + ASPB_SYNC + " INTEGER)";
        db.execSQL(product_batch_table);


        String product_barcode_table = "CREATE TABLE IF NOT EXISTS " + TABLE_AS_PRODUCT_BARCODES + " ( " + ASPBAR_PRODUCT_BARCODE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ASPBAR_EAN_CODE + " TEXT," + ASPBAR_RETAILER_CODE + " TEXT," + ASPBAR_PRODUCT_ID + " INTEGER," + ASPBAR_DATETIME + " TEXT)";
        db.execSQL(product_barcode_table);


        String brand_table = "CREATE TABLE IF NOT EXISTS " + TABLE_AS_BRAND + " ( " + ASBRAND_BRAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ASBRAND_SERVER_BRAND_ID + " INTEGER," + ASBRAND_COMPANY_ID + " INTEGER," + ASBRAND_BRAND_NAME + " TEXT," + ASBRAND_SEO_NAME + " TEXT,"
                + ASBRAND_CONTACT_NAME + " TEXT," + ASBRAND_PHONE + " INTEGER," + ASBRAND_MOBILE + " INTEGER," + ASBRAND_FAX + " INTEGER," + ASBRAND_EMAIL_ID + " TEXT,"
                + ASBRAND_WEBSITE + " TEXT," + ASBRAND_BRAND_DESCRIPTION + " TEXT," + ASBRAND_IMAGE + " TEXT," + ASBRAND_IS_PREMIUM + " INTEGER,"
                + ASBRAND_HOME_PAGE_ICON + " TEXT," + ASBRAND_PAGE_IMAGE + " TEXT," + ASBRAND_BRAND_LOGO + " TEXT," + ASBRAND_HEADING_BG_COLOR + " TEXT,"
                + ASBRAND_HEADING_COLOR + " TEXT," + ASBRAND_SUBHEADING_COLOR + " TEXT, " + ASBRAND_DOCUMENTORIES_LINK + " TEXT," + ASBRAND_AUDIO_LINK + " TEXT, "
                + ASBRAND_TVC_LINK + " TEXT," + ASBRAND_FACEBOOK_LINK + " TEXT," + ASBRAND_WEBSITE_LINK + " TEXT," + ASBRAND_PREMIUM_BRAND_BANNER + " TEXT,"
                + ASBRAND_PREMIUM_BRAND_LINK + " TEXT," + ASBRAND_PREMIUM_BRAND_BANNER_TYPE + " INTEGER," + ASBRAND_PREMIUM_BRAND_PRODUCT + " TEXT," + ASBRAND_PREMIUM_BRAND_STORE + " TEXT,"
                + ASBRAND_PREMIUM_BRAND_GALLERY_IMAGE + " TEXT," + ASBRAND_META_TITLE + " TEXT," + ASBRAND_META_KEYWORD + " TEXT," + ASBRAND_META_DESCRIPTION + " TEXT,"
                + ASBRAND_SORT_ORDER + " TEXT," + ASBRAND_ADDED_BY + " INTEGER," + ASBRAND_EMPLOYEE_ID + " INTEGER," + ASBRAND_TOP_BANNER + " TEXT," + ASBRAND_TOP_BANNER_LINK + " TEXT,"
                + ASBRAND_LEFT_BANNER + " TEXT," + ASBRAND_LEFT_BANNER_LINK + " TEXT," + ASBRAND_MERCHANT_STORE_ID + " TEXT," + ASBRAND_STATUS + " INTEGER,"
                + ASBRAND_EDITDELETE + " INTEGER," + ASBRAND_SHOW_ON + " INTEGER," + ASBRAND_DATE_ADDED + " TEXT," + ASBRAND_DATE + " TEXT,"
                + ASBRAND_PK_UPDATE + " INTEGER," + ASBRAND_SYNC + " INTEGER)";
        db.execSQL(brand_table);


        String company_table = "CREATE TABLE IF NOT EXISTS " + TABLE_AS_COMPANY + " ( " + ASCOMPANY_COMPANY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ASCOMPANY_SERVER_COMPANY_ID + " INTEGER," + ASCOMPANY_COMPANY_NAME + " TEXT," + ASCOMPANY_SEO_NAME + " TEXT," + ASCOMPANY_CONTACT_NAME + " TEXT,"
                + ASCOMPANY_PHONE + " TEXT," + ASCOMPANY_MOBILE + " TEXT," + ASCOMPANY_FAX + " TEXT," + ASCOMPANY_EMAIL_ID + " TEXT," + ASCOMPANY_WEBSITE + " TEXT,"
                + ASCOMPANY_COMPANY_DESCRIPTION + " TEXT," + ASCOMPANY_IMAGE + " TEXT," + ASCOMPANY_META_TITLE + " TEXT," + ASCOMPANY_META_KEYWORD + " REAL,"
                + ASCOMPANY_META_DESCRIPTION + " TEXT," + ASCOMPANY_COUNTRY_ID + " INTEGER," + ASCOMPANY_SORT_ORDER + " INTEGER," + ASCOMPANY_ADDED_BY + " INTEGER,"
                + ASCOMPANY_EMPLOYEE_ID + " TEXT," + ASCOMPANY_STATUS + " INTEGER, " + ASCOMPANY_SHOW_ON + " INTEGER," + ASCOMPANY_DATE_ADDED + " TEXT, "
                + ASCOMPANY_DATE + " TEXT," + ASCOMPANY_PK_UPDATE + " INTEGER," + ASCOMPANY_SOURCE + " INTEGER," + ASCOMPANY_SYNC + " INTEGER)";
        db.execSQL(company_table);


        String category_table = "CREATE TABLE IF NOT EXISTS " + TABLE_AS_CATEGORY + " ( " + ASCATEGORY_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ASCATEGORY_SERVER_CATEGORY_ID + " INTEGER," + ASCATEGORY_CATEGORY_NAME + " TEXT," + ASCATEGORY_SEO_NAME + " TEXT," + ASCATEGORY_CATEGORY_DESCRIPTION + " TEXT,"
                + ASCATEGORY_PARENT_ID + " INTEGER," + ASCATEGORY_CATEGORY_LEVEL + " INTEGER," + ASCATEGORY_CATEGORY_IMAGE + " TEXT," + ASCATEGORY_CATEGORY_WEB_ICON + " TEXT," + ASCATEGORY_CATEGORY_ICON + " TEXT,"
                + ASCATEGORY_CATEGORY_BANNER + " TEXT," + ASCATEGORY_SHOW_ON_TOP + " INTEGER," + ASCATEGORY_CATEGORY_TYPE + " INTEGER," + ASCATEGORY_META_TITLE + " TEXT," + ASCATEGORY_STATUS + " INTEGER,"
                + ASCATEGORY_META_KEYWORD + " TEXT," + ASCATEGORY_META_DESCRIPTION + " TEXT," + ASCATEGORY_SORT_ORDER + " INTEGER," + ASCATEGORY_EMPLOYEE_ID + " INTEGER,"
                + ASCATEGORY_SHOW_ON + " INTEGER," + ASCATEGORY_DATE_ADDED + " TEXT, " + ASCATEGORY_DATE + " TEXT," + ASCATEGORY_SOURCE + " INTEGER, "
                + ASCATEGORY_PK_UPDATE + " INTEGER," + ASCATEGORY_SYNC + " INTEGER,  " + ASCATEGORY_CGST_RATE + " REAL, " + ASCATEGORY_SGST_RATE
                + " REAL, " + ASCATEGORY_IGST_RATE + " REAL)";
        db.execSQL(category_table);


//        String product_barcode_list_table = "CREATE TABLE IF NOT EXISTS " + TABLE_AS_PRODUCT_BARCODE_LIST + " ( " + ASPBL_PRODUCT_BARCODE_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + ASPBL_LIST_NAME + " TEXT," + ASPBL_DATETIME + " TEXT)";
//        db.execSQL(product_barcode_list_table);
//
//        String barcode_product_list_table = "CREATE TABLE IF NOT EXISTS " + TABLE_AS_PRODUCT_BARCODE_LIST_PRODUCTBATCH + " ( " + ASPBLPB_PRODUCT_BARCODE_LIST_PRODUCT_BATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + ASPBLPB_PRODUCT_BARCODE_LIST_ID + " INTEGER," + ASPBLPB_PRODUCT_BATCH_ID + " INTEGER," + ASPBLPB_DATETIME + " TEXT)";
//        db.execSQL(barcode_product_list_table);


        String as_order = "CREATE TABLE IF NOT EXISTS " + TABLE_AS_ORDER + "(" + ASORDER_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ASORDER_ORDER_CODE + " TEXT, "
                + ASORDER_SERVER_ORDER_ID + " INTEGER," + ASORDER_RETAILER_ORDER_ID + " INTEGER," + ASORDER_SHOPPER_ADDRESS_ID + " INTEGER," + ASORDER_MERCHANT_STORE_ID +
                " INTEGER," + ASORDER_ORDER_SOURCE + " INTEGER," + ASORDER_DELIVERY_DATE + " TEXT," + ASORDER_DELIVERY_TIMESLOT + " TEXT," + ASORDER_DELIVERY_TIMING + " TEXT,"
                + ASORDER_ORDER_TIMINIG + " TEXT," + ASORDER_ORDER_STATUS + " INTEGER," + ASORDER_PARTIAL_RETURNED + " INTEGER," + ASORDER_TOTAL_AMOUNT + " REAL," + ASORDER_TAX_AMOUNT +
                "  REAL," + ASORDER_TAX_TYPE + " TEXT," + ASORDER_OFFER_TYPE + " INTEGER," + ASORDER_TOTAL_DISCOUNT + " REAL," + ASORDER_SPECAIL_DISCOUNT + " REAL," +
                ASORDER_DELIVERY_CHARGES + " REAL," + ASORDER_COUPON_CODE + " TEXT," + ASORDER_SHOPPER_PAYMENT_TYPEID + " INTEGER," + ASORDER_MERCHANT_PAYMENT_TYPE_ID + " INTEGER, " +
                ASORDER_IPADDRESS + " TEXT," + ASORDER_MERCHANT_STORE_PEOPLE_ID + "  INTEGER," + ASORDER_DELIVERY_BOY_TIMING + " TEXT, " + ASORDER_PACKED_TIMING + " TEXT," +
                ASORDER_DISPATCHED_TIMING + " TEXT," + ASORDER_DELIVERED_TIMING + " TEXT," + ASORDER_CUSTOMER_DUE + " REAL," + ASORDER_IS_READ + " INTEGER," +
                ASORDER_SPECIAL_REQUEST + " TEXT," + ASORDER_ORDER_COMPLETED + " INTEGER," + ASORDER_UNIQUE_ORDER_ID + " TEXT," + ASORDER_STATUS + " INTEGER," +
                ASORDER_JIO_TXN_REF + " TEXT," + ASORDER_ERROR_CODE + " TEXT," + ASORDER_RESPONSE_MSG + " TEXT," + ASORDER_TXT_TIME + " TEXT, " + ASORDER_CARD_NUMBER + " TEXT," +
                ASORDER_TXN_TYPE + " TEXT," + ASORDER_CARD_TYPE + " TEXT," + ASORDER_ORDER_COMPLETED_PAYMENT_MODE + " TEXT," + ASORDER_ORDER_COMPLETED_PAYMENT_MODE_AMOUNT + " TEXT," +
                ASORDER_ORDER_COMPLETED_PAYMENT_MODE_VOUCHERNO + " TEXT," + ASORDER_CALL_CENTER_EMP_ID + "  INTEGER, " + ASORDER_ORDER_CANCELLATION + " INTEGER," +
                ASORDER_CANCELLATION_REASON + " TEXT," + ASORDER_ORDER_CANCELLATION_TIMING + " TEXT," + ASORDER_ORDER_CANCELLATION_STATUS_CHANGE_TIMING + " TEXT," +
                ASORDER_STORE_PICKUP + " INTEGER," + ASORDER_ORDER_TYPE_ID + " INTEGER," + ASORDER_USER_ID + " INTEGER," + ASORDER_COUNTER_ID + " INTEGER," +
                ASORDER_BILL_FOOTER + " TEXT," + ASORDER_FEEDBACK_RATING + " INTEGER," + ASORDER_PK_UPDATE + " INTEGER," + ASORDER_SYNC + " INTEGER )";
        db.execSQL(as_order);


        String as_order_detail = "CREATE TABLE IF NOT EXISTS " + TABLE_AS_ORDER_DETAIL + " (" + ASORDERDETAIL_ORDER_DETAIL_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + ASORDERDETAIL_ORDER_ID + " INTEGER," + ASORDERDETAIL_PRODUCT_TYPE + " INTEGER,"
                + ASORDERDETAIL_PRODUCT_BATCH_ID + " INTEGER," + ASORDERDETAIL_MERCHANT_PRODUCT_ID + " INTEGER,"
                + ASORDERDETAIL_PRODUCT_NAME + " TEXT," + ASORDERDETAIL_QUANTITY + " REAL," + ASORDERDETAIL_PRODUCT_PRICE + " REAL,"
                + ASORDERDETAIL_OFFER_TYPE + " INTEGER," + ASORDERDETAIL_OFFER_PRICE + "  REAL," + ASORDERDETAIL_FREE_PRODUCT + "  TEXT,"
                + ASORDERDETAIL_COMBO_DETAIL_ITEM + " TEXT," + ASORDERDETAIL_IS_AVAILABLE + " INTEGER," + ASORDERDETAIL_IMAGE_NAME + " TEXT,"
                + ASORDERDETAIL_OFFER_DESCRIPTION + "  TEXT," + ASORDERDETAIL_DATE_TIME + " TEXT," + ASORDERDETAIL_TAX_PERCENTAGE + " REAL )";
        db.execSQL(as_order_detail);


        String cart_table = "CREATE TABLE IF NOT EXISTS " + TABLE_AS_TEMP_CART + " ( " + COLUMN_TEMP_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CART_NAME + " TEXT," + COLUMN_CART_VALUE + " REAL," + COLUMN_CART_ITEM + " INTEGER," + COLUMN_USER_ID + " INTEGER, " + COLUMN_ADDED_DATE + " TEXT)";
        db.execSQL(cart_table);

        String cart_detail = "CREATE TABLE IF NOT EXISTS " + TABLE_AS_TEMP_CART_ITEM + " ( " + COLUMN_TEMP_CART_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TEMP_CART_ID + " INTEGER, " + COLUMN_PRODUCT_ID + " INTEGER, " + COLUMN_PRODUCT_NAME + " INTEGER, " + COLUMN_PRODUCT_PRICE + " INTEGER,"
                + COLUMN_OFFER_PRICE + " INTEGER, " + COLUMN_QUANTITY + " INTEGER," + COLUMN_IMAGE_URL + " TEXT, " + COLUMN_CGST_RATE + " REAL, " + COLUMN_SGST_RATE
                + " REAL, " + COLUMN_IGST_RATE + " REAL, " + COLUMN_CESS_RATE + " REAL, " + COLUMN_TOTAL_AMOUNT + " REAL, " + COLUMN_TOTAL_TAX_AMOUNT + " REAL,"
                + COLUMN_BATCH_NO + " TEXT," + COLUMN_BATCH_ID + " TEXT)";
        db.execSQL(cart_detail);  // Added By Vijay Kumar on 20/05/2017
//        Log.d("Dropped", TABLE_AS_TEMP_PURCHASE);
//        dropTable(TABLE_AS_TEMP_PURCHASE, db);

        Log.d("Created", TABLE_AS_TEMP_PURCHASE);
        // as_temp_purchase table
        StringBuilder querystring = new StringBuilder();
        querystring.append("CREATE TABLE IF NOT EXISTS ");
        querystring.append(TABLE_AS_TEMP_PURCHASE);
        querystring.append(" (");
        querystring.append(COLUMN_PURCHASE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        querystring.append(COLUMN_PURCHASE_BILL_NO + " TEXT, ");
        querystring.append(COLUMN_PURCHASE_BILL_DATE + " TEXT, ");
        querystring.append(COLUMN_PURCHASE_SUPPLIER_ID + " INTEGER, ");
        querystring.append(COLUMN_PURCHASE_MERCHANT_STORE_ID + " INTEGER, ");
        querystring.append(COLUMN_PURCHASE_PAYMENT_TYPE + " TEXT, ");
        querystring.append(COLUMN_PURCHASE_PAYMENT_TERM + " TEXT, ");
        querystring.append(COLUMN_PURCHASE_DATE + " TEXT, ");
        querystring.append(COLUMN_PURCHASE_TOTAL_AMOUNT + " REAL, ");
        querystring.append(TAX_SCHEDULE_PERCENTAGE + " REAL, ");
        querystring.append(COLUMN_PURCHASE_AMOUNT_PAID + " REAL, ");
        querystring.append(COLUMN_PURCHASE_STATUS + " INTEGER, ");
        querystring.append(COLUMN_PURCHASE_DATE_ADDED + " TEXT, ");
        querystring.append(COLUMN_PURCHASE_DATETIME + " TEXT");
        querystring.append(")");
        db.execSQL(querystring.toString());

        // as_temp_purchase_detail table
        querystring = new StringBuilder();
        querystring.append("CREATE TABLE IF NOT EXISTS ");
        querystring.append(TABLE_AS_TEMP_PURCHASE_DETAIL);
        querystring.append(" (");
        querystring.append(COLUMN_PURCHASE_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        querystring.append(COLUMN_PURCHASE_ID + " INTEGER, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_PRODUCT_BATCH_ID + " INTEGER, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_RATE + " REAL, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_MRP + " REAL, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_SP + " REAL, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_QUANTITY + " REAL, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_TAX_PERCENTAGE + " REAL, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_CREATED_AT + " TEXT, ");
        querystring.append(COLUMN_PURCHASE_DATETIME + " TEXT, ");
        querystring.append(COLUMN_PURCHASE_STATUS + " INTEGER, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_CGST_RATE + " REAL, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_SGST_RATE + " REAL, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_IGST_RATE + " REAL, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_CESS_RATE + " REAL, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_TOTAL_AMOUNT + " REAL, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_TOTAL_TAX_AMOUNT + " REAL, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_BATCH_NO + " TEXT, ");
        querystring.append(COLUMN_PURCHASE_DETAIL_NEWPURCHASE_PRICE + " REAL ");
        querystring.append(")");
        db.execSQL(querystring.toString());

        // as_temp_purchase_detail_free_products
        querystring = new StringBuilder();
        querystring.append("CREATE TABLE IF NOT EXISTS ");
        querystring.append(TABLE_AS_TEMP_PURCHASE_DETAIL_FREE_PRODUCTS);
        querystring.append(" (");
        querystring.append(COLUMN_PURCHASE_DETAIL_FREE_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        querystring.append(COLUMN_PURCHASE_DETAIL_ID + " INTEGER, ");
        querystring.append(COLUMN_FREE_PRODUCT_BATCH_ID + " INTEGER, ");
        querystring.append(COLUMN_Free_PRODUCT_QTY + " REAL, ");
        querystring.append(COLUMN_PURCHASE_DATETIME + " TEXT");
        querystring.append(")");
        db.execSQL(querystring.toString());

        // Added on 22/05/2017
        // Tax Schedule Table
        querystring = new StringBuilder();
        querystring.append("CREATE TABLE IF NOT EXISTS ");
        querystring.append(TABLE_AS_TAX_SCHEDULES);
        querystring.append(" (");
        querystring.append(COLUMN_TAX_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, ");
        querystring.append(COLUMN_TAX_SCHEDULE_MERCHANT_STORE_ID + " INTEGER, ");
        querystring.append(COLUMN_TAX_SCHEDULE_TITLE + " TEXT, ");
        querystring.append(COLUMN_TAX_SCHEDULE_PERCENTAGE + " REAL, ");
        querystring.append(COLUMN_TAX_SCHEDULE_ACTIVE + " INTEGER, ");
        querystring.append(COLUMN_TAX_SCHEDULE_UPDATED_DATE + " TEXT");
        querystring.append(")");
        db.execSQL(querystring.toString());

//        dropTable(TABLE_AS_PRODUCT, db);
//        dropTable(TABLE_AS_PRODUCT_BATCHES, db);
//        dropTable(TABLE_AS_PRODUCT_BARCODES, db);
//       //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Upgrade DB", "Upgrade");
        onCreate(db);
       // String upgradeQuery = "ALTER TABLE " + TABLE_AS_TEMP_PURCHASE + " ADD COLUMN " + COLUMN_BILL_IMAGE_NAME + " TEXT";
        String upgradeQuery1 = "ALTER TABLE " + TABLE_AS_TEMP_PURCHASE_DETAIL + " ADD COLUMN " + COLUMN_PURCHASE_DETAIL_NEWPURCHASE_PRICE + " REAL";
//        if (oldVersion == 4 && newVersion == 5) {
//            db.execSQL(upgradeQuery);
//        }

        if (oldVersion == 2 && newVersion == 3) {
            db.execSQL(upgradeQuery1);
        }
    }

    private void dropTable(String TableName, SQLiteDatabase db) {
        Log.d("Dropped ", TableName);
        StringBuilder querystring = new StringBuilder();
        querystring.append("DROP TABLE IF EXISTS " + TableName);
        db.execSQL(querystring.toString());
    }
}