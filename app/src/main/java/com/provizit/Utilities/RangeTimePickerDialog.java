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

    private int minHour = -1;
    private int minMinute = -1;

    private int maxHour = 25;
    private int maxMinute = 25;
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

        Log.d("DADADADA", "onTimeChanged");
        System.out.println("sfjha"+hourOfDay);
        System.out.println("sfjha"+minute);
//
        minute = minute*15;
        System.out.println("sfjha1"+minute);
        boolean validTime = true;


        if (hourOfDay < minHour || (hourOfDay == minHour && minute < minMinute)) {
            validTime = false;
            System.out.println("subhash");

        }

        if (hourOfDay > maxHour || (hourOfDay == maxHour && minute > maxMinute)) {
            validTime = false;
            System.out.println("subhash12");
        }

        if (validTime) {

            currentHour = hourOfDay;
            currentMinute = minute;
            System.out.println("timew2412"+currentHour+" "+currentMinute/15);
            System.out.println("subhash123"+ ""+minute + " " + minMinute );

        }
//        if (currentMinute < 15) {
//            currentMinute = 15;
//        } else if (currentMinute < 30) {
//            currentMinute = 30;
//        } else if (minute < 45) {
//            currentMinute = 45;
//        } else {
//            currentHour = currentHour + 1;
//            currentMinute = 0;
//        }
        System.out.println("timew"+currentHour+" "+currentMinute);
        updateTime(currentHour, currentMinute/15);
