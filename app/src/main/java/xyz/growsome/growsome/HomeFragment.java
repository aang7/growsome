package xyz.growsome.growsome;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.growsome.growsome.DBTables.TableCategorias;
import xyz.growsome.growsome.DBTables.TableGastos;
import xyz.growsome.growsome.DBTables.TableIngresos;
import xyz.growsome.growsome.DBTables.TableTipoIngreso;
import xyz.growsome.growsome.Utils.DBHelper;

/**
 * Descripcion: Fragment de inicio de la aplicación
 * Autor: Sergio Cruz
 * Fecha: 2017-03-22
 */
public class HomeFragment extends Fragment
{
    DBHelper dbHelper;
    TextView tv_gastoTotal;
    TextView tv_ingresoTotal;


    public HomeFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(R.string.title_fragment_home);
        ((Main)getActivity()).showFAB(true);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dbHelper = new DBHelper(getActivity());
        tv_gastoTotal = (TextView) view.findViewById(R.id.tv_total_gastos);
        tv_ingresoTotal = (TextView) view.findViewById(R.id.tv_total_ingreso);

        if (setup())
            ;
        else
            Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();

        return view;
    }

    private final String SELECT_GASTOS_TOTAL = "SELECT SUM(" + TableGastos.COL_COSTO
            + ") FROM " + TableGastos.TABLE_NAME
            + " where " + TableGastos.COL_DELETE + " = 0";


    private final String SELECT_INGRESOS_TOTAL = "SELECT SUM(" + TableIngresos.COL_MONTO
            + ") FROM " + TableIngresos.TABLE_NAME
            + " where " + TableIngresos.COL_DELETE + " = 0" ;

    public boolean setup(){
        Cursor cursorGastoTotal = dbHelper.selectQuery(SELECT_GASTOS_TOTAL);
        Cursor cursorIngresoTotal = dbHelper.selectQuery(SELECT_INGRESOS_TOTAL);

        try {

            if (cursorGastoTotal.moveToNext())
                 tv_gastoTotal.setText(Double.valueOf(cursorGastoTotal.getDouble(0)).toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;

        } finally {
            cursorGastoTotal.close();
        }

            try {

                if (cursorIngresoTotal.moveToNext())
                    tv_ingresoTotal.setText(Double.valueOf(cursorIngresoTotal.getDouble(0)).toString());

            } catch (Exception ex) {
                ex.printStackTrace();
                return false;

            } finally {
                cursorIngresoTotal.close();
            }


        return true;
    }

    
}
