package com.aaramon.aaramonjio.supplier;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.order.MainDashboard;


public class SupplierAddUpdateSuccess extends Activity {
    private TextView managesupplier;
    String SupplierCompany, message;
    TextView TVSuccessMsg, backtohome;

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
        setContentView(R.layout.activity_supplier_add_update_suceessfully);
        Bundle bun = getIntent().getExtras();
        SupplierCompany = bun.getString("SupplierCompany");
        message = bun.getString("message");
        managesupplier = (TextView) findViewById(R.id.managesupplier);
        TVSuccessMsg = (TextView) findViewById(R.id.success_msg);
        backtohome = (TextView) findViewById(R.id.backtohome);
        TVSuccessMsg.setText(message + "");

        managesupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(getApplicationContext(), SupplierList.class);
                m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(m);
                finish();
            }
        });

        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(getApplicationContext(), MainDashboard.class);
//                startActivity(m);
                m.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(m);
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        managesupplier.performClick();
    }
}
