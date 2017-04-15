package xyz.growsome.growsome;


import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import xyz.growsome.growsome.Gastos.GastosAddFragment;
import xyz.growsome.growsome.Ingresos.IngresosAddFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class GastosMainFragment extends ListFragment {


    public GastosMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //to display additional menu items
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.title_fragment_gastos);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.stores));
        //return inflater.inflate(R.layout.fragment_gastos_main, container, false); //original
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState); //para ejemplos rapidos (muestra a vega)
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_add:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new GastosAddFragment(), "GastosMain2Add");
                ft.addToBackStack("GastosMain2Add");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);

    }

}
