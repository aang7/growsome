package xyz.growsome.growsome.Categorias;


import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import xyz.growsome.growsome.DBTables.TableCategorias;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriaReadFragment extends Fragment {

    DBHelper dbHelper;
    EditText etName;
    EditText etDesc;
    long iCodCategory;

    public CategoriaReadFragment() {
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
        View view = inflater.inflate(R.layout.fragment_categoria_read, container, false);

        dbHelper = new DBHelper(getActivity());

        etName = (EditText) view.findViewById(R.id.categorias_field_name);
        etDesc = (EditText) view.findViewById(R.id.categorias_field_desc);

        if (!readCategory())
            Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();

        return view;
    }


    public boolean readCategory() {

        Bundle args = getArguments();
        iCodCategory = args.getLong("id");
        final String SELECT_COD_CATEGORY = "SELECT * FROM "+ TableCategorias.TABLE_NAME + " WHERE " + TableCategorias.COL_ICOD + " = " + iCodCategory;

        Cursor cursor = dbHelper.selectQuery(SELECT_COD_CATEGORY);

        try
        {
            if (cursor.moveToFirst())
            {
                etName.setText(cursor.getString(TableCategorias.COL_NOMBRE_ID));
                etDesc.setText(cursor.getString(TableCategorias.COL_DESC_ID));
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        finally {
            cursor.close();
        }

        return true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_menu, menu);
        menu.setGroupVisible(R.id.general_group, false); //hidding main items
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_edit:
                getFragmentManager().popBackStack();
                Bundle bundle = new Bundle();
                bundle.putLong("id", iCodCategory);
                CategoriaEditFragment fragment = new CategoriaEditFragment();
                fragment.setArguments(bundle);
                ((Main) getActivity()).setFragment(fragment, true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                return true;
            case R.id.action_cancel:
                getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
