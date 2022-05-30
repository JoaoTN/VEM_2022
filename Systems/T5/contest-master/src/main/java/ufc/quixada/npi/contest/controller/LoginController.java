package ufc.quixada.npi.contest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.model.Token;
import ufc.quixada.npi.contest.service.*;
import ufc.quixada.npi.contest.util.Constants;
import ufc.quixada.npi.contest.util.GetPessoa;
import ufc.quixada.npi.contest.util.PessoaLogadaUtil;
import ufc.quixada.npi.contest.validator.ContestException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class LoginController {

	@Autowired
	PessoaService pessoaService;

	@Autowired
	EventoService eventoService;

	@Autowired
	TrabalhoService trabalhoService;

	public static final String PESSOA = "pessoa";

	@Autowired
	private TokenService tokenService;

	@Autowired
	private EnviarEmailService enviarEmailService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// OK
	@RequestMapping(value = {"", "/"})
	public String paginaInicial(Model model) {
		model.addAttribute("eventos", eventoService.findAll());
		return "evento/listagem-evento";
	}

	// OK
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(Pessoa pessoa) {
		ModelAndView modelAndView = new ModelAndView("login");
		modelAndView.addObject("user", pessoa);
		return modelAndView;
	}

	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginfailed(Authentication auth, RedirectAttributes redirectAttributes) {
		if (auth != null && auth.isAuthenticated()) {
			return "redirect:/";
		}

		redirectAttributes.addFlashAttribute("loginError", true);
		return Constants.REDIRECIONAR_PARA_LOGIN;
	}

	@RequestMapping(value = "/cadastro", method = RequestMethod.POST)
	public ModelAndView cadastro(@Valid Pessoa pessoa, @RequestParam String senha, @RequestParam String senhaConfirma, RedirectAttributes redirectAttributes) {

		if (senhaConfirma.equals(senha)) {
			String password = pessoaService.encodePassword(senha);
			pessoa.setPassword(password);
			try{
				pessoaService.addOrUpdate(pessoa);
			} catch (ContestException e) {
				ModelAndView modelAndView = login(pessoa);
				modelAndView.addObject("cadastroError", e.getMessage());
				modelAndView.addObject("cadastroPage", true);
				return modelAndView;
			}
		}

		redirectAttributes.addFlashAttribute("cadastroSucesso", true);
		return new ModelAndView(Constants.REDIRECIONAR_PARA_LOGIN);
	}
	
	@RequestMapping(value = "/perfil", method=RequestMethod.GET)
	public String perfil(Model model){
		Pessoa pessoa = PessoaLogadaUtil.pessoaLogada();
		model.addAttribute(PESSOA, pessoa);
		return Constants.PERFIL_USER;
	}
	
	@RequestMapping(value = "/perfil", method=RequestMethod.POST)
	public ModelAndView perfil(Model model, Pessoa pessoa){
		
		ModelAndView mav = new ModelAndView(Constants.PERFIL_USER);
		Pessoa pessoaLogada = PessoaLogadaUtil.pessoaLogada();
		
		try{
			
			pessoaLogada.setNome(pessoa.getNome());
			pessoaLogada.setEmail(GetPessoa.getEmail(pessoa));
			
			pessoaService.addOrUpdate(pessoaLogada);
			mav.addObject(PESSOA, pessoa);
			mav.addObject("success","Dados alterados com sucesso.");
		}catch(DataIntegrityViolationException | ContestException ex){
			logger.warn("Usuario[id={}] - O email {} pertence a outro usuario do sistema", pessoaLogada.getId()
					, GetPessoa.getEmail(pessoa), ex);
			mav.addObject("error", "Existe algum cadastro com o email selecionado");
		}
		
		return mav;
	}

	@RequestMapping(path = "resetar-senha/{token}", method = RequestMethod.GET)
	public ModelAndView resetarSenhaForm(@PathVariable("token") Token token) throws IllegalArgumentException {
		ModelAndView model = new ModelAndView();
		if (token.getAcao().equals(Constants.ACAO_RECUPERAR_SENHA)) {
			model.setViewName("resetar_senha");
			model.addObject("token", token);
		} else {
			throw new IllegalArgumentException("O token passado não corresponde a ação de recuperar senha.");
		}
		return model;
	}

	@RequestMapping(path = "/resetar-senha/{token}", method = RequestMethod.POST)
	public String resetarSenha(@PathVariable("token") Token token, @RequestParam String senha,
			@RequestParam String senhaConfirma, RedirectAttributes redirectAttributes) {
		enviarEmailService.resetarSenhaEmail(token, senha, senhaConfirma, redirectAttributes);
		return Constants.REDIRECIONAR_PARA_LOGIN;
	}

	@RequestMapping(path = "/esqueci-minha-senha", method = RequestMethod.GET)
	public String esqueciSenhaForm() {
		return "esqueci_senha";
	}

	@RequestMapping(path = "/esqueci-minha-senha", method = RequestMethod.POST)
	public String esqueciSenha(@RequestParam String email, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		try {
			enviarEmailService.esqueciSenhaEmail(email, redirectAttributes, url);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		redirectAttributes.addFlashAttribute("esqueciSenha", true);
		return Constants.REDIRECIONAR_PARA_LOGIN;

	}

}
