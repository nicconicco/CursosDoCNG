package br.com.cursosdocng.cursosdocng.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by mestre on 04/08/2015.
 */

// Aqui usamos disso para poder usar do suporte de transição entre os fragment
// Ser sempre android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    public BaseActivity getBaseActivity() {
        // Método existente no Fragment, sendo que ele esta sendo extendido na BaseActivity
        return (BaseActivity) getActivity();
    }

    public void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        BaseActivity baseActivity = getBaseActivity();
        if (baseActivity != null) {
            FragmentManager fm = baseActivity.getSupportFragmentManager();
            if(fm != null) {
                fm.beginTransaction().replace(containerViewId, fragment, tag).commit();
            }
        }
    }

}
