package artyfartyparty.solo.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class MyRideActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        Fragment f = new MyRideFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("userId", getIntent().getLongExtra("userId", -1));
        bundle.putLong("rideId", getIntent().getLongExtra("rideId", -1));
        f.setArguments(bundle);
        return f;
    }
}
