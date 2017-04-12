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
public class GastosEditFragment extends Fragment {


    public GastosEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gastos_edit, container, false);
    }

}
