package xyz.growsome.growsome.Ingresos;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
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

import xyz.growsome.growsome.Categorias.CategoriasAddFragment;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;
import xyz.growsome.growsome.DBTables.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngresosAddFragment extends Fragment implements DatePickerDialog.OnDateSetListener
{
    DBHelper dbHelper;
    Spinner spinner_type;
    Spinner spinner_cat;
    EditText et_nombre;
    EditText et_descripcion;
    EditText et_monto;
    EditText etDate;
    Button btn_save;
    DatePickerDialog datePickerDialog;
    Date date;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Ingresos Ingreso;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle(R.string.title_fragment_ingresos);

        dbHelper = new DBHelper(getActivity());

        ((Main)getActivity()).showDrawer(false);
        ((Main)getActivity()).showFAB(false);

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_ingresos_add, container, false);

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

        btn_save.setOnClickListener(new View.OnClickListener()
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

        setup();

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
        ((Main)getActivity()).showDrawer(true);
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
            et_descripcion.setError(null);
            et_nombre.setError(null);
            et_monto.setError(null);
            etDate.setError(null);

            View focusView;

            String tipo = (String) spinner_type.getSelectedItem();
            String cat = (String) spinner_cat.getSelectedItem();
            int catid = TableCategorias.getCatID(dbHelper.getReadableDatabase(), cat);
            String desc = et_descripcion.getText().toString();
            String nombre = et_nombre.getText().toString();
            double monto;

            if (catid == 0)
            {
                Toast.makeText(getActivity(), R.string.error_without_category, Toast.LENGTH_SHORT).show();
                ((Main)getActivity()).setFragment(new CategoriasAddFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                return false;
            }

            if(TextUtils.isEmpty(nombre.trim()))
            {
                et_nombre.setError(getString(R.string.error_field_required));
                focusView = et_nombre;
                focusView.requestFocus();
                return false;
            }

            try
            {
                monto = Double.parseDouble(et_monto.getText().toString());

            }
            catch (NumberFormatException ex)
            {
                et_monto.setError(getString(R.string.error_bad_input));
                focusView = et_monto;
                focusView.requestFocus();
                return false;
            }

            if(!(monto > 0) )
            {
                et_monto.setError(getString(R.string.error_bad_input));
                focusView = et_monto;
                focusView.requestFocus();
                return false;
            }

            if(tipo.equals("Salario"))
            {
                if(TextUtils.isEmpty(desc.trim()))
                {
                    et_descripcion.setError(getString(R.string.error_field_required));
                    focusView = et_descripcion;
                    focusView.requestFocus();
                    return false;
                }

                if(date == null)
                {
                    etDate.setError(getString(R.string.error_field_required));
                    focusView = etDate;
                    focusView.requestFocus();
                    return false;
                }

                Ingreso = new Salario(
                        TableUsuarios.getUserID(dbHelper.getReadableDatabase()),
                        catid,
                        desc,
                        nombre,
                        Double.parseDouble(et_monto.getText().toString()),
                        date);

            }
            else if(tipo.equals("Pago"))
            {
                if(TextUtils.isEmpty(desc.trim()))
                {
                    et_descripcion.setError(getString(R.string.error_field_required));
                    focusView = et_descripcion;
                    focusView.requestFocus();
                    return false;
                }

                if(date == null)
                {
                    etDate.setError(getString(R.string.error_field_required));
                    focusView = etDate;
                    focusView.requestFocus();
                    return false;
                }

                Ingreso = new Pago(
                        TableUsuarios.getUserID(dbHelper.getReadableDatabase()),
                        catid,
                        desc,
                        nombre,
                        monto,
                        date);
            }
            else
            {
                Toast.makeText(getActivity(), R.string.error_bad_input, Toast.LENGTH_SHORT).show();
                return  false;
            }

            try
            {
                TableIngresos.insert(dbHelper.getWritableDatabase(), Ingreso);
            } catch (Exception ex)
            {
                ex.printStackTrace();
                Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
            return false;
        }

        return  true;
    }
}
