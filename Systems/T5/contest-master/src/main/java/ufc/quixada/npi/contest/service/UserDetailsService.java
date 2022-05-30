package ufc.quixada.npi.contest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.repository.PessoaRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private MessageService messageService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Pessoa pessoa = pessoaRepository.findByEmail(s);

        if(pessoa == null) throw new UsernameNotFoundException(messageService.getMessage("LOGIN_INVALIDO"));

        return pessoa;
    }

    public Pessoa autenticar(String email, String senha) {
        Pessoa pessoa = pessoaRepository.findByEmail(email);

        if(pessoa == null) return null;

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(senha, pessoa.getPassword())) return null;

        return pessoa;
    }
}
