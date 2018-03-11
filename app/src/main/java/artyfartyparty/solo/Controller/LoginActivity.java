package artyfartyparty.solo.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import artyfartyparty.solo.Model.User;
import artyfartyparty.solo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that controlls logging in
 */

import static artyfartyparty.solo.R.layout.activity_login;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that controls login activities
 */

public class LoginActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
        final EditText emailEditText = (EditText)findViewById(R.id.emailEditText);
        final EditText passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        Button loginButton = (Button)findViewById(R.id.logInButton);
        Button registerButton = (Button)findViewById(R.id.registerButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                validateUserPassword(username, password);
                Log.v("Logintest", "Byrja");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg=" ";
        switch (item.getItemId()) {
            case R.id.add_ride:
                startActivity(new Intent(getApplicationContext(), AddRideActivity.class));
                msg = "Add Ride";
                break;
            case R.id.search:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                msg = "Search";
                break;
            case R.id.settings:
                msg = "Settings";
                break;
            case R.id.about:
                msg = "About";
                break;
        }
        Toast.makeText(this, msg+ " Checked", Toast.LENGTH_LONG).show(); // kemur skilaboð í hvert skipti sem eh af þessum items er klikkað á
        return super.onOptionsItemSelected(item);
    }

    private void validateUserPassword(String username, final String password) {
        String url = "https://solo-web-service.herokuapp.com/users/" + username;
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            final Context context = this;

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    Log.v("Logintest", "Failure");
                    //alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    String msg = "";
                    String jsonData = response.body().string();
                    User user = null;
                    try {
                        user = Parser.parseUserData(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final String finalMsg = msg;
                    if (user == null || password.compareTo(user.getPassword()) != 0)
                    {
                        // msg = "Login failed";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Login failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        // msg ="Login successful";
                        Log.v("helo", "hæyæhæh");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent startIntent = new Intent(getApplicationContext(), AllRidesActivity.class);
                                startActivity(startIntent);
                            }
                        });
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo!= null && networkInfo.isConnected()) isAvailable = true;
        return isAvailable;
    }
}
