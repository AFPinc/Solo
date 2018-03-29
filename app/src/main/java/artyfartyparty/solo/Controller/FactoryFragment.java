package artyfartyparty.solo.Controller;

import android.os.Bundle;

import java.util.Date;

public class FactoryFragment {
    public static final String FRAG_ARG_PREFIX = "fragment.argument.";
    public static final String FRAG_ARG_DATE = FRAG_ARG_PREFIX + Date.class.getName();
    public static final String FRAG_ARG_DATE_TYPE = FRAG_ARG_DATE + "Type";
    public static final String FRAG_ARG_DATETIME_PICKER_CHOICE = FRAG_ARG_PREFIX + "datetime.picker.choice";
    public static final String FRAG_ARG_DATETIME_PICKER_RESULT_HANDLER = FRAG_ARG_PREFIX + ".datetime.picker.result.handler";

    public static DatePickerFragment createDatePickerFragment(Date date, String dateType, String choice, DatePickerFragment.ResultHandler resultHandler) {
        DatePickerFragment ret = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(FRAG_ARG_DATE, date);
        args.putSerializable(FRAG_ARG_DATE_TYPE, dateType);
        args.putSerializable(FRAG_ARG_DATETIME_PICKER_CHOICE, choice);
        args.putSerializable(FRAG_ARG_DATETIME_PICKER_RESULT_HANDLER, resultHandler);
        ret.setArguments(args);
        return ret;
    }

    public static DatePickerFragment createDatePickerFragment(Date date, String dateType, String choice) {
        return createDatePickerFragment(date, dateType, choice, null);
    }
}
