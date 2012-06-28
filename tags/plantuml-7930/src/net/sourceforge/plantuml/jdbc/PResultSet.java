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
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

public class PResultSet extends TraceObject implements ResultSet {
	
	private final Iterator<PLine> it;
	private PLine current;
	private boolean wasNull;
	private final PResultSetMetaData resultSetMetaData;
	
	public PResultSet(PTable data) {
		this.it = data.iterator();
		this.resultSetMetaData = data.getMetaData();
	}


	
	public boolean next() throws SQLException {
		trace(0);
		if (it.hasNext()==false) {
			log(0, "return false");
			return false;
		}
		current = it.next();
		log(0, "next="+current);
		return true;
	}
	public Object getObject(int columnIndex) throws SQLException {
		trace(0, "col="+columnIndex+" "+current.getString(columnIndex));
		wasNull = current.getObject(columnIndex)==null;
		return current.getObject(columnIndex);
	}

	
	public String getString(int columnIndex) throws SQLException {
		trace(0, "col="+columnIndex+" "+current.getString(columnIndex));
		wasNull = current.getString(columnIndex)==null;
		return current.getString(columnIndex);
	}


	public Blob getBlob(int columnIndex) throws SQLException {
		trace(1);
		wasNull = current.getObject(columnIndex)==null;
		return current.getBlob(columnIndex);
	}


