package br.udesc.ceavi.pin.infosaude.control;

import br.udesc.ceavi.pin.infosaude.control.dao.ConexaoPostgresJDBC;
import br.udesc.ceavi.pin.infosaude.control.dao.PreparaStatement;
import br.udesc.ceavi.pin.infosaude.control.dao.ProfissionalStatement;
import br.udesc.ceavi.pin.infosaude.modelo.Instituicao;
import br.udesc.ceavi.pin.infosaude.modelo.Profissional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author lucas
 */
public class ProfissionalControl extends ProfissionalStatement{

	private Connection conexao;
    
    public Connection conexao() throws ClassNotFoundException, SQLException{
    	return this.conexao = ConexaoPostgresJDBC.getConnection();
    }
    
    public long getResultLong(ResultSet rs, String index) throws SQLException {
    	return rs.getLong(index);
    }
    
    public Long inserir(Profissional profissional, Instituicao instituicao) throws SQLException, ClassNotFoundException {
        Long id = null;
        String sqlQuery = "insert into profissional(id_instituicao) values(?)";

        PreparedStatement stmt = null;
        try {
        	long _id = instituicao.getId();
            stmt = executePrepared2(stmt, _id, sqlQuery);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = getResultLong(rs, "id_instituicao");
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

    public Long getAcessoProfissional(String login, String senha) throws SQLException, ClassNotFoundException {
        Long id_profissional = -1l;
        String sqlQuery = "select prof.id_profissional from pessoa as p natural inner join profissional as prof where p.login = ? and p.senha = ?";
        PreparedStatement stmt = null;
        int q = -1;
        try {
        	stmt = executePrepared(stmt, sqlQuery, login, senha); 
        	stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                id_profissional = getResultLong(rs, "id_profissional");
            }
        } catch (SQLException ex) {
            this.conexao.rollback();
            throw ex;
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

        return id_profissional;
    }
}
