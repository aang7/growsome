package xyz.growsome.growsome.Ingresos;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import xyz.growsome.growsome.DBTables.TableIngresos;
import xyz.growsome.growsome.DBTables.TableUsuarios;
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
    Button btn_save;

    public IngresosReadFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new DBHelper(getActivity());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingresos_read, container, false);

        et_nombre = (EditText) view.findViewById(R.id.editText_nombre);
        et_descripcion = (EditText) view.findViewById(R.id.editText_descripcion);
        et_monto = (EditText) view.findViewById(R.id.editText_monto);
        btn_save = (Button) view.findViewById(R.id.btn_guardar);
        texv_type = (TextView) view.findViewById(R.id.ingresos_tipos);
        spinner_cat = (Spinner) view.findViewById(R.id.ingresos_cat);

        if (readIngreso())
            ;
        else
            Toast.makeText(getActivity(), "Ups algo salió mal!", Toast.LENGTH_SHORT).show();
        return view;
    }


    public boolean readIngreso()
    {

        int item_selected = ((DataExchange)getActivity()).getPositon(); //este me lo tengo que traer del otro fragment
        Cursor cursor = dbHelper.selectQuery("SELECT * FROM "+ TableIngresos.TABLE_NAME + " WHERE " + TableIngresos.COL_ICOD + " = " + item_selected);

        /* Reading the cursor*/
        try {
            if(cursor.moveToFirst())
            {
                String tipo = cursor.getString(TableIngresos.COL_ICODTIPO_ID);
                String desc =  cursor.getString(TableIngresos.COL_DESC_ID);
                String nombre =  cursor.getString(TableIngresos.COL_NOMBRE_ID);
                double monto = cursor.getDouble(TableIngresos.COL_MONTO_ID);

                et_nombre.setText(nombre);
                et_descripcion.setText(desc);
                et_monto.setText(Double.toString(monto));
                texv_type.setText(tipo);

                //falta llenar tipo y categoria en un textView o algo asi
                //spinner_type = (Spinner) view.findViewById(R.id.ingresos_tipos);
                //spinner_cat = (Spinner) view.findViewById(R.id.ingresos_cat);
                return true;
            }

        } catch (Exception e)
            {
                e.printStackTrace();
                Log.d("CURSOR: ", "ERROR" );
            } finally {
                cursor.close();
                dbHelper.close();
             }

        return false;
    }

}
