/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.gdt.resource;

import br.org.gdt.bll.EtapaBLL;
import br.org.gdt.model.Comentario;
import br.org.gdt.bll.ComentarioBLL;
import br.org.gdt.model.Etapa;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Path("/comentario")
public class ComentarioResource {

    @Autowired
    private ComentarioBLL comentarioBLL;

    @Autowired
    private EtapaBLL etapaBLL;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/index/{atv_id}")
    public List<Comentario> getAll(@PathParam("atv_id") int atv_id) {
        System.out.println("chegou no metodo id:" + atv_id);
        List<Comentario> lsComentario = new ArrayList<Comentario>();
        if (atv_id > 0) {
            Etapa atv = etapaBLL.findById(atv_id);
            if (atv != null && atv.getId() > 0) {
                lsComentario = comentarioBLL.findbyAtividade(atv);
            }
        }
        return lsComentario;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/comentario/{com_id}")
    public Comentario getbyId(@PathParam("com_id") int com_id) {
        System.out.println("chegou no metodo id:" + com_id);
        //  List<Comentario> lsComentario = new ArrayList<Comentario>();
        return comentarioBLL.findById(com_id);
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/date")
    public List<Comentario> getAllFromDate(Comentario comentario) {
      //  System.out.println("chegou no metodo date:" + date.toString());
        List<Comentario> lsComentario = new ArrayList<>();
        if (null != comentario && comentario.getCriacao() != null) {
            lsComentario = comentarioBLL.findbyDate(comentario.getCriacao());
        }
        return lsComentario;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/salvar")
    public int Salvar(Comentario comentario) {
        if (comentario != null && comentario.getDescricao() != null) {
            if (comentario.getId() > 0) {
                comentarioBLL.update(comentario);
            } else {
                comentarioBLL.insert(comentario);
            }
            return comentario.getId();
        }
        return 0;
    }

//    @POST
//    @Path("/excluir/{com_id}")
//    //  @Produces(MediaType.APPLICATION_XML)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    // @GET
    //   @Path("/excluir/{com_id}")
    //  public Boolean Excluir(@PathParam("com_id") int com_id) {
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/excluir")
//    public Boolean Excluir(@FormParam("com_id") int com_id) {
    @POST
    @Path("/excluir")
    public Boolean Excluir(Comentario com) {
        System.out.println("Vai deletar o Comentario!");
        if (com != null && com.getId() > 0) {
            Comentario comentario = comentarioBLL.findById(com.getId());
            comentarioBLL.delete(comentario);
            return true;
        }
        return false;
    }

}
