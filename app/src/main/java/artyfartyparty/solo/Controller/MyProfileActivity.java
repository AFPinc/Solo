package artyfartyparty.solo.Controller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ása Júlía on 21.3.2018.
 */

public class MyProfileActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        Fragment f = new MyProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("userId", getIntent().getLongExtra("userId", -1));
        f.setArguments(bundle);
        return f;
    }
}
