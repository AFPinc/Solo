package artyfartyparty.solo.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import artyfartyparty.solo.R;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that controlls adding a trip
 */

public class AddRideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtrip);

        Spinner fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
        Spinner toSpinner = (Spinner) findViewById(R.id.toSpinner);
        Spinner stopoverSpinner = (Spinner) findViewById(R.id.stopoverSpinner);
        Button stopoverButton = (Button)findViewById(R.id.stopoverButton);
        Button addButton = (Button)findViewById(R.id.addButton);

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(AddRideActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        ArrayAdapter<String> toAdapter = new ArrayAdapter<String>(AddRideActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        ArrayAdapter<String> stopoverAdapter = new ArrayAdapter<String>(AddRideActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        stopoverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stopoverSpinner.setAdapter(stopoverAdapter);

    }

}
