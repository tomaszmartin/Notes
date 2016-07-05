package pl.codeinprogress.notes.ui;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

import pl.codeinprogress.notes.services.AlarmReceiver;
import pl.codeinprogress.notes.R;

/**
 * Created by tomaszmartin on 25.07.2015.
 */

public class AlarmDialogFragment extends DialogFragment implements DialogInterface.OnClickListener,
        View.OnClickListener {

    private TimePicker timePicker;
    private DatePicker datePicker;
    private Calendar calendar = Calendar.getInstance();
    private ToggleButton dateButton;
    private ToggleButton timeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Creating builder and view for dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.alarm_dialog, null);

        // Setup date and time pickers
        timePicker = (TimePicker) rootView.findViewById(R.id.time_picker);
        datePicker = (DatePicker) rootView.findViewById(R.id.date_picker);
        timePicker.setEnabled(true);
        timePicker.setIs24HourView(true);

        // Set up listener for buttons
        dateButton = (ToggleButton) rootView.findViewById(R.id.pick_date_btn);
        dateButton.setOnClickListener(this);
        timeButton = (ToggleButton) rootView.findViewById(R.id.pick_time_btn);
        timeButton.setOnClickListener(this);

        // Set the first visible set, button and picker
        dateButton.setChecked(true);
        datePicker.setVisibility(View.VISIBLE);
        timeButton.setChecked(false);
        timePicker.setVisibility(View.GONE);

        // Listeners does not work under Android 5.0
        // All values are being set at the click of the ok button
        timePicker.setOnTimeChangedListener(null);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), null);

        // Build the dialog
        builder.setView(rootView);
        builder.setPositiveButton(R.string.ok, this);
        builder.setNegativeButton(R.string.cancel, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            setAlarm();
        }
    }

    public void setAlarm() {
        // Set all values of calendar
        // For setting the alarm
        // Workaround for not working OnDateChangeListener and OnTimeChangeListener
        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

        // Set the alarm
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        alarmIntent.putExtra(DetailsActivity.NOTE_ID, getArguments().getString(DetailsActivity.NOTE_ID));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onClick(View v) {
        int id  = v.getId();
        switch (id) {
            case R.id.pick_date_btn:
                timePicker.setVisibility(View.GONE);
                datePicker.setVisibility(View.VISIBLE);
                timeButton.setChecked(false);
                break;
            case R.id.pick_time_btn:
                datePicker.setVisibility(View.GONE);
                timePicker.setVisibility(View.VISIBLE);
                dateButton.setChecked(false);
                break;
        }
    }
}
