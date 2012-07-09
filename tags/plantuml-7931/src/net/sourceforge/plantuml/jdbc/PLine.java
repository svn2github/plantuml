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

import java.sql.Blob;
import java.util.Arrays;

public class PLine extends TraceObject {

	private final PData data[];

	public PLine(PData... data) {
		this.data = data;
	}

	public int size() {
		return data.length;
	}

	@Override
	public String toString() {
		return "PLine " + Arrays.asList(data);
	}

	public String getString(int columnIndex) {
		if (columnIndex < 0 || columnIndex - 1 >= data.length) {
			log(1, "data=" + Arrays.asList(data) + " columnIndex=" + columnIndex);
		}
		if (data[columnIndex - 1] == null) {
			return null;
		}
		return data[columnIndex - 1].toString();
	}

	public Object getObject(int columnIndex) {
		if (columnIndex < 0 || columnIndex - 1 >= data.length) {
			log(1, "data=" + Arrays.asList(data) + " columnIndex=" + columnIndex);
		}
		if (data[columnIndex - 1] == null) {
			return null;
		}
		return data[columnIndex - 1].getObject();
	}

	public Blob getBlob(int columnIndex) {
		if (columnIndex < 0 || columnIndex - 1 >= data.length) {
			log(1, "data=" + Arrays.asList(data) + " columnIndex=" + columnIndex);
		}
		if (data[columnIndex - 1] == null) {
			return null;
		}
		return ((PBlob) data[columnIndex - 1]).getBlob();
	}

	public int getInt(int columnIndex) {
		if (columnIndex < 0 || columnIndex - 1 >= data.length) {
			log(1, "data=" + Arrays.asList(data) + " columnIndex=" + columnIndex);
		}
		PData d = data[columnIndex - 1];
		if (d instanceof PInteger) {
			return ((PInteger) d).intValue();
		}
		log(1, "ERROR " + d.getObject());
		return 0;
	}

}