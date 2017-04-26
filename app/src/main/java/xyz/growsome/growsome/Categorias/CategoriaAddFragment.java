package xyz.growsome.growsome.Categorias;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import xyz.growsome.growsome.DBTables.TableCategorias;
import xyz.growsome.growsome.DBTables.TableUsuarios;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriaAddFragment extends Fragment
{

    DBHelper dbHelper;
    EditText etName;
    EditText etDesc;
    Button btnSave;

    public CategoriaAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dbHelper = new DBHelper(getActivity());

        //((Main)getActivity()).showDrawer(false);

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_categoria_add, container, false);

        etName = (EditText) view.findViewById(R.id.categorias_add_field_name);
        etDesc = (EditText) view.findViewById(R.id.categorias_add_field_desc);
        btnSave = (Button) view.findViewById(R.id.categorias_add_btnSave);

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(saveIngreso())
                {
                    getFragmentManager().popBackStack();
                }
            }
        });

        return view;
    }

    public boolean saveIngreso()
    {
        try
        {
            String nombre = etName.getText().toString();
            String desc = etDesc.getText().toString();

            Integer id = TableCategorias.getCatID(dbHelper.getReadableDatabase(), nombre);

            if(id != 0)
            {
                Toast.makeText(getActivity(), R.string.error_repeated_category, Toast.LENGTH_SHORT).show();
                return false;
            }

            Categoria categoria = new Categoria(0, TableUsuarios.getUserID(dbHelper.getReadableDatabase()), nombre, "FFFFFFFF", desc);

            TableCategorias.insert(dbHelper.getWritableDatabase(), categoria);
        }
        catch (Exception ex)
        {
            Log.d("Error", ex.toString());
            return false;
        }

        return true;
    }

}
