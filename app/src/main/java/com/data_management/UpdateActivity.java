package com.data_management;

import android.app.DatePickerDialog;
import android.content.Context;
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

public class UpdateActivity extends AppCompatActivity {
    Button btnUpdate;
    EditText editNumber;
    EditText editDate;
    EditText editName;
    EditText editRemark;
    EditText editTextId;
    DatabaseHelper myDb;
    String id, name, number, date, remark;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_update);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        date = getIntent().getStringExtra("date");
        remark = getIntent().getStringExtra("remark");

        this.myDb = new DatabaseHelper(this);
        this.editName = (EditText) findViewById(R.id.edtupdate_name);
        this.editNumber = (EditText) findViewById(R.id.edtupdate_number);
        this.editRemark = (EditText) findViewById(R.id.edtupdate_remark);
        this.editDate = (EditText) findViewById(R.id.edtupdate_date);
        this.editTextId = (EditText) findViewById(R.id.edtupdate_id);
        this.btnUpdate = (Button) findViewById(R.id.btnupdate_update);

        editTextId.setText(id);
        editName.setText(name);
        editNumber.setText(number);
        editRemark.setText(remark);
        editDate.setText(date);

        editDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    set_From_Date();
                }
                return true;
            }
        });

        UpdateData();
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

    public void UpdateData() {
        this.btnUpdate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
              /*  if (TextUtils.isEmpty(UpdateActivity.this.editTextId.getText().toString())) {
                    Toast.makeText(UpdateActivity.this, "Plase enter ID which you want to update", 1).show();
                } else*/
                String name = UpdateActivity.this.editName.getText().toString();
                String number = UpdateActivity.this.editNumber.getText().toString();
                String date = UpdateActivity.this.editDate.getText().toString();
                String remark = UpdateActivity.this.editRemark.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(UpdateActivity.this, "Plase enter the Name", 1).show();
                } else if (TextUtils.isEmpty(number)) {
                    Toast.makeText(UpdateActivity.this, "Plase enter the Mobile Number", 1).show();
                } else if (TextUtils.isEmpty(date)) {
                    Toast.makeText(UpdateActivity.this, "Plase enter the Date", 1).show();
                } else if (TextUtils.isEmpty(remark)) {
                    Toast.makeText(UpdateActivity.this, "Plase enter the Remark", 1).show();
                } else if (UpdateActivity.this.myDb.updateData(id, UpdateActivity.this.editName.getText().toString(), UpdateActivity.this.editNumber.getText().toString(), UpdateActivity.this.editDate.getText().toString(), UpdateActivity.this.editRemark.getText().toString())) {
                    Toast.makeText(UpdateActivity.this, "Data Update", 1).show();
                    UpdateActivity.this.editName.setText("");
                    UpdateActivity.this.editDate.setText("");
                    UpdateActivity.this.editRemark.setText("");
                    UpdateActivity.this.editNumber.setText("");
                    UpdateActivity.this.editTextId.setText("");
                    finish();
                } else {
                    Toast.makeText(UpdateActivity.this, "Data not Updated", 1).show();
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
