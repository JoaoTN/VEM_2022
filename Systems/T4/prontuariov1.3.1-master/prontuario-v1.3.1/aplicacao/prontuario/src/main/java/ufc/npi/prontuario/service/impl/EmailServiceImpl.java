package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.EmailConstants.ASSUNTO_ATENDIMENTO_ANDAMENTO;
import static ufc.npi.prontuario.util.EmailConstants.ASSUNTO_ATENDIMENTO_AVALIACAO;
import static ufc.npi.prontuario.util.EmailConstants.ASSUNTO_RECUPERACAO_SENHA;
import static ufc.npi.prontuario.util.EmailConstants.DATA;
import static ufc.npi.prontuario.util.EmailConstants.DISCIPLINA;
import static ufc.npi.prontuario.util.EmailConstants.EMAIL_REMETENTE;
import static ufc.npi.prontuario.util.EmailConstants.RESPONSAVEL;
import static ufc.npi.prontuario.util.EmailConstants.TEMPLATE_EMAIL_RECUPERACAO_SENHA;
import static ufc.npi.prontuario.util.EmailConstants.TEXTO_ATENDIMENTO_ANDAMENTO;
import static ufc.npi.prontuario.util.EmailConstants.TEXTO_ATENDIMENTO_AVALIACAO;
import static ufc.npi.prontuario.util.EmailConstants.TURMA;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import ufc.npi.prontuario.model.Atendimento;
import ufc.npi.prontuario.model.Atendimento.Status;
import ufc.npi.prontuario.model.GetEmailUsuario;
import ufc.npi.prontuario.model.Token;
import ufc.npi.prontuario.repository.AtendimentoRepository;
import ufc.npi.prontuario.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private Environment env;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private AtendimentoRepository atendimentoRepository;

	private DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);

	private void enviarEmail(MimeMessage email) {
		this.mailSender.send(email);
	}

	private void enviarEmails(List<MimeMessage> emails) {
		for (MimeMessage email : emails) {
			this.enviarEmail(email);
		}
	}

	private String formatDate(Date date) {
		return dateFormat.format(date);
	}

	@Override
	public void emailRecuperacaoSenha(Token token) {
		Runnable email = new Runnable() {

			@Override
			public void run() {
				final MimeMessage mimeMessage = mailSender.createMimeMessage();
				final MimeMessageHelper mensagem = new MimeMessageHelper(mimeMessage, "UTF-8");

				final Context contexto = new Context();

				contexto.setVariable("token", token.getToken());
				contexto.setVariable("link", env.getProperty("sistema.link"));

				final String conteudo = templateEngine.process(TEMPLATE_EMAIL_RECUPERACAO_SENHA, contexto);

				try {
					mensagem.setTo(GetEmailUsuario.getUsuarioEmail(token.getUsuario()));
					mensagem.setFrom(EMAIL_REMETENTE);
					mensagem.setSubject(ASSUNTO_RECUPERACAO_SENHA);
					mensagem.setText(conteudo, true);
				} catch (MessagingException e) {
					e.printStackTrace();
				}

				enviarEmail(mimeMessage);
			}
		};

		Thread thread = new Thread(email);
		thread.start();
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void notificarAtendimentoAndamento() {
		List<MimeMessage> emails = new ArrayList<>();
		List<Atendimento> atendimentos = findAllByStatus(atendimentoRepository);

		if (atendimentoNotEmpty(atendimentos)) {
			construirEmails(emails, atendimentos);

			MimeMessage mimeMessage;
			MimeMessageHelper mensagem;
			String destinatario = null;
			String assuntoEmail = null;
			String texto = null;
			for (Atendimento a : atendimentos) {
				mimeMessage = createMimeMessage();
				mensagem = new MimeMessageHelper(mimeMessage, "UTF-8");

				destinatario = getResponsavelEmail(a);
				assuntoEmail = ASSUNTO_ATENDIMENTO_ANDAMENTO;
				texto = TEXTO_ATENDIMENTO_ANDAMENTO.replace(DATA, getAtendimentoData(a))
						.replaceAll(TURMA, getNomeTurma(a))
						.replaceAll(DISCIPLINA, getDisciplinaNome(a));

				try {
					setDestinatario(mensagem, destinatario);
					setAssunto(mensagem, assuntoEmail);
					setMensagem(mensagem, texto);
					setFrom(mensagem);
					addEmail(emails, mimeMessage);
					} catch (MessagingException e) {
					e.printStackTrace();
					continue;
				}
			}
		}

		enviarEmails(emails);
	}
	
	public void addEmail(List<MimeMessage> emails, MimeMessage mimeMessage) {
		emails.add(mimeMessage);
	}
	
	public void setMensagem(MimeMessageHelper mensagem, String texto) throws MessagingException {
		try {
			mensagem.setText(texto, true);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new MessagingException();
		}
	}
	
	public void setDestinatario(MimeMessageHelper mensagem, String destinatario) throws MessagingException {
		try {
			mensagem.setTo(destinatario);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new MessagingException();
		}
	}
	
	public void setFrom(MimeMessageHelper mensagem) throws MessagingException {
		try {
			mensagem.setFrom(EMAIL_REMETENTE);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new MessagingException();
		}
	}
	
	public void setAssunto(MimeMessageHelper mensagem, String assuntoEmail) throws MessagingException {
		try {
			mensagem.setSubject(assuntoEmail);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new MessagingException();
		}
	}
	
	public List<Atendimento> findAllByStatus(AtendimentoRepository atendimentoRepository){
		return atendimentoRepository.findAllByStatus(Status.EM_ANDAMENTO);
	}
	
	public boolean atendimentoNotEmpty(List<Atendimento> atendimentos) {
		return !atendimentos.isEmpty();
	}
	
	public MimeMessage createMimeMessage() {
		return mailSender.createMimeMessage();
	}
	
	public String getResponsavelEmail (Atendimento a){
		return GetEmailUsuario.getUsuarioEmail(a.getResponsavel());
	}
	
	public String getAtendimentoData(Atendimento a) {
		return formatDate(a.getData());
	}
	
	public String getNomeTurma(Atendimento a) {
		return a.getTurma().getNome();
	}
	
	public String getDisciplinaNome(Atendimento a) {
		return a.getTurma().getDisciplina().getNome();
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void construirEmails(List<MimeMessage> emails, List<Atendimento> atendimentos) {
		MimeMessage mimeMessage;
		MimeMessageHelper mensagem;
		String destinatario = null;
		String assuntoEmail = null;
		String texto = null;

		for (Atendimento a : atendimentos) {
			mimeMessage = mailSender.createMimeMessage();
			mensagem = new MimeMessageHelper(mimeMessage, "UTF-8");

			destinatario = GetEmailUsuario.getUsuarioEmail(a.getResponsavel());
			assuntoEmail = ASSUNTO_ATENDIMENTO_ANDAMENTO;
			texto = gerarTextoMensagem(a);

			try {
				configurarMensagem(mensagem, destinatario, assuntoEmail, texto);
				emails.add(mimeMessage);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	private String gerarTextoMensagem(Atendimento a) {
		return TEXTO_ATENDIMENTO_ANDAMENTO.replace(DATA, formatDate(a.getData()))
				.replaceAll(TURMA, a.getTurma().getNome())
				.replaceAll(DISCIPLINA, a.getTurma().getDisciplina().getNome());
	}

	private void configurarMensagem(MimeMessageHelper mensagem, String destinatario, String assuntoEmail, String texto) throws MessagingException {
			mensagem.setTo(destinatario);
			mensagem.setFrom(EMAIL_REMETENTE);
			mensagem.setSubject(assuntoEmail);
			mensagem.setText(texto, true);
	}

	@Override
	public void notificarAtendimentoAvaliacao() {
		List<MimeMessage> emails = new ArrayList<>();
		List<Atendimento> atendimentos = atendimentoRepository.findAllByStatus(Status.REALIZADO);

		if (!atendimentos.isEmpty()) {
			for (Atendimento atendimento : atendimentos) {
				try {
					MimeMessage mimeMessage = mailSender.createMimeMessage();

					criarMensagemAtendimento(mimeMessage, atendimento);

					emails.add(mimeMessage);
				} catch (MessagingException e) {
					e.printStackTrace();
					continue;
				}
			}
		}

		enviarEmails(emails);
	}

	private void criarMensagemAtendimento(MimeMessage mimeMessage, Atendimento atendimento) throws MessagingException {
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

		String destinatario = GetEmailUsuario.getUsuarioEmail(atendimento.getProfessor());
		String assunto = ASSUNTO_ATENDIMENTO_AVALIACAO;
		String texto = TEXTO_ATENDIMENTO_AVALIACAO.replace(DATA, formatDate(atendimento.getData()))
				.replaceAll(TURMA, atendimento.getTurma().getNome())
				.replaceAll(DISCIPLINA, atendimento.getTurma().getDisciplina().getNome())
				.replaceAll(RESPONSAVEL, atendimento.getResponsavel().getNome());

		mimeMessageHelper.setTo(destinatario);
		mimeMessageHelper.setFrom(EMAIL_REMETENTE);
		mimeMessageHelper.setSubject(assunto);
		mimeMessageHelper.setText(texto, true);
	}
}
