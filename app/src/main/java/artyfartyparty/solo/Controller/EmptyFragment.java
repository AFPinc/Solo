package artyfartyparty.solo.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import artyfartyparty.solo.R;

/**
 * Created by Ása Júlía on 29.3.2018.
 */

public class EmptyFragment extends Fragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_fragment,container,false);
    }
}
