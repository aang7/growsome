package xyz.growsome.growsome.Gastos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.growsome.growsome.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GastosReadFragment extends Fragment {


    public GastosReadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gastos_read, container, false);
    }

}
