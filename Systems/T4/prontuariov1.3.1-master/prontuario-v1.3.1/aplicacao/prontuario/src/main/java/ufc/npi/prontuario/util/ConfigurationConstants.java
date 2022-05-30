package ufc.npi.prontuario.util;

public class ConfigurationConstants {

	public static final String DATE_PATTERN = "dd/MM/yyyy";

	public static final String PERMISSAO_ESTUDANTE = "hasAnyAuthority('ESTUDANTE')";
	
	public static final String PERMISSAO_ESTUDANTE_VERIFICACAO_ESTUDANTE = "hasAnyAuthority('ESTUDANTE') AND #atendimento.responsavel.id EQ authentication.principal.id";

	public static final String PERMISSAO_PROFESSOR = "hasAnyAuthority('PROFESSOR')";
	
	public static final String PERMISSAO_PROFESSOR_VERIFICACAO_PROFESSOR = "hasAnyAuthority('PROFESSOR') AND #atendimento.professor.id EQ authentication.principal.id";

	public static final String PERMISSAO_ESTUDANTE_VERFICACAO_PROFESSOR_VERIFICACAO = 
			"((hasAnyAuthority('ESTUDANTE') AND #atendimento.responsavel.id EQ authentication.principal.id) "
				+ "AND (#atendimento.status.name() EQ 'EM_ANDAMENTO'))"
			+ " OR "  
			+ "((hasAnyAuthority('PROFESSOR') AND #atendimento.professor.id EQ authentication.principal.id) "
				+ "AND (#atendimento.status.name() EQ 'EM_ANDAMENTO' OR #atendimento.status.name() EQ 'REALIZADO'))";
	
	public static final String PERMISSAO_ATENDENTE = "hasAnyAuthority('ATENDENTE')";

	public static final String PERMISSAO_ADMINISTRACAO = "hasAnyAuthority('ADMINISTRACAO')";
	
	public static final String PERMISSAO_ADMINISTRACAO_VERIFICACAO_ID_PROFESSOR = "#professor.id EQ #authentication.principal.id OR hasAnyAuthority('ADMINISTRACAO')";
	
	public static final String PERMISSOES_ESTUDANTE_PROFESSOR = "hasAnyAuthority('ESTUDANTE','PROFESSOR')";
	
	public static final String PERMISSOES_PROFESSOR_ADMINISTRACAO = "hasAnyAuthority('PROFESSOR','ADMINISTRACAO')";

	public static final String PERMISSOES_ESTUDANTE_PROFESSOR_ADMINISTRACAO = "hasAnyAuthority('ESTUDANTE','PROFESSOR','ADMINISTRACAO')"; 
	
	public static final String PERMISSOES_PROFESSOR_ADMINISTRACAO_VERIFICACAO_ESTUDANTE = "hasAnyAuthority('PROFESSOR','ADMINISTRACAO') or returnObject.getModelMap().get('aluno').id == authentication.principal.id";

	public static final String PERMISSOES_ADMINISTRACAO_VERIFICACAO_PROFESSOR = "hasAnyAuthority('ADMINISTRACAO') or returnObject.getModelMap().get('turma').professores.contains(authentication.principal)";

	public static final String AUTENTICADO = "isAuthenticated()";
	
	public static final String ANONIMO = "isAnonymous()";
	
	public static final String AUTENTICADO_VERIFICACAO_ID = "isAuthenticated() and #usuarioId == authentication.principal.id";
}
