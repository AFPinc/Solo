package artyfartyparty.solo.Controller;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Date;

import artyfartyparty.solo.R;

import static android.R.layout.simple_list_item_1;

/**
 * Created by valas on 21.2.2018.
 */

public class AddRideActivity extends android.support.v4.app.Fragment {

    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_addride, container, false);



        Spinner fromSpinner = (Spinner) view.findViewById(R.id.fromSpinner);
        Spinner toSpinner = (Spinner) view.findViewById(R.id.toSpinner);
        Spinner stopoverSpinner = (Spinner) view.findViewById(R.id.stopoverSpinner);
        Button stopoverButton = (Button)view.findViewById(R.id.stopoverButton);
        Button addButton = (Button)view.findViewById(R.id.addButton);
        Button fromAtButton = (Button)view.findViewById(R.id.fromAtButton);

        fromAtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(new Date());
                dialog.setTargetFragment(AddRideActivity.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        Button toAtButton = (Button)view.findViewById(R.id.toAtButton);



        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(getActivity(),
                simple_list_item_1, getResources().getStringArray(R.array.names));
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(getActivity(),
                simple_list_item_1, getResources().getStringArray(R.array.names));
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        ArrayAdapter<String> stopoverAdapter = new ArrayAdapter<String>(getActivity(),
                simple_list_item_1, getResources().getStringArray(R.array.names));
        stopoverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stopoverSpinner.setAdapter(stopoverAdapter);

        return view;
    }

}
