package com.data_management;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InsertActivity extends AppCompatActivity {
    Button btnAddData;
    EditText editNumber;
    EditText editDate;
    EditText editName;
    EditText editRemark;
    EditText editTextId;
    DatabaseHelper myDb;
    private SQLiteDatabase sql;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_insert);
        this.myDb = new DatabaseHelper(this);
        this.editName = (EditText) findViewById(R.id.editText_name);
        this.editNumber = (EditText) findViewById(R.id.editText_number);
        this.editRemark = (EditText) findViewById(R.id.editText_remark);
        this.editDate = (EditText) findViewById(R.id.editText_date);
        this.btnAddData = (Button) findViewById(R.id.button_add);
        AddData();

        editDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    set_From_Date();
                }
                return true;
            }
        });
    }

    private void set_From_Date() {

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                if (monthOfYear != 12) {
                    monthOfYear = monthOfYear + 1;
                }

                String str_dt1 = dayOfMonth + "-" + monthOfYear + "-" + year;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd MMM yy");
                try {
                    Date dt = simpleDateFormat.parse(str_dt1);
                    DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    String str_dt = format.format(dt);
                    editDate.setText(str_dt);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                View view1 = getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void AddData() {
        this.btnAddData.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String name = InsertActivity.this.editName.getText().toString();
                String number = InsertActivity.this.editNumber.getText().toString();
                String date = InsertActivity.this.editDate.getText().toString();
                String remark = InsertActivity.this.editRemark.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(InsertActivity.this, "Plase enter the Name", 1).show();
                } else if (TextUtils.isEmpty(number)) {
                    Toast.makeText(InsertActivity.this, "Plase enter the Mobile Number", 1).show();
                } else if (TextUtils.isEmpty(date)) {
                    Toast.makeText(InsertActivity.this, "Plase enter the Date", 1).show();
                } else if (TextUtils.isEmpty(remark)) {
                    Toast.makeText(InsertActivity.this, "Plase enter the Remark", 1).show();
                } else if (InsertActivity.this.myDb.insertData(name, number, date, remark)) {
                    Toast.makeText(InsertActivity.this, "Data Inserted", 1).show();
                    InsertActivity.this.editName.setText("");
                    InsertActivity.this.editDate.setText("");
                    InsertActivity.this.editRemark.setText("");
                    InsertActivity.this.editNumber.setText("");
                } else {
                    Toast.makeText(InsertActivity.this, "Data not Inserted", 1).show();
                }
            }
        });
    }


    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
