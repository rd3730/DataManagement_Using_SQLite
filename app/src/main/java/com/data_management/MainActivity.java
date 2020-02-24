package com.data_management;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.data_management.Utils.Config;
import com.data_management.Utils.Constans;
import com.data_management.model.UserList;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv_main;
    ArrayList<UserList> userLists;
    DatabaseHelper myDb;
    String regId = "";
    BroadcastReceiver mRegistrationBroadcastReceiver;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AIzaSyBMhLL1c9q7DGy4JoAeW6Sh7trmsEBAx80";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv_main = (RecyclerView) findViewById(R.id.rv_main);
        rv_main.setHasFixedSize(true);
        rv_main.setLayoutManager(new LinearLayoutManager(this));

        viewAll();
        findViewById(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InsertActivity.class));
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/raw/fairy");

            //Log.e("Abc :: ", ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/raw/fairy");

            Ringtone r = RingtoneManager.getRingtone(this, alarmSound);
            //r.play();

            NotificationChannel mChannel = new NotificationChannel(Constans.CHANNEL_ID, Constans.CHANNEL_NAME, importance);
            mChannel.setDescription(Constans.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setSound(alarmSound, attributes);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    //Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    //txtMessage.setText(message);
                }
            }
        };



        JSONObject jsonRequest = new JSONObject();
        JSONObject dataJson = new JSONObject();
        JSONObject notificationJson = new JSONObject();
        try {
            //jsonRequest.put("to","d7s40gkMBiE:APA91bEtZj2u6qEzPTBZGF86_IlI9Pr-9-kl3BtD3TUrw19IVeyxEZ68wo91eBVjkj-TD10T86yizFnKee6wlua8230SMEx1pSdrVe0XvHBf5nx7boGSSOOXZcWpXzTW4P54aevPmFKz");
            notificationJson.put("title", "Data Added...");
            notificationJson.put("body", "Data Added...");
            jsonRequest.put("notification", notificationJson);
            /*dataJson.put("title", "Data Added...");
            dataJson.put("body", "Data Added...");
            jsonRequest.put("data", dataJson);*/

        } catch (JSONException e) {
            e.printStackTrace();
        }


        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonRequest));
        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .addHeader("Authorization", "key=AIzaSyBMhLL1c9q7DGy4JoAeW6Sh7trmsEBAx80")
                .addHeader("Content-Type", "application/json")
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("Failed :: ", "yes");

            }

            @Override
            public void onResponse(okhttp3.Call call, final okhttp3.Response response) {
                Log.e("Success:: ", "yes");

                if (response.isSuccessful()) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            String abcd = null;

                        }
                    });


                }

            }
        });

    }

    public void viewAll() {
        this.myDb = new DatabaseHelper(this);
        userLists = new ArrayList<>();
        Cursor res = this.myDb.getAllData();
        if (res.getCount() == 0) {
            findViewById(R.id.tv_no_data).setVisibility(View.VISIBLE);
            rv_main.setVisibility(View.GONE);
            return;
        } else {
            findViewById(R.id.tv_no_data).setVisibility(View.GONE);
            rv_main.setVisibility(View.VISIBLE);
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            UserList ul = new UserList();
            ul.setId(res.getString(0));
            ul.setName(res.getString(1));
            ul.setNumber(res.getString(2));
            ul.setDate(res.getString(3));
            ul.setRemarks(res.getString(4));

            userLists.add(ul);
        }

        rv_main.setAdapter(new UserList1Adapter(this, userLists));
    }


    public class UserList1Adapter extends RecyclerView.Adapter<UserList1Adapter.ViewHolder> {

        Context context;
        ArrayList<UserList> userLists;

        public UserList1Adapter(Context context, ArrayList<UserList> userLists) {
            this.context = context;
            this.userLists = userLists;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_user, parent, false));
        }

        @Override
        public int getItemCount() {
            //return bankDetails.size();
            return userLists == null ? 0 : userLists.size();
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.tv_name.setText(userLists.get(position).getName());
            holder.tv_number.setText(userLists.get(position).getNumber());
            holder.tv_date.setText(userLists.get(position).getDate());
            holder.tv_remark.setText(userLists.get(position).getRemarks());

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDb.deleteData(userLists.get(position).getId()).intValue();
                    viewAll();
                }
            });

            holder.iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                    NOTIFICATION_TITLE ="NOtify";
                    NOTIFICATION_MESSAGE = "Hello User";


                    JSONObject notification = new JSONObject();
                    JSONObject notifcationBody = new JSONObject();
                    try {
                        notifcationBody.put("title", NOTIFICATION_TITLE);
                        notifcationBody.put("message", NOTIFICATION_MESSAGE);

                        notification.put("to",regId);
                        notification.put("data", notifcationBody);
                    } catch (JSONException e) {
                        Log.e(TAG, "onCreate: " + e.getMessage() );
                    }
                    sendNotification(notification);
                    context.startActivity(new Intent(context, UpdateActivity.class)
                            .putExtra("id", userLists.get(position).getId())
                            .putExtra("name", userLists.get(position).getName())
                            .putExtra("number", userLists.get(position).getNumber())
                            .putExtra("date", userLists.get(position).getDate())
                            .putExtra("remark", userLists.get(position).getRemarks()));
                }
            });


        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_name, tv_number, tv_date, tv_remark;
            ImageView iv_edit, iv_delete;
            RelativeLayout rl_main;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                iv_edit = (ImageView) itemView.findViewById(R.id.iv_edit);
                iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                tv_number = (TextView) itemView.findViewById(R.id.tv_number);
                tv_date = (TextView) itemView.findViewById(R.id.tv_date);
                tv_remark = (TextView) itemView.findViewById(R.id.tv_remark);

            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewAll();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        // NotificationUtils.clearNotifications(getApplicationContext());

        regId = FirebaseInstanceId.getInstance().getToken();

        Log.e("Device Token resume :: ", regId);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        // Log.e("Reg No ::", "Login Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {
            //txtRegId.setText("Firebase Reg Id: " + regId);
            //Log.e("Reg Not Empty ::", "Firebase reg id: " + regId);
        } else {
            //Log.e("Reg Empty ::", "Firebase Reg Id is not received yet!");

            //txtRegId.setText("Firebase Reg Id is not received yet!");
        }
    }

    /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse: " + response.toString());
                        //edtTitle.setText("");
                        //edtMessage.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAffinity(MainActivity.this);
        finish();
    }
}
