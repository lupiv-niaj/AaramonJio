package com.aaramon.aaramonjio.supplier;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.controller.Constant;
import com.aaramon.aaramonjio.dataaccess.BitmapUtils;
import com.aaramon.aaramonjio.dataaccess.DataStatic;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CreatePurchaseEntryActivity extends Activity implements Constant{
    SupplierService SupplierService = new SupplierService();
    int SupplierId;
    String SupplierCode;
    String SupplierName;

    ImageView BackBtn = null;
    TextView AddNewProductTextView = null;
    EditText BillDateEditText = null;
    EditText BillNumberEditText = null;
    Spinner PaymentTermsSpinner = null;
    Spinner PaymentTypeSpinner = null;
    ImageView BillScanView = null;
    // TextView text_copy = null;
    public static Uri fileUri = null;
    String selectedPhotoPath = "";
    private SharedPreference_Main sharedPreference;
    float density;
    Bitmap bm;
    String stateid = "", gst_category_id = "";
    ByteArrayBody bab;
    TextView tvChangePic;
    Calendar to_calender;
    private ProgressDialog progressDialog;
    String image_name, image_url;
    //
    //  ImageView backbtn,ivPic,imv_background_blur;
//    EditText edit_bill_date,edit_bill_number;
//    private TextView AddProductTextViewId;

    //  ByteArrayBody bab;
//    TextView AddNewPurchaseEntryTextViewId,tvChangePic,text_copy;
    //  private SharedPreference_Main sharedPreference;

    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color));
        }
        setContentView(R.layout.create_purchase_entry);

        BackBtn = (ImageView) findViewById(R.id.imageView2);
        AddNewProductTextView = (TextView) findViewById(R.id.AddProductTextViewId);
        BillDateEditText = (EditText) findViewById(R.id.edit_bill_date);
        BillNumberEditText = (EditText) findViewById(R.id.edit_bill_number);
        PaymentTermsSpinner = (Spinner) findViewById(R.id.payment_term);
        PaymentTypeSpinner = (Spinner) findViewById(R.id.PaymentTypeSpinner);
        BillScanView = (ImageView) findViewById(R.id.BillScanView);
        tvChangePic = (TextView) findViewById(R.id.text_copy);
        // text_copy = (TextView) findViewById(R.id.text_copy);
        to_calender = Calendar.getInstance();
        Bundle bun = getIntent().getExtras();
        sharedPreference = new SharedPreference_Main(getApplicationContext());


        // Business Methods
        try {
            // Populate Supplier Details
            populateSupplierDetails(bun);

            // Populate Credit Period
            populateCreditPeriods(PaymentTermsSpinner);

            // Populate Payment Types
            populatePaymentTypes(PaymentTypeSpinner);

            // Set Bill Date As Current Date
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            ((EditText) findViewById(R.id.edit_bill_date)).setText(sdf.format(new Date()));

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        BillDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                        CreatePurchaseEntryActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        BillDateEditText.setText(sdf.format(newDate
                                .getTime()));
                    }

                }, to_calender.get(Calendar.YEAR), to_calender
                        .get(Calendar.MONTH), to_calender
                        .get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.getDatePicker().setMaxDate(
                        to_calender.getTimeInMillis());
                fromDatePickerDialog.show();
            }
        });
        // Event Handlers
        PaymentTermsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.jio_main_color));
            }//

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//
            }
        });

        PaymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
                if (i == 0) {
                    PaymentTermsSpinner.setSelection(0);
                    PaymentTermsSpinner.setEnabled(false);
                } else {
                    PaymentTermsSpinner.setSelection(0);
                    PaymentTermsSpinner.setEnabled(true);
                }
            }//

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {//
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AddNewProductTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateActivityData() == true) {
                    try {
                        Log.d("SUCCESS", "ALL OK");


                        if (bab != null) {
                            new UploadImage(UPLOAD_PHOTOS_SUCCESS, UPLOAD_PHOTOS_FAILED, sharedPreference.getbase_inpk_url() + "Supplier/uploadSupplierBillImage",
                                    sharedPreference.getDeviceId(), "2", bab, sharedPreference.getStoreId(), SupplierId).execute();

                        } else {
                            String image_name = "";
                            // Create New Purchase Entry in Temp Table with available data & send across purchase entry No & Open Scan Activity Window
                            PurchaseEntryModel PurchaseEntryModel = GeneratePurchaseEntryModel(image_name);

                            // Create Purchase Entry in Temp Database
                            //       Log.d("POPO", "in activity");
                            int PurchaseEntryId = SupplierService.insertPurchaseEntry(PurchaseEntryModel, Integer.parseInt(new SharedPreference_Main(CreatePurchaseEntryActivity.this).getStoreId()));

                            Intent TransferData = new Intent(getApplicationContext(), ScanPurchaseProducts.class);
                            TransferData.putExtra("PurchaseEntryId", PurchaseEntryId + "");
                            TransferData.putExtra("image_name", image_name + "");
                            TransferData.putExtra("SupplierId", SupplierId + "");
                            TransferData.putExtra("SupplierCode", SupplierCode + "");
                            TransferData.putExtra("stateid", stateid);
                            TransferData.putExtra("gst_category_id",gst_category_id);
                            TransferData.putExtra("SupplierName", ((TextView) findViewById(R.id.SupplierNameTextViewId)).getText().toString());
                            TransferData.putExtra("PaymentTerm", PaymentTypeSpinner.getSelectedItem().toString());
                            TransferData.putExtra("PaymentMode", PaymentTermsSpinner.getSelectedItem().toString());
                            TransferData.putExtra("TotalItems", "0");
                            TransferData.putExtra("TotalAmount", "0");
                            TransferData.putExtra("TaxAmount","0");
                            startActivity(TransferData);
                        }
                    } catch (Exception e) {
                        Toast.makeText(CreatePurchaseEntryActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        BillScanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your code in here!
                selectImage(CreatePurchaseEntryActivity.this);
            }
        });


    }

    private void selectImage(final Context context) {
        final Dialog logoutDialog = new Dialog(context);

        logoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logoutDialog.setContentView(R.layout.selectimage);
        logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout cameraButton = (LinearLayout) logoutDialog
                .findViewById(R.id.ll_select_image_camera);
        LinearLayout libraryButton = (LinearLayout) logoutDialog
                .findViewById(R.id.ll_select_image_library);
        LinearLayout cancelButton = (LinearLayout) logoutDialog
                .findViewById(R.id.ll_select_image_cancel);

        cameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri(DataStatic.MEDIA_TYPE_IMAGE);
                // set video quality
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the
                // image
                try {
                    // intent.putExtra("return-data", true);
                    startActivityForResult(intent,
                            DataStatic.ACTIVITY_CAMERA_IMAGE);
                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }
                logoutDialog.cancel();
            }
        });

        libraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");
                }
                startActivityForResult(intent, DataStatic.ACTIVITY_SELECT_IMAGE);
                logoutDialog.cancel();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                logoutDialog.cancel();
            }
        });

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        if (!(logoutDialog.isShowing())) {
            logoutDialog.show();
        }
        logoutDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        logoutDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getPath(Uri uri) {
        // ICS
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (DataStatic.isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (DataStatic.isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return DataStatic.getDataColumn(this, contentUri, null, null);
            }
            // MediaProvider
            else if (DataStatic.isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return DataStatic.getDataColumn(this, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return DataStatic.getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Android File Upload");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // Log.d(TAG, "Oops! Failed create "
                // + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DataStatic.ACTIVITY_SELECT_IMAGE) {

            if (data != null) {

                try {
                    Uri selectedImageUri = data.getData();
                    selectedPhotoPath = getPath(selectedImageUri);

//                    sharedPreference.setBlurBgImagePath(selectedPhotoPath);

                    File imagePath = new File(selectedPhotoPath);
                    ExifInterface exif = new ExifInterface(imagePath.getPath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    int angle = 0;

                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        angle = 90;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        angle = 180;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        angle = 270;
                    }

                    Matrix matrix = new Matrix();
                    matrix.postRotate(angle);
                    int maxImageSize = BitmapUtils.getMaxSize(this);
                    Bitmap sourceBitmap = BitmapUtils.getScaledBitmap(
                            imagePath, maxImageSize);
                    // density = getResources().getDisplayMetrics().density;
                    Bitmap mImageBitmap = Bitmap.createBitmap(sourceBitmap, 0,
                            0, sourceBitmap.getWidth(),
                            sourceBitmap.getHeight(), matrix, true);
                    DisplayMetrics dm = getApplicationContext().getResources()
                            .getDisplayMetrics();
                    int densityDpi = dm.densityDpi;

                    Bitmap bitmap = mImageBitmap;

                    BillScanView.setImageBitmap(bitmap);
                    tvChangePic.setText(getResources().getString(R.string.changepicture));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bm = BitmapFactory.decodeFile(imagePath + "", options);
                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                    byte[] data1 = stream.toByteArray();
                    Log.e("ByteArrayBody(BAB)", "" + data1);
                    bab = new ByteArrayBody(data1, "profileImage.jpg");
                    MultipartEntity reqEntity = new MultipartEntity(
                            HttpMultipartMode.BROWSER_COMPATIBLE);
                } catch (Exception ex) {
                    Log.d("EXCEPTION", "" + ex);
                }
            }
        }

        if (requestCode == DataStatic.ACTIVITY_CAMERA_IMAGE
                && resultCode == Activity.RESULT_OK) {
            try {

                selectedPhotoPath = fileUri.getPath();

                // sharedPreference.setBlurBgImagePath(selectedPhotoPath);

                File imagePath = new File(selectedPhotoPath);
                ExifInterface exif = new ExifInterface(imagePath.getPath());
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

                int angle = 0;

                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    angle = 90;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    angle = 180;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    angle = 270;
                }

                Matrix matrix = new Matrix();
                matrix.postRotate(angle);
                int maxImageSize = BitmapUtils.getMaxSize(this);
                Bitmap sourceBitmap = BitmapUtils.getScaledBitmap(
                        imagePath, maxImageSize);
                // density = getResources().getDisplayMetrics().density;
                Bitmap mImageBitmap = Bitmap.createBitmap(sourceBitmap, 0,
                        0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight(), matrix, true);
                DisplayMetrics dm = getApplicationContext().getResources()
                        .getDisplayMetrics();
                int densityDpi = dm.densityDpi;

                Bitmap bitmap = mImageBitmap;

                BillScanView.setImageBitmap(bitmap);

                tvChangePic.setText(getResources().getString(R.string.changepicture));

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm = BitmapFactory.decodeFile(imagePath + "", options);
                ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 75, stream);
                byte[] data1 = stream.toByteArray();
                Log.e("ByteArrayBody(BAB)", "" + data1);
                bab = new ByteArrayBody(data1, "profileImage.jpg");
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            } catch (Exception e) {
                Log.e("Image Exception", e.toString());
            }
        }
    }

    public void populateSupplierDetails(Bundle bun) {
        SupplierId = Integer.parseInt(bun.getString("SupplierId"));
        SupplierCode = bun.getString("SupplierCode");
        stateid = bun.getString("stateid");
        gst_category_id = bun.getString("gst_category_id");
        ((TextView) findViewById(R.id.SupplierNameHeaderTextViewId)).setText(bun.getString("SupplierName"));
        ((TextView) findViewById(R.id.SupplierNameTextViewId)).setText(bun.getString("SupplierName"));
        ((TextView) findViewById(R.id.SupplierAddressTextViewId)).setText(bun.getString("SupplierAddress"));
        ((TextView) findViewById(R.id.SupplierMobileNoTextViewId)).setText(bun.getString("SupplierMobile"));
        ((TextView) findViewById(R.id.SupplierOutstandingTextViewId)).setText(bun.getString("SupplierOutstanding"));
    }

    public void populateCreditPeriods(Spinner PaymentTermsSpinner) {
        String[] payment_term = {"Select Due Days", "Due in 7 Days", "Due in 15 Days", "Due in 1 Month"};
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, payment_term);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PaymentTermsSpinner.setAdapter(aa);
        PaymentTermsSpinner.setSelection(0);
    }

    public void populatePaymentTypes(Spinner PaymentTypeSpinner) {
        String[] payment_type = {"Cash", "Credit"};
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, payment_type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PaymentTypeSpinner.setAdapter(aa);
        PaymentTypeSpinner.setSelection(0);
    }

    public Boolean ValidateActivityData() {
        if (BillNumberEditText.getText().toString().trim().equals("")) {
            Toast.makeText(CreatePurchaseEntryActivity.this, "Please Provide Bill Number", Toast.LENGTH_SHORT).show();
            BillNumberEditText.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(BillNumberEditText, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (BillDateEditText.getText().toString().trim().equals("")) {
            Toast.makeText(CreatePurchaseEntryActivity.this, "Please Provide Bill Date", Toast.LENGTH_SHORT).show();
            BillDateEditText.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.showSoftInput(BillDateEditText, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
        if (PaymentTypeSpinner.getSelectedItemPosition() == 1 && PaymentTermsSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(CreatePurchaseEntryActivity.this, "Please Select Payment Due Terms", Toast.LENGTH_SHORT).show();
            PaymentTermsSpinner.requestFocus();
            return false;
        }
        return true;
    }

    public PurchaseEntryModel GeneratePurchaseEntryModel(String image_name) {
        PurchaseEntryModel PurchaseEntryModel = new PurchaseEntryModel();
        PurchaseEntryModel.setBillImageName(image_name);
        PurchaseEntryModel.getSupplierModel().setSupplierId(SupplierId);
        PurchaseEntryModel.setBillNumber(BillNumberEditText.getText().toString().trim());
        PurchaseEntryModel.setBillDate(BillDateEditText.getText().toString().trim());
        PurchaseEntryModel.setPaymentMode(PaymentTypeSpinner.getSelectedItem().toString());
        PurchaseEntryModel.setPaymentTerm(PaymentTermsSpinner.getSelectedItem().toString());

        Log.d("supplierid", PurchaseEntryModel.getSupplierModel().getSupplierId() + "");
        Log.d("Bill Number", PurchaseEntryModel.getBillNumber());
        Log.d("Bill Date", PurchaseEntryModel.getBillDate());
        Log.d("Payment Mode", PurchaseEntryModel.getPaymentMode());
        Log.d("Payment Term", PurchaseEntryModel.getPaymentTerm());

        return PurchaseEntryModel;

    }

    class UploadImage extends AsyncTask<Void, Void, Void> {

        private String urlhit;
        private String deviceId;
        private String deviceType;
        private String merchantId;
        private int supplierId;
        private String aaramshopMagicKey;
        private ByteArrayBody image;
        int status = 0;
        String image_name;
        String message = "";
        private byte event_fail, event_success;

        public UploadImage(byte event_success, byte event_fail,
                           String urlhit, String deviceId, String deviceType, ByteArrayBody image, String merchantId, int supplierId) {
            this.event_success = event_success;
            this.event_fail = event_fail;
            this.urlhit = urlhit;
            this.deviceId = deviceId;
            this.deviceType = deviceType;
            this.image = image;
            this.merchantId = merchantId;
            this.supplierId = supplierId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CreatePurchaseEntryActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            Log.i("HIT URL", urlhit);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            try {
                HttpPost httppost = new HttpPost(urlhit);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("DeviceId", new StringBody(deviceId));
                reqEntity.addPart("DeviceType", new StringBody(deviceType));
                reqEntity.addPart("Image", image);
                reqEntity.addPart("MerchantStoreId", new StringBody(merchantId));
                reqEntity.addPart("SupplierId", new StringBody(String.valueOf(supplierId)));
                reqEntity.addPart("AaramShopMagicKey", new StringBody(String.valueOf("AaramShop@Android$vipul#dinesh|||6364")));

                httppost.setEntity(reqEntity);
                Log.e("HTTP POST ", httppost.toString());
                Log.e("reqEntity ", reqEntity.toString());
                response = httpclient.execute(httppost);
                Log.e("send Requestsss", httppost + "");
                Log.e("response ", "" + response);
                HttpEntity resEntityGet = response.getEntity();
                Log.e("resEntityGet ", "" + resEntityGet);
                //Log.e("HTTP POST ", httppost.toString());
                if (resEntityGet != null) {

                    String sres = EntityUtils.toString(resEntityGet);
                    Log.e("DATA POSTED", sres);
                    Log.e("HIMANSHU  = ", " sending data");
                    try {
                        JSONObject jsonO = new JSONObject(sres);
                        JSONObject controls = jsonO.getJSONObject("Control");
                        status = Integer.parseInt(controls.getString("Status"));
                        if (controls.getString("Status").equals("1")) {
                            JSONObject json_data = jsonO.getJSONObject("Data");
                            image_name = json_data.getString("ImageName");
                            message = controls.getString("Message");
                        } else {
                            image_name = "";
                            message = controls.getString("Message");
                        }
                    } catch (Exception e) {
                        status = 0;
                        image_name = "";
                        message = "Image Not Upload";
                    }

                } else {
                    status = 0;
                    image_name = "";
                    message = "Image Not Upload";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.i("HIMANSHU = ", " Inexception" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (status == 1) {
                try {
                    PurchaseEntryModel PurchaseEntryModel = GeneratePurchaseEntryModel(image_name);
                    int PurchaseEntryId = SupplierService.insertPurchaseEntry(PurchaseEntryModel, Integer.parseInt(new SharedPreference_Main(CreatePurchaseEntryActivity.this).getStoreId()));

                    Intent TransferData = new Intent(getApplicationContext(), ScanPurchaseProducts.class);
                    TransferData.putExtra("PurchaseEntryId", PurchaseEntryId + "");
                    //TransferData.putExtra("image_name", image_name + "");
                    TransferData.putExtra("SupplierId", SupplierId + "");
                    TransferData.putExtra("SupplierCode", SupplierCode + "");
                    TransferData.putExtra("stateid", stateid);
                    TransferData.putExtra("gst_category_id",gst_category_id);
                    TransferData.putExtra("SupplierName", ((TextView) findViewById(R.id.SupplierNameTextViewId)).getText().toString());
                    TransferData.putExtra("PaymentTerm", PaymentTypeSpinner.getSelectedItem().toString());
                    TransferData.putExtra("PaymentMode", PaymentTermsSpinner.getSelectedItem().toString());
                    TransferData.putExtra("TotalItems", "0");
                    TransferData.putExtra("TotalAmount", "0");
                    TransferData.putExtra("TaxAmount","0");
                    startActivity(TransferData);
                } catch (Exception e) {

                }
            } else {
                Toast.makeText(CreatePurchaseEntryActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
