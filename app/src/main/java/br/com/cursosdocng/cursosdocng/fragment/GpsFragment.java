package br.com.cursosdocng.cursosdocng.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.cursosdocng.cursosdocng.R;
import br.com.cursosdocng.cursosdocng.base.BaseActivity;
import br.com.cursosdocng.cursosdocng.base.BaseFragment;

/**
 * Created by mestre on 04/08/2015.
 */
public class GpsFragment extends BaseFragment {


    // TODO: ver como funciona o application
    // TODO: ver como funciona o Pref

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_gps, container, false);



        return view;
    }
}
