package br.com.cursosdocng.cng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.cursosdocng.cng.CngApplication;
import br.com.cursosdocng.cng.R;
import br.com.cursosdocng.cng.base.BaseFragment;
import br.com.cursosdocng.cng.domain.Gps;
import br.com.cursosdocng.cng.domain.GpsService;
import br.com.cursosdocng.cng.util.BroadcastUtil;
import br.com.cursosdocng.cng.util.Constantes;
import br.com.cursosdocng.cng.util.Pref;

/**
 * Created by mestre on 04/08/2015.
 */
public class ControleFragment extends BaseFragment {

    private static final String TAG = ControleFragment.class.getSimpleName();
//    private TextView btnAlteraStatusServico;
//    private TextView btnDesligaRequisicao;
//    private TextView btnTrocarFragments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_controle_gps, container, false);


        boolean estado_servico = Pref.getBoolean(getContext(), Constantes.LIGAR_SERVICO);

        if (!estado_servico) {

            if (CngApplication.getInstance().getGpsIntent() == null) {
                CngApplication.getInstance().setGpsIntent(new Intent(getContext(), GpsService.class));
            }
            getActivity().startService(CngApplication.getInstance().getGpsIntent());
        }


        view.findViewById(R.id.btnAlteraStatusServico).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean estado_servico = Pref.getBoolean(getContext(), Constantes.LIGAR_SERVICO);

                if (!estado_servico) {
                    if (CngApplication.getInstance().getGpsIntent() == null) {
                        CngApplication.getInstance().setGpsIntent(new Intent(getContext(), GpsService.class));
                    }

                    getActivity().startService(CngApplication.getInstance().getGpsIntent());
                    Pref.setBoolean(getContext(), Constantes.LIGAR_SERVICO, true);
                    Fragment frag = (Fragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
                    mudaStatusGps(frag, true);
                } else {

                    Pref.setBoolean(getContext(), Constantes.LIGAR_SERVICO, false);

                    Intent i = CngApplication.getInstance().getGpsIntent();

                    if(i != null) {
                        getActivity().stopService(i);
                    }

                    Fragment frag = (Fragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
                    mudaStatusGps(frag, false);
                }
            }
        });

        view.findViewById(R.id.btnDesligaRequisicao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int i = Pref.getInteger(getBaseActivity(), Constantes.MARCOU_PROXIMO_AGENDAMENTO);

                if (i == 1) {
                    // vou delegar para alguem, se este estiver registrado claro..
//                    delegateToGpsFragment(true);
                    Pref.setInteger(getBaseActivity(), Constantes.MARCOU_PROXIMO_AGENDAMENTO, 0);
                    BroadcastUtil.cancelaProcuraServicoDiferente(getContext());

                    Fragment frag = (Fragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
                    mudaStatusRequisicao(frag, false);

                } else {
                    // vou delegar para alguem, se este estiver registrado claro..
//                    delegateToGpsFragment(false);
                    Pref.setInteger(getBaseActivity(), Constantes.MARCOU_PROXIMO_AGENDAMENTO, 1);

                    Fragment frag = (Fragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
                    mudaStatusRequisicao(frag, true);

                    startTaskProcuraPorOutroServicoACadaXTempo();
                }
            }
        });

        view.findViewById(R.id.btnTrocarFragments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CngApplication.getInstance().getEstadoDosFragments() == 0) {
                    CngApplication.getInstance().setEstadoDosFragments(1);
                    replaceFragment(R.id.container, new GpsFragment(), null);
                    replaceFragment(R.id.container_dois, new ControleFragment(), null);
                } else {
                    CngApplication.getInstance().setEstadoDosFragments(0);
                    replaceFragment(R.id.container, new ControleFragment(), null);
                    replaceFragment(R.id.container_dois, new GpsFragment(), null);
                }

            }
        });



        return view;
    }

    private void mudaStatusRequisicao(Fragment frag, boolean itemProcuraOutrosServicos) {
        if (frag instanceof GpsFragment) {
            ((GpsFragment) frag).preencheCampoRequisicao(itemProcuraOutrosServicos);
        } else {
            frag = (Fragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.container_dois);
            if (frag instanceof GpsFragment) {
                ((GpsFragment) frag).preencheCampoRequisicao(itemProcuraOutrosServicos);
            }
        }
    }

    private void mudaStatusGps(Fragment frag, boolean itemGps) {
        if (frag instanceof GpsFragment) {
            ((GpsFragment) frag).preencheCampoGps(itemGps);
        } else {
            frag = (Fragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.container_dois);
            if (frag instanceof GpsFragment) {
                ((GpsFragment) frag).preencheCampoGps(itemGps);
            }
        }
    }

//    //------------------------------------------------------------------//
//    // CallBack para parar de ficar procurando por outros servicos..
//    private CallBackMudaStatusOutroServico delegateOutroServico;
//
//    private void delegateToGpsFragment(boolean item) {
//        if (delegateOutroServico != null) {
//            delegateOutroServico.atualizaStatus(item);
//        }
//    }
//
//    public interface CallBackMudaStatusOutroServico {
//        public void atualizaStatus(boolean item);
//    }
//
//    public void setCallBackStatusOutroServico(CallBackMudaStatusOutroServico delegate) {
//        this.delegateOutroServico = delegate;
//    }
//
//    //------------------------------------------------------------------//
//    // CallBack para parar de ficar procurando por outros servicos..
//    private CallBackMudaStatusGps delegateGps;
//
//    private void delegateToGpsFragmentGps(boolean item) {
//        if (delegateGps != null) {
//            delegateGps.atualizaStatusGps(item);
//        }
//    }
//
//    public interface CallBackMudaStatusGps {
//        public void atualizaStatusGps(boolean item);
//    }
//
//    public void setCallBackGpsFragmentGps(CallBackMudaStatusGps delegate) {
//        this.delegateGps = delegate;
//    }

    /*
    * Frag1 frag = (Frag1) getActivity().getSupportFragmentManager().findFragmentById(R.id.container1);
               frag.mudar("Opa mudou");
    * */

}
