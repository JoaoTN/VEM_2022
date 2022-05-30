package br.udesc.ceavi.pin.infosaude.control;

import br.udesc.ceavi.pin.infosaude.control.dao.ConexaoPostgresJDBC;
import br.udesc.ceavi.pin.infosaude.control.dao.PreparaStatement;
import br.udesc.ceavi.pin.infosaude.control.dao.UsuarioStatement;
import br.udesc.ceavi.pin.infosaude.modelo.Pessoa;
import br.udesc.ceavi.pin.infosaude.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author lucas
 */
public class UsuarioControl extends UsuarioStatement{

	private Connection conexao;
    
    public Connection conexao() throws ClassNotFoundException, SQLException{
    	return this.conexao = ConexaoPostgresJDBC.getConnection();
    }
   
    public boolean inserir(Usuario usuario) throws SQLException, ClassNotFoundException {
        Long id = null;
        String sqlQuery = "insert into usuario(id_pessoa) values(?)";
        PreparedStatement stmt = null;
        long userId = usuario.getId();
        try {
            ResultSet rs = getResultSET(sqlQuery, stmt, userId);
            if (rs.next()) {
                id = rs.getLong("id_usuario");
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
        return true;
    }

    public Pessoa setarId(Pessoa pessoa, long id) {
    	pessoa.setId(id);
    	return pessoa;
    }
    
    public Pessoa setarNome(Pessoa pessoa, String nome) {
    	pessoa.setNome(nome);
    	return pessoa;
    }
    
    public long getResultLong(ResultSet rs, int index) throws SQLException {
    	return rs.getLong(index);
    }
    
    public String getResultString(ResultSet rs, int index) throws SQLException {
    	return rs.getString(index);
    }
    
    public Pessoa buscarPeloCPF(String cpf_usuario) throws SQLException, ClassNotFoundException {
        String sqlQuery = "select u.id_usuario, p.nome_pessoa, p.numero_sus from usuario as u natural inner join pessoa as p where p.cpf = ?";
        PreparedStatement stmt = null;
        Pessoa pessoa = null;
        try {
            ResultSet rs = executePrepared(stmt, cpf_usuario, sqlQuery);
            if (rs.next()) {
                pessoa = new Pessoa();
                long id = getResultLong(rs, 1);
                String nome = getResultString(rs, 2);
                String numeroSUS = getResultString(rs, 3);
                pessoa = setarId(pessoa, id);
                pessoa = setarNome(pessoa, nome);
                pessoa = setarNome(pessoa, numeroSUS);
            }
            this.conexao.commit();
        } catch (SQLException error) {
            this.conexao.rollback();
            error.printStackTrace();
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
        return pessoa;
    }
}
