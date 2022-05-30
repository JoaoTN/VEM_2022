package br.udesc.ceavi.pin.infosaude.control;

import br.udesc.ceavi.pin.infosaude.control.dao.ConexaoPostgresJDBC;
import br.udesc.ceavi.pin.infosaude.modelo.PublicoAlvo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author lucas
 */
public class PublicoAlvoControl {

    private Connection conexao;
    
    public Connection conexao() throws ClassNotFoundException, SQLException{
    	return this.conexao = ConexaoPostgresJDBC.getConnection();
    }

    public PreparedStatement setInt(PreparedStatement stmt, int idade, int num) throws SQLException {
    	stmt.setInt(num, idade);
        return stmt;
    }
    
    public void executeStatement(PreparedStatement stmt, PublicoAlvo publicoAlvo, long id_vacina, String sqlQuery) throws SQLException, ClassNotFoundException {
        stmt = this.conexao().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
    	stmt.setLong(1, id_vacina);
    	stmt = setInt(stmt, publicoAlvo.getMinIdade(),2);
    	stmt = setInt(stmt, publicoAlvo.getMaxIdade(),3);
        stmt.setString(4, publicoAlvo.getSexo().toString());
        stmt.executeUpdate();
    }
    
    public Long inserir(PublicoAlvo publicoAlvo, long id_vacina) throws SQLException, ClassNotFoundException {
        Long id = null;
        String sqlQuery = "insert into publico_alvo(id_vacina,min_idade,max_idade,sexo) values(?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            executeStatement(stmt, publicoAlvo, id_vacina, sqlQuery);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getLong("id_publico_alvo");
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
