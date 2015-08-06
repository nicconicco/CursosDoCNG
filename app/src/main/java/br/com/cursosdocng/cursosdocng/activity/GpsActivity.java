package br.com.cursosdocng.cursosdocng.activity;

import android.content.Intent;
import android.os.Bundle;

import br.com.cursosdocng.cursosdocng.R;
import br.com.cursosdocng.cursosdocng.base.BaseActivity;
import br.com.cursosdocng.cursosdocng.domain.GpsService;
import br.com.cursosdocng.cursosdocng.fragment.GpsFragment;

/**
 * Created by mestre on 04/08/2015.
 */
public class GpsActivity extends BaseActivity {

    private static final String TAG = GpsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (savedInstanceState == null) {
            // instancia do seu fragment
            GpsFragment gpsFragment = new GpsFragment();
            // Utiliza do suporte para fazer a transição
            getSupportFragmentManager().beginTransaction().replace(R.id.container, gpsFragment).commit();
        }


        // Ligar o servico
//        Intent intent = new Intent(this, GpsService.class);
//        startService(intent);

//        getActivity().startService(intent);
    }
}
