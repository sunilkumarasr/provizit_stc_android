package com.provizit.Utilities;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RangeTimePickerDialog extends TimePickerDialog {

   //new
   private int minHour = -1;
    private int minMinute = -1;
    private int maxHour = 25;
    private int maxMinute = 59;
    Context context;
    private int currentHour = 0;
    private int currentMinute = 0;
    TimePicker mTimePicker;
    private final OnTimeSetListener callback;
    private Calendar calendar = Calendar.getInstance();
    private DateFormat dateFormat;


    public void setMin(int hour, int minute) {
        minHour = hour;
        minMinute = minute;
    }
    public void setMax(int hour, int minute) {
        maxHour = hour;
        maxMinute = minute;
    }
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        Log.d("RangeTimePickerDialog", "onTimeChanged: " + hourOfDay + ":" + minute);
        boolean validTime = true;
        if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)) {
            validTime = false;
            Log.d("RangeTimePickerDialog", "Time is less than minimum");
        }
        if (hourOfDay > maxHour || (hourOfDay == maxHour && minute > maxMinute)) {
            validTime = false;
            Log.d("RangeTimePickerDialog", "Time is greater than maximum");
        }
        if (validTime) {
            currentHour = hourOfDay;
            currentMinute = minute;
        }
        updateTime(currentHour, currentMinute);
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            mTimePicker = (TimePicker) findField(TimePickerDialog.class, TimePicker.class, "mTimePicker").get(this);
            NumberPicker mMinuteSpinner = (NumberPicker) mTimePicker.findViewById(Resources.getSystem().getIdentifier( "minute", "id", "android"));
            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue(59);
        } catch (Exception e) {
            throw new RuntimeException(e);
//            e.printStackTrace();
        }
    }
    private static Field findField(Class<?> objectClass, Class<?> fieldClass, String expectedName) {
        try {
            Field field = objectClass.getDeclaredField(expectedName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
    public RangeTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
        super(context, THEME_HOLO_LIGHT, callBack, hourOfDay, minute, is24HourView);
        this.callback = callBack;
        currentHour = hourOfDay;
        currentMinute = minute;
        dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        try {
            Field mTimePickerField = TimePickerDialog.class.getDeclaredField("mTimePicker");
            mTimePickerField.setAccessible(true);
            mTimePicker = (TimePicker) mTimePickerField.get(this);
            mTimePicker.setOnTimeChangedListener(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void show() {
        super.show();
        Button b = getButton(BUTTON_POSITIVE);
        Button b1 = getButton(BUTTON_NEGATIVE);
        b.setBackgroundResource(0);
        b1.setBackgroundResource(0);
    }
    public void updateTime(int hourOfDay, int minuteOfHour) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour);
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == BUTTON_POSITIVE && callback != null && mTimePicker != null) {
            mTimePicker.clearFocus();
            callback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                    mTimePicker.getCurrentMinute());
        }
    }
    @Override
    public Button getButton(int whichButton) {
        return super.getButton(whichButton);
    }




    //old
//    private int minHour = -1;
//    private int minMinute = -1;
//
//    private int maxHour = 25;
//    private int maxMinute = 25;
//    Context context;
//    private int currentHour = 0;
//    private int currentMinute = 0;
//    TimePicker mTimePicker;
//    private final OnTimeSetListener callback;
//    private Calendar calendar = Calendar.getInstance();
//    private DateFormat dateFormat;
//
//    public void setMin(int hour, int minute) {
//        minHour = hour;
//        minMinute = minute;
//    }
//    public void setMax(int hour, int minute) {
//        maxHour = hour;
//        maxMinute = minute;
//    }
//    @Override
//    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//        Log.d("DADADADA", "onTimeChanged");
//        System.out.println("sfjha"+hourOfDay);
//        System.out.println("sfjha"+minute);
//        minute = minute*15;
//        System.out.println("sfjha1"+minute);
//        boolean validTime = true;
//        if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)) {
//            validTime = false;
//            System.out.println("subhash");
//        }
//        if (hourOfDay > maxHour || (hourOfDay == maxHour && minute > maxMinute)) {
//            validTime = false;
//            System.out.println("subhash12");
//        }
//        if (validTime) {
//            currentHour = hourOfDay;
//            currentMinute = minute;
//            System.out.println("timew2412"+currentHour+" "+currentMinute/15);
//            System.out.println("subhash123"+ ""+minute + " " + minMinute );
//        }
//        System.out.println("timew"+currentHour+" "+currentMinute);
//        updateTime(currentHour, currentMinute/15);
//    }
//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        try {
//            mTimePicker = (TimePicker) findField(TimePickerDialog.class, TimePicker.class, "mTimePicker").get(this);
//            NumberPicker mMinuteSpinner = (NumberPicker) mTimePicker.findViewById(Resources.getSystem().getIdentifier( "minute", "id", "android"));
//            mMinuteSpinner.setMinValue(0);
//            mMinuteSpinner.setMaxValue((60 / 15) - 1);
//            List<String> displayedValues = new ArrayList<String>();
//            for (int i = 0; i < 60; i += 15) {
//                displayedValues.add(String.format("%02d", i));
//            }
//            mMinuteSpinner.setDisplayedValues(displayedValues
//                    .toArray(new String[0]));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
////            e.printStackTrace();
//        }
//    }
//    private static Field findField(Class objectClass, Class fieldClass, String expectedName) {
//        try {
//            Field field = objectClass.getDeclaredField(expectedName);
//            field.setAccessible(true);
//            return field;
//        } catch (NoSuchFieldException e) {} // ignore
//        for (Field searchField : objectClass.getDeclaredFields()) {
//            if (searchField.getType() == fieldClass) {
//                searchField.setAccessible(true);
//                return searchField;
//            }
//        }
//        return null;
//    }
//    public RangeTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
//        super(context, THEME_HOLO_LIGHT, callBack, hourOfDay, minute/15, is24HourView);
//        this.callback = callBack;
//        currentHour = hourOfDay;
//        currentMinute = minute/15;
//        context = context;
//        dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
//        try {
//            Class<?> superclass = getClass().getSuperclass();
//            Field mTimePickerField = superclass.getDeclaredField("mTimePicker");
//            mTimePickerField.setAccessible(true);
//            TimePicker mTimePicker = (TimePicker) mTimePickerField.get(this);
//            mTimePicker.setOnTimeChangedListener(this);
//        } catch (NoSuchFieldException e) {
//        } catch (IllegalArgumentException e) {
//        } catch (IllegalAccessException e) {
//        }
//    }
//    @Override
//    public void show() {
//        super.show();
//        Button b = getButton(BUTTON_POSITIVE);
//        Button b1 = getButton(BUTTON_NEGATIVE);
//        b.setBackgroundResource(0);
//        b1.setBackgroundResource(0);
//    }
//    public void updateTime(int hourOfDay, int minuteOfHour) {
//        mTimePicker.setCurrentHour(hourOfDay);
//        mTimePicker.setCurrentMinute(minuteOfHour);
//        System.out.println("mihdjg"+minuteOfHour);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            System.out.println("mihdjg1"+mTimePicker.getMinute());
//        }
//    }
//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//        if (which == BUTTON_POSITIVE && callback != null && mTimePicker != null) {
//            mTimePicker.clearFocus();
//            callback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
//                    mTimePicker.getCurrentMinute() * 15);
//        }
//    }
//    @Override
//    public Button getButton(int whichButton) {
//        return super.getButton(whichButton);
//    }

}
