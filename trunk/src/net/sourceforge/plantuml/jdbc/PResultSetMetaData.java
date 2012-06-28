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

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class PResultSetMetaData extends TraceObject implements ResultSetMetaData {

	private final PName cols[];

	public PResultSetMetaData(PName... colNames) {
		this.cols = colNames;
	}
	
	public int size() {
		return cols.length;
	}

	public String getCatalogName(int column) throws SQLException {
		trace(1);
		return null;
	}

	public String getColumnClassName(int column) throws SQLException {
		trace(1);
		log(0, "type = " + cols[column - 1], "value=" + cols[column - 1].getColumnClassName());
		return cols[column - 1].getColumnClassName();
	}

	public int getColumnCount() throws SQLException {
		trace(0);
		return cols.length;
	}

	public int getColumnDisplaySize(int column) throws SQLException {
		trace(0);
		return cols[column - 1].getPrecision();
	}

	public String getColumnLabel(int column) throws SQLException {
		trace(0);
		return getColumnName(column);
	}

	public String getColumnName(int column) throws SQLException {
		trace(0);
		return cols[column - 1].getName();
	}

	public int getColumnType(int column) throws SQLException {
		trace(0);
		log(0, "type = " + cols[column - 1], "value=" + cols[column - 1].getColumnType());
		return cols[column - 1].getColumnType();
	}

	public String getColumnTypeName(int column) throws SQLException {
		trace(0);
		log(0, "type = " + cols[column - 1], "value=" + cols[column - 1].getColumnTypeName());
		return cols[column - 1].getColumnTypeName();
	}

	public int getPrecision(int column) throws SQLException {
		trace(0);
		return cols[column - 1].getPrecision();
	}

	public int getScale(int column) throws SQLException {
		trace(0);
		return 0;
	}

	public String getSchemaName(int column) throws SQLException {
		trace(1);
		return null;
	}

	public String getTableName(int column) throws SQLException {
		trace(1);
		return null;
	}

	public boolean isAutoIncrement(int column) throws SQLException {
		trace(0);
		return false;
	}

	public boolean isCaseSensitive(int column) throws SQLException {
		trace(1);
		return false;
	}

	public boolean isCurrency(int column) throws SQLException {
		trace(0);
		return false;
	}

	public boolean isDefinitelyWritable(int column) throws SQLException {
		trace(1);
		return false;
	}

	public int isNullable(int column) throws SQLException {
		trace(0);
		return columnNullableUnknown;
	}

	public boolean isReadOnly(int column) throws SQLException {
		trace(1);
		return false;
	}

	public boolean isSearchable(int column) throws SQLException {
		trace(1);
		return false;
	}

	public boolean isSigned(int column) throws SQLException {
		trace(0);
		return false;
	}

	public boolean isWritable(int column) throws SQLException {
		trace(1);
		return false;
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
