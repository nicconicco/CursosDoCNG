package br.com.cursosdocng.cng.activity;


import android.os.Bundle;

import br.com.cursosdocng.cng.R;
import br.com.cursosdocng.cng.base.BaseActivity;


public class StartActivity extends BaseActivity {

    private static final int CHAVE_ENTRADA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Pego o contexto atual da aplicação
        // Mudo para a tela que desejo, não preciso andar desde o começo do app
        // Importante você lembrar de deixar o primeiro como sendo sempre o
        // ciclo que desejar para seu app

        switch (CHAVE_ENTRADA) {
            case 0:
                showActivity(getContext(), GpsActivity.class);
                finish();
                break;
            case 1:
                showActivity(getContext(), GpsActivity.class);
                finish();
                break;

            default:
                showActivity(getContext(), GpsActivity.class);
                finish();
                break;
        }
    }
    /* Lembre do atalho para windows ou linux control + alt + l para sempre formatar..
    * Um código não formatado é muito feio de se ver
    * mantenha um nível de escrita
    *
    *  Exemplo:
    *   public void meuMetodo () {
    *
    *   }
    *
    *  Mantenha o mesmo padrão no próximo método
    *
    *  public void meuMetodoDois () {
    *
    *  }
    *
    *  Errado:
    *
    *  public void MeUmEtOdOdOis2()
    *  {
    *
    *  }
    *
    * */
}



