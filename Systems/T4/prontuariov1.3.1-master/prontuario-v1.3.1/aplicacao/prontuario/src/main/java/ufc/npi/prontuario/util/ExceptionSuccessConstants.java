package ufc.npi.prontuario.util;

public class ExceptionSuccessConstants {
	
	public static final String ERRO_CAMPOS_OBRIGATORIOS = "Preencha os campos obrigatórios.";
	
	public static final String ERRO_SALVAR_TURMA_EXISTENTE = "Não foi possível salvar a turma. Nome da turma já existente para esta disciplina e período.";
	
	public static final String ERRO_EXCLUIR_TURMA_EXCEPTION = "Não foi possível excluir a turma! Ela ainda possui atendimentos realizados ou ainda possui alunos matriculados.";
	
	public static final String ERRO_EXCLUIR_TIPO_PATOLOGIA = "Não foi possível excluir o Tipo de Patologia! Ele ainda está sendo usado por alguma patologia.";
	
	public static final String ERRO_EXCLUIR_TIPO_PROCEDIMENTO = "Não foi possível excluir o Tipo de Procedimento! Ele ainda está sendo usado por algum procedimento.";
	
	public static final String ERRO_EXCLUIR_SERVIDOR = "Não foi possível excluir o servidor! Ele ainda possui atendimentos ou ainda está lecionando em uma turma.";
	
	public static final String ERRO_AVALIACAO_ATIVA = "Já existe uma avaliação ativa!";
	
	public static final String ERRO_AVALIACAO_QTD_ITENS = "É necessário no mínimo um criterio!";
	
	public static final String ERRO_EXCLUIR_AVALIACAO_COM_ATENDIMENTO = "Não foi possível excluir! Avaliação foi utilizada em um atendimento!";
	
	public static final String ERRO_AVALIACAO_CAMPOS = "Preencha todos os campos!";
	
	public static final String ERRO_ATUALIZAR_ALUNO = "Não foi possível atualizar o(a) aluno(a)! Matricula já existente.";

	public static final String PESSOA_NAO_ENCONTRADA = "Pessoa não encontrada.";

	public static final String NENHUM_ATENDIMENTO_ABERTO_EXCEPTION = "Não foram encontrados atendimentos em andamento para este paciente. Operação não permitida";

	public static final String ERRO_ADICIONAR_ATENDIMENTO = "Não foi possível adicionar um atendimento! Existe outro atendimento em andamento.";

	public static final String ERRO_EDITAR_ATENDIMENTO = "Não foi possível atualizar esse atendimento.";
	
	public static final String ERRO_EXCLUIR_ATENDIMENTO = "Não foi possível excluir esse atendimento.";
	
	public static final String ERRO_SALVAR_ARQUIVO = "Não foi possível salvar o arquivo!";
	
	public static final String ERRO_CARREGAR_ARQUIVO = "Não foi possível carregar o arquivo!";
	
	public static final String ERRO_ADD_PLANO_TRATAMENTO = "Paciente possui um tratamento ativo nessa clínica!";
	
	public static final String ERRO_ARQUIVO_EXISTENTE = "Já existe um documento com mesmo nome para esse paciente!";

	public static final String ERRO_PACIENTE_CPF_EXISTENTE = "Não foi possível adicionar o paciente! Este CPF já foi cadastrado.";

	public static final String ERRO_PACIENTE_CNS_EXISTENTE = "Não foi possível adicionar o paciente! Este Cartão Nacional do SUS já foi cadastrado.";

	public static final String ERRO_ADICIONAR_ALUNO = "Não foi possível adicionar o(a) aluno(a)! Matricula já cadastrada.";
	
	public static final String ERRO_EXCLUIR_ALUNO = "Não foi possível excluir o(a) aluno(a)! Ele ainda possui atendimentos ou ainda está matriculado em um turma.";
	
	public static final String ERRO_EXCLUIR_DISCIPLINA = "Não foi possível excluir disciplina! Esta disciplina deve está associada a uma turma.";
	
	public static final String ERRO_ADICIONAR_SERVIDOR = "Não foi possível adicionar o servidor! Matricula ou email já cadastrados.";
	
	public static final String ERRO_ADICIONAR_SERVIDOR_VAZIO = "Não foi possível adicionar o servidor! Preencha os campos vazios.";

