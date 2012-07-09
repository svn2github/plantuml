package net.sourceforge.plantuml.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

public class PStatement extends TraceObject implements Statement {

	public ResultSet executeQuery(String sql) throws SQLException {
		trace(1, sql);
		sql = sql.toUpperCase();
		if (sql.contains("FROM PLANTUML_VERSION")) {
			return PlantumlDriver.PlantumlVersionTable.executeQuery(sql);
		}
		if (sql.contains("FROM DIAGRAM")) {
			return PlantumlDriver.DiagramTable.executeQuery(sql);
		}
		return null;
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

	public boolean execute(String sql) throws SQLException {
		trace(1, sql);
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

	public void setMaxRows(int max) throws SQLException {
		trace(1, "max=" + max);

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

}
