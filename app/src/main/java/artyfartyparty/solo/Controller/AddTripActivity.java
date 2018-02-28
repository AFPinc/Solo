package artyfartyparty.solo.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.sql.Array;

import artyfartyparty.solo.R;

/**
 * Created by valas on 21.2.2018.
 */

public class AddTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtrip);

        Spinner fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
        Spinner toSpinner = (Spinner) findViewById(R.id.toSpinner);
        Spinner stopoverSpinner = (Spinner) findViewById(R.id.stopoverSpinner);
        Button stopoverButton = (Button)findViewById(R.id.stopoverButton);
        Button addButton = (Button)findViewById(R.id.addButton);

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(AddTripActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(AddTripActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        ArrayAdapter<String> stopoverAdapter = new ArrayAdapter<String>(AddTripActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        stopoverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stopoverSpinner.setAdapter(stopoverAdapter);

    }

}