//        updateDialogTitle(view, currentHour, currentMinute);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) { // fixes the bug in API 24
//            try {
//                System.out.print("Subhashhdjh");
//                Log.e("Subhashhdjh","sfd");
//                // Get the theme's android:timePickerMode
//                final int MODE_SPINNER = 1;
////                @SuppressLint("PrivateApi") Class<?> styleableClass = Class.forName("com.android.internal.R$id");
////                Field timePickerStyleableField = styleableClass.getField("timePicker");
////                int[] timePickerStyleable = (int[]) timePickerStyleableField.get(null);
////                final TypedArray a = context.obtainStyledAttributes(null, timePickerStyleable, android.R.attr.timePickerStyle, 0);
////                Field timePickerModeStyleableField = styleableClass.getField("TimePicker_timePickerMode");
////                int timePickerModeStyleable = timePickerModeStyleableField.getInt(null);
////                final int mode = a.getInt(timePickerModeStyleable, MODE_SPINNER);
////                a.recycle();
////                if (mode == MODE_SPINNER) {
//                    TimePicker mTimePicker = (TimePicker) findField(TimePickerDialog.class, TimePicker.class, "mTimePicker").get(this);
//                    @SuppressLint("PrivateApi") Class<?> delegateClass = Class.forName("android.widget.TimePicker$TimePickerDelegate");
//                    Field delegateField = findField(TimePicker.class, delegateClass, "mDelegate");
//                    Field mField = findField(TimePicker.class, delegateClass, "minute");
//                    NumberPicker mMinuteSpinner;
//                assert mTimePicker != null;
//                mMinuteSpinner = (NumberPicker) mTimePicker.findViewById(mField.getInt(null));
//                    mMinuteSpinner.setMinValue(0);
//            mMinuteSpinner.setMaxValue((60 / 15) - 1);
//            List<String> displayedValues = new ArrayList<String>();
//            for (int i = 0; i < 60; i += 15) {
//                displayedValues.add(String.format("%02d", i));
//            }
//            mMinuteSpinner.setDisplayedValues(displayedValues
//                    .toArray(new String[0]));
//                    Object delegate = delegateField.get(mTimePicker);
//                    Class<?> spinnerDelegateClass;
//                    spinnerDelegateClass = Class.forName("android.widget.TimePickerSpinnerDelegate");
//                    // In 7.0 Nougat for some reason the timePickerMode is ignored and the delegate is TimePickerClockDelegate
////                    if (delegate.getClass() != spinnerDelegateClass) {
////                        delegateField.set(mTimePicker, null); // throw out the TimePickerClockDelegate!
////                        mTimePicker.removeAllViews(); // remove the TimePickerClockDelegate views
////                        Constructor spinnerDelegateConstructor = spinnerDelegateClass.getConstructor(TimePicker.class, Context.class, AttributeSet.class, int.class, int.class);
////                        spinnerDelegateConstructor.setAccessible(true);
////                        // Instantiate a TimePickerSpinnerDelegate
////                        delegate = spinnerDelegateConstructor.newInstance(mTimePicker, context, null, android.R.attr.timePickerStyle, 0);
////                        delegateField.set(mTimePicker, delegate); // set the TimePicker.mDelegate to the spinner delegate
////                        // Set up the TimePicker again, with the TimePickerSpinnerDelegate
//////                        timePicker.setIs24HourView(is24HourView);
//////                        timePicker.setCurrentHour(hourOfDay);
//////                        timePicker.setCurrentMinute(minute);
////                        mTimePicker.setOnTimeChangedListener(this);
////                    }
////                }
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//
        try {
//            Class<?> classForid = Class.forName("com.android.internal.R$id");
//
//            Field timePickerField = classForid.getField("timePicker");
//            this.mTimePicker = (TimePicker) findViewById(timePickerField
//                    .getInt(null));
//            Field field = classForid.getField("minute");
            mTimePicker = (TimePicker) findField(TimePickerDialog.class, TimePicker.class, "mTimePicker").get(this);
            NumberPicker mMinuteSpinner = (NumberPicker) mTimePicker.findViewById(Resources.getSystem().getIdentifier( "minute", "id", "android"));
            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue((60 / 15) - 1);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += 15) {
                displayedValues.add(String.format("%02d", i));
            }
            mMinuteSpinner.setDisplayedValues(displayedValues
                    .toArray(new String[0]));
        } catch (Exception e) {
            throw new RuntimeException(e);
//            e.printStackTrace();
        }


    }
    private static Field findField(Class objectClass, Class fieldClass, String expectedName) {
        try {
            Field field = objectClass.getDeclaredField(expectedName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {} // ignore
        // search for it if it wasn't found under the expected ivar name
        for (Field searchField : objectClass.getDeclaredFields()) {
            if (searchField.getType() == fieldClass) {
                searchField.setAccessible(true);
                return searchField;
            }
        }
        return null;
    }



//        mTimePicker.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//            }
//        });
//        mTimePicker.scrollBy(0,1);
//
//        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//
//            }
//        });

    public RangeTimePickerDialog(Context context, OnTimeSetListener callBack, int hourOfDay, int minute, boolean is24HourView) {
        super(context, THEME_HOLO_LIGHT, callBack, hourOfDay, minute/15, is24HourView);
        this.callback = callBack;
        currentHour = hourOfDay;
        currentMinute = minute/15;
        context = context;
        dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

        try {

            Class<?> superclass = getClass().getSuperclass();

//
//           b.setBackgroundResource(0);
            Field mTimePickerField = superclass.getDeclaredField("mTimePicker");
            mTimePickerField.setAccessible(true);
            TimePicker mTimePicker = (TimePicker) mTimePickerField.get(this);
            mTimePicker.setOnTimeChangedListener(this);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
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
        System.out.println("mihdjg"+minuteOfHour);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             System.out.println("mihdjg1"+mTimePicker.getMinute());
         }

//        mTimePicker.setCurrentMinute(minuteOfHour);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        if (which == BUTTON_POSITIVE && callback != null && mTimePicker != null) {
            mTimePicker.clearFocus();
            callback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                    mTimePicker.getCurrentMinute() * 15);
        }
    }

    @Override
    public Button getButton(int whichButton) {
        return super.getButton(whichButton);


    }
//    private void updateDialogTitle(TimePicker timePicker, int hourOfDay, int minute) {
//        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
//        calendar.set(Calendar.MINUTE, minute);
//        try {
//            timePicker.setCurrentHour(hourOfDay);
//            timePicker.setCurrentMinute(minute);
//        } catch (Exception ignored) {
//
//            Log.d("DADADADA", String.valueOf(ignored));
//        }
//        String title = dateFormat.format(calendar.getTime());
//        setTitle(title);
//    }
}
