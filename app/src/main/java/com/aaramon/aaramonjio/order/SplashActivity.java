package com.aaramon.aaramonjio.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.aaramon.aaramonjio.R;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class SplashActivity extends Activity {
    private SharedPreference_Main sharedPreference;
    String BASE_URLINDIA = "";
    Context context;
    String locale = "";
    Locale locale1;
    public final static int REQUEST_CODE = 65635;

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
        setContentView(R.layout.activity_splash);
        sharedPreference = new SharedPreference_Main(getApplicationContext());
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.aaramon.aaramonjio", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
                locale = GetCountryZipCode();
                // TelephonyManager tm = (TelephonyManager) context
                // .getSystemService(Context.TELEPHONY_SERVICE);
                // String isoCountryCode = tm.getSimCountryIso();
            }


            BASE_URLINDIA = "http://www.aaramshop.co.in/api/index.php/merchant/";
            sharedPreference.setbaseurl(BASE_URLINDIA);
            sharedPreference.setbase_inpk_url("http://www.aaramshop.co.in/api/index.php/");
            sharedPreference.setcountry("INDIA");
            String deviceId = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            sharedPreference.deviceId(deviceId);
            Log.e("current", "" + locale);

        } catch (NameNotFoundException e) {
            Log.e("NameNotFoundException:", "" + e);
        } catch (NoSuchAlgorithmException e) {
            Log.e("NoAlgorithmException", "" + e);
        }
        //  showChangeLangDialog();

    }


    public void setLangRecreate() {
        String lang = sharedPreference.getlanguage();
        Configuration config = getBaseContext().getResources().getConfiguration();
        locale1 = new Locale(lang);
        Locale.setDefault(locale1);
        config.locale = locale1;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    public String GetCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        // getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources()
                .getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;

    }

    @Override
    protected void onResume() {
        super.onResume();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    checkDrawOverlayPermission();
                }
            }

        };

        new Thread(runnable).start();
    }

    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /** request permission via start activity for result */
                startActivityForResult(intent, REQUEST_CODE);
            } else {
                if (sharedPreference.getIsRegistered().equalsIgnoreCase(
                        "true")) {
                    setLangRecreate();
                    Log.i("Splash", "Resigter");
                    Intent intent = new Intent(SplashActivity.this,
                            MainDashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i("Splash", "Log In");
                    Intent intent = new Intent(SplashActivity.this,
                            LogInActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        } else {
            if (sharedPreference.getIsRegistered().equalsIgnoreCase(
                    "true")) {
                setLangRecreate();
                Log.i("Splash", "Resigter");
                Intent intent = new Intent(SplashActivity.this,
                        MainDashboard.class);
                startActivity(intent);
                finish();
            } else {
                Log.i("Splash", "Log In");
                Intent intent = new Intent(SplashActivity.this,
                        LogInActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
            // ** if so check once again if we have permission */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // continue here - permission was granted
                    if (sharedPreference.getIsRegistered().equalsIgnoreCase(
                            "true")) {
                        setLangRecreate();
                        Log.i("Splash", "Resigter");
                        Intent intent = new Intent(SplashActivity.this,
                                MainDashboard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.i("Splash", "Log In");
                        Intent intent = new Intent(SplashActivity.this,
                                LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        }
    }

}