	public static final String ERRO_ADICIONAR_SERVIDOR_PAPEL = "Não foi possível adicionar o servidor! Selecione pelo menos um papel";

	public static final String ERRO_ALTERAR_SENHA = "Sua senha atual está incorreta";

	public static final String ERRO_SALVAR_TIPO_PATOLOGIA_EXISTENTE = "Já existe um tipo de patologia cadastrada com o mesmo nome";

	public static final String ERRO_SALVAR_TIPO_PROCEDIMENTO_EXISTENTE = "Já existe um tipo de procedimento cadastrado com o mesmo nome";
	
	public static final String ERRO_SALVAR_DISCIPLINA_EXISTENTE = "Já existe uma disciplina cadastrada com o mesmo nome ou código";
	
	public static final String ERRO_EXISTE_AVALIACAO_ATIVA = "Só pode existir uma avaliação ativa!";
	
	public static final String ERROR = "error";
	
	public static final String ERROR_ALUNO_JA_CADASTRADO = "Aluno(a) já cadastrado(a) nessa turma!";

	public static final String ERROR_MATRICULA_INEXISTENTE = "Matrícula inexistente!";
	
	public static final String ERROR_ALUNO_POSSUI_ATENDIMENTO = "Aluno(a) possui atendimento realizado nessa turma!";
	
	public static final String ERROR_INSCRICAO_ALUNO_NAO_ENCONTRADA = "Inscrição do aluno não encontrada para essa turma!";
	
	public static final String ERROR_PROFESSOR_POSSUI_ATENDIMENTO = "Professor(a) possui atendimento nessa turma!";
	
	public static final String ERROR_NOME_ANAMNESE = "Já existe uma anamnese cadastrada com esse nome!";

	public static final String ERROR_EXCLUIR_ANAMNESE = "Não é possível excluir uma anamnese finalizada!";
	
	public static final String ERROR_EXCLUIR_PLANO_TRATAMENTO = "Não é possível excluir um tratamento finalizado.";
	
	public static final String ERROR_FINALIZAR_PLANO_TRATAMENTO = "Não foi possível finalizar esse plano de tratamento.";
	
	public static final String ERROR_EDITAR_PLANO_TRATAMENTO = "Não foi possível atualizar esse plano de tratamento.";
	
	public static final String ERRO_EXCLUIR_AVALIACAO_INATIVA = "Não é possível excluir uma avaliação inativa!";
	
	//Sucess mensagens
	
	public static final String SUCCESS = "sucess";
	
	public static final String SUCCESS_CADASTRAR_ALUNO = "Aluno(a) cadastrado(a) com sucesso!";
	
	public static final String SUCCESS_ATUALIZAR_ALUNO = "Aluno(a) atualizado(a) com sucesso!";
	
	public static final String SUCCESS_EXCLUIR_ALUNO = "Aluno(a) excluído(a) com sucesso!";
	
	public static final String SUCCESS_SALVAR_ANAMNESE = "Anamnese cadastrada com sucesso!";
	
	public static final String SUCCESS_SALVAR_PERGUNTA = "Pergunta cadastrada com sucesso!";
	
	public static final String SUCCESS_EXCLUIR_PERGUNTA = "Pergunta excluída com sucesso!";
	
	public static final String SUCCESS_FINALIZAR_ANAMNESE = "Anamnese finalizada com sucesso!";

	public static final String SUCCESS_REMOVER_ANAMNESE = "Anamnese excluída com sucesso!";
	
	public static final String SUCCESS_CADASTRAR_ATENDIMENTO = "Atendimento cadastrado com sucesso!";
	
	public static final String SUCCESS_EXCLUIR_ATENDIMENTO = "Atendimento excluído com sucesso!";
	
	public static final String SUCCESS_EDITAR_ATENDIMENTO = "Atendimento atualizado com sucesso!";
	
	public static final String SUCCESS_AVALIAR_ATENDIMENTO = "Atendimento avaliado com sucesso!";
	
	public static final String SUCCESS_FINALIZAR_ATENDIMENTO = "Atendimento finalizado com sucesso!";
	
	public static final String SUCCESS_EXCLUIR_PROCEDIMENTO = "Procedimento excluído com sucesso!";
	
