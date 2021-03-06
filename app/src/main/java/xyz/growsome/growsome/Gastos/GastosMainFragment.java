package xyz.growsome.growsome.Gastos;

import android.app.FragmentTransaction;
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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import xyz.growsome.growsome.DBTables.TableCategorias;
import xyz.growsome.growsome.DBTables.TableGastos;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.CustomAdapter;
import xyz.growsome.growsome.Utils.DBHelper;
import xyz.growsome.growsome.Utils.ItemData;

public class GastosMainFragment extends Fragment {

    DBHelper dbHelper;
    ArrayList<ItemData> lGastositems;
    private ListView listView;

    public GastosMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle(R.string.title_fragment_gastos);

        ((Main)getActivity()).showFAB(false);

        View view = inflater.inflate(R.layout.fragment_gastos_main, container, false);

        dbHelper = new DBHelper(getActivity());

        Cursor cursor = dbHelper.selectQuery("select " +
                "a." + TableGastos.COL_ICOD + ", " +
                "a." + TableGastos.COL_NOMBRE + ", " +
                "a." + TableGastos.COL_COSTO + ", " +
                "b." + TableCategorias.COL_COLOR + " " +
                "from " + TableGastos.TABLE_NAME + " a " +
                "inner join " + TableCategorias.TABLE_NAME + " b " +
                "on a." + TableGastos.COL_ICODCAT + " = " + "b." + TableCategorias.COL_ICOD +
                " where a. " + TableGastos.COL_DELETE + " = 0");

        lGastositems = new ArrayList<>();

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

                lGastositems.add(itemData);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
            return view;
        }
        finally
        {
            cursor.close();
        }

        CustomAdapter adapter = new CustomAdapter(lGastositems,inflater.getContext());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ItemData itemData = lGastositems.get(position);
                Bundle bundle = new Bundle();
                bundle.putLong("id", itemData.getId());
                GastosReadFragment fragment = new GastosReadFragment();
                fragment.setArguments(bundle);
                ((Main)getActivity()).setFragment(fragment, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }
        });

        registerForContextMenu(listView);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_menu, menu);
        menu.setGroupVisible(R.id.general_group, false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Item Operations");
        menu.add(0, v.getId(), 0, "Edit Item");
        menu.add(0, v.getId(), 0, "Delete Item");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add:
                ((Main) getActivity()).setFragment(new GastosAddFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        long id = lGastositems.get(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position).getId();

        if (item.getTitle() == "Edit Item")
        {
            Bundle bundle = new Bundle();
            bundle.putLong("id", id);
            GastosEditFragment fragment = new GastosEditFragment();
            fragment.setArguments(bundle);
            ((Main)getActivity()).setFragment(fragment, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        else if (item.getTitle() == "Delete Item")
        {
            try
            {
                TableGastos.delete(dbHelper.getWritableDatabase(), id);
                GastosMainFragment fragment = new GastosMainFragment();
                ((Main)getActivity()).setFragment(fragment);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return false;
            }

            return true;
        }

        return super.onContextItemSelected(item);
    }
}

