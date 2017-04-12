package xyz.growsome.growsome.Ingresos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.growsome.growsome.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngresosAddFragment extends Fragment {


    Pago pago = new Pago();

    public IngresosAddFragment() {
        // Required empty public constructor
        pago.setNombre("Bank");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingresos_add, container, false);
    }

}
