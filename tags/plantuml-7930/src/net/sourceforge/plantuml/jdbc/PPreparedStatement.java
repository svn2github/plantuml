/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2012, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 7715 $
 *
 */
package net.sourceforge.plantuml.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PPreparedStatement extends TraceObject implements PreparedStatement {

	private final SqlTable sqlTable;
	private String sql;
	private final Map<Integer, Object> args = new HashMap<Integer, Object>();

	public PPreparedStatement(SqlTable sqlTable, String sql) {
		this.sql = sql.toUpperCase();
		this.sqlTable = sqlTable;
	}

	public ResultSet executeQuery(String sqlNew) throws SQLException {
		trace(1, "old=" + sql, "new=" + sqlNew);
		return sqlTable.executeQuery(sqlNew);
	}
	
	public ResultSet executeQuery() throws SQLException {
		trace(1);
		return sqlTable.executeQuery(sql);
	}
	
	public int executeUpdate() throws SQLException {
		trace(1);
		return sqlTable.executeUpdate(args);
	}


	public void setString(int parameterIndex, String x) throws SQLException {
		trace(0);
		args.put(parameterIndex, x);

	}

	public void addBatch(String arg0) throws SQLException {
		trace(1);

	}

	public void cancel() throws SQLException {
		trace(1);

	}

	public void clearBatch() throws SQLException {
		trace(1);

	}

	public void clearWarnings() throws SQLException {
		trace(1);

	}

	public void close() throws SQLException {
		trace(1);

	}

	public boolean execute(String arg0) throws SQLException {
		trace(1);
		return false;
	}

	public boolean execute(String arg0, int arg1) throws SQLException {
		trace(1);
		return false;
	}

	public boolean execute(String arg0, int[] arg1) throws SQLException {
		trace(1);
		return false;
	}

	public boolean execute(String arg0, String[] arg1) throws SQLException {
		trace(1);
		return false;
	}

	public int[] executeBatch() throws SQLException {
		trace(1);
		return null;
	}

	public int executeUpdate(String arg0) throws SQLException {
		trace(1);
		return 0;
	}

	public int executeUpdate(String arg0, int arg1) throws SQLException {
		trace(1);
		return 0;
	}

	public int executeUpdate(String arg0, int[] arg1) throws SQLException {
		trace(1);
		return 0;
	}

	public int executeUpdate(String arg0, String[] arg1) throws SQLException {
		trace(1);
		return 0;
	}

	public Connection getConnection() throws SQLException {
		trace(1);
		return null;
	}

	public int getFetchDirection() throws SQLException {
		trace(1);
		return 0;
	}

	public int getFetchSize() throws SQLException {
		trace(1);
		return 0;
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		trace(1);
		return null;
	}

	public int getMaxFieldSize() throws SQLException {
		trace(1);
		return 0;
	}

	public int getMaxRows() throws SQLException {
		trace(1);
		return 0;
	}

	public boolean getMoreResults() throws SQLException {
		trace(1);
		return false;
	}

	public boolean getMoreResults(int arg0) throws SQLException {
		trace(1);
		return false;
	}

	public int getQueryTimeout() throws SQLException {
		trace(1);
		return 0;
	}

	public ResultSet getResultSet() throws SQLException {
		trace(1);
		return null;
	}

	public int getResultSetConcurrency() throws SQLException {
		trace(1);
		return 0;
	}

	public int getResultSetHoldability() throws SQLException {
		trace(1);
		return 0;
	}

	public int getResultSetType() throws SQLException {
		trace(1);
		return 0;
	}

	public int getUpdateCount() throws SQLException {
		trace(1);
		return 0;
	}

	public SQLWarning getWarnings() throws SQLException {
		trace(1);
		return null;
	}

	public boolean isClosed() throws SQLException {
		trace(1);
		return false;
	}

	public boolean isPoolable() throws SQLException {
		trace(1);
		return false;
	}

	public void setCursorName(String arg0) throws SQLException {
		trace(1);

	}

	public void setEscapeProcessing(boolean arg0) throws SQLException {
		trace(1);

	}

	public void setFetchDirection(int arg0) throws SQLException {
		trace(1);

	}

	public void setFetchSize(int arg0) throws SQLException {
		trace(1);

	}

	public void setMaxFieldSize(int arg0) throws SQLException {
		trace(1);

	}

	public void setMaxRows(int arg0) throws SQLException {
		trace(1);

	}

	public void setPoolable(boolean arg0) throws SQLException {
		trace(1);

	}

	public void setQueryTimeout(int arg0) throws SQLException {
		trace(1);

	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		trace(1);
		return false;
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		trace(1);
		return null;
	}

	public void addBatch() throws SQLException {
		trace(1);

	}

	public void clearParameters() throws SQLException {
		trace(1);

	}

	public boolean execute() throws SQLException {
		trace(1);
		return false;
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		trace(1);
		return null;
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		trace(1);
		return null;
	}

	public void setArray(int arg0, Array arg1) throws SQLException {
		trace(1);

	}

	public void setAsciiStream(int arg0, InputStream arg1) throws SQLException {
		trace(1);

	}

	public void setAsciiStream(int arg0, InputStream arg1, int arg2) throws SQLException {
		trace(1);

	}

	public void setAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException {
		trace(1);

	}

	public void setBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
		trace(1);

	}

	public void setBinaryStream(int arg0, InputStream arg1) throws SQLException {
		trace(1);

	}

	public void setBinaryStream(int arg0, InputStream arg1, int arg2) throws SQLException {
		trace(1);

	}

	public void setBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException {
		trace(1);

	}

	public void setBlob(int arg0, Blob arg1) throws SQLException {
		trace(1);

	}

	public void setBlob(int arg0, InputStream arg1) throws SQLException {
		trace(1);

	}

	public void setBlob(int arg0, InputStream arg1, long arg2) throws SQLException {
		trace(1);

	}

	public void setBoolean(int arg0, boolean arg1) throws SQLException {
		trace(1);

	}

	public void setByte(int arg0, byte arg1) throws SQLException {
		trace(1);

	}

	public void setBytes(int arg0, byte[] arg1) throws SQLException {
		trace(1);

	}

	public void setCharacterStream(int arg0, Reader arg1) throws SQLException {
		trace(1);

	}

	public void setCharacterStream(int arg0, Reader arg1, int arg2) throws SQLException {
		trace(1);

	}

	public void setCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
		trace(1);

	}

	public void setClob(int arg0, Clob arg1) throws SQLException {
		trace(1);

	}

	public void setClob(int arg0, Reader arg1) throws SQLException {
		trace(1);

	}

	public void setClob(int arg0, Reader arg1, long arg2) throws SQLException {
		trace(1);

	}

	public void setDate(int arg0, Date arg1) throws SQLException {
		trace(1);

	}

	public void setDate(int arg0, Date arg1, Calendar arg2) throws SQLException {
		trace(1);

	}

	public void setDouble(int arg0, double arg1) throws SQLException {
		trace(1);

	}

	public void setFloat(int arg0, float arg1) throws SQLException {
		trace(1);

	}

	public void setInt(int arg0, int arg1) throws SQLException {
		trace(1);

	}

	public void setLong(int arg0, long arg1) throws SQLException {
		trace(1);

	}

	public void setNCharacterStream(int arg0, Reader arg1) throws SQLException {
		trace(1);

	}

	public void setNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
		trace(1);

	}

	public void setNClob(int arg0, NClob arg1) throws SQLException {
		trace(1);

	}

	public void setNClob(int arg0, Reader arg1) throws SQLException {
		trace(1);

	}

	public void setNClob(int arg0, Reader arg1, long arg2) throws SQLException {
		trace(1);

	}

	public void setNString(int arg0, String arg1) throws SQLException {
		trace(1);

	}

	public void setNull(int arg0, int arg1) throws SQLException {
		trace(1);

	}

	public void setNull(int arg0, int arg1, String arg2) throws SQLException {
		trace(1);

	}

	public void setObject(int arg0, Object arg1) throws SQLException {
		trace(1);

	}

	public void setObject(int arg0, Object arg1, int arg2) throws SQLException {
		trace(1);

	}

	public void setObject(int arg0, Object arg1, int arg2, int arg3) throws SQLException {
		trace(1);

	}

	public void setRef(int arg0, Ref arg1) throws SQLException {
		trace(1);

	}

	public void setRowId(int arg0, RowId arg1) throws SQLException {
		trace(1);

	}

	public void setSQLXML(int arg0, SQLXML arg1) throws SQLException {
		trace(1);

	}

	public void setShort(int arg0, short arg1) throws SQLException {
		trace(1);

	}

	public void setTime(int arg0, Time arg1) throws SQLException {
		trace(1);

	}

	public void setTime(int arg0, Time arg1, Calendar arg2) throws SQLException {
		trace(1);

	}

	public void setTimestamp(int arg0, Timestamp arg1) throws SQLException {
		trace(1);

	}

	public void setTimestamp(int arg0, Timestamp arg1, Calendar arg2) throws SQLException {
		trace(1);

	}

	public void setURL(int arg0, URL arg1) throws SQLException {
		trace(1);

	}

	public void setUnicodeStream(int arg0, InputStream arg1, int arg2) throws SQLException {
		trace(1);

	}

}
