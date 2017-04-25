package xyz.growsome.growsome.Ingresos;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;
import xyz.growsome.growsome.DBTables.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngresosAddFragment extends Fragment
{
    DBHelper dbHelper;
    Spinner spinner_type;
    Spinner spinner_cat;
    EditText et_nombre;
    EditText et_descripcion;
    EditText et_monto;
    Button btn_save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dbHelper = new DBHelper(getActivity());

        ((Main)getActivity()).showDrawer(false);

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_ingresos_add, container, false);

        et_nombre = (EditText) view.findViewById(R.id.editText_nombre);
        et_descripcion = (EditText) view.findViewById(R.id.editText_descripcion);
        et_monto = (EditText) view.findViewById(R.id.editText_monto);
        btn_save = (Button) view.findViewById(R.id.btn_guardar);
        spinner_type = (Spinner) view.findViewById(R.id.ingresos_tipos);
        spinner_cat = (Spinner) view.findViewById(R.id.ingresos_cat);

        btn_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(saveIngreso())
                {
                    getFragmentManager().popBackStack();
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
                }
            }
        });

        setup();

        ((Main)getActivity()).showFAB(false);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.general_group, false); //hidding main items
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean setup()
    {
        Cursor cursorTipos = dbHelper.selectQuery(TableTipoIngreso.SELECT_ALL);
        Cursor cursorCats = dbHelper.selectQuery(TableCategorias.SELECT_ALL);

        List<String> tipos = new ArrayList<>();
        List<String> cats = new ArrayList<>();

        try
        {
            while (cursorTipos.moveToNext())
            {
                tipos.add(cursorTipos.getString(TableTipoIngreso.COL_DESC_ID));
            }
        }
        finally
        {
            cursorTipos.close();
        }

        try
        {
            while (cursorCats.moveToNext())
            {
                cats.add(cursorCats.getString(TableCategorias.COL_NOMBRE_ID));
            }
        }
        finally
        {
            cursorCats.close();
        }

        ArrayAdapter<String> adapterTipos = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tipos);
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(adapterTipos);

        ArrayAdapter<String> adapterCats = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cats);
        adapterCats.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_cat.setAdapter(adapterCats);

        return true;
    }

    public boolean saveIngreso()
    {
        try
        {
            String tipo = (String) spinner_type.getSelectedItem();
            String cat = (String) spinner_cat.getSelectedItem();
            int catid = TableCategorias.getCategoriaID(dbHelper.getReadableDatabase(), cat);
            String desc = et_descripcion.getText().toString();
            String nombre = et_nombre.getText().toString();
            double monto = Double.parseDouble(et_monto.getText().toString());

            if(tipo.equals("Salario")) //tengo que generalizar esto
            {
                Salario salario = new Salario(TableUsuarios.getUserID(dbHelper.getReadableDatabase()), catid, desc, nombre, monto, new Date());
                TableIngresos.insert(dbHelper.getWritableDatabase(), salario);
            }
            else if(tipo.equals("Pago"))
            {
                Pago pago = new Pago(TableUsuarios.getUserID(dbHelper.getReadableDatabase()), catid, desc, nombre, monto, new Date());
                TableIngresos.insert(dbHelper.getWritableDatabase(), pago);
            }
        }
        catch (Exception ex)
        {
            return false;
        }

        return  true;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ((Main)getActivity()).showDrawer(true); //enable drawer again
    }
}
