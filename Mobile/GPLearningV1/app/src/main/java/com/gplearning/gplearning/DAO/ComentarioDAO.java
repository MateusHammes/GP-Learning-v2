package com.gplearning.gplearning.DAO;


import com.gplearning.gplearning.Models.Atividade;
import com.gplearning.gplearning.Models.Comentario;
import com.gplearning.gplearning.Utils.MetodosPublicos;
import com.gplearning.gplearning.Utils.UrlDomain;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComentarioDAO extends UrlDomain {


    public List<Comentario> SelecionaComentarioPorAtividade(Atividade atividade) {
        String url = UrlDefault += "/comentario/index/" + atividade.getId();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        MetodosPublicos.Log("DAO", " vai seleecioanr url:" + url);
        ResponseEntity<Comentario[]> responseEntity = restTemplate.getForEntity(url, Comentario[].class);
        Comentario[] comentarioArray = responseEntity.getBody();
        if (comentarioArray != null && comentarioArray.length > 0)
            return Arrays.asList(comentarioArray);
        return null;
    }


    public int SalvarComentario(Comentario comentario) {
        String url = UrlDefault + "/comentario/salvar/";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        MetodosPublicos.Log("DAO", " vai salvar url:" + url);

        ResponseEntity<Integer> responseEntity = restTemplate.postForEntity(url, comentario, Integer.class);
        MetodosPublicos.Log("DAO", " retornou com :" + responseEntity.getBody());

        int valor = responseEntity.getBody();
        return valor;
    }


    public boolean DeletaComentario(Comentario comentario) {
        if (comentario != null && comentario.getId() > 0) {
            String url = UrlDefault + "/comentario/excluir/"; // + comentario.getId();
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            MetodosPublicos.Log("DAO", " vai deletar url:" + url);

            Map<String, Integer> vars = new HashMap<String, Integer>();
            vars.put("com_id", comentario.getId());

            boolean responseEntity = restTemplate.postForObject(url, null, Boolean.class, vars);
            MetodosPublicos.Log("DAO", " retornou com :" + responseEntity);


//            ResponseEntity<Boolean> responseEntity = restTemplate.getForEntity(url, Boolean.class);
//            return responseEntity.getBody();

        }
        return false;
    }

}