package ufc.quixada.npi.contest.controller;

import org.apache.commons.io.IOUtils;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ufc.quixada.npi.contest.model.Pessoa;
import ufc.quixada.npi.contest.model.Sessao;
import ufc.quixada.npi.contest.model.Trabalho;
import ufc.quixada.npi.contest.service.PessoaService;
import ufc.quixada.npi.contest.service.SessaoService;
import ufc.quixada.npi.contest.service.TrabalhoService;

import javax.servlet.http.HttpServletResponse;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CertificadoController {
	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private TrabalhoService trabalhoService;

	@Autowired
	private SessaoService sessaoService;

	@RequestMapping(value = "/certificados/organizadores", method = RequestMethod.POST)
	public void gerarCertificadoOrganizador(Long[] organizadoresIds, Model model, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		criarDadosODS(organizadoresIds, model, "Organizadores", response);
	}

	@RequestMapping(value = "/certificados/revisores", method = RequestMethod.POST)
	public void gerarCertificadoRevisores(Long[] revisoresIds, Model model, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		criarDadosODS(revisoresIds, model, "Revisores", response);
	}

	@RequestMapping(value = "/certificados/chefesessao", method = RequestMethod.POST)
	public void gerarCertificadoChefeSessao(Long[] sessoesId, Model model, HttpServletResponse response)
			throws FileNotFoundException, IOException {

		if (sessoesId != null) {
			final Object[][] dados = new Object[sessoesId.length][2];
			for (int i = 0; i < sessoesId.length; i++) {
				Sessao sessao = sessaoService.get(sessoesId[i]);
				dados[i] = new Object[] { sessao.getResponsavel().getNome().toUpperCase(),
						sessao.getNome().toUpperCase() };
			}

			String[] colunas = new String[] { "chefe_de_sessao", "nome_da_sessao" };
			gerarODS("chefe_de_sessao", colunas, dados, response);
		}
	}

	/*@RequestMapping(value = "/certificados/trabalhos", method = RequestMethod.POST)
	public void gerarCertificadoTrabalhos(@RequestParam Long[] trabalhosIds, Model model, HttpServletResponse response)
			throws FileNotFoundException, IOException {

		if (trabalhosIds != null) {
			final Object[][] dados = new Object[trabalhosIds.length][4];
			SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy");
			for (int i = 0; i < trabalhosIds.length; i++) {
				Long id = trabalhosIds[i];
				Trabalho t = trabalhoService.getTrabalhoById(id);
				String data = formatadorData.format(t.getEvento().getCameraReady());
				dados[i] = new Object[] { t.getAutor().getNome().toUpperCase(), t.getCoautoresInString().toUpperCase(),
						t.getTitulo().toUpperCase(), t.getTrilha().getNome().toUpperCase(), data };
			}
			String[] colunas = new String[] { "Nome", "Coautores", "Título", "Trilha", "Data" };
			gerarODS("trabalhos", colunas, dados, response);
		}
	}*/

	public void criarDadosODS(Long[] ids, Model model, String nomeDocumento, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		if (ids != null) {
			List<Pessoa> pessoas = new ArrayList<>();
			for (Long id : ids) {
				Pessoa p = pessoaService.get(id);
				p.setNome(p.getNome().toUpperCase());
				pessoas.add(p);
			}

			if (pessoas != null) {
				final Object[][] dados = new Object[pessoas.size()][1];
				for (int i = 0; i < pessoas.size(); i++) {
					dados[i] = new Object[] { pessoas.get(i).getNome().toUpperCase() };
				}

				String[] colunas = new String[] { nomeDocumento };
				gerarODS(nomeDocumento, colunas, dados, response);
			}
		}
	}

	public void gerarODS(String nomeDocumento, String[] colunas, Object[][] dados, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		TableModel modelo = new DefaultTableModel(dados, colunas);
		final File file = new File(nomeDocumento + ".ods");
		SpreadSheet.createEmpty(modelo).saveAs(file);

		response = estruturaResponse(response, nomeDocumento);
		
		InputStream is = new FileInputStream(file);
		IOUtils.copy(is, response.getOutputStream());
		response.flushBuffer();
	}
	
	private HttpServletResponse estruturaResponse(HttpServletResponse response, String nomeDocumento) {
		response.setContentType("application/ods");
		response.setHeader("Content-Disposition", "attachment; filename = " + nomeDocumento + ".ods");
		return response;
	}
}