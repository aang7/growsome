package xyz.growsome.growsome;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Descripcion: Fragment de inicio de la aplicación
 * Autor: Sergio Cruz
 * Fecha: 2017-03-22
 */
public class HomeFragment extends Fragment
{
    public HomeFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle(R.string.home_fragment_title);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
