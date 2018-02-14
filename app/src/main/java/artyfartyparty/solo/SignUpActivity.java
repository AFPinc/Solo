package artyfartyparty.solo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

                addUser();

                Toast.makeText(getApplicationContext(), "Congrats!",
                        Toast.LENGTH_LONG).show();
            }
        });
        getUser();
    }

    private void addUser() {
        String url = "https://solo-web-service.herokuapp.com/users/add";
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, "{\"name\":\"Kjáni\", " +
                    "\"uniMail\":\"kjk69\", " +
                    "\"address\":\"Jónsgata 55\", " +
                    "\"phoneNumber\":\"9876543\"," +
                    "\"password\": \"egerbestur\"}");

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
                        }
                    });
                    Log.v("Tókst", "Villa!");
                    //alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    Log.v("Tókst", response.body().string());
                }
            });
            Toast.makeText(this, "Added user", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
        }
    }
    private void getUser() {
        String url = "https://solo-web-service.herokuapp.com/users/sth301";
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    //alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                    try {
                        String jsonData = response.body().string();
                        Log.v("DRASL1", jsonData);
                    } catch (IOException e) {
                        Log.e("DRASL2", "Exception caught: ", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        String bla = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo!= null && networkInfo.isConnected()) isAvailable = true;
        return isAvailable;
    }
}
