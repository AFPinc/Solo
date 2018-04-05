package artyfartyparty.solo.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import artyfartyparty.solo.Model.User;
import artyfartyparty.solo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that controls signing up (new account)
 */

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText nameEditText = findViewById(R.id.nameEditText);
        final EditText emailEditText = findViewById(R.id.emailEditText);
        final EditText addressEditText = findViewById(R.id.addressEditText);
        final EditText phoneEditText = findViewById(R.id.phoneEditText);
        final EditText password1EditText = findViewById(R.id.password1EditText);
        final EditText password2EditText = findViewById(R.id.password2EditText);
        Button signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = password1EditText.getText().toString();
                String password2 = password2EditText.getText().toString();

                if (password.compareTo(password2) == 0){
                    String name = nameEditText.getText().toString();
                    String uniMail = emailEditText.getText().toString();
                    String address = addressEditText.getText().toString();
                    String phone = phoneEditText.getText().toString();
                    addUser(name, uniMail, address, Integer.parseInt(phone), password);

                    Intent startIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(startIntent);
                }
                else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.passwords_not_match),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addUser(String name, String uniMail, String address, int phoneNumber, String password) {
        String url = getResources().getString(R.string.add_user);
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            User user = new User();
            user.setName(name);
            user.setUniMail(uniMail);
            user.setAddress(address);
            user.setPhoneNumber(phoneNumber);
            user.setPassword(password);

            if (password.length() < 4) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.password_empty),
                        Toast.LENGTH_LONG).show();
            }
            else if (!password.matches("[a-zA-Z.? ][a-zA-Z0-9.? ]*")) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.password_invalid),
                        Toast.LENGTH_LONG).show();
            }

            if(TextUtils.isEmpty(name)) {
                Toast.makeText(this, getResources().getString(R.string.name_missing), Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(uniMail)) {
                Toast.makeText(this, getResources().getString(R.string.uni_mail_missing), Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(address)) {
                Toast.makeText(this, getResources().getString(R.string.address_missing), Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty("" + phoneNumber)) {
                Toast.makeText(this, getResources().getString(R.string.phone_number_missing), Toast.LENGTH_SHORT).show();
                return;
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String json = Parser.parseUserToJSON(user);
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_success),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    User user = null;
                    try {
                        user = Parser.parseUserData(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    UserData userData = UserDataDB.get(getApplicationContext()).getUserData();
                    userData.addUser(user);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
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
