package xyz.growsome.growsome.Ingresos;


import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.database.Cursor;
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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import xyz.growsome.growsome.DBTables.TableIngresos;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngresosMainFragment extends ListFragment {

    DBHelper dbHelper;
    List<String> lIngresosNombre;
    List<Long> lIngresosId;

    public IngresosMainFragment() {
        // Required empty public constructor
    }

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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Bundle bundle = new Bundle();
        bundle.putLong("id", lIngresosId.get(position));
        IngresosReadFragment fragment = new IngresosReadFragment();
        fragment.setArguments(bundle);
        ((Main)getActivity()).setFragment(fragment, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
    }


    /* Esta cosa es del List Fragment*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle(R.string.title_fragment_ingresos);


        /*Se muestra como generar un objeto pago, se inserta con la clase tabla correspondiente,
        * se usa el dbhelper para traer el cursor con la informacion, se recorre y se agrega la
        * info a una lista que alimenta el array adapter para mostrar la info.
        * este pedo se reinsertara cada que se cree el fragmento*/

        dbHelper = new DBHelper(getActivity());

        /*Pago p = new Pago(TableUsuarios.getUserID(dbHelper.getReadableDatabase()), "TEST PAGO", "TEST",
                21.3, new Date());

        Salario s = new Salario(TableUsuarios.getUserID(dbHelper.getReadableDatabase()), "TEST Salario", "TEST2",
                23.3, new Date());

        TableIngresos.insert(dbHelper.getWritableDatabase(), s);
        TableIngresos.insert(dbHelper.getWritableDatabase(), p);*/

        Cursor cursor = dbHelper.selectQuery(TableIngresos.SELECT_ALL);

        lIngresosNombre = new ArrayList<>();
        lIngresosId = new ArrayList<>();

        try
        {
            while (cursor.moveToNext())
            {
                lIngresosNombre.add(cursor.getString(TableIngresos.COL_NOMBRE_ID));
                lIngresosId.add(cursor.getLong(TableIngresos.COL_ICOD_ID));
            }
        }
        finally
        {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                inflater.getContext(),
                android.R.layout.simple_list_item_1,
                lIngresosNombre);

        setListAdapter(adapter);

        ((Main)getActivity()).showFAB(false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_menu, menu);
        menu.setGroupVisible(R.id.general_group, false); //hidding main items
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
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
                ((Main) getActivity()).setFragment(new IngresosAddFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        Bundle bundle = new Bundle();
        bundle.putLong("id", lIngresosId.get(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position));
        IngresosEditFragment fragment = new IngresosEditFragment();
        fragment.setArguments(bundle);
        if (item.getTitle() == "Edit Item")
        {
            ((Main)getActivity()).setFragment(fragment, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        else if (item.getTitle() == "Delete Item")
        {
            //TODO: Delete item
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
