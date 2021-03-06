package com.gplearning.gplearning.DAO;

import com.gplearning.gplearning.Models.Requisito;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RequisitoDAO extends DefaultDAO {

    public List<Requisito> SelecionaRequisitosData(int id) {
        String url = UrlDefault + "/requisito/pessoa/"+id;
        RestTemplate restTemplate = getResTemplateDefault();
//        Requisito requisito = new Requisito();
//        requisito.setCriacao(date);
        ResponseEntity<Requisito[]> responseEntity = restTemplate.getForEntity(url, Requisito[].class); //.postForEntity(url, requisito, Requisito[].class);
        Requisito[] requisitos = responseEntity.getBody();
        return Arrays.asList(requisitos);
    }

}
