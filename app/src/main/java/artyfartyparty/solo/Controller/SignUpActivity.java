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

        final EditText nameEditText = (EditText)findViewById(R.id.nameEditText);
        final EditText emailEditText = (EditText)findViewById(R.id.emailEditText);
        final EditText addressEditText = (EditText)findViewById(R.id.addressEditText);
        final EditText phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        final EditText password1EditText = (EditText)findViewById(R.id.password1EditText);
        final EditText password2EditText = (EditText)findViewById(R.id.password2EditText);
        Button signUpButton = (Button)findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = password1EditText.getText().toString();
                String password2 = password2EditText.getText().toString();

                User user = null;

                if (password.compareTo(password2) == 0){
                    String name = nameEditText.getText().toString();
                    String uniMail = emailEditText.getText().toString();
                    String address = addressEditText.getText().toString();
                    String phone = phoneEditText.getText().toString();
                    addUser(name, uniMail, address, phone, password);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Passwords don't match",
                            Toast.LENGTH_LONG).show();
                }


                Intent startIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(startIntent);
            }
        });
    }

    private void addUser(String name, String uniMail, String address, String phoneNumber, String password) {
        String url = "https://solo-web-service.herokuapp.com/users/add";
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            if(TextUtils.isEmpty(name)) {
                //email is empty
                Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if(TextUtils.isEmpty(uniMail)) {
                //password is empty
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if(TextUtils.isEmpty(address)) {
                //email is empty
                Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            if(TextUtils.isEmpty(phoneNumber)) {
                //password is empty
                Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                //stopping the function execution further
                return;
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, "{\"name\":\"" + name + "\", " +
                    "\"uniMail\":\"" + uniMail + "\", " +
                    "\"address\":\"" + address + "\", " +
                    "\"phoneNumber\":\"" + phoneNumber + "\"," +
                    "\"password\": \"" + password + "\"}");

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
                            Toast.makeText(getApplicationContext(), "Sign up successful!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.v("Tókst", "Villa!");
                    //alertUserAboutError();
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
                    List<User> users = userData.getAll();
                    Log.v("users", "" + users.size());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });
                }
            });
            Toast.makeText(this, "Added user", Toast.LENGTH_LONG).show();
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
