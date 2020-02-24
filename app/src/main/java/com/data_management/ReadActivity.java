package com.data_management;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ReadActivity extends AppCompatActivity {
    Button btnfetch;
    DatabaseHelper myDb;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_read);
        this.myDb = new DatabaseHelper(this);
        this.btnfetch = (Button) findViewById(R.id.btnfetch);
        viewAll();
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle((CharSequence) title);
        builder.setMessage((CharSequence) Message);
        builder.show();
    }

    public void viewAll() {
        this.btnfetch.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Cursor res = ReadActivity.this.myDb.getAllData();
                if (res.getCount() == 0) {
                    ReadActivity.this.showMessage("Alert", "Nothing found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("Id :" + res.getString(0) + "\n");
                    buffer.append("Name :" + res.getString(1) + "\n");
                    buffer.append("Company Name :" + res.getString(2) + "\n");
                    buffer.append("Designation :" + res.getString(3) + "\n");
                    buffer.append("Contact Num :" + res.getString(4) + "\n\n");
                }
                ReadActivity.this.showMessage("Data", buffer.toString());
            }
        });
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
