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
 */
package net.sourceforge.plantuml.turing;

import net.sourceforge.plantuml.DiagramType;
import net.sourceforge.plantuml.PSystemBasicFactory;

public class PSystemTuringFactory implements PSystemBasicFactory {

	private StringBuilder prg;
	private StringBuilder input;

	public void init(String startLine) {
		prg = null;
		input = null;
		if ("@startturing".equalsIgnoreCase(startLine)) {
			prg = new StringBuilder();
		}
	}

	public boolean executeLine(String line) {
		if (prg == null) {
			return false;
		}
		if (input == null) {
			if (line.length() == 0) {
				input = new StringBuilder();
			} else {
				prg.append(line);
				prg.append('\n');
			}
		} else {
			input.append(line);
			input.append('\n');
		}
		return true;
	}

	public PSystemTuring getSystem() {
		return new PSystemTuring(prg.toString(), input == null ? "" : input.toString());
	}

	public DiagramType getDiagramType() {
		return DiagramType.TURING;
	}

}