	public static final String SUCCESS_EXCLUIR_PATOLOGIA = "Patologia excluída com sucesso!";
	
	public static final String SUCCESS_CADASTRAR_AVALIACAO = "Avaliação cadastrada com sucesso!";
	
	public static final String SUCCESS_CADASTRAR_CRITERIO = "Item/critério cadastrado com sucesso!";

	public static final String SUCCESS_CADASTRAR_TRATAMENTO = "Plano de tratamento cadastrado com sucesso!";
	
	public static final String SUCCESS_EDITAR_TRATAMENTO = "Plano de tratamento atualizado com sucesso!";
	
	public static final String SUCCESS_EXCLUIR_TRATAMENTO = "Plano de tratamento excluído com sucesso!";
	
	public static final String SUCCESS_VALIDAR_ATENDIMENTO = "Atendimento validado com sucesso!";
	
	public static final String SUCCESS_EXCLUIR_AVALIACAO = "Avaliação excluída com sucesso!";
	
	public static final String SUCCESS_EXCLUIR_ITEM_AVALIACAO = "Item excluído com sucesso!";
	
	public static final String SUCCESS_CADASTRAR_DISCIPLINA = "Disciplina cadastrada com sucesso!";
	
	public static final String SUCCESS_EDITAR_DISCIPLINA = "Disciplina editada com sucesso!";
	
	public static final String SUCCESS_EXCLUIR_DISCIPLINA = "Disciplina excluída com sucesso!";
	
	public static final String SUCCESS_SALVAR_DOCUMENTO = "Documento(s) salvo(s) com sucesso!";
	
	public static final String SUCCESS_EXCLUIR_DOCUMENTO = "Documento excluído com sucesso!";
	
	public static final String SUCCESS_CADASTRAR_PACIENTE = "Paciente cadastrado com sucesso!";
	
	public static final String SUCCESS_EDITAR_PACIENTE = "Paciente editado com sucesso!";
	
	public static final String SUCCESS_REALIZAR_ANAMNESE = "Anamnese realizada com sucesso!";
	
	public static final String SUCCESS_CADASTRAR_SERVIDOR = "Servidor(a) cadastrado(a) com sucesso!";

	public static final String SUCCESS_ATUALIZAR_SERVIDOR = "Servidor(a) atualizado(a) com sucesso!";
	
	public static final String SUCCESS_EXCLUIR_SERVIDOR = "Servido(a) excluído(a) com sucesso!";
	
	public static final String SUCCESS_SALVAR_TIPO_PATOLOGIA = "Tipo de patologia cadastrada com sucesso!";

	public static final String SUCCESS_EDITAR_TIPO_PATOLOGIA = "Tipo de patologia atualizada com sucesso!";

	public static final String SUCCESS_EXCLUIR_TIPO_PATOLOGIA = "Tipo de patologia excluída com sucesso!";
	
	public static final String SUCCESS_SALVAR_TIPO_PROCEDIMENTO = "Tipo de procedimento cadastrado com sucesso!";

	public static final String SUCCESS_EDITAR_TIPO_PROCEDIMENTO = "Tipo de procedimento atualizado com sucesso!";

	public static final String SUCCESS_EXCLUIR_TIPO_PROCEDIMENTO = "Tipo de procedimento excluído com sucesso!";
	
	public static final String SUCCESS_CADASTRAR_TURMA = "Turma cadastrada com sucesso!";
	
	public static final String SUCCESS_MATRICULAR_ALUNO = "Aluno(a) matriculado(a) com sucesso";
	
	public static final String SUCCESS_VINCULAR_PROFESSOR = "Professor(es) vinculado(s) com sucesso!";
	
	public static final String SUCCESS_EXCLUIR_TURMA = "Turma excluída com sucesso!";
	
	public static final String SUCCESS_ALTERAR_STATUS = "Status alterado com sucesso!";
	
	public static final String SUCCESS_REMOVER_INSCRICAO = "Inscrição removida com sucesso!";
	
	public static final String SUCCESS_DESVINCULAR_PROFESSOR = "Professor(a) desvinculado(a) com sucesso!";
	
	public static final String SUCCESS_FINALIZAR_AVALIACAO = "Avaliação finalizada com sucesso!";
}
