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

    @Override
    protected String doInBackground(ArrayList<CompanyData>... params) {
        ArrayList<CompanyData> meetings = params[0];

        for (int i = 0; i < meetings.size(); i++) {
            CompanyData meeting = meetings.get(i);

            if (meeting == null || meeting.getRand_id() == null || meeting.getRand_id().get$numberLong() == null) {
                Log.e("AddToCalendar", "Skipping null meeting or rand_id at index " + i);
                continue;
            }

            final ContentResolver cr = activity.getContentResolver();
            Cursor calendarCursor = null;

            try {
                // Get calendar info
                Uri calendarUri = Uri.parse("content://com.android.calendar/calendars");
                calendarCursor = cr.query(calendarUri, new String[]{"_id", "calendar_displayName"}, null, null, null);

                if (calendarCursor == null || !calendarCursor.moveToFirst()) {
                    Log.e("AddToCalendar", "No calendars found on device.");
                    continue;
                }

                // Pick the first calendar
                int calendarId = calendarCursor.getInt(0);

                // Prepare event values
                ContentValues cv = new ContentValues();
                cv.put(CalendarContract.Events.CALENDAR_ID, calendarId);
                cv.put(CalendarContract.Events.TITLE, meeting.getSubject());

                long randId = Long.parseLong(meeting.getRand_id().get$numberLong());
                cv.put(CalendarContract.Events._ID, randId);
                cv.put(CalendarContract.Events.DTSTART, (meeting.getStart() + Conversions.timezone()) * 1000L);
                cv.put(CalendarContract.Events.DTEND, (meeting.getEnd() + Conversions.timezone() + 1) * 1000L);
                cv.put(CalendarContract.Events.HAS_ALARM, 1);
                cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

                // Location handling
                if (meeting.getMtype() != null) {
                    if (!meeting.getMtype().equals("1")) {
                        cv.put(CalendarContract.Events.EVENT_LOCATION, meeting.getMtype_val());
                    } else {
                        String room = meeting.getRoom() != null ? meeting.getRoom().getName() : "Room";
                        String entry = meeting.getEntry() != null ? meeting.getEntry().getName() : "Entry";
                        String location = meeting.getLocations() != null ? meeting.getLocations().getName() : "Location";
                        cv.put(CalendarContract.Events.EVENT_LOCATION, room + ", " + entry + ", " + location);
                    }
                }

                // Check if event already exists
                Cursor cursor1 = cr.query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{CalendarContract.Events._ID},
                        CalendarContract.Events._ID + " = ?",
                        new String[]{String.valueOf(randId)},
                        null
                );

                boolean eventExists = cursor1 != null && cursor1.moveToFirst();
                if (cursor1 != null) cursor1.close();

                if (eventExists) {
                    Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, randId);
                    cr.update(updateUri, cv, null, null);
                    Log.d("AddToCalendar", "Event updated: " + randId);
                } else {
                    // Insert new event
                    Uri eventUri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                    if (eventUri == null) {
                        Log.e("AddToCalendar", "Failed to insert event.");
                        continue;
                    }

                    long newEventId = Long.parseLong(eventUri.getLastPathSegment());

                    // Add attendees
                    if (meeting.getInvites() != null) {
                        for (Invited invite : meeting.getInvites()) {
                            if (invite == null) continue;

                            ContentValues values = new ContentValues();
                            values.put(CalendarContract.Attendees.ATTENDEE_NAME, invite.getName());
                            values.put(CalendarContract.Attendees.ATTENDEE_EMAIL, invite.getEmail());
                            values.put(CalendarContract.Attendees.ATTENDEE_RELATIONSHIP, CalendarContract.Attendees.RELATIONSHIP_ATTENDEE);
                            values.put(CalendarContract.Attendees.ATTENDEE_TYPE, CalendarContract.Attendees.TYPE_OPTIONAL);
                            values.put(CalendarContract.Attendees.ATTENDEE_STATUS, CalendarContract.Attendees.ATTENDEE_STATUS_INVITED);
                            values.put(CalendarContract.Attendees.EVENT_ID, newEventId);

                            cr.insert(CalendarContract.Attendees.CONTENT_URI, values);
                        }
                    }

                    // Add reminder
                    ContentValues reminderValues = new ContentValues();
                    reminderValues.put(CalendarContract.Reminders.EVENT_ID, newEventId);
                    reminderValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                    reminderValues.put(CalendarContract.Reminders.MINUTES, 15);
                    cr.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues);
                }

            } catch (Exception e) {
                Log.e("AddToCalendar", "Error adding/updating meeting at index " + i, e);
            } finally {
                if (calendarCursor != null) calendarCursor.close();
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
