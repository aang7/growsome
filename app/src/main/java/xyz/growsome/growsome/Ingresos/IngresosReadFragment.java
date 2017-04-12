package xyz.growsome.growsome.Ingresos;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.growsome.growsome.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngresosReadFragment extends Fragment {


    public IngresosReadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingresos_read, container, false);
    }

}
