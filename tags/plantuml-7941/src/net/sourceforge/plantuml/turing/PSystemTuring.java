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

import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.CMapData;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.graphic.GraphicPosition;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.ugraphic.UFont;

public class PSystemTuring extends AbstractPSystem {

	private final List<String> strings = new ArrayList<String>();

	PSystemTuring(String program, String input) {
		final BFMachine machine = new BFMachine(program, input);
		/* final boolean ok = */machine.run();
		final String output = machine.getOutput();
		for (String s : output.split("\n")) {
			strings.add(s);
		}
	}

	public void exportDiagram(OutputStream os, CMapData cmap, int index, FileFormatOption fileFormat)
			throws IOException {
		getGraphicStrings().writeImage(os, fileFormat);
	}

	private GraphicStrings getGraphicStrings() throws IOException {
		final UFont font = new UFont("Monospaced", Font.PLAIN, 14);
		return new GraphicStrings(strings, font, HtmlColorUtils.BLACK, HtmlColorUtils.WHITE, null,
				GraphicPosition.BACKGROUND_CORNER, false);
	}

	public String getDescription() {
		return "(Turing)";
	}

}
