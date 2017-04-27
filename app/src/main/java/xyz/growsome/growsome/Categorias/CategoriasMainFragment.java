package xyz.growsome.growsome.Categorias;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
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
import xyz.growsome.growsome.Ingresos.IngresosEditFragment;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.CustomAdapter;
import xyz.growsome.growsome.Utils.DBHelper;
import xyz.growsome.growsome.Utils.ItemData;

public class CategoriasMainFragment extends Fragment
{
    DBHelper dbHelper;
    ArrayList<ItemData> lCategoriaitems;
    ListView listView;

    public CategoriasMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle(R.string.title_fragment_categorias);

        ((Main)getActivity()).showFAB(false);

        View view = inflater.inflate(R.layout.fragment_categoria_main, container, false);

        dbHelper = new DBHelper(getActivity());

        Cursor cursor = dbHelper.selectQuery(TableCategorias.SELECT_ALL);

        lCategoriaitems = new ArrayList<>();

        listView = (ListView)view.findViewById(R.id.list);

        try
        {
            while (cursor.moveToNext())
            {
                ItemData itemData = new ItemData(
                        cursor.getLong(TableCategorias.COL_ICOD_ID),
                        cursor.getString(TableCategorias.COL_NOMBRE_ID),
                        cursor.getString(TableCategorias.COL_DESC_ID),
                        cursor.getString(TableCategorias.COL_COLOR_ID));

                lCategoriaitems.add(itemData);
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
            return view;
        }
        finally {
            cursor.close();
        }

        CustomAdapter adapter = new CustomAdapter(lCategoriaitems, inflater.getContext());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ItemData itemData = lCategoriaitems.get(position);
                Bundle bundle = new Bundle();
                bundle.putLong("id", itemData.getId());
                CategoriasReadFragment fragment = new CategoriasReadFragment();
                fragment.setArguments(bundle);
                ((Main)getActivity()).setFragment(fragment, true, FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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
                ((Main) getActivity()).setFragment(new CategoriasAddFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        Bundle bundle = new Bundle();
        bundle.putLong("id", lCategoriaitems.get(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position).getId());

        if (item.getTitle() == "Edit Item")
        {
            CategoriasEditFragment fragment = new CategoriasEditFragment();
            fragment.setArguments(bundle);
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
