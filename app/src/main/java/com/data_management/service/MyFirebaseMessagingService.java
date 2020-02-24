package com.data_management.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.data_management.BuildConfig;
import com.data_management.MainActivity;
import com.data_management.Utils.Config;
import com.data_management.Utils.Constans;
import com.data_management.Utils.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());


        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData());
            final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();



               Ringtone r = RingtoneManager.getRingtone(this, alarmSound);
              r.play();

                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(Constans.CHANNEL_ID, Constans.CHANNEL_NAME, importance);
                mChannel.setDescription(Constans.CHANNEL_DESCRIPTION);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
               // mChannel.setSound(null, null);
                mChannel.setSound(alarmSound,attributes);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);
            }


            try {


                Map<String, String> data = remoteMessage.getData();
                // JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(data);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

          /*  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Map<String, String> data = remoteMessage.getData();
                String title = data.get("title");
                String message = data.get("message");
                // boolean isBackground = data.getBoolean("is_background");
                //String imageUrl = data.getString("image");
                //  String timestamp = data.getString("timestamp");
                //  JSONObject payload = data.getJSONObject("payload");

                Log.e(TAG, "title: " + title);
                Log.e(TAG, "message: " + message);

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
                mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);

                Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                resultIntent.putExtra("message", message);

                showNotificationMessage(getApplicationContext(), title, message, "", resultIntent);

                Log.e("Yes Oreo or +:: ", "Yes");
            } else {

                try {
                    Map<String, String> data = remoteMessage.getData();
                    // JSONObject json = new JSONObject(remoteMessage.getData().toString());
                    handleDataMessage(data);
                } catch (Exception e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }
            }*/
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
           // NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            //notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
            //NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
           // notificationUtils.playNotificationSound();
        }
    }

    private void handleDataMessage(Map<String, String> data) {
        //Log.e(TAG, "push json: " + json);

        try {
            //JSONObject data = new JSONObject(json);

            String title = data.get("title");
            String message = data.get("message");
            // boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.get("image");
            //  String timestamp = data.getString("timestamp");
            //  JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            //Log.e(TAG, "isBackground: " + isBackground);
            // Log.e(TAG, "payload: " + payload.toString());
             Log.e(TAG, "imageUrl: " + imageUrl);
            // Log.e(TAG, "timestamp: " + timestamp);



         /*   Intent resultIntent1 = new Intent(getApplicationContext(), HomeActivity.class);
            resultIntent1.putExtra("message", message);
            showNotificationMessage(getApplicationContext(), title, message, "", resultIntent1);
*/
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                Log.e("Yes back:: ", "Yes");
                // play notification sound
               // NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                //notificationUtils.playNotificationSound();

                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, "", resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, "", resultIntent, imageUrl);
                }

            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                //showNotificationMessage(getApplicationContext(), title, message, "", resultIntent);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, "", resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, "", resultIntent, imageUrl);
                }

                Log.e("Yes :: ", "Yes");
            }
            // } catch (JSONException e) {
            //    Log.e(TAG, "Json Exception: " + e.getMessage());
            // }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    // Showing notification with text only


    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }


    // Showing notification with text and image


    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}
