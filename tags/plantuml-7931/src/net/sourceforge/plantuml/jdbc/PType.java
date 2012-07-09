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

import java.sql.Types;

public enum PType {

	VARCHAR(Types.VARCHAR, String.class, 128 * 1024), BLOB(Types.BLOB, byte[].class, 1024 * 1024), INTEGER(
			Types.INTEGER, Integer.class, 10), BOOLEAN(Types.BOOLEAN, Boolean.class, 1);

	private final int sqlType;
	private final String className;
	private final int precision;

	private PType(int sqlType, Class clazz, int precision) {
		this.sqlType = sqlType;
		this.className = clazz.getName();
		this.precision = precision;
	}

	public int getColumnType() {
		return sqlType;
	}

	public String getColumnTypeName() {
		return this.name();
	}

	public String getColumnClassName() {
		return className;
	}

	public int getPrecision() {
		return precision;
	}
}