package artyfartyparty.solo.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import artyfartyparty.solo.Model.User;
import artyfartyparty.solo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static artyfartyparty.solo.R.layout.activity_login;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_login);

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
                        user = parseUserData(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String pw = user.getPassword();
                    if (user == null || password.compareTo(pw) != 0)
                    {
                        msg = "Login failed";
                    }
                    else {
                        msg ="Login successful";
                    }
                    final String finalMsg = msg;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, finalMsg, Toast.LENGTH_LONG).show();
                        }
                    });
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

    private User parseUserData(String jsonData) throws JSONException {
        JSONObject json = new JSONObject(jsonData);
        User user = new User();
        String idString = json.getString("id");
        int id = Integer.parseInt(idString);
        user.setId(id);
        user.setName(json.getString("name"));
        user.setAddress(json.getString("address"));
        user.setPassword(json.getString("password"));
        user.setPhoneNumber(Integer.parseInt(json.getString("phoneNumber")));
        user.setUniMail(json.getString("uniMail"));

        return user;
    }
}
