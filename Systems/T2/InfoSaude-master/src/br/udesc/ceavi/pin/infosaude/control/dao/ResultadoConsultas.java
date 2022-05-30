package br.udesc.ceavi.pin.infosaude.control.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultadoConsultas {
	public long getResultLong(ResultSet rs, int index) throws SQLException {
    	return rs.getLong(index);
    }
	
	public long getResultLong(ResultSet rs, String index) throws SQLException {
    	return rs.getLong(index);
    }
	
	public int getResultInt(ResultSet rs, int index) throws SQLException {
    	return rs.getInt(index);
    }
	
	public int getResultInt(ResultSet rs, String index) throws SQLException {
    	return rs.getInt(index);
    }
    
    public String getResultString(ResultSet rs, int index) throws SQLException {
    	return rs.getString(index);
    }
    
    public String getResultString(ResultSet rs, String index) throws SQLException {
    	return rs.getString(index);
    }
}
