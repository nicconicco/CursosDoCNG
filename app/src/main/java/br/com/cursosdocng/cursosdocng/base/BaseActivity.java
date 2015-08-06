package br.com.cursosdocng.cursosdocng.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by mestre on 04/08/2015.
 */

// Utilizamos do FragmentActivity para a transição de telas como o setContentView, por isso não usamos fragment, senão ele acusa desse erro
// Além do onCreate de um ser protected e outro public

public class BaseActivity extends FragmentActivity {


    // transição de telas da activiy pro fragment sempre é bom jogarmos na base para não repitir código
    public void showActivity(Context context, Class c) {
        Intent intent = new Intent(context, c);
        startActivity(intent);
    }


    protected Context getContext() {
        return this.getActivity();
    }

    protected Activity getActivity() {
        return this;
    }

}
