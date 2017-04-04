package xyz.growsome.growsome;


import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class GastosMainFragment extends ListFragment {


    public GastosMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.gastos_fragment_title);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.stores));
        //return inflater.inflate(R.layout.fragment_gastos_main, container, false); //original
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState); //para ejemplos rapidos (muestra a vega)
    }

}
