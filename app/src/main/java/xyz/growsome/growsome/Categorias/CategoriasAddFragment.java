package xyz.growsome.growsome.Categorias;

import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import xyz.growsome.growsome.DBTables.TableCategorias;
import xyz.growsome.growsome.DBTables.TableUsuarios;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

public class CategoriasAddFragment extends Fragment
{
    DBHelper dbHelper;
    EditText etName;
    EditText etDesc;
    Button btnSave;
    ColorPicker colorPicker;
    View colorButton;
    ImageView colorImage;
    String pickercolor = null;

    public CategoriasAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);

        ((Main)getActivity()).showDrawer(false);
        ((Main)getActivity()).showFAB(false);

        View view = inflater.inflate(R.layout.fragment_categoria_add, container, false);

        dbHelper = new DBHelper(getActivity());

        etName = (EditText) view.findViewById(R.id.categorias_field_name);
        etDesc = (EditText) view.findViewById(R.id.categorias_field_desc);
        btnSave = (Button) view.findViewById(R.id.categorias_btnSave);
        colorImage = (ImageView)  view.findViewById(R.id.categorias_image);

        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(saveCategoria())
                {
                    getFragmentManager().popBackStack();
                }
            }
        });

        colorButton = view.findViewById(R.id.categorias_field_color);

        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                colorPicker.show();
            }
        });

        colorPicker = new ColorPicker(getActivity(), 255, 64, 67);

        colorPicker.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(int color)
            {
                colorImage.setBackgroundColor(color);
                pickercolor = String.valueOf(color);
                colorPicker.hide();
            }
        });

        pickercolor = String.valueOf(colorPicker.getColor());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cancel_menu, menu);
        menu.setGroupVisible(R.id.general_group, false);
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
        dbHelper.close();
    }

    public boolean saveCategoria()
    {
        try
        {
            etName.setError(null);
            etDesc.setError(null);
            View focusView;

            String nombre = etName.getText().toString();
            String desc = etDesc.getText().toString();

            if(TextUtils.isEmpty(nombre.trim()))
            {
                etName.setError(getString(R.string.error_field_required));
                focusView = etName;
                focusView.requestFocus();
                return false;
            }

            if(TextUtils.isEmpty(desc.trim()))
            {
                etDesc.setError(getString(R.string.error_field_required));
                focusView = etDesc;
                focusView.requestFocus();
                return false;
            }

            Integer id = TableCategorias.getCatID(dbHelper.getReadableDatabase(), nombre);

            if(id != 0)
            {
                Toast.makeText(getActivity(), R.string.error_repeated_category, Toast.LENGTH_SHORT).show();
                return false;
            }

            if (pickercolor != null)
            {
                Categoria categoria = new Categoria(0, TableUsuarios.getUserID(dbHelper.getReadableDatabase()), nombre, pickercolor, desc);

                try
                {
                    TableCategorias.insert(dbHelper.getWritableDatabase(), categoria);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(getActivity(), R.string.error_default, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            else
            {
                Toast.makeText(getActivity(), R.string.error_bad_input, Toast.LENGTH_SHORT).show();
                return false;
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
