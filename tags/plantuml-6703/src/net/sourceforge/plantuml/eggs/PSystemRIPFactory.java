/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 3830 $
 *
 */
package net.sourceforge.plantuml.eggs;

import java.io.IOException;

import net.sourceforge.plantuml.DiagramType;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.PSystemBasicFactory;

public class PSystemRIPFactory implements PSystemBasicFactory {

	private PSystemRIP system;

	public void init() {
	}

	public boolean executeLine(String line) {
		if (line.equalsIgnoreCase("jean canouet")) {
			try {
				system = new PSystemRIP();
				return true;
			} catch (IOException e) {
				Log.error("Error " + e);
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public PSystemRIP getSystem() {
		return system;
	}
	
	public DiagramType getDiagramType() {
		return DiagramType.UML;
	}


}
