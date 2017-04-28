package xyz.growsome.growsome.Categorias;

import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import xyz.growsome.growsome.DBTables.TableCategorias;
import xyz.growsome.growsome.Main;
import xyz.growsome.growsome.R;
import xyz.growsome.growsome.Utils.DBHelper;

public class CategoriasReadFragment extends Fragment
{
    DBHelper dbHelper;
    EditText etName;
    EditText etDesc;
    long iCodCategory;

    public CategoriasReadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);

        ((Main)getActivity()).showDrawer(false);
        ((Main)getActivity()).showFAB(false);

        View view = inflater.inflate(R.layout.fragment_categoria_read, container, false);

        dbHelper = new DBHelper(getActivity());

        etName = (EditText) view.findViewById(R.id.categorias_field_name);
        etDesc = (EditText) view.findViewById(R.id.categorias_field_desc);

        if(!readCategoria())
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
                bundle.putLong("id", iCodCategory);
                CategoriasEditFragment fragment = new CategoriasEditFragment();
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

    public boolean readCategoria()
    {
        try
        {
            Bundle args = getArguments();

            iCodCategory = args.getLong("id");

            Cursor cursor = dbHelper.selectQuery("SELECT * FROM " + TableCategorias.TABLE_NAME + " WHERE " + TableCategorias.COL_ICOD + " = " + iCodCategory);

            try
            {
                if (cursor.moveToFirst())
                {
                    etName.setText(cursor.getString(TableCategorias.COL_NOMBRE_ID));
                    etDesc.setText(cursor.getString(TableCategorias.COL_DESC_ID));
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
