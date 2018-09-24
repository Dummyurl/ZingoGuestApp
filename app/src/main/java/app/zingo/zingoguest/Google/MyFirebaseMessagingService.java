package app.zingo.zingoguest.Google;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;

import app.zingo.zingoguest.R;
import app.zingo.zingoguest.UI.BookingDetails.RoomAcceptanceActivtiy;
import app.zingo.zingoguest.UI.BookingDetails.RoomServiceAcceptanceReportActivity;
import app.zingo.zingoguest.UI.BookingDetails.TripDetailsScreen;
import app.zingo.zingoguest.UI.LandingScreens.WelcomeScreen;
import app.zingo.zingoguest.Utils.PreferenceHandler;

/**
 * Created by ZingoHotels Tech on 24-09-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> map = remoteMessage.getData();

        sendNotification(notification.getTitle(), notification.getBody(), map);

      /*  if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }*/
    }


    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    /*private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "HotelNotification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("body");
            String imageUrl = data.getString("image");

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification
            Intent intent;

            if(title.equalsIgnoreCase("Room Bookings")){
                intent = new Intent(getApplicationContext(), PendingCheckOutActivities.class);
            }else{
                intent = new Intent(getApplicationContext(), FireBaseCheck.class);
            }
            //Intent intent = new Intent(getApplicationContext(), FireBaseCheck.class);

            //if there is no image
            if(imageUrl.equals("null")){
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
            }else{
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }*/


    private void sendNotification(String title, String body, Map<String, String> map) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Intent intent = null;
        String s = null;

        if(title.equalsIgnoreCase("Room Acceptance")){

            //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent = new Intent(this, WelcomeScreen.class);
            intent.putExtra("Title",title);
            intent.putExtra("Message",body);
            s = "Thank you for doing pre checkin. Your room request has been accepted";


        }
        else if(title.equalsIgnoreCase("Checkout Successful"))
        {
            intent = new Intent(this, TripDetailsScreen.class);
            intent.putExtra("Title",title);
            intent.putExtra("Message",body);
            s = "Thank you for doing checkout. Your room request has been accepted";
        }
        else if(title.equalsIgnoreCase("Room Changed"))
        {
            intent = new Intent(this,WelcomeScreen.class);
            if(body != null && !body.isEmpty())
            {
                int i = Integer.parseInt(body);
                intent.putExtra("BookingID",i);
            }
            intent.putExtra("Title",title);
            intent.putExtra("Message",body);
            s = "Thank you for upgrading room. Your room request has been accepted";

        }
        else if(title.equalsIgnoreCase("Room Service Response"))
        {
            intent = new Intent(this,RoomServiceAcceptanceReportActivity.class);
            if(body != null && !body.isEmpty())
            {
                int i = Integer.parseInt(body);
                intent.putExtra("BookingID",i);
            }
            intent.putExtra("Title",title);
            intent.putExtra("Message",body);
            s = "Thank you for ordering food. Please find the response from hotelier";
        }
        else{
            intent = new Intent(this, RoomAcceptanceActivtiy.class);
            intent.putExtra("Title",title);
            intent.putExtra("Message",body);
            if(title.equalsIgnoreCase("Room Rejected"))
            {
                s = "Sorry. Your request is rejected. Please do pre checkin for other room(s)";
            }
            else if(title.equalsIgnoreCase("Room Suggested"))
            {
                s = "Thank you for doing pre checkin.Hotelier has suggest some rooms please check and revert back";
            }else if(title.equalsIgnoreCase("Room Upgrade Suggested"))
            {
                s = "Thank you for doing room upgrade.Hotelier has suggest some rooms please check and revert back";
            }
            else if(title.equalsIgnoreCase("Checkout Rejected"))
            {
                s = body;
            }
            else if(title.equalsIgnoreCase("Room Upgrade Rejected"))
            {
                s = "Sorry. Your request for room upgrade is rejected.";
            }
        }

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);



        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/good_morning");

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK );
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String CHANNEL_ID = ""+ PreferenceHandler.getInstance(MyFirebaseMessagingService.this).getUserId();// The id of the channel.
        CharSequence name = ""+ PreferenceHandler.getInstance(MyFirebaseMessagingService.this).getUserFullName();// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }

        Notification.Builder notificationBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(this)
                    .setTicker(title).setWhen(0)
                    .setContentTitle(title)
                    .setContentText(s)
                    .setAutoCancel(true)
                    .setFullScreenIntent(pendingIntent,true)
                    .setSound(sound)
                    //.setNumber()
                    .setContentIntent(pendingIntent)
                    .setContentInfo(title)
                    .setLargeIcon(icon)
                    .setChannelId("1")
                    .setPriority(Notification.PRIORITY_MAX)

                    // .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setSmallIcon(R.mipmap.ic_launcher);
        }else{
            notificationBuilder = new Notification.Builder(this)
                    .setTicker(title).setWhen(0)
                    .setContentTitle(title)
                    .setContentText(s)
                    .setAutoCancel(true)
                    .setFullScreenIntent(pendingIntent,true)
                    .setSound(sound)
                    .setContentIntent(pendingIntent)
                    .setContentInfo(title)
                    .setLargeIcon(icon)
                    .setPriority(Notification.PRIORITY_MAX)

                    .setNumber(1)
                    // .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setSmallIcon(R.mipmap.ic_launcher);
        }

        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(m, notificationBuilder.build());
    }

    @Override
    public void handleIntent(Intent intent) {
        try
        {
            if (intent.getExtras() != null)
            {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");

                for (String key : intent.getExtras().keySet())
                {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }

                onMessageReceived(builder.build());
            }
            else
            {
                super.handleIntent(intent);
            }
        }
        catch (Exception e)
        {
            super.handleIntent(intent);
        }
    }



}