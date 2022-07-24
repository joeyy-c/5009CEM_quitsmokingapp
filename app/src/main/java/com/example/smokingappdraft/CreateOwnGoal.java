package com.example.smokingappdraft;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Calendar;
import java.util.Date;


public class CreateOwnGoal extends AppCompatActivity {

//    private String mDate;
    private TextInputEditText etDateToComplete;
    private TextInputEditText etOptionTrack;
    private TextInputEditText etGoalTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_own_goal);

        etDateToComplete = findViewById(R.id.pickerDateToComplete);
        etOptionTrack = findViewById(R.id.optionTrack);
        etGoalTitle = findViewById(R.id.etGoalTitle);

        setupUI(findViewById(R.id.parentCreateOwnGoal));
    }

    public void setupUI(View view) {
        if (!(view instanceof TextInputEditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(CreateOwnGoal.this);
                    return false;
                }
            });
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    private static boolean isBrokenSamsungDevice() {
        return (Build.MANUFACTURER.equalsIgnoreCase("samsung")
                && isBetweenAndroidVersions(
                Build.VERSION_CODES.LOLLIPOP,
                Build.VERSION_CODES.LOLLIPOP_MR1));
    }

    private static boolean isBetweenAndroidVersions(int min, int max) {
        return Build.VERSION.SDK_INT >= min && Build.VERSION.SDK_INT <= max;
    }

    public void DateToCompletePicker(View v) /*throws ParseException*/ {
        final DatePickerDialog[] picker = new DatePickerDialog[1];
//        final TextInputEditText eText;
//        eText= findViewById(R.id.pickerDateToComplete);
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        Context context = CreateOwnGoal.this;
        if (isBrokenSamsungDevice()) {
            context = new ContextThemeWrapper(CreateOwnGoal.this, android.R.style.Theme_Holo_Light_Dialog);
        }
        picker[0] = new DatePickerDialog(context, (view, year1, monthOfYear, dayOfMonth) -> {
            etDateToComplete.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
//            setDate( dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1);
        }, year, month, day);
        picker[0].getDatePicker().setMinDate(new Date().getTime());
        picker[0].show();
    }

    public void optionTrackOnclick(View view) {
        String[] optionsTrack = {"None", "Track Everyday (Yes or No)"};

        new AlertDialog.Builder(this)
            .setSingleChoiceItems(optionsTrack, 0, null)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                    // Do something useful withe the position of the selected radio button
                    etOptionTrack.setText(optionsTrack[selectedPosition]);
                }
            })
            .show();
    }

    public void btnCreateOwnGoalOnclick(View view) {
        if (TextUtils.isEmpty(etGoalTitle.getText().toString())) {
//            etGoalTitle.setError(err);
            Toast.makeText(this, "Goal Title is required.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(etDateToComplete.getText().toString())) {
            Toast.makeText(this, "Date to complete is required.", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(etOptionTrack.getText().toString())) {
            Toast.makeText(this, "Methods to track is required.", Toast.LENGTH_SHORT).show();
        }
        else {
            //save to db
            Toast.makeText(this, "Goal created successfully.", Toast.LENGTH_SHORT).show();
        }
    }

//    public void setDate(String date) {
//        mDate = date;
//    }
}
