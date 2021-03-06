package xyz.growsome.growsome.Gastos;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import xyz.growsome.growsome.DBTables.TableCategorias;
import xyz.growsome.growsome.DBTables.TableGastos;
import xyz.growsome.growsome.DBTables.TableIngresos;
import xyz.growsome.growsome.DBTables.TableTipoGasto;
import xyz.growsome.growsome.DBTables.TableTipoIngreso;
import xyz.growsome.growsome.Ingresos.Ingresos;
import xyz.growsome.growsome.Ingresos.IngresosEditFragment;
import xyz.growsome.growsome.Ingresos.Pago;
import xyz.growsome.growsome.Ingresos.Salario;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;

public class GastosReadFragment extends Fragment {

    DBHelper dbHelper;
    Spinner spinner_type;
    Spinner spinner_cat;
    EditText et_nombre;
    EditText et_descripcion;
    EditText et_costo;
    EditText et_cantidad;
    EditText etDate;

    Gastos Gasto;
    long iCodGasto;
    List<String> cats;
    List<String> tipos;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public GastosReadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ((Main)getActivity()).showDrawer(false);
        ((Main)getActivity()).showFAB(false);

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_gastos_read, container, false);

        dbHelper = new DBHelper(getActivity());

        et_nombre = (EditText) view.findViewById(R.id.gastos_field_nombre);
        et_descripcion = (EditText) view.findViewById(R.id.gastos_field_desc);
        et_costo = (EditText) view.findViewById(R.id.gastos_field_costo);
        etDate = (EditText) view.findViewById(R.id.gastos_field_date);
        spinner_type = (Spinner) view.findViewById(R.id.gastos_tipos);
        spinner_cat = (Spinner) view.findViewById(R.id.gastos_cat);
        et_cantidad = (EditText) view.findViewById(R.id.gastos_cantidad);

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

        if (!readGasto())
        {
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_menu, menu);
        menu.setGroupVisible(R.id.general_group, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_edit:
                getFragmentManager().popBackStack();
                Bundle bundle = new Bundle();
                bundle.putLong("id", iCodGasto);
                GastosEditFragment fragment = new GastosEditFragment();
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

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ((Main)getActivity()).showDrawer(true);
        dbHelper.close();
    }

    public boolean setup()
    {
        Cursor cursorTipos = dbHelper.selectQuery(TableTipoGasto.SELECT_ALL);
        Cursor cursorCats = dbHelper.selectQuery(TableCategorias.SELECT_ALL);

        tipos = new ArrayList<>();
        cats = new ArrayList<>();

        try
        {
            while (cursorTipos.moveToNext())
            {
                tipos.add(cursorTipos.getString(TableTipoGasto.COL_DESC_ID));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
            return false;
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
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
            return false;
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

    public boolean readGasto()
    {
        try
        {
            Bundle args = getArguments();

            iCodGasto = args.getLong("id");

            Cursor cursor = dbHelper.selectQuery("SELECT * FROM "+ TableGastos.TABLE_NAME + " WHERE " + TableGastos.COL_ICOD + " = " + iCodGasto);

            try
            {
                if(cursor.moveToFirst())
                {
                    int tipo = cursor.getInt(TableGastos.COL_ICODTIPO_ID);

                    if(tipo == 1)
                    {
                        Gasto = new Servicio(
                                cursor.getInt(TableGastos.COL_ICODUSUARIO_ID),
                                cursor.getLong(TableGastos.COL_ICODCAT_ID),
                                cursor.getString(TableGastos.COL_DESC_ID),
                                cursor.getString(TableGastos.COL_NOMBRE_ID),
                                cursor.getDouble(TableGastos.COL_COSTO_ID),
                                df.parse(cursor.getString(TableGastos.COL_FECHA_ID)));

                        et_cantidad.setText("NA");
                    }
                    else if(tipo == 2)
                    {
                        Gasto = new Producto(
                                cursor.getLong(TableGastos.COL_ICODUSUARIO_ID),
                                cursor.getLong(TableGastos.COL_ICODCAT_ID),
                                cursor.getString(TableGastos.COL_DESC_ID),
                                cursor.getString(TableGastos.COL_NOMBRE_ID),
                                cursor.getDouble(TableGastos.COL_COSTO_ID),
                                cursor.getInt(TableGastos.COL_CANT_ID),
                                df.parse(cursor.getString(TableGastos.COL_FECHA_ID)));

                        et_cantidad.setText(Integer.toString(Gasto.getCantidad()));
                    }
                    else
                    {
                        return  false;
                    }

                    spinner_type.setSelection(tipo - 1);
                    spinner_type.setEnabled(false);
                    spinner_cat.setSelection(cats.indexOf(TableCategorias.getCatName(dbHelper.getReadableDatabase(), Gasto.getCatid())));
                    spinner_cat.setEnabled(false);
                    et_nombre.setText(Gasto.getNombre());
                    et_descripcion.setText(Gasto.getDesc());
                    et_costo.setText(Double.toString(Gasto.getCosto()));
                    etDate.setText(df.format(Gasto.getFecha()));
                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
                return false;
            }
            finally
            {
                cursor.close();
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
