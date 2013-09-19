

package edu.rpi.tw.mobilehealth;

import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.util.Patterns;

public class CalendarActivity extends Activity {

	public static final String[] EVENT_PROJECTION = new String[] {
        Calendars._ID,                           // 0
        Calendars.ACCOUNT_NAME,                  // 1
        Calendars.CALENDAR_DISPLAY_NAME,         // 2
        Calendars.OWNER_ACCOUNT                  // 3
    };
      
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
   
	
    private static final String DEBUG_TAG = "CalendarActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String email = "";
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                 email = account.name;
            }
        }
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = Calendars.CONTENT_URI;   
        String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND (" 
                                + Calendars.ACCOUNT_TYPE + " = ?) AND ("
                                + Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[] {email, "com.google",email}; 
        // Submit the query and get a Cursor object back. 
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        
        
        Intent intent = new Intent(this, QualitativeDataLogActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

      
        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;
              
            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
                      
            // Do something with the values...

            // Build notification
            // Actions are just fake
            Notification noti = new Notification.Builder(this)
                    .setContentTitle(String.valueOf(calID))
                    .setContentText(displayName)
                    .setSmallIcon(R.drawable.icon)
                    .setContentIntent(pIntent).getNotification();
                
              
            NotificationManager notificationManager = 
              (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // Hide the notification after its selected
            noti.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(0, noti); 
        }
    }

    

    

}