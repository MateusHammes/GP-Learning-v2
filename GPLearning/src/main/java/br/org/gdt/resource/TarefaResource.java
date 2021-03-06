package br.org.gdt.resource;

import br.org.gdt.bll.EAPBLL;
import br.org.gdt.bll.ProjetoBLL;
import br.org.gdt.bll.RecursoBLL;
import br.org.gdt.bll.TarefaBLL;
import br.org.gdt.model.EAP;
import br.org.gdt.model.Projeto;
import br.org.gdt.model.Recurso;
import br.org.gdt.model.Tarefa;
import java.util.List;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/tarefa")
public class TarefaResource {
    
    @Autowired
    private TarefaBLL tarefaBLL;
    @Autowired
    private EAPBLL eapBLL;
    @Autowired
    private ProjetoBLL projetoBLL;
    @Autowired
    private RecursoBLL recursoBLL;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/index/{pro_id}")
    public EAP getAll(@PathParam("pro_id") int pro_id) {
        Projeto projeto = projetoBLL.findById(pro_id);
        if (projeto != null) {
            EAP eap = tarefaBLL.findbyEAP(projeto);
            return eap;
        }
        return null;
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/salvar")
    public int Salvar(Tarefa model) {
        if (model.getRecursos() != null) {
            for (Recurso recurso : model.getRecursos()) {
                recurso.setTarefa(model);
            }
        }
        if (model.getId() > 0) {
            List<Recurso> lsRecurso = recursoBLL.findbyTarefa(model);
            for (Recurso recurso : lsRecurso) {
                recursoBLL.delete(recurso.getId());
            }
            tarefaBLL.update(model);
        } else {
            tarefaBLL.insert(model);
        }
        return model.getId();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/excluir")
    public Boolean Excluir(@FormParam("id") int id) {
        if (id > 0) {
            tarefaBLL.delete(id);
        }
        return true;
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/marcar")
    public Boolean Marcar(@FormParam("id") int id, @FormParam("marco") boolean marco) {
        if (id > 0) {
            Tarefa tarefa = tarefaBLL.findById(id);
            tarefa.setMarco(marco);
            tarefaBLL.update(tarefa);
        }
        return true;
    }
}
