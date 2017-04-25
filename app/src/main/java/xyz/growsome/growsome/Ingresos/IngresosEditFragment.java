package xyz.growsome.growsome.Ingresos;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import xyz.growsome.growsome.DBTables.TableCategorias;
import xyz.growsome.growsome.DBTables.TableIngresos;
import xyz.growsome.growsome.DBTables.TableTipoIngreso;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngresosEditFragment extends Fragment {

    DBHelper dbHelper;
    Spinner spinner_cat;
    Spinner spinner_type;
    EditText et_nombre;
    EditText et_descripcion;
    EditText et_monto;
    Button btn_save;

    Ingresos Ingreso;
    long iCodIngreso;

    public IngresosEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dbHelper = new DBHelper(getActivity());

        ((Main)getActivity()).showDrawer(false); //disable drawer

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_ingresos_edit, container, false);

        et_nombre = (EditText) view.findViewById(R.id.editText_nombre);
        et_descripcion = (EditText) view.findViewById(R.id.editText_descripcion);
        et_monto = (EditText) view.findViewById(R.id.editText_monto);
        btn_save = (Button) view.findViewById(R.id.btn_guardar);
        spinner_type = (Spinner) view.findViewById(R.id.ingresos_tipos);
        spinner_cat = (Spinner) view.findViewById(R.id.ingresos_cat);

        setup();
        readIngreso();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.general_group, false); //hidding main items
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ((Main)getActivity()).showDrawer(true);
    }

    public void setup()
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

        ((Main)getActivity()).showFAB(false);
        ((Main)getActivity()).showDrawer(false);
    }

    public boolean readIngreso()
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
                            cursor.getLong(TableIngresos.COL_DESC_ID),
                            cursor.getLong(TableIngresos.COL_ICODCAT_ID),
                            cursor.getString(TableIngresos.COL_DESC_ID),
                            cursor.getString(TableIngresos.COL_NOMBRE_ID),
                            cursor.getDouble(TableIngresos.COL_MONTO_ID),
                            new Date());
                }
                else if(tipo == 2)
                {
                    Ingreso = new Pago(
                            cursor.getLong(TableIngresos.COL_ICODUSUARIO_ID),
                            cursor.getLong(TableIngresos.COL_ICODCAT_ID),
                            cursor.getString(TableIngresos.COL_DESC_ID),
                            cursor.getString(TableIngresos.COL_NOMBRE_ID),
                            cursor.getDouble(TableIngresos.COL_MONTO_ID),
                            new Date());
                }

                et_nombre.setText(Ingreso.getNombre());
                et_descripcion.setText(Ingreso.getDesc());
                et_monto.setText(Double.toString(Ingreso.getMonto()));

                //TODO: Set Tipo y categoria, fix date;
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.d("CURSOR: ", "ERROR" );
        }
        finally
        {
            cursor.close();
            dbHelper.close();
        }

        return false;
    }

    public boolean editIngreso()
    {
        try
        {
            //String tipo = (String) spinner_type.getSelectedItem(); /** falta esta llenarlo **/
            Ingreso.setDesc(et_descripcion.getText().toString());
            Ingreso.setNombre(et_nombre.getText().toString());
            Ingreso.setMonto(Double.parseDouble(et_monto.getText().toString()));

            TableIngresos.update(dbHelper.getWritableDatabase(), Ingreso);
        }
        catch (Exception ex)
        {
            return false;
        }

        return true;
    }

}
