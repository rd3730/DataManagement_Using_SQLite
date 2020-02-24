package com.data_management;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


public class SplashActivity extends RuntimePermissionsActivity {
    private ImageView img_splash;

    //DBPerson dbp;
    int per_code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

      /*  dbp = new DBPerson(this);
        // Open Database
        try {
            dbp.Open(DBPerson.WRITE_MODE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        dbp.clearAllData();*/


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.super.requestAppPermissions(new
                                String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, R.string.runtime_permissions_txt
                        , 20);
            }
        }, 1000);

    }


    @Override
    public void onPermissionsGranted(int requestCode) {
        if (requestCode == 20) {

            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }


}
