package xyz.growsome.growsome.Ingresos;


import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
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

import xyz.growsome.growsome.DBTables.TableCategorias;
import xyz.growsome.growsome.DBTables.TableIngresos;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.CustomAdapter;
import xyz.growsome.growsome.Utils.DBHelper;
import xyz.growsome.growsome.Utils.ItemData;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngresosMainFragment extends Fragment {

    DBHelper dbHelper;
    ArrayList<ItemData> lIngresositems;
    ListView listView;

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

    }

    /* Esta cosa es del List Fragment*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.title_fragment_ingresos);

        View view = inflater.inflate(R.layout.fragment_ingresos_main, container, false);

        dbHelper = new DBHelper(getActivity());

        Cursor cursor = dbHelper.selectQuery("select " +
                "a." + TableIngresos.COL_ICOD + ", " +
                "a." + TableIngresos.COL_NOMBRE + ", " +
                "a." + TableIngresos.COL_MONTO + ", " +
                "b." + TableCategorias.COL_COLOR + " " +
                "from " + TableIngresos.TABLE_NAME + " a " +
                "inner join " + TableCategorias.TABLE_NAME + " b " +
                "on a." + TableIngresos.COL_ICODCAT + " = " + "b." + TableCategorias.COL_ICOD);

        lIngresositems = new ArrayList<>();

        listView=(ListView)view.findViewById(R.id.list);

        try
        {
            while (cursor.moveToNext())
            {
                ItemData itemData = new ItemData(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));

                lIngresositems.add(itemData);
            }
        }
        finally
        {
            cursor.close();
        }

        CustomAdapter adapter = new CustomAdapter(lIngresositems,inflater.getContext());


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ItemData itemData = lIngresositems.get(position);
                Bundle bundle = new Bundle();
                bundle.putLong("id", itemData.getId());
                IngresosReadFragment fragment = new IngresosReadFragment();
                fragment.setArguments(bundle);
                ((Main)getActivity()).setFragment(fragment, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }
        });

        ((Main)getActivity()).showFAB(false);
        registerForContextMenu(listView);

        return view;
        //return inflater.inflate(R.layout.fragment_ingresos_main, container, false); //original
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
        bundle.putLong("id", lIngresositems.get(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position).getId());
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
