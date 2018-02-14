package artyfartyparty.solo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        EditText emailEditText = (EditText)findViewById(R.id.emailEditText);
        EditText passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        Button loginButton = (Button)findViewById(R.id.logInButton);
        Button registerButton = (Button)findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Congrats!",
                        Toast.LENGTH_LONG).show();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(startIntent);
            }
        });
    }

}
