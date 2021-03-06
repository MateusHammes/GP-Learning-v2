package br.org.gdt.bll;

import br.org.gdt.dao.LoginDAO;
import br.org.gdt.model.Login;
import br.org.gdt.model.LoginRole;
import br.org.gdt.model.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loginBLL")
public class LoginBLL extends BLL<Login> {

    @Autowired
    private LoginDAO dao;

    @Autowired
    private EtapaBLL etapaBLL;

    public Login findbyPessoa(Pessoa pessoa) {
        if (pessoa != null && pessoa.getId() > 0) {
            return dao.findbyPessoa(pessoa);
        }
        return null;
    }

    public Pessoa findLogin(String email, String senha) {
        Pessoa user = null;
        if (email != null && !email.isEmpty() && senha != null && !senha.isEmpty()) {
            user = dao.findLogin(email, senha);
            if (user != null) {
                if (user.getLogin().getLoginRoles() != null) {
                    for (LoginRole lr : user.getLogin().getLoginRoles()) {
                        lr.setLogin(null);
                    }
                }
                user.getLogin().setPessoa(null);
                user.setIndicadoresprofessor(null);
                user.setProjetos(null);
                user.setProjetosgerente(null);
                user.setTurmasprofessor(null);
                if (user.getTurma() != null) {
                    user.getTurma().setProfessor(null);
                    user.getTurma().setAcademicos(null);
                    user.getTurma().setProjetos(null);
                    user.getTurma().setTurmaParametros(null);
                    user.getTurma().setEtapas(etapaBLL.findbyTurma(user.getTurma()));
                }
            }
        }
        return user;
    }
}
