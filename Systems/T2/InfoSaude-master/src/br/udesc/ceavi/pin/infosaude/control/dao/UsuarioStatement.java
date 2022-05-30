package br.udesc.ceavi.pin.infosaude.control.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuarioStatement extends PreparaStatement{
	public ResultSet executePrepared(PreparedStatement stmt, String cpf_usuario, String sqlQuery) throws ClassNotFoundException, SQLException {
    	stmt = this.conexao().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
        stmt = setPreparedString(stmt, cpf_usuario, 1);
        ResultSet rs = stmt.executeQuery();
        
        return rs;
    }
	
	public PreparedStatement executePrepared(String sqlQuery, PreparedStatement stmt, long userId) throws ClassNotFoundException, SQLException {
    	stmt = this.conexao().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
    	stmt = setPreparedLong(stmt, userId, 1);
    	stmt.executeUpdate();
        
    	return stmt;
    }
	
	public ResultSet getResultSET(String sqlQuery, PreparedStatement stmt, long userId) throws SQLException, ClassNotFoundException {
		stmt = executePrepared(sqlQuery, stmt, userId);
		ResultSet rs = stmt.getGeneratedKeys();
    	return rs;
	}
}
