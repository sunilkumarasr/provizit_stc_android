package com.provizit.Utilities;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;
import com.provizit.Conversions;
import java.util.ArrayList;
import java.util.TimeZone;

public class AddToCalendar extends AsyncTask<ArrayList<CompanyData>, Integer, String> {
    private static final String TAG = "AddToCalendar";
    String company_name;
    DatabaseHelper myDb;
    Context activity;

    public AddToCalendar(Context context) {
        activity = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(company_name + " PreExceute", "On pre Exceute......");
    }

    protected String doInBackground(ArrayList<CompanyData>... params) {
        ArrayList<CompanyData> meeting = params[0];
         for(int i=0;i< meeting.size();i++) {

             if (meeting.get(i).getRand_id() != null) {
                 final ContentResolver cr = activity.getContentResolver();
                 Cursor cursor;
                 if (Integer.parseInt(Build.VERSION.SDK) >= 8)
                     cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"), new String[]{"_id", "calendar_displayName"}, null, null, null);
                 else
                     cursor = cr.query(Uri.parse("content://calendar/calendars"), new String[]{"_id", "calendar_displayName"}, null, null, null);
                 if (cursor.moveToFirst()) {
                     final String[] calNames = new String[cursor.getCount()];
                     final int[] calIds = new int[cursor.getCount()];
                     for (int k = 0; k < calNames.length; k++) {
                         calIds[k] = cursor.getInt(0);
                         calNames[k] = cursor.getString(1);
                         cursor.moveToNext();
                     }
                     ContentValues cv = new ContentValues();
                     cv.put("calendar_id", calIds[0]);
                     cv.put("title", meeting.get(i).getSubject());
                     cv.put(CalendarContract.Events._ID, Long.parseLong(meeting.get(i).getRand_id().get$numberLong()));
                     cv.put("dtstart",(meeting.get(i).getStart() + Conversions.timezone()) * 1000);
                     cv.put("hasAlarm", 1);
                     cv.put("dtend", (meeting.get(i).getEnd() + Conversions.timezone() + 1) * 1000);
                     if(!meeting.get(i).getMtype().equals("1")){
                         cv.put(CalendarContract.Events.EVENT_LOCATION,meeting.get(i).getMtype_val());
                     }
                     else{
                         Log.e(TAG, "doInBackground:EVENT_LOCATION "+CalendarContract.Events.EVENT_LOCATION );
                         Log.e(TAG, "doInBackground:getRoom "+meeting.get(i).getRoom().getName() );
                         Log.e(TAG, "doInBackground:getEntry "+meeting.get(i).getEntry().getName() );
                        // Log.e(TAG, "doInBackground:getLocations "+meeting.get(i).getLocations().getName() );
                         cv.put(CalendarContract.Events.EVENT_LOCATION, meeting.get(i).getRoom().getName() + ", " + meeting.get(i).getEntry().getName() + ", " + meeting.get(i).getLocations().getName());
                     }
                     cv.put("eventTimezone", TimeZone.getDefault().getID());
                     String[] projection = new String[]{CalendarContract.Events._ID};
                     Cursor cursor1 = activity.getContentResolver()
                             .query(
                                     Uri.parse("content://com.android.calendar/events"),
                                     projection, CalendarContract.Events._ID + " =? ", new String[]{meeting.get(i).getRand_id().get$numberLong() + ""},
                                     null, null);
                     cursor1.moveToFirst();
                     if (cursor1.getCount() > 0) {
                         Uri updateEvent = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(meeting.get(i).getRand_id().get$numberLong()));
                         int rows = cr.update(updateEvent, cv, null, null);

                     } else {
                         Uri newEvent;
                         if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
                             newEvent = cr.insert(Uri.parse("content://com.android.calendar/events"), cv);
                         } else {
                             newEvent = cr.insert(Uri.parse("content://calendar/events"), cv);
                         }
                         if (newEvent != null) {

                             for (int j = 0; j < meeting.get(i).getInvites().size(); j++) {
                                 ContentValues values = new ContentValues();
                                 values.put(CalendarContract.Attendees.ATTENDEE_NAME, meeting.get(i).getInvites().get(j).getName());
                                 values.put(CalendarContract.Attendees.ATTENDEE_EMAIL, meeting.get(i).getInvites().get(j).getEmail());
                                 values.put(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, CalendarContract.Attendees.RELATIONSHIP_ATTENDEE);
                                 values.put(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_OPTIONAL);
                                 values.put(CalendarContract.Attendees.ATTENDEE_STATUS, CalendarContract.Attendees.ATTENDEE_STATUS_INVITED);
                                 values.put(CalendarContract.Attendees.EVENT_ID, Long.parseLong(meeting.get(i).getRand_id().get$numberLong()));
                                 if (Integer.parseInt(Build.VERSION.SDK) >= 8)
                                     cr.insert(Uri.parse("content://com.android.calendar/attendees"), values);

                                 else
                                     cr.insert(Uri.parse("content://calendar/attendees"), values);
                             }
                         }
                         if (newEvent != null) {
                             long id = Long.parseLong(newEvent.getLastPathSegment());
                             ContentValues values = new ContentValues();
                             values.put("event_id", id);
                             values.put("method", 1);
                             values.put("minutes", 15); // 15 minutes
                             if (Integer.parseInt(Build.VERSION.SDK) >= 8)
                                 cr.insert(Uri.parse("content://com.android.calendar/reminders"), values);

                             else
                                 cr.insert(Uri.parse("content://calendar/reminders"), values);

                         }
                     }
                 }
                 cursor.close();
             } else {

             }
         }

        return "You are at PostExecute";
    }

    protected void onProgressUpdate(Integer... a) {
        super.onProgressUpdate(a);
        Log.d("tab" + " onProgressUpdate", "You are in progress update ... " + a[0]);
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("asf" + " onPostExecute", "" + result);
    }

}
