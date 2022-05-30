package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import br.ufc.quixada.npi.gestaocompetencia.config.JwtTokenProvider;
import br.ufc.quixada.npi.gestaocompetencia.exception.ResourceNotFoundException;
import br.ufc.quixada.npi.gestaocompetencia.model.Mapeamento;
import br.ufc.quixada.npi.gestaocompetencia.model.Unidade;
import br.ufc.quixada.npi.gestaocompetencia.model.Usuario;
import br.ufc.quixada.npi.gestaocompetencia.repository.UsuarioRepository;
import br.ufc.quixada.npi.gestaocompetencia.service.ComissaoService;

import br.ufc.quixada.npi.gestaocompetencia.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("")
public class AuthControllerImpl {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ComissaoService comissaoService;

    @PostMapping("/login")
    public ResponseEntity<Object> signin(@RequestBody AuthenticationRequest data) {
    	
        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, new ArrayList());
            Usuario usuario = this.usuarioRepository.findByEmail(username);
            System.out.println(usuario.toString());
            Map<Object, Object> model = new HashMap<>();
            model.put("username", usuario.getNome());
            model.put("token", "Bearer " + token);
            return ok(model);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
    
    @GetMapping("/auth/comissao")
    public ResponseEntity<Boolean> isComissao(@AuthenticationPrincipal Usuario usuario){
    	return ResponseEntity.ok(comissaoService.isMembroComissao(usuario));
    }

    @GetMapping("/auth/chefia")
    public ResponseEntity<Boolean> isChefia(@AuthenticationPrincipal Usuario usuario){
        return ResponseEntity.ok(usuarioService.isChefia(usuario));
    }

    @GetMapping("/auth/vice-chefia")
    public ResponseEntity<Boolean> isViceChefia(@AuthenticationPrincipal Usuario usuario){
        return ResponseEntity.ok(usuarioService.isViceChefia(usuario));
    }
    
    @GetMapping("/auth/subchefia")
    public ResponseEntity<Boolean> hasSubchefia(@AuthenticationPrincipal Usuario usuario){
        return ResponseEntity.ok(usuarioService.hasUnidadesFilhas(usuario));
    }

    @GetMapping("/auth/unidade")
    public ResponseEntity<Unidade> getUnidadeLotacao(@AuthenticationPrincipal Usuario usuario){
        return ResponseEntity.ok(usuario.getUnidade());
    }
    
    @GetMapping("/permissoes/{mapeamento}")
    public ResponseEntity<Map<String, Boolean>> getPermissoes(@PathVariable Mapeamento mapeamento,
    															@AuthenticationPrincipal Usuario usuario,
    															HttpServletRequest request){
 
    	if(mapeamento == null)
    		throw new ResourceNotFoundException("Mapeamento", "Código", 
    				request.getRequestURI().toString().replace("/permissoes/", ""));
    	
    	Map<String, Boolean> permissoes = new TreeMap<>();
    	
    	permissoes.put("comissao", comissaoService.isMembroComissao(usuario));
    	
    	return ResponseEntity.ok(permissoes);
    }
    
    

}
