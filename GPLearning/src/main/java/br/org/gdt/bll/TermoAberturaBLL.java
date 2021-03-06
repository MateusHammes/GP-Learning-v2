package br.org.gdt.bll;

import br.org.gdt.dao.TermoAberturaDAO;
import br.org.gdt.model.Projeto;
import br.org.gdt.model.TermoAbertura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("termoAberturaBLL")
public class TermoAberturaBLL extends BLL<TermoAbertura> {

    @Autowired
    private TermoAberturaDAO dao;

    @Autowired
    private MarcoBLL marcoBLL;

    @Autowired
    private PremissaBLL premissaBLL;

    @Autowired
    private RequisitoTermoAberturaBLL requisitoTermoAberturaBLL;

    @Autowired
    private RestricaoBLL restricaoBLL;

    public TermoAbertura findByIdRelatorio(int id) {
        return dao.findByIdRelatorio(id);
    }

    public TermoAbertura findByProjeto(Projeto projeto) {
        return dao.findByProjeto(projeto);
    }

    public TermoAbertura findByProjetoCompleto(Projeto projeto) {
        TermoAbertura termoAbertura = dao.findByProjeto(projeto);
        if (termoAbertura != null) {
            termoAbertura.setMarcos(marcoBLL.findbyTermoAbertura(termoAbertura));
            termoAbertura.setPremissas(premissaBLL.findbyTermoAbertura(termoAbertura));
            termoAbertura.setRequisitosTermoAberturas(requisitoTermoAberturaBLL.findbyTermoAbertura(termoAbertura));
            termoAbertura.setRestricoes(restricaoBLL.findbyTermoAbertura(termoAbertura));
        }
        return termoAbertura;
    }

}
