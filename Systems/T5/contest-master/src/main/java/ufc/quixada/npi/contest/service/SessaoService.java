package ufc.quixada.npi.contest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufc.quixada.npi.contest.model.Evento;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.model.Sessao;
import ufc.quixada.npi.contest.model.Trabalho;
import ufc.quixada.npi.contest.repository.SessaoRepository;
import ufc.quixada.npi.contest.repository.TrabalhoRepository;
import ufc.quixada.npi.contest.util.GetEvento;

import java.util.List;

@Service
public class SessaoService {
	@Autowired
	private SessaoRepository sessaoRepository;

	@Autowired
	private TrabalhoRepository trabalhoRepository;
	
	public void addOrUpdate(Sessao sessao) {
		sessaoRepository.save(sessao);
	}

	public List<Sessao> list() {
		return (List<Sessao>) sessaoRepository.findAll();
	}

	public void delete(Long id) {
		sessaoRepository.delete(id);
	}

	public Sessao get(Long id) {
		return sessaoRepository.findOne(id);
	}
	
	public List<Sessao> listByEvento(Evento evento){
		return sessaoRepository.findByEventoOrderByNome_Asc(evento);
	}

	public List<Sessao> findByChefe(Evento evento, Pessoa chefe) {
		return sessaoRepository.findByEventoAndResponsavelOrderByNome_Asc(evento, chefe);
	}

	public void adicionarTrabalhos(Sessao sessao, List<Trabalho> trabalhos) {
		trabalhoRepository.atualizarSessao(sessao, trabalhos);
	}

	public boolean hasTrabalhosAlocados(Sessao sessao) {
		return trabalhoRepository.countBySessao(sessao) > 0;
	}

	@PreAuthorize("#sessao.evento.isOrganizador(authentication.principal)")
	@RequestMapping(value = "/{id}/excluir")
	public String excluirSessao(@PathVariable("id") Sessao sessao, RedirectAttributes redirectAttributes) {
		if (hasTrabalhosAlocados(sessao)) {
			redirectAttributes.addFlashAttribute("error",
					"Não é possível excluir uma sessão que possui trabalhos alocados");
		} else {
			delete(sessao.getId());
			redirectAttributes.addFlashAttribute("info", "Sessão excluída com sucesso");
		}
		return "redirect:/evento/" + GetEvento.getId(sessao.getEvento()) + "/sessoes";
	}
}