	public boolean absolute(int row) throws SQLException {
		trace(1);
		return false;
	}

	
	public void afterLast() throws SQLException {
		trace(1);
		
	}

	
	public void beforeFirst() throws SQLException {
		trace(1);
		
	}

	
	public void cancelRowUpdates() throws SQLException {
		trace(1);
		
	}

	
	public void clearWarnings() throws SQLException {
		trace(1);
		
	}

	
	public void close() throws SQLException {
		trace(0);
	}

	
	public void deleteRow() throws SQLException {
		trace(1);
		
	}

	
	public int findColumn(String columnLabel) throws SQLException {
		trace(1);
		return 0;
	}

	
	public boolean first() throws SQLException {
		trace(1);
		return false;
	}

	
	public Array getArray(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public Array getArray(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		trace(1);
		return null;
	}

	
	public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
		trace(1);
		return null;
	}

	
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	
	public Blob getBlob(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public boolean getBoolean(int columnIndex) throws SQLException {
		trace(1);
		return false;
	}

	
	public boolean getBoolean(String columnLabel) throws SQLException {
		trace(1);
		return false;
	}

	
	public byte getByte(int columnIndex) throws SQLException {
		trace(1);
		return 0;
	}

	
	public byte getByte(String columnLabel) throws SQLException {
		trace(1);
		return 0;
	}

	
	public byte[] getBytes(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public byte[] getBytes(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public Clob getClob(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public Clob getClob(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public int getConcurrency() throws SQLException {
		trace(1);
		return 0;
	}

	
	public String getCursorName() throws SQLException {
		trace(1);
		return null;
	}

	
	public Date getDate(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public Date getDate(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		trace(1);
		return null;
	}

	
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		trace(1);
		return null;
	}

	
	public double getDouble(int columnIndex) throws SQLException {
		trace(1);
		return 0;
	}

	
	public double getDouble(String columnLabel) throws SQLException {
		trace(1);
		return 0;
	}

	
	public int getFetchDirection() throws SQLException {
		trace(1);
		return 0;
	}

	
	public int getFetchSize() throws SQLException {
		trace(1);
		return 0;
	}

	
	public float getFloat(int columnIndex) throws SQLException {
		trace(1);
		return 0;
	}

	
	public float getFloat(String columnLabel) throws SQLException {
		trace(1);
		return 0;
	}

	
	public int getHoldability() throws SQLException {
		trace(1);
		return 0;
	}

	
	public int getInt(int columnIndex) throws SQLException {
		trace(0, "columnIndex="+columnIndex, "object="+getObject(columnIndex));
		return current.getInt(columnIndex);
	}

	
	public int getInt(String columnLabel) throws SQLException {
		trace(1);
		return 0;
	}

	
	public long getLong(int columnIndex) throws SQLException {
		trace(1);
		return 0;
	}

	
	public long getLong(String columnLabel) throws SQLException {
		trace(1);
		return 0;
	}

	
	public ResultSetMetaData getMetaData() throws SQLException {
		trace(0);
		return resultSetMetaData;
	}

	
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public NClob getNClob(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public NClob getNClob(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public String getNString(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public String getNString(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public Object getObject(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
		trace(1);
		return null;
	}

	
	public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
		trace(1);
		return null;
	}

	
	public Ref getRef(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public Ref getRef(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public int getRow() throws SQLException {
		trace(1);
		return 0;
	}

	
	public RowId getRowId(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public RowId getRowId(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public short getShort(int columnIndex) throws SQLException {
		trace(1);
		return 0;
	}

	
	public short getShort(String columnLabel) throws SQLException {
		trace(1);
		return 0;
	}

	
	public Statement getStatement() throws SQLException {
		trace(1);
		return null;
	}

	
	public String getString(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public Time getTime(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public Time getTime(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		trace(1);
		return null;
	}

	
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		trace(1);
		return null;
	}

	
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		trace(1);
		return null;
	}

	
	public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
		trace(1);
		return null;
	}

	
	public int getType() throws SQLException {
		trace(1);
		return 0;
	}

	
	public URL getURL(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public URL getURL(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		trace(1);
		return null;
	}

	
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		trace(1);
		return null;
	}

	
	public SQLWarning getWarnings() throws SQLException {
		trace(1);
		return null;
	}

	
	public void insertRow() throws SQLException {
		trace(1);
		
	}

	
	public boolean isAfterLast() throws SQLException {
		trace(1);
		return false;
	}

	
	public boolean isBeforeFirst() throws SQLException {
		trace(1);
		return false;
	}

	
	public boolean isClosed() throws SQLException {
		trace(1);
		return false;
	}

	
	public boolean isFirst() throws SQLException {
		trace(1);
		return false;
	}

	
	public boolean isLast() throws SQLException {
		trace(1);
		return false;
	}

	
	public boolean last() throws SQLException {
		trace(1);
		return false;
	}

	
	public void moveToCurrentRow() throws SQLException {
		trace(1);
		
	}

	
	public void moveToInsertRow() throws SQLException {
		trace(1);
		
	}

	

	
	public boolean previous() throws SQLException {
		trace(1);
		return false;
	}

	
	public void refreshRow() throws SQLException {
		trace(1);
		
	}

	
	public boolean relative(int rows) throws SQLException {
		trace(1);
		return false;
	}

	
	public boolean rowDeleted() throws SQLException {
		trace(1);
		return false;
	}

	
	public boolean rowInserted() throws SQLException {
		trace(1);
		return false;
	}

	
	public boolean rowUpdated() throws SQLException {
		trace(1);
		return false;
	}

	
	public void setFetchDirection(int direction) throws SQLException {
		trace(1);
		
	}

	
	public void setFetchSize(int rows) throws SQLException {
		trace(1);
		
	}

	
	public void updateArray(int columnIndex, Array x) throws SQLException {
		trace(1);
		
	}

	
	public void updateArray(String columnLabel, Array x) throws SQLException {
		trace(1);
		
	}

	
	public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
		trace(1);
		
	}

	
	public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
		trace(1);
		
	}

	
	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		trace(1);
		
	}

	
	public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
		trace(1);
		
	}

	
	public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		trace(1);
		
	}

	
	public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
		trace(1);
		
	}

	
	public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
		trace(1);
		
	}

	
	public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
		trace(1);
		
	}

	
	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		trace(1);
		
	}

	
	public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
		trace(1);
		
	}

	
	public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		trace(1);
		
	}

	
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		trace(1);
		
	}

	
	public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
		trace(1);
		
	}

	
	public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
		trace(1);
		
	}

	
	public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		trace(1);
		
	}

	
	public void updateBoolean(String columnLabel, boolean x) throws SQLException {
		trace(1);
		
	}

	
	public void updateByte(int columnIndex, byte x) throws SQLException {
		trace(1);
		
	}

	
	public void updateByte(String columnLabel, byte x) throws SQLException {
		trace(1);
		
	}

	
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		trace(1);
		
	}

	
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		trace(1);
		
	}

	
	public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
		trace(1);
		
	}

	
	public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
		trace(1);
		
	}

	
	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		trace(1);
		
	}

	
	public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
		trace(1);
		
	}

	
	public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		trace(1);
		
	}

	
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		trace(1);
		
	}

	
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		trace(1);
		
	}

	
	public void updateClob(String columnLabel, Reader reader) throws SQLException {
		trace(1);
		
	}

	
	public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateDate(int columnIndex, Date x) throws SQLException {
		trace(1);
		
	}

	
	public void updateDate(String columnLabel, Date x) throws SQLException {
		trace(1);
		
	}

	
	public void updateDouble(int columnIndex, double x) throws SQLException {
		trace(1);
		
	}

	
	public void updateDouble(String columnLabel, double x) throws SQLException {
		trace(1);
		
	}

	
	public void updateFloat(int columnIndex, float x) throws SQLException {
		trace(1);
		
	}

	
	public void updateFloat(String columnLabel, float x) throws SQLException {
		trace(1);
		
	}

	
	public void updateInt(int columnIndex, int x) throws SQLException {
		trace(1);
		
	}

	
	public void updateInt(String columnLabel, int x) throws SQLException {
		trace(1);
		
	}

	
	public void updateLong(int columnIndex, long x) throws SQLException {
		trace(1);
		
	}

	
	public void updateLong(String columnLabel, long x) throws SQLException {
		trace(1);
		
	}

	
	public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
		trace(1);
		
	}

	
	public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
		trace(1);
		
	}

	
	public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateNClob(int columnIndex, NClob clob) throws SQLException {
		trace(1);
		
	}

	
	public void updateNClob(String columnLabel, NClob clob) throws SQLException {
		trace(1);
		
	}

	
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		trace(1);
		
	}

	
	public void updateNClob(String columnLabel, Reader reader) throws SQLException {
		trace(1);
		
	}

	
	public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
		trace(1);
		
	}

	
	public void updateNString(int columnIndex, String string) throws SQLException {
		trace(1);
		
	}

	
	public void updateNString(String columnLabel, String string) throws SQLException {
		trace(1);
		
	}

	
	public void updateNull(int columnIndex) throws SQLException {
		trace(1);
		
	}

	
	public void updateNull(String columnLabel) throws SQLException {
		trace(1);
		
	}

	
	public void updateObject(int columnIndex, Object x) throws SQLException {
		trace(1);
		
	}

	
	public void updateObject(String columnLabel, Object x) throws SQLException {
		trace(1);
		
	}

	
	public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
		trace(1);
		
	}

	
	public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
		trace(1);
		
	}

	
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		trace(1);
		
	}

	
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		trace(1);
		
	}

	
	public void updateRow() throws SQLException {
		trace(1);
		
	}

	
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		trace(1);
		
	}

	
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		trace(1);
		
	}

	
	public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
		trace(1);
		
	}

	
	public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
		trace(1);
		
	}

	
	public void updateShort(int columnIndex, short x) throws SQLException {
		trace(1);
		
	}

	
	public void updateShort(String columnLabel, short x) throws SQLException {
		trace(1);
		
	}

	
	public void updateString(int columnIndex, String x) throws SQLException {
		trace(1);
		
	}

	
	public void updateString(String columnLabel, String x) throws SQLException {
		trace(1);
		
	}

	
	public void updateTime(int columnIndex, Time x) throws SQLException {
		trace(1);
		
	}

	
	public void updateTime(String columnLabel, Time x) throws SQLException {
		trace(1);
		
	}

	
	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		trace(1);
		
	}

	
	public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
		trace(1);
		
	}

	
	public boolean wasNull() throws SQLException {
		trace(0);
		return wasNull;
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
