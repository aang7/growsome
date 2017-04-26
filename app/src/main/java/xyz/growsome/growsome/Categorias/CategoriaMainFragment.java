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

import java.util.ArrayList;

import xyz.growsome.growsome.DBTables.TableCategorias;
import xyz.growsome.growsome.Ingresos.IngresosEditFragment;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.CustomAdapter;
import xyz.growsome.growsome.Utils.DBHelper;
import xyz.growsome.growsome.Utils.ItemData;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriaMainFragment extends Fragment {

    DBHelper dbHelper;
    ArrayList<ItemData> lCategoriaitems;
    ListView listView;

    public CategoriaMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.title_fragment_categorias);

        View view = inflater.inflate(R.layout.fragment_categoria_main, container, false);

        dbHelper = new DBHelper(getActivity());

        Cursor cursor = dbHelper.selectQuery(TableCategorias.SELECT_ALL);

        lCategoriaitems = new ArrayList<>();

        listView=(ListView)view.findViewById(R.id.list);

        try
        {
            while (cursor.moveToNext())
            {
                ItemData itemData = new ItemData(
                        cursor.getLong(TableCategorias.COL_ICOD_ID),
                        cursor.getString(TableCategorias.COL_NOMBRE_ID),
                        cursor.getString(TableCategorias.COL_DESC_ID),
                        cursor.getString(TableCategorias.COL_NOMBRE_ID));

                lCategoriaitems.add(itemData);
            }
        } finally {
            cursor.close();
        }

        CustomAdapter adapter = new CustomAdapter(lCategoriaitems, inflater.getContext());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ItemData itemData = lCategoriaitems.get(position);
                Bundle bundle = new Bundle();
                bundle.putLong("id", itemData.getId());
                CategoriaReadFragment fragment = new CategoriaReadFragment();
                fragment.setArguments(bundle);
                ((Main)getActivity()).setFragment(fragment, true, FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            }
        });

        ((Main)getActivity()).showFAB(false);
        registerForContextMenu(listView);

        return view;
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
                ((Main) getActivity()).setFragment(new CategoriaAddFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        Bundle bundle = new Bundle();
        bundle.putLong("id", lCategoriaitems.get(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position).getId());
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
