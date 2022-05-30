package br.udesc.ceavi.pin.infosaude.control.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import br.udesc.ceavi.pin.infosaude.principal.Main;

public class PreparaStatement {
	private Connection conexao;
	
	public Connection conexao() throws ClassNotFoundException, SQLException{
    	return this.conexao = ConexaoPostgresJDBC.getConnection();
    }
	
	public PreparedStatement setPreparedString(PreparedStatement stmt, String info, int num) throws SQLException {
    	stmt.setString(num, info);
        return stmt;
    }
    
    public PreparedStatement setPreparedLong(PreparedStatement stmt, long info, int num) throws SQLException {
    	stmt.setLong(num, info);
    	return stmt;
    }
    
    public PreparedStatement setPreparedInt(PreparedStatement stmt, int info, int num) throws SQLException {
    	stmt.setLong(num, info);
    	return stmt;
    }
    
    public PreparedStatement setPreparedDate(PreparedStatement stmt, java.sql.Date info, int num) throws SQLException {
    	stmt.setDate(num, info);
    	return stmt;
    }
    
    public ResultSet executePrepared(PreparedStatement stmt, String[] dados, String sqlQuery) throws ClassNotFoundException, SQLException {
    	stmt = this.conexao().prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
        stmt = setPreparedLong(stmt, Long.parseLong(dados[0]), 1);
        stmt = setPreparedLong(stmt, Long.parseLong(dados[1]), 2);
        stmt = setPreparedDate(stmt, new Date(Long.parseLong(dados[2])), 3);
        stmt = setPreparedDate(stmt, new Date(Long.parseLong(dados[3])), 4);
        stmt = setPreparedString(stmt, dados[4], 5);
        
        stmt.executeUpdate();
        return stmt.getGeneratedKeys();
    }
    
    public ResultSet executePrepared(PreparedStatement stmt, String sqlQuery1) throws ClassNotFoundException, SQLException {
    	stmt = this.conexao().prepareStatement(sqlQuery1);
    	stmt = setPreparedLong(stmt, Main.usuario.getId(), 1);
        stmt.execute();
        ResultSet resultSet = stmt.getResultSet();
        
        return resultSet;
    }
    
    public ResultSet executePrepared(PreparedStatement stmt, long id_vacina, String sqlQuery) throws ClassNotFoundException, SQLException {
    	stmt = this.conexao().prepareStatement(sqlQuery);
    	stmt = setPreparedLong(stmt, id_vacina, 1);
    	stmt = setPreparedDate(stmt, new java.sql.Date(Calendar.getInstance().getTimeInMillis()), 2);
        stmt.executeQuery();
        ResultSet resultSet = stmt.getResultSet();
        
        return resultSet;
    }
    
    public PreparedStatement criaPS(PreparedStatement stmt, Long id_vacina, Long id_usuario, Long id_campanha, Long id_profissional, int dose, String observacoes, String sql) throws SQLException, ClassNotFoundException {
    	stmt = this.conexao().prepareStatement(sql);
    	
    	stmt = setPreparedLong(stmt, id_usuario, 1);
        stmt = setPreparedLong(stmt, id_vacina, 2);
    	stmt = setPreparedLong(stmt, id_profissional, 3);
    	stmt = setPreparedDate(stmt, new java.sql.Date(Calendar.getInstance().getTimeInMillis()), 4);
        stmt = setPreparedInt(stmt, dose, 5);
        return stmt;
    }
}
