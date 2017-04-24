package xyz.growsome.growsome.Ingresos;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import xyz.growsome.growsome.DBTables.TableIngresos;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngresosReadFragment extends Fragment {

    DBHelper dbHelper;
    TextView texv_type;
    Spinner spinner_cat;
    EditText et_nombre;
    EditText et_descripcion;
    EditText et_monto;

    Ingresos Ingreso;
    long iCodIngreso;

    public IngresosReadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dbHelper = new DBHelper(getActivity());

        ((Main)getActivity()).showDrawer(false); //disable drawer

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_ingresos_read, container, false);

        et_nombre = (EditText) view.findViewById(R.id.editText_nombre);
        et_descripcion = (EditText) view.findViewById(R.id.editText_descripcion);
        et_monto = (EditText) view.findViewById(R.id.editText_monto);
        texv_type = (TextView) view.findViewById(R.id.ingresos_tipos);
        spinner_cat = (Spinner) view.findViewById(R.id.ingresos_cat);

        if (!readIngreso())
        {
            Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
        }

        return view; //Inflate the layout for this fragment
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
                Bundle bundle = new Bundle();
                bundle.putLong("id", iCodIngreso);
                IngresosEditFragment fragment = new IngresosEditFragment();
                fragment.setArguments(bundle);
                ((Main) getActivity()).setFragment(fragment);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ((Main)getActivity()).showDrawer(true);
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
                                cursor.getInt(TableIngresos.COL_ICODUSUARIO_ID),
                                cursor.getString(TableIngresos.COL_DESC_ID),
                                cursor.getString(TableIngresos.COL_NOMBRE_ID),
                                cursor.getDouble(TableIngresos.COL_MONTO_ID),
                                new Date());
                    }
                    else if(tipo == 2)
                    {
                        Ingreso = new Pago(
                                cursor.getInt(TableIngresos.COL_ICODUSUARIO_ID),
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

}
