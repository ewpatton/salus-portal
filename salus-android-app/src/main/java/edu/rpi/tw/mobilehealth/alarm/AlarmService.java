package edu.rpi.tw.mobilehealth.alarm;
 
 
import edu.rpi.tw.mobilehealth.R;
import edu.rpi.tw.mobilehealth.util.LocalIntents;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
                            
 
public class AlarmService extends Service 
{
      
   private NotificationManager mManager;
 
    @Override
    public IBinder onBind(Intent arg0)
    {
       // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public void onCreate() 
    {
       // TODO Auto-generated method stub  
       super.onCreate();
       mManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }
 
   @Override
   public int onStartCommand(Intent intent, int flags, int startId)
   {
       super.onStartCommand(intent, flags, startId);

       Intent intent1 = new Intent(LocalIntents.ACTION_QUERY_USER);
       intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
       PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

       Notification notification = new NotificationCompat.Builder(this)
           .setSmallIcon(R.drawable.ic_launcher)
           .setContentText( "AlarmServiceDemo" )
           .setContentText( "This is a test message!" )
           .setWhen(System.currentTimeMillis())
           .setContentIntent( pendingNotificationIntent )
           .getNotification();
       notification.flags |= Notification.FLAG_AUTO_CANCEL;

       mManager.notify(0, notification);

       return START_STICKY;
    }
 
    @Override
    public void onDestroy() 
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
 
}
