package xyz.growsome.growsome.Ingresos;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import xyz.growsome.growsome.Login;
import xyz.growsome.growsome.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngresosAddFragment extends Fragment {


    //Pago pago = new Pago();

    EditText et_nombre;
    EditText et_descripcion;
    EditText et_monto;
    Button btn_save;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingresos_add, container, false);

        /* Edit text's */
        et_nombre = (EditText) view.findViewById(R.id.editText_nombre);
        et_descripcion = (EditText) view.findViewById(R.id.editText_descripcion);
        et_monto = (EditText) view.findViewById(R.id.editText_monto);
        btn_save = (Button) view.findViewById(R.id.btn_guardar);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("NOMBRE: ", et_nombre.getText().toString());
                Log.d("MONTO: ", String.format("%f", Double.valueOf(et_monto.getText().toString())));
                Log.d("DESCRIPCIPN: ", et_descripcion.getText().toString());
            }
        });

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.spinner_ingresosType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        return view;
    }

}
