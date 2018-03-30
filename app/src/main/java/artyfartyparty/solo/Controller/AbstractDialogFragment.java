package artyfartyparty.solo.Controller;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public abstract class AbstractDialogFragment extends DialogFragment {

    protected abstract void processFragmentArguments();

    @Override
    public abstract Dialog onCreateDialog(Bundle savedInstanceState);

    protected abstract void saveInstanceState(Bundle outState);

    protected abstract void restoreInstanceState(Bundle savedInstanceState);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveInstanceState(outState);
    }
}
