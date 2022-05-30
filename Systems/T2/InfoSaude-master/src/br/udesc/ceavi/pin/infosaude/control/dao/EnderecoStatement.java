package br.udesc.ceavi.pin.infosaude.control.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.udesc.ceavi.pin.infosaude.modelo.Endereco;

public class EnderecoStatement extends PreparaStatement{
	public PreparedStatement setPreparedStatementString(PreparedStatement stmt, String info, int num) throws SQLException {
    	stmt.setString(num, info);
        
        return stmt;
    }
    
    public PreparedStatement setPreparedStatementInt(PreparedStatement stmt, int info , int num) throws SQLException {
        stmt.setInt(num, info);
    	return stmt;
    }
    
    public PreparedStatement setPreparedStatementLong(PreparedStatement stmt, long info, int num) throws SQLException {
    	stmt.setLong(num, info);
    	return stmt;
    }
    
    public ResultSet executePrepared(PreparedStatement stmt, long info, String sqlQuery) throws ClassNotFoundException, SQLException {
    	stmt = this.conexao().prepareStatement(sqlQuery);
        stmt = setPreparedStatementLong(stmt, info, 1);
        ResultSet rs = stmt.executeQuery();
        
    	return stmt.executeQuery();
    }
    
	public PreparedStatement executePrepared(PreparedStatement stmt, String[] dados, String sqlQuery, int numero, long id) throws ClassNotFoundException, SQLException {	
    	stmt = this.conexao().prepareStatement(sqlQuery);
    	stmt = setPreparedStatementString(stmt,dados[1], 1);
    	stmt = setPreparedStatementString(stmt,dados[2], 2);
    	stmt = setPreparedStatementString(stmt,dados[3], 3);
    	stmt = setPreparedStatementString(stmt,dados[4], 4);
    	stmt = setPreparedStatementString(stmt,dados[6], 6);
    	stmt = setPreparedStatementString(stmt,dados[7], 7);
    	stmt = setPreparedStatementString(stmt,dados[8], 8);
    	stmt = setPreparedStatementString(stmt,dados[9], 9);

    	stmt = setPreparedStatementInt(stmt, numero, 10);
    	stmt = setPreparedStatementLong(stmt, id, 11);
    	stmt.executeUpdate();
    	
    	return stmt;
    }
	
	public ResultSet getResult(PreparedStatement stmt, String[] dados, String sqlQuery) throws ClassNotFoundException, SQLException {
		return executePrepared(stmt, dados, sqlQuery);
	}
}
