package xyz.growsome.growsome.Categorias;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import xyz.growsome.growsome.DBTables.TableCategorias;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriaEditFragment extends Fragment {


    DBHelper dbHelper;
    EditText etName;
    EditText etDesc;
    Button btnSave;

    long iCodCategory;

    public CategoriaEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new DBHelper(getActivity());

        View view = inflater.inflate(R.layout.fragment_categoria_edit, container, false);

        etName = (EditText) view.findViewById(R.id.categorias_field_name);
        etDesc = (EditText) view.findViewById(R.id.categorias_field_desc);
        btnSave = (Button) view.findViewById(R.id.categorias_btnSave);

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(editCategory())
                    getFragmentManager().popBackStack();
                else
                    Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
            }
        });

        if (!readCategory())
            Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();

        return view;
    }


    public boolean readCategory()
    {
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
        } finally {
            cursor.close();
        }

        return true;
    }

    public boolean editCategory() {

        try {
            String nombre = etName.getText().toString();
            String desc = etDesc.getText().toString();

            Integer id = TableCategorias.getCatID(dbHelper.getReadableDatabase(), nombre);

            if(id != 0)
            {
                Toast.makeText(getActivity(), R.string.error_repeated_category, Toast.LENGTH_SHORT).show();
                return false;
            }

            Categoria categoria = new Categoria( 0,
                    TableCategorias.getUserID(dbHelper.getReadableDatabase()),
                    nombre,
                    "FFFFFFFF",
                    desc);

            TableCategorias.update(dbHelper.getWritableDatabase(), categoria, iCodCategory);

        }catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }


        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cancel_menu, menu);
        menu.setGroupVisible(R.id.general_group, false); //hidding main items
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_cancel:
                getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ((Main)getActivity()).showDrawer(true);
    }

}
