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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

public class PConnection extends TraceObject implements Connection {

	private final PDatabaseMetaData metaData;

	public PConnection(String username) {
		metaData = new PDatabaseMetaData(username);
	}

	public void clearWarnings() throws SQLException {
		trace(1);

	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		trace(1, "resultSetType=" + resultSetType, "resultSetConcurrency=" + resultSetConcurrency);
		throw new SQLFeatureNotSupportedException();
	}
	
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		trace(1, sql);
		sql = sql.toUpperCase();
		if (sql.contains("FROM PLANTUML_VERSION")) {
			return PlantumlDriver.PlantumlVersionTable.prepareStatement(sql);
		}
		if (sql.contains("FROM DIAGRAM") || sql.startsWith("UPDATE DIAGRAM")) {
			return PlantumlDriver.DiagramTable.prepareStatement(sql);
		}
		return null;
	}


	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		trace(1);
		throw new SQLFeatureNotSupportedException();
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		trace(1);
		throw new SQLFeatureNotSupportedException();
	}

	public String nativeSQL(String sql) throws SQLException {
		trace(1);
		return sql;
	}

	private boolean autoCommit = false;

	public boolean getAutoCommit() throws SQLException {
		trace(0);
		return autoCommit;
	}

	public void commit() throws SQLException {
		trace(1);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		trace(0);
		this.autoCommit = autoCommit;
	}

	public void rollback() throws SQLException {
		trace(1);
	}

	public void close() throws SQLException {
		trace(1);
	}

	public boolean isClosed() throws SQLException {
		trace(1);
		return false;
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		trace(0);
		return metaData;
	}

	// --------

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		trace(1);
		throw new SQLFeatureNotSupportedException();
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		trace(1);
		throw new SQLFeatureNotSupportedException();
	}

	public void setHoldability(int holdability) throws SQLException {
		trace(1);
		throw new SQLFeatureNotSupportedException();
	}

	public int getHoldability() throws SQLException {
		trace(1);
		throw new SQLFeatureNotSupportedException();
	}

	public Savepoint setSavepoint() throws SQLException {
		trace(1);
		throw new SQLFeatureNotSupportedException();
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		trace(1);
		throw new SQLFeatureNotSupportedException();
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		trace(1);
		throw new SQLFeatureNotSupportedException();
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		trace(1);

		return null;
	}

	public Blob createBlob() throws SQLException {
		trace(1);

		return null;
	}

	public Clob createClob() throws SQLException {
		trace(1);

		return null;
	}

	public NClob createNClob() throws SQLException {
		trace(1);

		return null;
	}

	public SQLXML createSQLXML() throws SQLException {
		trace(1);

		return null;
	}

	public Statement createStatement() throws SQLException {
		trace(0);
		return new PStatement();
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		trace(1);

		return null;
	}

	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		trace(1);

		return null;
	}

	public String getCatalog() throws SQLException {
		trace(0);
		return null;
	}

	public Properties getClientInfo() throws SQLException {
		trace(1);

		return null;
	}

	public String getClientInfo(String name) throws SQLException {
		trace(1);

		return null;
	}

	public int getTransactionIsolation() throws SQLException {
		trace(1);

		return 0;
	}

	public SQLWarning getWarnings() throws SQLException {
		trace(1);

		return null;
	}

	public boolean isReadOnly() throws SQLException {
		trace(1);

		return false;
	}

	public boolean isValid(int timeout) throws SQLException {
		trace(1);

		return false;
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		trace(1);

		return null;
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		trace(1);

		return null;
	}


	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		trace(1);

		return null;
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		trace(1);

		return null;
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		trace(1);

		return null;
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		trace(1);

		return null;
	}

	public void setCatalog(String catalog) throws SQLException {
		trace(1);

	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		trace(1);

	}

	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		trace(1);

	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		trace(1);

	}

	public Savepoint setSavepoint(String name) throws SQLException {
		trace(1);

		return null;
	}

	public void setTransactionIsolation(int level) throws SQLException {
		trace(1);

	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		trace(1);

		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		trace(1);

		return null;
	}
}
