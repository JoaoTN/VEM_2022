package br.udesc.ceavi.pin.infosaude.control;

import br.udesc.ceavi.pin.infosaude.control.dao.ConexaoPostgresJDBC;
import br.udesc.ceavi.pin.infosaude.control.dao.PreparaStatement;
import br.udesc.ceavi.pin.infosaude.control.excecpton.DadosVaziosExcepitions;
import br.udesc.ceavi.pin.infosaude.modelo.Endereco;
import br.udesc.ceavi.pin.infosaude.modelo.Instituicao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author lucas
 */
public class InstituicaoControl {

	private Connection conexao;
    
    public Connection conexao() throws ClassNotFoundException, SQLException{
    	return this.conexao = ConexaoPostgresJDBC.getConnection();
    }

    public boolean validaCampos(String cnpj, String nome, String senha) throws DadosVaziosExcepitions {
        boolean a = true;

        if (cnpj.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("CNPJ INVALIDO!");
        }
        if (nome.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("NOME INVALIDO!");
        }
        if (senha.equals("")) {
            a = false;
            throw new DadosVaziosExcepitions("SENHA INVALIDO!");
        }

        return a;
    }
    
    public PreparaStatement getStatement() {
    	PreparaStatement ps = new PreparaStatement();
    	return ps;
    }
    
    public ResultSet executePrepared(PreparedStatement stmt, String[] dados, String sqlQuery, long id) throws ClassNotFoundException, SQLException {
    	stmt = this.conexao().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
        stmt = getStatement().setPreparedLong(stmt, id, 1);
        stmt = getStatement().setPreparedString(stmt, dados[1], 2);
        stmt = getStatement().setPreparedString(stmt, dados[2], 3);
        stmt = getStatement().setPreparedString(stmt, dados[3], 4);
        
        stmt.executeUpdate();
        return stmt.getGeneratedKeys();
    }
    
    public long getResultLong(ResultSet rs, int index) throws SQLException {
    	return rs.getLong(index);
    }

    public Long inserir(Instituicao instituicao, Endereco endereco) throws SQLException, ClassNotFoundException{
        Long id = null;
        String sqlQuery = "insert into instituicao(id_endereco,cnpj,nome_instituicao,senha) values(?,?,?,?)";

        PreparedStatement stmt = null;
        try {
        	String[] dados = instituicao.retornaInstituicao();
        	long _id = endereco.getId();
            
            ResultSet rs = executePrepared(stmt, dados, sqlQuery, _id);
            if (rs.next()) {
                id = getResultLong(rs, 1);
            }
            
            this.conexao.commit();
        } catch (SQLException error) {
            this.conexao.rollback();
            throw error;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                }
            }
            if (conexao != null) {
                this.conexao.close();
            }
        }

        return id;
    }

}
