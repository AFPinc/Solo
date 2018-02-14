package artyfartyparty.solo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText nameEditText = (EditText)findViewById(R.id.nameEditText);
        EditText emailEditText = (EditText)findViewById(R.id.emailEditText);
        EditText addressEditText = (EditText)findViewById(R.id.addressEditText);
        EditText phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        EditText password1EditText = (EditText)findViewById(R.id.password1EditText);
        EditText password2EditText = (EditText)findViewById(R.id.password2EditText);
        Button signUpButton = (Button)findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Congrats!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
