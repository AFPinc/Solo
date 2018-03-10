package artyfartyparty.solo.Controller;

import android.support.v4.app.Fragment;

/**
 * Created by valas on 3/10/2018.
 */

public class SearchActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new SearchFragment();
    }
}
