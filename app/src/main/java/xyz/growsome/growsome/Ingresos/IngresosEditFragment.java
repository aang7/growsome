package xyz.growsome.growsome.Ingresos;

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
import xyz.growsome.growsome.DBTables.TableIngresos;
import xyz.growsome.growsome.DBTables.TableTipoIngreso;
import xyz.growsome.growsome.DBTables.TableUsuarios;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngresosEditFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    DBHelper dbHelper;
    Spinner spinner_cat;
    Spinner spinner_type;
    EditText et_nombre;
    EditText et_descripcion;
    EditText et_monto;
    EditText etDate;
    Button btn_save;
    DatePickerDialog datePickerDialog;
    Date date;

    Ingresos Ingreso;
    long iCodIngreso;
    List<String> cats;
    List<String> tipos;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public IngresosEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dbHelper = new DBHelper(getActivity());

        ((Main)getActivity()).showDrawer(false);
        ((Main)getActivity()).showFAB(false);

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_ingresos_edit, container, false);

        et_nombre = (EditText) view.findViewById(R.id.ingresos_field_nombre);
        et_descripcion = (EditText) view.findViewById(R.id.ingresos_field_desc);
        et_monto = (EditText) view.findViewById(R.id.ingresos_field_monto);
        etDate = (EditText) view.findViewById(R.id.ingresos_field_date);
        btn_save = (Button) view.findViewById(R.id.btn_guardar);
        spinner_type = (Spinner) view.findViewById(R.id.ingresos_tipos);
        spinner_cat = (Spinner) view.findViewById(R.id.ingresos_cat);

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

        setup();
        readIngreso();
        return view;
    }

    public void onDateSet(DatePicker view, int year, int month, int day)
    {
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

    public void setup()
    {
        Cursor cursorTipos = dbHelper.selectQuery(TableTipoIngreso.SELECT_ALL);
        Cursor cursorCats = dbHelper.selectQuery(TableCategorias.SELECT_ALL);

        tipos = new ArrayList<>();
        cats = new ArrayList<>();

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


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(editIngreso())
                {
                    getFragmentManager().popBackStack();//close this fragment
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean readIngreso()
    {
        try
        {
            Bundle args = getArguments();

            iCodIngreso = args.getLong("id");

            Cursor cursor = dbHelper.selectQuery("SELECT * FROM "+ TableIngresos.TABLE_NAME + " WHERE " + TableIngresos.COL_ICOD + " = " + iCodIngreso);

            try
            {
                if(cursor.moveToFirst())
                {
                    int tipo = cursor.getInt(TableIngresos.COL_ICODTIPO_ID);

                    if(tipo == 1)
                    {

                        Ingreso = new Salario(
                                cursor.getLong(TableIngresos.COL_ICODUSUARIO_ID),
                                cursor.getLong(TableIngresos.COL_ICODCAT_ID),
                                cursor.getString(TableIngresos.COL_DESC_ID),
                                cursor.getString(TableIngresos.COL_NOMBRE_ID),
                                cursor.getDouble(TableIngresos.COL_MONTO_ID),
                                df.parse(cursor.getString(TableIngresos.COL_FECHA_ID)));
                    }
                    else if(tipo == 2)
                    {
                        Ingreso = new Pago(
                                cursor.getInt(TableIngresos.COL_ICODUSUARIO_ID),
                                cursor.getLong(TableIngresos.COL_ICODCAT_ID),
                                cursor.getString(TableIngresos.COL_DESC_ID),
                                cursor.getString(TableIngresos.COL_NOMBRE_ID),
                                cursor.getDouble(TableIngresos.COL_MONTO_ID),
                                df.parse(cursor.getString(TableIngresos.COL_FECHA_ID)));
                    }
                    else
                    {
                        return  false;
                    }

                    spinner_cat.setSelection(cats.indexOf(TableCategorias.getCatName(dbHelper.getReadableDatabase(), Ingreso.getCatid())));
                    spinner_cat.setFocusable(false);
                    spinner_type.setSelection(tipo - 1);
                    spinner_type.setFocusable(false);
                    et_nombre.setText(Ingreso.getNombre());
                    et_descripcion.setText(Ingreso.getDesc());
                    et_monto.setText(Double.toString(Ingreso.getMonto()));
                    etDate.setText(df.format(Ingreso.getFecha()));
                }

            }
            catch (Exception ex)
            {
                Log.d("Error", ex.toString());
                return false;
            }
            finally
            {
                cursor.close();
                dbHelper.close();
            }

        }
        catch (Exception ex)
        {
            Log.d("Error", ex.toString());
            return false;
        }

        return true;
    }

    public boolean editIngreso()
    {
        try
        {
            String tipo = (String) spinner_type.getSelectedItem();
            String cat = (String) spinner_cat.getSelectedItem();
            int catid = TableCategorias.getCatID(dbHelper.getReadableDatabase(), cat);
            String desc = et_descripcion.getText().toString();
            String nombre = et_nombre.getText().toString();
            double monto = Double.parseDouble(et_monto.getText().toString());

            if(tipo.equals("Salario")) //tengo que generalizar esto
            {
                Ingreso = new Salario(
                        TableUsuarios.getUserID(dbHelper.getReadableDatabase()),
                        catid,
                        desc,
                        nombre,
                        monto,
                        df.parse(etDate.getText().toString()));
            }
            else if(tipo.equals("Pago"))
            {
                Ingreso = new Pago(
                        TableUsuarios.getUserID(dbHelper.getReadableDatabase()),
                        catid,
                        desc,
                        nombre,
                        monto,
                        df.parse(etDate.getText().toString()));
            }
            else
            {
                return  false;
            }

            TableIngresos.update(dbHelper.getWritableDatabase(), Ingreso, iCodIngreso);
        }
        catch (Exception ex)
        {
            return false;
        }

        return true;
    }

}
