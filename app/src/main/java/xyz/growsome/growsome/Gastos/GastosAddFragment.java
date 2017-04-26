package xyz.growsome.growsome.Gastos;


import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import xyz.growsome.growsome.DBTables.TableCategorias;
import xyz.growsome.growsome.DBTables.TableGastos;
import xyz.growsome.growsome.DBTables.TableIngresos;
import xyz.growsome.growsome.DBTables.TableTipoGasto;
import xyz.growsome.growsome.DBTables.TableTipoIngreso;
import xyz.growsome.growsome.DBTables.TableUsuarios;
import xyz.growsome.growsome.Ingresos.Pago;
import xyz.growsome.growsome.Ingresos.Salario;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class GastosAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    DBHelper dbHelper;
    Spinner spinner_type;
    Spinner spinner_cat;
    EditText et_cantidad;
    EditText et_nombre;
    EditText et_descripcion;
    EditText et_costo;
    EditText etDate;
    Button btn_save;
    DatePickerDialog datePickerDialog;
    Date date;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Gastos Gasto;

    public GastosAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dbHelper = new DBHelper(getActivity());

        ((Main)getActivity()).showDrawer(false);
        ((Main)getActivity()).showFAB(false);

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_gastos_add, container, false);

        et_nombre = (EditText) view.findViewById(R.id.gastos_field_nombre);
        et_descripcion = (EditText) view.findViewById(R.id.gastos_field_desc);
        et_costo = (EditText) view.findViewById(R.id.gastos_field_costo);
        etDate = (EditText) view.findViewById(R.id.gastos_field_date);
        btn_save = (Button) view.findViewById(R.id.btn_guardar);
        spinner_type = (Spinner) view.findViewById(R.id.gastos_tipos);
        spinner_cat = (Spinner) view.findViewById(R.id.gastos_cat);
        et_cantidad = (EditText) view.findViewById(R.id.gastos_cantidad);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        etDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(saveGasto())
                {
                    getFragmentManager().popBackStack();
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setCantidad(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        setup();

        return view;
    }

    public boolean setup()
    {
        Cursor cursorTipos = dbHelper.selectQuery(TableTipoGasto.SELECT_ALL);
        Cursor cursorCats = dbHelper.selectQuery(TableCategorias.SELECT_ALL);

        List<String> tipos = new ArrayList<>();
        List<String> cats = new ArrayList<>();

        try
        {
            while (cursorTipos.moveToNext())
            {
                tipos.add(cursorTipos.getString(TableTipoGasto.COL_DESC_ID));
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

        setCantidad(spinner_type.getSelectedItemPosition());

        return true;
    }

    public boolean saveGasto()
    {
        try
        {
            String tipo = (String) spinner_type.getSelectedItem();
            String cat = (String) spinner_cat.getSelectedItem();
            int catid = TableCategorias.getCatID(dbHelper.getReadableDatabase(), cat);
            String desc = et_descripcion.getText().toString();
            String nombre = et_nombre.getText().toString();
            double costo = Double.parseDouble(et_costo.getText().toString());

            if(tipo.equals("Producto") && et_cantidad.isEnabled())
            {

                int cantidad = Integer.parseInt(et_cantidad.getText().toString());

                Gasto = new Producto(
                        TableUsuarios.getUserID(dbHelper.getReadableDatabase()),
                        catid,
                        desc,
                        nombre,
                        costo,
                        cantidad,
                        date);
            }
            else if(tipo.equals("Servicio"))
            {
                Gasto = new Servicio(
                        TableUsuarios.getUserID(dbHelper.getReadableDatabase()),
                        catid,
                        desc,
                        nombre,
                        costo,
                        date);
            }
            else
            {
                return  false;
            }

            TableGastos.insert(dbHelper.getWritableDatabase(), Gasto);
        }
        catch (Exception ex)
        {
            Log.d("Error", ex.toString());
            return false;
        }

        return  true;
    }

    public void setCantidad(int position)
    {
        String tipo = (String) spinner_type.getItemAtPosition(position);

        if(tipo.equals("Servicio"))
        {
            et_cantidad.setEnabled(false);
        }
        else if(tipo.equals("Producto"))
        {
            et_cantidad.setEnabled(true);
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        date = cal.getTime();
        etDate.setText(df.format(date));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cancel_menu, menu);
        menu.setGroupVisible(R.id.general_group, false); //hidding main items
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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
        ((Main)getActivity()).showDrawer(true); //enable drawer again
    }
}
