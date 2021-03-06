package com.gplearning.gplearning.Controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gplearning.gplearning.Adapters.ProjetoAdapter;
import com.gplearning.gplearning.DAO.App;
import com.gplearning.gplearning.Models.DaoSession;
import com.gplearning.gplearning.Models.Pessoa;
import com.gplearning.gplearning.Models.PessoaDao;
import com.gplearning.gplearning.Models.Projeto;
import com.gplearning.gplearning.Models.ProjetoComponentes;
import com.gplearning.gplearning.Models.ProjetoComponentesDao;
import com.gplearning.gplearning.Models.ProjetoDao;
import com.gplearning.gplearning.Models.Turma;
import com.gplearning.gplearning.Models.TurmaDao;
import com.gplearning.gplearning.R;
import com.gplearning.gplearning.Utils.MetodosPublicos;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ProjetoFragment extends Fragment {

    // TODO: Customize parameters
    private ProjetoAdapter projetoAdapter;
    private View view;
    private RecyclerView recyclerView;
    public List<Projeto> lsProjetos = new ArrayList<>();
    private ProjetoDao dao;


    public ProjetoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_projeto_list, container, false);

        // set the adapter
        if (view instanceof RecyclerView) {
            Log.i("Event", "Chegou na ProjetoFragment");
            recyclerView = (RecyclerView) view.findViewById(R.id.projetoListview);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            //((LinearLayoutManager) layoutManager).setStackFromEnd(true);
            recyclerView.setLayoutManager(layoutManager);

            DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
            dao = daoSession.getProjetoDao();
            new CarregaProjetos().execute();

            projetoAdapter = new ProjetoAdapter(lsProjetos, getActivity()); //new ProjetoRecyclerViewAdapter(lsProjetos, listenerClick, listenerLongClick);
            recyclerView.setAdapter(projetoAdapter);
            recyclerView.addOnItemTouchListener(new MetodosPublicos.RecyclerItemClickListener(getActivity(), recyclerView, new MetodosPublicos.RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Fragment fragment = EtapasFragment.newInstance(lsProjetos.get(position).get_id());
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                    Log.i("Event", "Clicou");
                }

                @Override
                public void onLongItemClick(View view, int position) {
                    Log.i("Event", "Long Click");
                }
            }));
        }


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Projeto item);
    }

    private class CarregaProjetos extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
            PessoaDao pessoaDao = daoSession.getPessoaDao();
            ProjetoDao projetoDao = daoSession.getProjetoDao();
            Pessoa user = pessoaDao.load(MetodosPublicos.SelecionaSessaoId(getActivity()));

            if (MetodosPublicos.ModoAcessoAluno(getActivity())) {
                MetodosPublicos.Log("projetos", " Turmar do user:" + user.getId() + " da turma:" + user.getIdTurma());
                ProjetoComponentesDao componentesDao = daoSession.getProjetoComponentesDao();
                List<ProjetoComponentes> lsComponentes = componentesDao.queryBuilder().where(ProjetoComponentesDao.Properties.IdPessoa.eq(user.get_id())).list();
                if (lsComponentes != null) {
                    for (ProjetoComponentes c : lsComponentes) {
                        lsProjetos.add(projetoDao.load(c.getIdProjeto()));
                    }
                }
            } else {
                lsProjetos.addAll(projetoDao.queryBuilder().where(ProjetoDao.Properties.IdTurma.eq(user.getIdTurma())).list());
            }
            for (Projeto prj : lsProjetos) {
                prj.setGerente(pessoaDao.queryBuilder().where(PessoaDao.Properties._id.eq(prj.getIdGerente())).unique());
            }

            MetodosPublicos.Log("projetos", " retorno com:" + lsProjetos.size());
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            ProgressBar pg = ((ProgressBar) getActivity().findViewById(R.id.projetoProgressbar));
            if (pg != null)
                pg.setVisibility(View.GONE);
            MetodosPublicos.Log("projetos", " PostExecuted");

            if (lsProjetos.size() == 0)
                ((TextView) getActivity().findViewById(R.id.TxtNenhumRegistro)).setVisibility(View.VISIBLE);
            else {
                ((RecyclerView) view.findViewById(R.id.projetoListview)).setVisibility(View.VISIBLE);
                projetoAdapter.notifyDataSetChanged();
                projetoAdapter.notifyItemInserted(1);
            }
        }
    }


}
