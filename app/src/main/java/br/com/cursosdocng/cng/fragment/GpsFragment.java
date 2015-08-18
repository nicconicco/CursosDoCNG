package br.com.cursosdocng.cng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.cursosdocng.cng.CngApplication;
import br.com.cursosdocng.cng.R;
import br.com.cursosdocng.cng.base.BaseActivity;
import br.com.cursosdocng.cng.base.BaseFragment;
import br.com.cursosdocng.cng.domain.Gps;
import br.com.cursosdocng.cng.domain.GpsService;
import br.com.cursosdocng.cng.util.Constantes;
import br.com.cursosdocng.cng.util.Pref;

/**
 * Created by mestre on 04/08/2015.
 */
public class GpsFragment extends BaseFragment implements BaseActivity.MessageFromServiceDelegate {

    private static final String TAG = GpsFragment.class.getSimpleName();
    private TextView tGpsStatus;
    private TextView tPosicaoAtual;
    private TextView tStatusServico;
    private TextView tRequisicoes;
    private Gps gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_gps, container, false);

        tGpsStatus = (TextView) view.findViewById(R.id.tGpsStatus);
        tPosicaoAtual = (TextView) view.findViewById(R.id.tPosicaoAtual);
        tStatusServico = (TextView) view.findViewById(R.id.tStatusServico);
        tRequisicoes = (TextView) view.findViewById(R.id.tRequisicoes);

        if(Pref.getInteger(getBaseActivity(), Constantes.MARCOU_PROXIMO_AGENDAMENTO) == 1) {
            preencheCampoRequisicao(true);
        } else {
            preencheCampoRequisicao(false);
        }

        if(Pref.getBoolean(getContext(), Constantes.LIGAR_SERVICO)) {
            preencheCampoGps(true);
        } else {
            preencheCampoGps(false);
        }

        boolean estado_servico = Pref.getBoolean(getContext(), Constantes.LIGAR_SERVICO);


        if (!estado_servico) {

            if (CngApplication.getInstance().getGpsIntent() == null) {
                CngApplication.getInstance().setGpsIntent(new Intent(getContext(), GpsService.class));
            }

            getActivity().startService(CngApplication.getInstance().getGpsIntent());
//            Caso queira fazer por inicializacao sem precisar ficar ligando sempre
//            utilizar pref, e perguntar .. usar boot_receiver..
            Pref.setBoolean(getContext(), Constantes.LIGAR_SERVICO, true);
        }
        return view;
    }


    @Override
    protected void dispararTaskProcurarServico() {
        super.dispararTaskProcurarServico();
        Log.d(TAG, "dispararTaskProcurarServico > startTaskProcuraPorOutroServicoACadaXTempo()");

        startTaskProcuraPorOutroServicoACadaXTempo();
    }

    @Override
    public void messageFromServiceReceived(Intent intent) {
        Log.d(TAG, getClass().getSimpleName() + ": messageFromServiceReceived: " + intent.getAction());

        gps = (Gps) intent.getSerializableExtra(Gps.KEY);

        if (tPosicaoAtual != null && gps != null) {
            tPosicaoAtual.setText(gps.getLatitude() + "/" + gps.getLongitude());
        }
    }

    public void preencheCampoRequisicao(boolean itemProcuraOutrosServicos) {

        if (itemProcuraOutrosServicos) {
            tRequisicoes.setText("Ligado");
        } else {
            tRequisicoes.setText("Desligado");
        }
    }

    public void preencheCampoGps(boolean itemGps) {

        if (itemGps) {
            tGpsStatus.setText("Ligado");
            tStatusServico.setText("Ligado");
        } else {
            tGpsStatus.setText("Último registro");
            tStatusServico.setText("Desligado");
        }
    }

}
