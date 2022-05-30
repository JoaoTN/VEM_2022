package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ConfigurationConstants.ANONIMO;
import static ufc.npi.prontuario.util.ConfigurationConstants.AUTENTICADO;
import static ufc.npi.prontuario.util.ConfigurationConstants.AUTENTICADO_VERIFICACAO_ID;
import static ufc.npi.prontuario.util.FragmentsConstants.FRAGMENT_ALTERAR_SENHA;
import static ufc.npi.prontuario.util.PagesConstants.ALTERACAO_SENHA;
import static ufc.npi.prontuario.util.PagesConstants.DETALHES_USUARIO;
import static ufc.npi.prontuario.util.PagesConstants.PAGINA_LOGIN;
import static ufc.npi.prontuario.util.PagesConstants.RECUPERACAO_SENHA;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LOGIN;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LOGOUT;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_USUARIO_DETALHES;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Token;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@PreAuthorize(AUTENTICADO)
	@GetMapping("/detalhes")
	public ModelAndView detalhesUsuario(Authentication authentication) {
		ModelAndView modelAndView = new ModelAndView(DETALHES_USUARIO);

		Usuario usuario = usuarioService.buscarPorEmail(authentication.getName());
		modelAndView.addObject("usuario", usuario);

		return modelAndView;
	}

	@PreAuthorize(AUTENTICADO_VERIFICACAO_ID)
	@PostMapping("/alterar-senha")
	public ModelAndView alterarSenha(Integer usuarioId, String senhaAtual, String novaSenha) {
		ModelAndView modelAndView = new ModelAndView();

		try {
			usuarioService.alterarSenha(usuarioId, senhaAtual, novaSenha);
		} catch (ProntuarioException e) {
			modelAndView.setViewName(FRAGMENT_ALTERAR_SENHA);
			modelAndView.addObject("mensagem", e.getMessage());
			modelAndView.setStatus(HttpStatus.BAD_REQUEST);
			return modelAndView;
		}

		modelAndView.setViewName(REDIRECT_LOGOUT);

		return modelAndView;
	}

	@PreAuthorize(AUTENTICADO_VERIFICACAO_ID)
	@PostMapping("/alterar-dados")
	public ModelAndView alterarDados(Integer usuarioId, String nome, String email, String matricula) {
		ModelAndView modelAndView = new ModelAndView();

		usuarioService.alterarDados(usuarioId, nome, email, matricula);

		modelAndView.setViewName(REDIRECT_USUARIO_DETALHES);

		return modelAndView;
	}

	@PreAuthorize(ANONIMO)
	@GetMapping("/recuperar-senha")
	public ModelAndView formRecuperacaoSenha() {
		return new ModelAndView(RECUPERACAO_SENHA);
	}

	@PreAuthorize(ANONIMO)
	@PostMapping("/recuperar-senha")
	public ModelAndView recuperacaoSenha(String email) {
		ModelAndView modelAndView = new ModelAndView(PAGINA_LOGIN);
		usuarioService.recuperarSenha(email);
		modelAndView.addObject("msg",
				"Se " + email + " corresponde ao e-mail cadastrado, uma mensagem foi enviada. Verifique seu email.");
		return modelAndView;
	}

	@PreAuthorize(ANONIMO)
	@GetMapping("/senha")
	public ModelAndView alteracaoSenhaForm(@RequestParam("token") Token token) {
		ModelAndView modelAndView = new ModelAndView(ALTERACAO_SENHA);
		modelAndView.addObject("token", token);
		return modelAndView;
	}

	@PreAuthorize(ANONIMO)
	@PostMapping("/senha")
	public ModelAndView alteracaoSenha(Token token, String senha) {
		usuarioService.novaSenha(token, senha);
		return new ModelAndView(REDIRECT_LOGIN);
	}
}
