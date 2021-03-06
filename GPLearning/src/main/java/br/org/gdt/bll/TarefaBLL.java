package br.org.gdt.bll;

import br.org.gdt.dao.TarefaDAO;
import br.org.gdt.model.EAP;
import br.org.gdt.model.Projeto;
import br.org.gdt.model.Recurso;
import br.org.gdt.model.Tarefa;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tarefaBLL")
public class TarefaBLL extends BLL<Tarefa> {

    @Autowired
    private TarefaDAO dao;
    @Autowired
    private EAPBLL eapBLL;
    @Autowired
    private RecursoBLL recursoBLL;

    public List<Tarefa> findbyTarefa(Tarefa tarefa) {
        List<Tarefa> lsTarefa = new ArrayList<>();
        if (tarefa != null && tarefa.getId() > 0) {
            dao.findbyTarefa(tarefa);
        }
        return lsTarefa;
    }

    public List<Tarefa> findbyEAP(EAP eap) {
        List<Tarefa> lsTarefa = new ArrayList<>();
        if (eap != null && eap.getId() > 0) {
            dao.findbyEAP(eap);
        }
        return lsTarefa;
    }

    public EAP findbyEAP(Projeto projeto) {
        EAP eap = eapBLL.getEAP(projeto);
        if (eap != null) {
            List<Tarefa> tarefas = findbyChildren(eap, null);
            eap.setTarefas(tarefas);
            return eap;
        }
        return null;
    }

    public List<Tarefa> findbyChildren(EAP eap, Tarefa tarefa) {
        List<Tarefa> tarefas = null;
        if (eap != null && eap.getId() > 0) {
            tarefas = dao.findbyEAP(eap);
            eap.setTarefas(tarefas);

            if (eap.getEaps() != null) {
                for (EAP child : eap.getEaps()) {
                    findbyChildren(child, null);
                }
            }
        } else if (tarefa != null && tarefa.getId() > 0) {
            tarefas = dao.findbyTarefa(tarefa);
        }
        if (tarefas != null) {
            for (Tarefa child : tarefas) {
                List<Tarefa> tarefas2 = findbyChildren(null, child);
                List<Recurso> recursos = recursoBLL.findbyTarefa(child);
                child.setRecursos(recursos);
                child.setTarefas(tarefas2);
                clearRecursive(child);
            }
        }
        return tarefas;
    }

    private void clearRecursive(Tarefa tarefa) {
        if (tarefa != null) {
            if (tarefa.getEap() != null) {
                tarefa.getEap().setPai(null);
                tarefa.getEap().setEaps(null);
                tarefa.getEap().setProjeto(null);
                tarefa.getEap().setTarefas(null);
            }

            if (tarefa.getPai() != null) {
                tarefa.getPai().setEap(null);
                tarefa.getPai().setTarefas(null);
                tarefa.getPai().setRecursos(null);
            }
            if (tarefa.getTarefas() != null) {
                for (Tarefa tarefa_son : tarefa.getTarefas()) {
                    clearRecursive(tarefa_son);
                }
            }
            if (tarefa.getRecursos() != null) {
                for (Recurso rec : tarefa.getRecursos()) {
                    rec.setTarefa(null);
                }
            }
        }
    }
}
