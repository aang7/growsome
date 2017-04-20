package xyz.growsome.growsome.Ingresos;


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

import xyz.growsome.growsome.DBTables.TableIngresos;
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

    public IngresosEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dbHelper = new DBHelper(getActivity());

        View view = inflater.inflate(R.layout.fragment_ingresos_edit, container, false);

        et_nombre = (EditText) view.findViewById(R.id.editText_nombre);
        et_descripcion = (EditText) view.findViewById(R.id.editText_descripcion);
        et_monto = (EditText) view.findViewById(R.id.editText_monto);
        btn_save = (Button) view.findViewById(R.id.btn_guardar);
        //spinner_type = (Spinner) view.findViewById(R.id.ingresos_tipos);
        spinner_cat = (Spinner) view.findViewById(R.id.ingresos_cat);

        setup();
        readIngreso();
        return view;
    }

    public void setup()
    {


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(editIngreso()) {
                    Toast.makeText(getActivity(), "Igreso Actualizado", Toast.LENGTH_SHORT).show();
                    getFragmentManager().popBackStack();//close this fragment
                }
                else
                    Toast.makeText(getActivity(), "ups algo salió mal!", Toast.LENGTH_SHORT).show();


            }
        });

        ((Main)getActivity()).setDrawerEnabled(false); //disable drawer
        ((Main)getActivity()).hideFloatingActionButton();
    }


    public boolean editIngreso()
    {
        try{
            int item_selected = ((DataExchange)getActivity()).getPositon();

            //String tipo = (String) spinner_type.getSelectedItem(); /** falta esta llenarlo **/
            String desc = et_descripcion.getText().toString();
            String nombre = et_nombre.getText().toString();
            double monto = Double.parseDouble(et_monto.getText().toString());

            /** sample**/
            String SQLupdateCommand = "UPDATE "+ TableIngresos.TABLE_NAME + " SET " + TableIngresos.COL_NOMBRE+ " = " + "'" + nombre + "'"
                    + " WHERE "  +  TableIngresos.COL_ICOD + " = " + item_selected;

            TableIngresos.insert(dbHelper.getWritableDatabase(), SQLupdateCommand);
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    /*    Cursor cursor = dbHelper.selectQuery("UPDATE "+ TableIngresos.TABLE_NAME + "SET " + TableIngresos.COL_NOMBRE + " = "
                + "WHERE "  +
                TableIngresos.COL_ICOD + " = " + item_selected);
*/

    public boolean readIngreso()
    {

        int item_selected = ((DataExchange)getActivity()).getPositon();
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
                //spinner_type.setText(tipo);

                /** falta llenar tipo y categoria en un textView o algo asi **/
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((Main)getActivity()).setDrawerEnabled(true); //enable drawer again
    }

}
