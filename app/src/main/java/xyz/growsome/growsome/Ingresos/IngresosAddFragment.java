package xyz.growsome.growsome.Ingresos;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
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

        ((Main)getActivity()).setDrawerEnabled(false); //deshabilito drawer

        setHasOptionsMenu(true);//show new items
        //((Main)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View view = inflater.inflate(R.layout.fragment_ingresos_add, container, false);

        et_nombre = (EditText) view.findViewById(R.id.editText_nombre);
        et_descripcion = (EditText) view.findViewById(R.id.editText_descripcion);
        et_monto = (EditText) view.findViewById(R.id.editText_monto);
        btn_save = (Button) view.findViewById(R.id.btn_guardar);
        spinner_type = (Spinner) view.findViewById(R.id.ingresos_tipos);
        spinner_cat = (Spinner) view.findViewById(R.id.ingresos_cat);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(saveIngreso())
                {
                    Toast.makeText(getActivity(), "Ingreso Insertado", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Cursor cursor = dbHelper.selectQuery(TableTipoIngreso.SELECT_ALL);

        List<String> tipos = new ArrayList<>();

        try
        {
            while (cursor.moveToNext())
            {
                tipos.add(cursor.getString(TableTipoIngreso.COL_DESC_ID));
            }
        }
        finally
        {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tipos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(adapter);

        ((Main)getActivity()).hideFloatingActionButton();

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.operations_menu, menu);
        menu.setGroupVisible(R.id.general_group, false); //ocultando los settinggs
        //menu.removeItem(R.id.drawer_layout);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.operations_cancel_item:
                getFragmentManager().popBackStack();//Cerramos el fragment
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean saveIngreso()
    {

        try
        {
            String tipo = (String) spinner_type.getSelectedItem();
            String desc = et_descripcion.getText().toString();
            String nombre = et_nombre.getText().toString();
            double monto = Double.parseDouble(et_monto.getText().toString());

            if(tipo.equals("Salario"))
            {
                Salario salario = new Salario(TableUsuarios.getUserID(dbHelper.getReadableDatabase()), desc, nombre, monto, new Date());
                TableIngresos.insert(dbHelper.getWritableDatabase(), salario);
            }
            else if(tipo.equals("Pago"))
            {
                Pago pago = new Pago(TableUsuarios.getUserID(dbHelper.getReadableDatabase()), desc, nombre, monto, new Date());
                TableIngresos.insert(dbHelper.getWritableDatabase(), pago);
            }
        }
        catch (Exception ex)
        {
            return false;
        }

        return  true;
    }

}
