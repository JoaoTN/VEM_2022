package ufc.npi.prontuario.util;

public class EmailConstants {
	
	public static final String EMAIL_REMETENTE = "naoresponda@prontuario.ufc.br";

	public static final String ASSUNTO_RECUPERACAO_SENHA = "Recuperação de senha";
	
	public static final String ASSUNTO_ATENDIMENTO_ANDAMENTO = "Atendimento em andamento";
	
	public static final String ASSUNTO_ATENDIMENTO_AVALIACAO = "Atendimento aguardando avaliação";

	public static final String TEXTO_RECUPERACAO_SENHA = "Você pode alterar sua senha no link a seguir: ";
	
	public static final String TEXTO_ATENDIMENTO_ANDAMENTO = "Você possui um atendimento em andamento de #DATA#, na turma #TURMA# da disciplina #DISCIPLINA#. Lembre-se de finalizá-lo.";
	
	public static final String TEXTO_ATENDIMENTO_AVALIACAO = "Você possui um atendimento aguardando validação de #DATA#, na turma #TURMA# da disciplina #DISCIPLINA#, com o aluno responsável #RESPONSAVEL#. Lembre-se de validá-lo.";

	public static final String TEMPLATE_EMAIL_RECUPERACAO_SENHA = "mail/recuperar-senha";
	
	public static final String DATA = "#DATA#";
	
	public static final String TURMA = "#TURMA#";
	
	public static final String DISCIPLINA = "#DISCIPLINA#";
	
	public static final String RESPONSAVEL = "#RESPONSAVEL#";
}
