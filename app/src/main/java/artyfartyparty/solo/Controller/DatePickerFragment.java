package artyfartyparty.solo.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import artyfartyparty.solo.R;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Fragment class that controls the date picker
 */

public class DatePickerFragment extends AbstractDialogFragment {

    public static final String DIALOG_TAG = DatePickerFragment.class.getName();

    public static final String RESULT_DATE_TIME = "result." + DatePickerFragment.class.getName();
    public static final String TIME = "Time";
    public static final String DATE = "Date";
    public static final String BOTH = "BOTH";

    private static final String TAG = DatePickerFragment.class.getSimpleName();

    private Date mDate;
    private String mDateType;
    private String mDateOrTimeChoice;

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private Spinner dateTimeSpinner;
    private ResultHandler mResultHandler;

    public interface ResultHandler extends Serializable {
        void setDate(Date result);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        processFragmentArguments();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        dateTimeSpinner = view.findViewById(R.id.spinner_date_time_choice);
        mDatePicker = view.findViewById(R.id.date_picker);
        mTimePicker = view.findViewById(R.id.time_picker);
        mDatePicker.init(year, month, day, null);

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteOfHour = calendar.get(Calendar.MINUTE);

        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour);

        configureDateTimeSpinner(dateTimeSpinner);
        return new android.app.AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.choose_date_or_time)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (getTargetFragment() == null && mResultHandler == null) {
                        } else {
                            //noinspection deprecation
                            mDate = computeDateFromComponents(
                                    mDatePicker.getYear(),
                                    mDatePicker.getMonth(),
                                    mDatePicker.getDayOfMonth(),
                                    mTimePicker.getCurrentHour(),
                                    mTimePicker.getCurrentMinute());
                            if (mResultHandler == null) {
                                Intent intent = new Intent();
                                intent.putExtra(RESULT_DATE_TIME, mDate);
                                getTargetFragment().onActivityResult(
                                        getTargetRequestCode(),
                                        Activity.RESULT_OK,
                                        intent);
                            } else {
                                mResultHandler.setDate(mDate);
                            }
                        }
                    }
                })
                .create();
    }

    @Override
    protected void saveInstanceState(Bundle outState) {

    }

    private void configureDateTimeSpinner(final Spinner dateTimeSpinner) {
        List<String> choices = new ArrayList<>();
        if (DATE == mDateOrTimeChoice) {
            choices.add(computeChoice(DATE));
        } else if (TIME == mDateOrTimeChoice) {
            choices.add(computeChoice(TIME));
        } else {
            choices.add(computeChoice(TIME));
            choices.add(computeChoice(DATE));
        }
        dateTimeSpinner.setAdapter(
                new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        choices));
        dateTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = (String) dateTimeSpinner.getAdapter().getItem(position);
                if (choice.equalsIgnoreCase(computeChoice(DATE))) {
                    mDatePicker.setVisibility(View.VISIBLE);
                    mTimePicker.setVisibility(View.GONE);
                } else {
                    mTimePicker.setVisibility(View.VISIBLE);
                    mDatePicker.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (mDateOrTimeChoice == null) {
            dateTimeSpinner.setSelection(choices.indexOf(computeChoice(TIME)));
        } else {
            dateTimeSpinner.setSelection(choices.indexOf(computeChoice(mDateOrTimeChoice)));
        }
    }

    private String computeChoice(String baseChoice) {
        return mDateType + " " + baseChoice;
    }

    @Override
    protected void processFragmentArguments() {
        mDate = (Date) getArguments().getSerializable(FactoryFragment.FRAG_ARG_DATE);
        if (mDate == null) {
            throw new RuntimeException("Fragment argument (" + FactoryFragment.FRAG_ARG_DATE + ") cannot be null!");
        }
        mDateType = (String) getArguments().getSerializable(FactoryFragment.FRAG_ARG_DATE_TYPE);
        if (mDateType == null || mDateType.isEmpty()) {
            throw new RuntimeException("Fragment argument (" + FactoryFragment.FRAG_ARG_DATE_TYPE + ") cannot be null!");
        }

        mDateOrTimeChoice = (String) getArguments().getSerializable(FactoryFragment.FRAG_ARG_DATETIME_PICKER_CHOICE);

        mResultHandler = (ResultHandler)getArguments().getSerializable(FactoryFragment.FRAG_ARG_DATETIME_PICKER_RESULT_HANDLER);
    }

    private Date computeDateFromComponents(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour) {
        final String METHOD = "computeDateFromComponents(" + year + "," + monthOfYear + "," + dayOfMonth + "," + hourOfDay + "," + minuteOfHour + ")";
        Calendar changedDateCalendar = Calendar.getInstance();
        changedDateCalendar.set(Calendar.YEAR, year);
        changedDateCalendar.set(Calendar.MONTH, monthOfYear);
        changedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        changedDateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        changedDateCalendar.set(Calendar.MINUTE, minuteOfHour);
        changedDateCalendar.set(Calendar.SECOND, 0);
        changedDateCalendar.set(Calendar.MILLISECOND, 0);
        Date ret = changedDateCalendar.getTime();
        Log.d(TAG, METHOD + "Returning date: " + ret);
        return ret;
    }
}
