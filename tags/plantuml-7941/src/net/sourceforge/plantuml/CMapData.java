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
 * Revision $Revision: 8368 $
 *
 */
package net.sourceforge.plantuml;

public class CMapData {

	private final StringBuilder stringBuilder = new StringBuilder();

	public void copy(CMapData other) {
		this.stringBuilder.setLength(0);
		this.stringBuilder.append(other.stringBuilder);
	}

	public String asString(String nameId) {
		return "<map id=\"" + nameId + "_map\" name=\"" + nameId + "_map\">\n" + stringBuilder.toString() + "</map>\n";
	}

	// public void appendHeader(UmlDiagram diag) {
	// if (diag.getUmlDiagramType() == UmlDiagramType.SEQUENCE) {
	// stringBuilder.append("<map id=\"sequence\" name=\"sequence\">\n");
	// } else {
	// stringBuilder.append("<map id=\"unix\" name=\"unix\">\n");
	// }
	// }

	public boolean containsData() {
		return stringBuilder.length() > 0;
	}

	public void appendString(String s) {
		stringBuilder.append(s);
	}

	public void appendLong(long s) {
		stringBuilder.append(s);
	}

}
