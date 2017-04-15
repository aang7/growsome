package xyz.growsome.growsome;


import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import xyz.growsome.growsome.Ingresos.IngresosAddFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngresosMainFragment extends ListFragment {


    public IngresosMainFragment() {
        // Required empty public constructor
    }

    /*@Override
    public void onActivityCreated(Bundle savedState) {

    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); //to display additional menu items
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());

    }

    /* Esta cosa es del List Fragment*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.title_fragment_ingresos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.dummy));

        setListAdapter(adapter);
        //registerForContextMenu(getListAdapter());

        return super.onCreateView(inflater, container, savedInstanceState); //para ejemplos rapidos (muestra a vega)
        //return inflater.inflate(R.layout.fragment_ingresos_main, container, false); //original
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_menu, menu);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Item Operations");
        menu.add(0, v.getId(), 0, "Edit Item");
        menu.add(0, v.getId(), 0, "Delete Item");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_add:
                /*Intent intent = new Intent(getActivity(), Settings.class); //aqui me quede
                startActivity(intent);*/
                /* Fragment stuff */
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Layout a remplazar, instancia del fragmento, tag opcional
                ft.replace(R.id.content_frame, new IngresosAddFragment(), "tag");
                ft.addToBackStack("tag");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        if (item.getTitle() == "Edit Item") {
        } else if (item.getTitle() == "Delete Item") {

            return true;
        }
        return super.onContextItemSelected(item);
    }

}
