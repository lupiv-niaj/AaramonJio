package com.aaramon.aaramonjio.supplier;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.R;

import java.util.ArrayList;

public class SupplierHistoryActivity extends Activity {

    SupplierModel SupplierModel = new SupplierModel();
    SupplierService SupplierService = new SupplierService();
    private ListView SupplierBillDetailsListView;
    String supplier_id, supplier_code;
    ImageView backbtn;
    TextView AddNewPurchaseEntryTextViewId;
    ImageView ImageViewSupplierEdit;
    String stateid = "", gst_category_id = "";

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
        setContentView(R.layout.activity_supplier_history);
        Bundle bun = getIntent().getExtras();
        supplier_id = bun.getString("supplier_id");
        supplier_code = bun.getString("SupplierCode");
        stateid = bun.getString("stateid");
        gst_category_id = bun.getString("category_id");
        SupplierBillDetailsListView = (ListView) findViewById(R.id.BillsListViewId);
        backbtn = (ImageView) findViewById(R.id.imageView2);
        ImageViewSupplierEdit = (ImageView) findViewById(R.id.ImageViewSupplierEdit);
        AddNewPurchaseEntryTextViewId = (TextView) findViewById(R.id.AddNewPurchaseEntryTextViewId);
        try {
            SupplierBillDetailAdapter BillDetailAdapter = new SupplierBillDetailAdapter(SupplierHistoryActivity.this);
            TextView[] SupplierHeaderArray = {
                    (TextView) findViewById(R.id.SupplierNameHeaderTextViewId),
                    (TextView) findViewById(R.id.SupplierNameTextViewId),
                    (TextView) findViewById(R.id.SupplierAddressTextViewId),
                    (TextView) findViewById(R.id.SupplierMobileNoTextViewId),
                    (TextView) findViewById(R.id.SupplierOutstandingTextViewId),
                    (TextView) findViewById(R.id.NoPurchaseEntryId),
                    (TextView) findViewById(R.id.SupplierOutstandingTextViewId),
                    (TextView) findViewById(R.id.SettleAllTextViewId)
            };
            SupplierService.PopulateSupplierBillsFromAPI(Integer.parseInt(supplier_id), SupplierHistoryActivity.this, SupplierBillDetailsListView, BillDetailAdapter, SupplierHeaderArray, supplier_code);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        ImageViewSupplierEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent m = new Intent(SupplierHistoryActivity.this, UpdateSupplier.class);
                m.putExtra("supplier_id", supplier_id);
                startActivity(m);
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        AddNewPurchaseEntryTextViewId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MAN", "STARTED");
                Intent TransferData = new Intent(SupplierHistoryActivity.this, CreatePurchaseEntryActivity.class);
                TransferData.putExtra("SupplierId", supplier_id);
                TransferData.putExtra("SupplierCode", supplier_code);
                TransferData.putExtra("stateid", stateid);
                TransferData.putExtra("gst_category_id",gst_category_id);
                TransferData.putExtra("SupplierName", ((TextView) findViewById(R.id.SupplierNameTextViewId)).getText());
                TransferData.putExtra("SupplierAddress", ((TextView) findViewById(R.id.SupplierAddressTextViewId)).getText());
                TransferData.putExtra("SupplierMobile", ((TextView) findViewById(R.id.SupplierMobileNoTextViewId)).getText());
                TransferData.putExtra("SupplierOutstanding", ((TextView) findViewById(R.id.SupplierOutstandingTextViewId)).getText());

                startActivity(TransferData);
            }
        });
    }

    public ArrayList<SupplierBillModel> GenerateBillList() {
        ArrayList<SupplierBillModel> SupplierBillList = new ArrayList<SupplierBillModel>();
        SupplierBillList.add(new SupplierBillModel("Jan", 02, "12345", "Credit", 10, 2500.00));
        SupplierBillList.add(new SupplierBillModel("Jan", 20, "12346", "Cash", 3, 500.00));
        SupplierBillList.add(new SupplierBillModel("Feb", 21, "12347", "Credit", 20, 5000.00));
        SupplierBillList.add(new SupplierBillModel("Mar", 15, "12348", "Cash", 30, 7500.00));
        SupplierBillList.add(new SupplierBillModel("Apr", 10, "12349", "Cash", 7, 150.25));
        return SupplierBillList;
    }
}
