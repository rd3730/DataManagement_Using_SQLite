package com.data_management;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteActivity extends AppCompatActivity {
    Button btnDelete;
    EditText editTextId;
    DatabaseHelper myDb;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        this.myDb = new DatabaseHelper(this);
        this.editTextId = (EditText) findViewById(R.id.edtdelete_id);
        this.btnDelete = (Button) findViewById(R.id.btndelete_delete);
        DeleteData();
    }

    public void DeleteData() {
        this.btnDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(DeleteActivity.this.editTextId.getText().toString())) {
                    Toast.makeText(DeleteActivity.this, "Plase enter ID Which you want to delete", 1).show();
                } else if (DeleteActivity.this.myDb.deleteData(DeleteActivity.this.editTextId.getText().toString()).intValue() > 0) {
                    Toast.makeText(DeleteActivity.this, "Data Deleted", 1).show();
                    DeleteActivity.this.editTextId.setText("");
                } else {
                    Toast.makeText(DeleteActivity.this, "Data not Deleted", 1).show();
                }
            }
        });
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
