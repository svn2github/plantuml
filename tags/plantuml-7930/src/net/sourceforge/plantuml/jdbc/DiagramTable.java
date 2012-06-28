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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import net.sourceforge.plantuml.SourceStringReader;


public class DiagramTable extends TraceObject implements SqlTable {

	private String code = "@startuml\nBob->Alice\n@enduml";
	private String description;
	private byte[] png;

	public DiagramTable() {
		updatePng();
	}
	
	public int executeUpdate(Map<Integer, Object> args) {
		code = args.get(1).toString();
		updatePng();
		return 1;
	}

	private void updatePng() {
		SourceStringReader source = new SourceStringReader(code);
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			description = source.generateImage(baos);
			baos.close();
			png = baos.toByteArray();
		} catch (IOException e) {
			description = e.toString();
		}
	}

	public PTable getColumns(PResultSetMetaData meta) {
		final PTable ptable = new PTable(meta);
		ptable.addLine(new PLine(new PString(PDatabaseMetaData.CATALOG2), new PString(PDatabaseMetaData.SCHEMA1),
				new PString(PDatabaseMetaData.TABLE_DIAGRAM), new PString("CODE"), new PInteger(PType.VARCHAR
						.getColumnType()), new PString(PType.VARCHAR.getColumnTypeName()), new PInteger(PType.VARCHAR
						.getPrecision()), null, null, new PInteger(10), new PInteger(
						PDatabaseMetaData.columnNullableUnknown), new PString("UML starting with @startuml"), null,
				new PInteger(0), new PInteger(0), new PInteger(PType.VARCHAR.getPrecision()), new PInteger(1),
				new PString(""), null, null, null, null, new PString("NO")));
		ptable.addLine(new PLine(new PString(PDatabaseMetaData.CATALOG2), new PString(PDatabaseMetaData.SCHEMA1),
				new PString(PDatabaseMetaData.TABLE_DIAGRAM), new PString("DESCRIPTION"), new PInteger(PType.VARCHAR
						.getColumnType()), new PString(PType.VARCHAR.getColumnTypeName()), new PInteger(PType.VARCHAR
						.getPrecision()), null, null, new PInteger(10), new PInteger(
						PDatabaseMetaData.columnNullableUnknown), new PString("Description of the diagram"), null,
				new PInteger(0), new PInteger(0), new PInteger(PType.VARCHAR.getPrecision()), new PInteger(2),
				new PString(""), null, null, null, null, new PString("NO")));
		ptable.addLine(new PLine(new PString(PDatabaseMetaData.CATALOG2), new PString(PDatabaseMetaData.SCHEMA1),
				new PString(PDatabaseMetaData.TABLE_DIAGRAM), new PString("PNG"), new PInteger(PType.BLOB
						.getColumnType()), new PString(PType.BLOB.getColumnTypeName()), new PInteger(PType.BLOB
						.getPrecision()), null, null, new PInteger(10), new PInteger(
						PDatabaseMetaData.columnNullableUnknown), new PString("PNG image"), null, new PInteger(0),
				new PInteger(0), new PInteger(PType.VARCHAR.getPrecision()), new PInteger(3), new PString(""), null,
				null, null, null, new PString("NO")));
		return ptable;
	}

	public PTable asBestRowIdentifier(PResultSetMetaData meta) {
		final PTable ptable = new PTable(meta);
		ptable.addLine(new PLine(new PInteger(PDatabaseMetaData.bestRowSession), new PString("CODE"), new PInteger(
				PType.VARCHAR.getColumnType()), new PString(PType.VARCHAR.getColumnTypeName()), new PInteger(
				PType.VARCHAR.getPrecision()), new PInteger(0), null, new PInteger(PDatabaseMetaData.bestRowNotPseudo)));
		return ptable;
	}

	public ResultSet executeQuery(String sql) {
		sql = sql.toUpperCase();
		boolean count = sql.contains("COUNT(*)");
		final PTable ptable;
		if (count) {
			final PResultSetMetaData meta = new PResultSetMetaData(new PName("COUNT", PType.INTEGER));
			ptable = new PTable(meta);
			ptable.addLine(new PLine(new PInteger(0)));
		} else {
			final PResultSetMetaData meta = new PResultSetMetaData(new PName("CODE", PType.VARCHAR), new PName(
					"DESCRIPTION", PType.VARCHAR), new PName("PNG", PType.BLOB));
			ptable = new PTable(meta);
			ptable.addLine(new PLine(new PString(code), new PString(description), new PBlob(png)));
		}
		return new PResultSet(ptable);
	}

	public PTable getPrimaryKeys(PResultSetMetaData meta) {
		final PTable ptable = new PTable(meta);
		ptable.addLine(new PLine(new PString(PDatabaseMetaData.CATALOG2), new PString(PDatabaseMetaData.SCHEMA1),
				new PString(PDatabaseMetaData.TABLE_DIAGRAM), new PString("CODE"), new PInteger(1), new PString("CODE")));
		return ptable;
	}

	public PreparedStatement prepareStatement(String sql) {
		return new PPreparedStatement(this, sql);
	}


}